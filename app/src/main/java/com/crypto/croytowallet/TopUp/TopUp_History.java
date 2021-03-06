package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.New_Currency;
import com.crypto.croytowallet.Adapter.NewCoinAdapter;
import com.crypto.croytowallet.Adapter.TopUp_HistoryAdapter;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TopUp_HistoryResponse;
import com.crypto.croytowallet.Extra_Class.ApiResponse.TransactionHistoryResponse;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.List;
import java.util.List;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUp_History extends AppCompatActivity implements TopUp_HistoryAdapter.OnOrderItemListener {
    RecyclerView recyclerView;
     KProgressHUD progressDialog;
    TextView history_Empty;
    EditText search_input;

    CharSequence search1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__history);
        search_input = findViewById(R.id.search_currency);
        recyclerView=findViewById(R.id.recyclerTransation);
        history_Empty =findViewById(R.id.txt_list_is_empty);
        getRewardHistory();
    }

    private void getRewardHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(TopUp_History.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<List<TopUp_HistoryResponse>> call = RetrofitClient.getInstance().getApi().getRewardHistory(token);

        call.enqueue(new Callback<List<TopUp_HistoryResponse>>() {
            @Override
            public void onResponse(Call<List<TopUp_HistoryResponse>> call, Response<List<TopUp_HistoryResponse>> response) {
                hidepDialog();
                if (response.code() == 200 || response.isSuccessful()) {
                    if (response.body() != null || response.body().size() > 0) {

                        TopUp_HistoryAdapter adapter = new TopUp_HistoryAdapter(response.body(), getApplicationContext(), TopUp_History.this);
                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(TopUp_History.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager1);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(adapter);

                        search_input.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                adapter.getFilter().filter(s);
                                search1 = s;
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } else {

                        history_Empty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TopUp_HistoryResponse>> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(TopUp_History.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    @Override
    public void onOrderItemClick(List<TopUp_HistoryResponse> data, int position) {

    }
}