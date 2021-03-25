package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.CountryNameSpinnerAddapter;
import com.crypto.croytowallet.Model.CountryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.Utility;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitCountryName;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kyc extends AppCompatActivity implements View.OnClickListener {
    Spinner countryNameSpinner,stateNameSpinner,selectDocumentSpinner;
    TextView chooseFile;
    String countryName,stateName,documentName;
    KProgressHUD progressDialog;
    ArrayList<CountryModel>countryModels ;
    ArrayList<String>stateList;
    String[] documentType = { "Select Document","Driving License", " Voter ID card", "Passport", " Birth Certificate"};
    Button submit;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private String Document_img1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        countryNameSpinner = findViewById(R.id.CountryNameSpinner);
        stateNameSpinner = findViewById(R.id.stateNameSpinner);
        selectDocumentSpinner = findViewById(R.id.select_documet_Spinner);
        chooseFile = findViewById(R.id.chooseFile);
       // takeImge = findViewById(R.id.camra);
        ivImage = findViewById(R.id.setImageView);
        submit = findViewById(R.id.submit);
        chooseFile.setOnClickListener(this);
        //takeImge.setOnClickListener(this);

        countryModels = new ArrayList<CountryModel>();
        stateList  = new ArrayList<String>();

        getCountryName();

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,documentType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDocumentSpinner.setAdapter(aa);

        selectDocumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documentName = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Kyc.this, ""+Document_img1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void getCountryName() {

        progressDialog = KProgressHUD.create(Kyc.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitCountryName.getInstance().getApiCountryName().getCountryname();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s=null;
                hidepDialog();

                if (response.isSuccessful()){
                    try {
                        s = response.body().string();

                        JSONArray jsonArray = new JSONArray(s);
                        for (int i =0; i<=jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            CountryModel countryModel = new CountryModel();
                            String name = object.getString("name");
                            String alpha2Code = object.getString("alpha2Code");
                            String flags = object.getString("flag");
                            countryModel.setCountryName(name);
                            countryModel.setImage(flags);
                            countryModel.setCountryCode(alpha2Code);

                            countryModels.add(countryModel);

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    CountryNameSpinnerAddapter  adp =  new CountryNameSpinnerAddapter(Kyc.this,countryModels);
                    countryNameSpinner.setAdapter(adp);

                    countryNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            countryName = countryModels.get(position).getCountryName();
                            String countryCode = countryModels.get(position).getCountryCode();
                            String Code = countryCode.toLowerCase();

                            getState(Code);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else {
                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("Api not is working")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
          hidepDialog();

                Snacky.builder()
                        .setActivity(Kyc.this)
                        .setText("Internet problem")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }


    public void getState(String code){

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().GET_State(code);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;

                if (response.isSuccessful()){
                    stateList.clear();
                    try {
                        s = response.body().string();
                        JSONArray jsonArray  = new JSONArray(s);

                        for (int i=0; i<=jsonArray.length();i++){
                            String state = jsonArray.getString(i);
                            stateList.add(state);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String>(Kyc.this,android.R.layout.simple_spinner_dropdown_item,stateList);

                    stateNameSpinner.setAdapter(adp);

                    stateNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stateName  = parent.getItemAtPosition(position).toString();
                            Toast.makeText(Kyc.this, ""+stateName, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }else{
                    Snacky.builder()
                            .setActivity(Kyc.this)
                            .setText("State not found")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Kyc.this)
                        .setText("Internet problem")
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.chooseFile:
                selectImage();
                 break;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Kyc.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(Kyc.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
        BitMapToString(thumbnail);
        thumbnail=getResizedBitmap(thumbnail, 400);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
        bm=getResizedBitmap(bm, 400);
        BitMapToString(bm);
    }


    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}