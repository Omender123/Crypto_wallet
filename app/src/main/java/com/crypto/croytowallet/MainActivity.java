package com.crypto.croytowallet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.SharedPrefernce.PearToPearSharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.login.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    NavController navController;
    DrawerLayout drawer;
    NavigationView navigationView;
    AppBarConfiguration appBarConfiguration;
    BottomNavigationView bottomNavigationView;
    private View navHeader,navDrawer;
    TextView username,usergmail;
    Toolbar toolbar;
    KProgressHUD progressDialog;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  toolbar.setTitleTextColor(getResources().getColor(R.color.toolbar_text_color));

      // change_menu_icon();
        changeStatusBarColor();
        init();
        moreOptions();

        navHeader = navigationView.getHeaderView(0);
        username = (TextView) navHeader.findViewById(R.id.nav_username);
        usergmail= (TextView) navHeader.findViewById(R.id.nav_usergmail);



        //getting the current user
        UserData user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        username.setText(user.getUsername());
        usergmail.setText(user.getEmail());

        //  toolbar.setNavigationIcon(R.drawable.your_drawable_name);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
      //  NavigationView();

        Menu menu = navigationView.getMenu();
        MenuItem share = menu.findItem(R.id.share);

        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String code =user.getReferral_code();

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    String sAux = "Hey,\n Its amazing install Crypto Wallet app and use this\n Referral code : "+code +"\n Download "+ getResources().getString(R.string.app_name) + "\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                   startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    private void init() {
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_support, getApplicationContext().getTheme());

        navController = Navigation.findNavController(this,R.id.main);
        appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.deshboard,R.id.myWallet,R.id.exchange,R.id.profile,R.id.security,R.id.support,R.id.setting})
                .setDrawerLayout(drawer)
                .build();


    }

    public void change_menu_icon(){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_support);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration );
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void moreOptions(){

        Menu menu = navigationView.getMenu();
        MenuItem logout = menu.findItem(R.id.logout);


        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialogBox();

                return true;
            }
        });

    }

    public void AlertDialogBox(){

        //Logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Crypto Wallet");

        // set dialog message
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        alertDialogBuilder
                .setMessage("Are you sure to Logout !!!!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
public void logout(){
    progressDialog = KProgressHUD.create(MainActivity.this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait.....")
            .setCancellable(false)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .show();
    UserData user = SharedPrefManager.getInstance(this).getUser();
    String username=user.getUsername();
    String token=user.getToken();

    String url="http://13.233.136.56:8080/api/user/removeCurrentlyActiveDevices";
    showpDialog();

    StringRequest stringRequest =new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            hidepDialog();
            SharedPrefManager.getInstance(getApplicationContext()).logout();
            PearToPearSharedPrefManager.getInstance(getApplicationContext()).clearPearData();
            Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

         //   Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            hidepDialog();
            parseVolleyError(error);
           // Toast.makeText(MainActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("jwt", token);
           /*  params.put("ip",ipAddress);
                params.put("os",os);
                params.put("location",locations);
*/
            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();

            headers.put("Authorization", token);

            return headers;
        }

    };

    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    queue.add(stringRequest);

    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message=data.getString("error");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}