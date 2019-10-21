package com.egov.win10.simpelv.TTD;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.egov.win10.simpelv.Login;
import com.egov.win10.simpelv.MainActivity;
import com.egov.win10.simpelv.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Callback;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class DaftarTTD extends AppCompatActivity {

    EditText editNik, editNama, editEmail, editNoTelepon, editNip, editJabatan, editUnitKerja;

    String nik, nama, email, noTelepon, nip, kota, provinsi, jabatan, unitkerja;


    public static final String PREFS_NAME = "MyPrefsFile";

    String id_user;
    String nama_instansi;

    private RequestQueue mQueue;

    ProgressDialog progressDialog;

    Button btnDaftar, btnFileSK, btnFileKTP, btnFileTTD;


    String token = "1077e2ca-7c8e-484e-85d8-0284a7bef30f";

    



    private String URl_ESIGN = "http://simpel.pasamanbaratkab.go.id/0.0.0.0_android/simaya/esign.php";




    private String BASE_PATH = "http://0.0.0.0/0.0.0.0/user";
    private String URL_DAFTAR_TTD = "http://0.0.0.0/0.0.0.0/user/registrasi?nik=";
    //private String 0.0.0.0_DAFTAR_TTD = "http://0.0.0.0/0.0.0.0/user/registrasi?nik=3434&nama=Nani Fajriani&email=nanifajriani316@yahoo.com&nomor_telepon=082386437418&kota=Pasaman Barat&provinsi=Sumatera Barat&nip=19790221 199711 2 001&jabatan=Kepala Bidang E-Goverment&unit_kerja=Diskominfo Pasaman Barat";
    private String 0.0.0.0_DAFTAR_TTD = "";

    File scanKTP;
    File scanSK;
    File scanTTD;

    // private Uri filepath;

    final int PICK_IMAGE_REQUEST = 234;


    private static final int REQUEST_CODE_RESOLUTION = 3;
    String filepath;
    String filepath2;
    String filepath3;

    ImageView ssKtp, ssTTD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        id_user = settings.getString("id_user", "default");
        String stat_code2 = settings.getString("nik", "null");



        if(stat_code2.equals("null")){
            setContentView(R.layout.activity_daftar_ttd);

            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }




            nama_instansi = settings.getString("nama_instansi", "default");


            editNik = (EditText) findViewById(R.id.daftarNikTTD);
            editNama = (EditText) findViewById(R.id.daftarNamaTTD);
            editEmail = (EditText) findViewById(R.id.daftarEmailTTD);
            editNoTelepon = (EditText) findViewById(R.id.daftarNoTeleponTTD);
            editNip = (EditText) findViewById(R.id.daftarNipTTD);
            editJabatan = (EditText) findViewById(R.id.daftarJabatanTTD);
            editUnitKerja = (EditText) findViewById(R.id.daftarUnitKerjakTTD);

            ssKtp = (ImageView) findViewById(R.id.showScanKTP);
            ssTTD = (ImageView) findViewById(R.id.showTandaTangan);

            btnDaftar = (Button) findViewById(R.id.btnDaftarTTD);
            btnFileKTP = (Button) findViewById(R.id.btnScanKTP);
            btnFileSK = (Button) findViewById(R.id.btnScanSK);
            btnFileTTD = (Button) findViewById(R.id.btnImageTTD);



            btnFileKTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialFilePicker()
                            .withActivity(DaftarTTD.this)
                            .withRequestCode(1000)
                            .withHiddenFiles(false)

                            .start();
                }
            });

            btnFileSK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialFilePicker()
                            .withActivity(DaftarTTD.this)
                            .withRequestCode(1100)

                            .withHiddenFiles(false)
                            .start();
                }
            });

            btnFileTTD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialFilePicker()
                            .withActivity(DaftarTTD.this)
                            .withRequestCode(1200)
                            .withHiddenFiles(false)

                            .start();
                }
            });

            btnDaftar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    nik = editNik.getText().toString();
                    nama = editNama.getText().toString();
                    email = editEmail.getText().toString();
                    noTelepon = editNoTelepon.getText().toString();
                    nip = editNip.getText().toString();
                    kota = "Pasaman Barat";
                    provinsi = "Sumatera Barat";
                    jabatan = editJabatan.getText().toString();
                    unitkerja = editUnitKerja.getText().toString();

                    0.0.0.0_DAFTAR_TTD = URL_DAFTAR_TTD + nik + "&nama="+nama+"&email="+email+"&nomor_telepon="+noTelepon+"&kota="+kota+"&provinsi="+provinsi+"&nip="+nip+"&jabatan="+jabatan+"&unit_kerja="+unitkerja;
                    // Toast.makeText(DaftarTTD.this, ""+0.0.0.0_DAFTAR_TTD, Toast.LENGTH_SHORT).show();


                    if(nik.equals("") || nama.equals("")||email.equals("") || noTelepon.equals("") || nip.equals("")||kota.equals("")||provinsi.equals("")||jabatan.equals("")||unitkerja.equals("")){
                        Toast.makeText(DaftarTTD.this, "Data tidak boleh ada yang kosong !!", Toast.LENGTH_SHORT).show();
                    }else {

                        signUp(nik, nama, id_user, nama_instansi);
                    }
                    //send_data_esign(nama, nik, id_user, nama_instansi);
                    //  Toast.makeText(DaftarTTD.this, ""+id_user, Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(DaftarTTD.this, "ID User "+id_user+"\nNama instansi "+nama_instansi, Toast.LENGTH_SHORT).show();

                    //  Toast.makeText(DaftarTTD.this, ""+0.0.0.0_DAFTAR_TTD, Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(DaftarTTD.this, "1 : "+filepath+"\n 2 : "+filepath2+"\n3 : "+filepath3, Toast.LENGTH_SHORT).show();
                }
            });
           // Toast.makeText(this, "Belum Tedaftar"+stat_code2, Toast.LENGTH_SHORT).show();
        }else {
            setContentView(R.layout.user_terdaftar);
            //Toast.makeText(this, "Terdaftar"+stat_code2, Toast.LENGTH_SHORT).show();
        }






    }

    public void verif_user(){


    }

    public void signUp(final String nikk, final String namaa, final String id_userr, final String nama_instansii) {
        progressDialog = new ProgressDialog(DaftarTTD.this);
        progressDialog.setMessage("Mohon Tunggu....");
        progressDialog.show();

        try {
            scanKTP = new File(filepath);
            scanSK = new File(filepath2);
            scanTTD = new File(filepath3);

        } catch (Exception e) {

        }

        AndroidNetworking.upload(0.0.0.0_DAFTAR_TTD)
                .addMultipartFile("image_ttd", scanTTD)
                .addMultipartFile("ktp", scanKTP)
                .addMultipartFile("surat_rekomendasi", scanSK)
                .setTag("Registrasi")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                String responseString = response.get("message").toString();

                                Toast.makeText(DaftarTTD.this, "" + responseString, Toast.LENGTH_SHORT).show();
                                send_data_esign(namaa, nikk, id_userr, nama_instansii);

                                upload_image_ttd();
                                progressDialog.dismiss();
                                Intent intent = new Intent(DaftarTTD.this, SuksesDaftarTTD.class);
                                startActivity(intent);


                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(DaftarTTD.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(DaftarTTD.this, "ERROR : " + anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR  : ", ""+anError.getErrorDetail());
                    }
                });
    }

    public void upload_image_ttd(){

        scanTTD = new File(filepath3);

        AndroidNetworking.upload("http://simpel.pasamanbaratkab.go.id/0.0.0.0_android/simaya/upload_image_ttd.php")
                .addMultipartFile("image_ttd", scanTTD)
                .addMultipartParameter("id_user", id_user)
                .setTag("Upload Image")
                .setPriority(Priority.HIGH)

                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    //    Toast.makeText(DaftarTTD.this, "ERROR : " + anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR  : ", ""+anError.getErrorDetail());
                    }
                });

    }

    public void send_data_esign(String aNama, String aNik, String aId_user, String aNama_instansi){


        AndroidNetworking.post(URl_ESIGN)
                .addBodyParameter("nama", aNama)
                .addBodyParameter("nik", aNik)
                .addBodyParameter("id_user", aId_user)
                .addBodyParameter("nama_instansi", aNama_instansi)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("ERROR  : ", ""+anError.getErrorDetail());
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1001:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Ijin DIterima!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Ijin Ditolak!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == RESULT_OK){
            filepath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            Bitmap myBitmap = BitmapFactory.decodeFile(filepath);

            ssKtp.setImageBitmap(myBitmap);

            // Log.d(TAG, String.valueOf(bitmap));



        }
        if(requestCode == 1100 && resultCode == RESULT_OK){
            filepath2 = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);



        }
        if(requestCode == 1200 && resultCode == RESULT_OK){
            filepath3 = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            Bitmap myBitmap2 = BitmapFactory.decodeFile(filepath3);

            ssTTD.setImageBitmap(myBitmap2);


        }
    }


    public void auth(final String ktp, final String sk, final String ttd) {


        StringRequest request = new StringRequest(Request.Method.POST, 0.0.0.0_DAFTAR_TTD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("onResponse", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Basic Authentication
                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                headers.put("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image_ttd", ttd);
                params.put("ktp", ktp);
                params.put("surat_rekomendasi", sk);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void daftar() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, 0.0.0.0_DAFTAR_TTD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaftarTTD.this,
                        "Error daftar : " + error.toString(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();

                params.put("Authorization", "Bearer " + token);
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Authorization", "Bearer 1077e2ca-7c8e-484e-85d8-0284a7bef30f,Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void auth2() {

        String URL = "http://0.0.0.0/0.0.0.0/user/registrasi?nik=137111610279000&nama=Nani Fajriani&email=nanifajriani316@yahoo.com&nomor_telepon=082386437418&kota=Pasaman Barat&provinsi=Sumatera Barat&nip=19790221 199711 2 001&jabatan=Kepala Bidang E-Goverment&unit_kerja=Diskominfo Pasaman Barat";


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DaftarTTD.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("Authorization", "Bearer 1077e2ca-7c8e-484e-85d8-0284a7bef30f");
                return header;
            }
        };

        mQueue.add(request);
    }
}
