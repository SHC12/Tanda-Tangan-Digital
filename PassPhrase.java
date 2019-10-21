package com.egov.win10.simpelv.TTD;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import com.egov.win10.simpelv.DetailSignature;
import com.egov.win10.simpelv.MainActivity;
import com.egov.win10.simpelv.R;

import com.egov.win10.simpelv.footer.HeaderFooterPageEvent;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PassPhrase extends AppCompatActivity {



    private String URL = "http://0.0.0.0/api_android/simaya/model_detail_konsep.php?token_surat=";
    private String URL_DETAIL_KONSEP = "";

    private String URLL = "http://0.0.0.0/api_android/simaya/model_penerima_tanda_tangan.php?id_surat=";
    private String URL_TTD = "";

    private String api_pdf_konsep = "http://0.0.0.0/api_android/simaya/get_file_pdf_disposisi_masuk.php?id_surat=";
    public String url_pdf_konsep = "";

    public static final String url_fcm = "http://0.0.0.0/api_android/simaya/push_fcm_tu_penerima_ttd.php?id_user=";

    private String URL_TANDA_TANGAN = "http://0.0.0.0/api_android/simaya/kirim_surat_signature.php";

    private String API_NOTIFIKASI_WEB_TU = "http://0.0.0.0/api_android/simaya/notifikasi_web_surat_masuk_TU.php";

    String Tag = "Signature";
    String URL_FILE = "";
    String nama_surat = "";

    String nama_lengkap = "";

    File scanFile;
    File scanImageTTD;


    TextView debug;

    public static final String PREFS_NAME = "MyPrefsFile";


    String id_surat = "";
    String token_surat = "";
    String nama_tujuan = "";
    String nama_pengirim = "";
    String judul_surat = "";
    String nomor_surat = "";
    String agenda_nomor_surat_keluar = "";
    String tgl_surat = "";
    String jenis_nota_dinas = "";

    String username = "";
    String id_instansi = "";



    String URL_FILE_SIGN = "";
    public String id_pengirim;

    String tembusan = "Tidak Ada";

    Button btnPassPhrase;


    private String URLLL = "http://0.0.0.0/api_android/simaya/menu_penerima_tembusan.php?id_surat=";
    private String URL_TEMBUSAN = "";

    private ProgressDialog progressDialog;


    private String URL_SIGN_DOKUMEN = "http://0.0.0.0/api/sign/pdf?nik=";
    String API_SIGN_DOKUMEN = "";




    private String url_update_ttd = "0.0.0.0/api_android/simaya/update_pdf_ttd.php";

    EditText editPassPhrase;

    String id_user;

    String FILE_NAME;

    String tgl_surat_format;


    String pathFile = "";
    String pathFile2 = "";
    String pathImageTTD = "";
    String NIK;
    String passphrase;

    String aUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_phrase);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

       // DownloadFilesIMG();




        debug = (TextView) findViewById(R.id.statPP);
        id_surat = getIntent().getStringExtra("id_surat");
        token_surat = getIntent().getStringExtra("token_surat");
        nama_tujuan = getIntent().getStringExtra("nama_tujuan");
        nama_pengirim = getIntent().getStringExtra("nama_pengirim");
        judul_surat = getIntent().getStringExtra("judul_surat");
        id_instansi = getIntent().getStringExtra("id_instansi");
        nomor_surat = getIntent().getStringExtra("nomor_surat");
        agenda_nomor_surat_keluar = getIntent().getStringExtra("no_agenda_surat_keluar");
        tgl_surat = getIntent().getStringExtra("tanggal_surat");
        jenis_nota_dinas = getIntent().getStringExtra("jenis_nota_dinas");
        tgl_surat_format = getIntent().getStringExtra("tanggal_surat_format");
        tembusan = getIntent().getStringExtra("nama_tembusan");

        URL_TTD = URLL + id_surat;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = settings.getString("username", "default");
        id_pengirim = settings.getString("id_user", "default");
        NIK = settings.getString("nik", "default");
        id_user = settings.getString("id_user", "default");
        nama_lengkap = settings.getString("nama_lengkap", "default");

        URL_FILE_SIGN = "http://0.0.0.0/upload/img_sign/"+id_user+".jpg";
        FILE_NAME = id_user+".jpg";


        pathImageTTD = Environment.getExternalStorageDirectory() + "/" + FILE_NAME;
/*
        Fragment fragment = null;
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_container, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
*/
        editPassPhrase = (EditText) findViewById(R.id.editPassPhrase);


        btnPassPhrase = (Button) findViewById(R.id.btnPassPhrase);



        // id_instansi = settings.getString("id_instansi", "default");


        progressDialog = new ProgressDialog(PassPhrase.this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        url_pdf_konsep = api_pdf_konsep + id_surat;

        URL_DETAIL_KONSEP = URL + token_surat;
        URL_TEMBUSAN = URLLL + id_surat;





        getPdf();


        btnPassPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passphrase = editPassPhrase.getText().toString();
                API_SIGN_DOKUMEN = URL_SIGN_DOKUMEN + NIK +"&passphrase="+passphrase+"&tampilan=visible&halaman=terakhir&image=true&xAxis=593.363&width=330.28&height=450.120&yAxis=28.70";
//                Toast.makeText(PassPhrase.this, ""+Environment.getExternalStorageDirectory() + "/" + nama_surat+"\n NIK : "+NIK+"\nAPI : "+API_SIGN_DOKUMEN, Toast.LENGTH_SHORT).show();

                // Toast.makeText(PassPhrase.this, ""+URL_FILE_SIGN, Toast.LENGTH_SHORT).show();
                //   Toast.makeText(PassPhrase.this, "IMG : "+Environment.getExternalStorageDirectory() + "/" + FILE_NAME, Toast.LENGTH_SHORT).show();


                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    sign_in_pdf(API_SIGN_DOKUMEN);
                    //connection is avlilable
                }else{
                    //no connection
                    Toast toast = Toast.makeText(PassPhrase.this, "No Internet Connection",
                            Toast.LENGTH_LONG);
                    toast.show();
                }

              // footer(pathFile);
                // debug.setText(Environment.getExternalStorageDirectory() + "/" + nama_surat+"\n NIK : "+NIK+"\nAPI : "+API_SIGN_DOKUMEN);
                //Toast.makeText(PassPhrase.this, "File : "+pathFile+"\nImage : "+pathImageTTD, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(), ""+nama_surat, Toast.LENGTH_SHORT).show();

               // Toast.makeText(PassPhrase.this, ""+pathFile2, Toast.LENGTH_SHORT).show();



            }
        });


    }



   public void footer() throws IOException, DocumentException {

       PdfReader reader = new PdfReader(pathFile);

       PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Environment.getExternalStorageDirectory() + "/"+"test.pdf"));
       PdfContentByte content = stamper.getOverContent(1);
       Image image = Image.getInstance(pathImageTTD);

        int number =  reader.getNumberOfPages();


// scale the image to 50px height
       image.scaleAbsoluteHeight(50);
       image.scaleAbsoluteWidth((image.getWidth() * 350) / image.getHeight());

       image.setAbsolutePosition(200, 240);

       content.addImage(image);
       stamper.close();
   }




    public void download_sign_pdf(String aUrl){
        AndroidNetworking.download(aUrl, "/storage/emulated/0/", "test.pdf")
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=")
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    public void sign_in_pdf(String aURL_SIGN){
        progressDialog = new ProgressDialog(PassPhrase.this);
        progressDialog.setMessage("Mohon Tunggu....");
        progressDialog.show();

        try {
            pathFile2 = Environment.getExternalStorageDirectory()+"/esign_"+nama_surat+".pdf";
            scanFile = new File(pathFile2);
            scanImageTTD = new File(pathImageTTD);


        } catch (Exception e) {

        }
        AndroidNetworking.upload(aURL_SIGN)
                .addMultipartFile("file", scanFile)
                .addMultipartFile("imageTTD", scanImageTTD)
                .setTag("Registrasi")
                .setPriority(Priority.HIGH)
                .addHeaders("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=")
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(okhttp3.Response response) {





                        if(response.code() == 400){

                            progressDialog.dismiss();
                            Toast.makeText(PassPhrase.this, "Kata sandi anda salah atau anda belum terdaftar", Toast.LENGTH_SHORT).show();

                            AndroidNetworking.post("http://0.0.0.0/api_android/simaya/log_failed.php")
                                    .addBodyParameter("id_user", id_user)
                                    .addBodyParameter("nama", nama_lengkap)
                                    .addBodyParameter("error_message", "Kata sandi anda salah atau anda belum terdaftar")
                                    .setTag("Upload Log failed")
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                        }

                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                        }else{
                            if (response.isSuccessful()) {
                                Log.d("AA", "Headers :" + response.headers());
                                aUrl = "http://0.0.0.0/api/sign/download/"+response.header("id_dokumen");
                                final String nama_surat_sign = "esign_"+nama_surat;
                                final String file_esign = Environment.getExternalStorageDirectory() + "/" + nama_surat_sign;




                                AndroidNetworking.download(aUrl, "/storage/emulated/0/", nama_surat_sign)
                                        .setTag("downloadTest")
                                        .setPriority(Priority.MEDIUM)
                                        .addHeaders("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=")
                                        .build()
                                        .setDownloadProgressListener(new DownloadProgressListener() {
                                            @Override
                                            public void onProgress(long bytesDownloaded, long totalBytes) {
                                                // do anything with progress

                                            }
                                        })
                                        .startDownload(new DownloadListener() {
                                            @Override
                                            public void onDownloadComplete() {
                                                // do anything after completion


                                                Intent intent = new Intent(PassPhrase.this, DetailSignature.class);
                                                intent.putExtra("nama_tujuan", nama_tujuan);
                                                intent.putExtra("nama_pengirim", nama_pengirim);
                                                intent.putExtra("judul_surat", judul_surat);
                                                intent.putExtra("nomor_surat", nomor_surat);
                                                intent.putExtra("no_agenda_surat_keluar", agenda_nomor_surat_keluar);
                                                intent.putExtra("tanggal_surat", tgl_surat);
                                                intent.putExtra("tanggal_surat_format", tgl_surat_format);
                                                intent.putExtra("jenis_nota_dinas", jenis_nota_dinas);


                                                intent.putExtra("nama_tembusan", tembusan);




                                                intent.putExtra("id_surat", id_surat);
                                                intent.putExtra("id_instansi", id_instansi);
                                                intent.putExtra("token_surat", token_surat);
                                                intent.putExtra("path_file_esign", nama_surat_sign);
                                                startActivity(intent);
                                                progressDialog.dismiss();

                                            }
                                            @Override
                                            public void onError(ANError error) {
                                                // handle error
                                                Log.e("PassPhrase2 ", "Error : " + error.getErrorBody());
                                                   //Toast.makeText(PassPhrase.this, "ER"+ error.getErrorBody(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                //    Toast.makeText(PassPhrase.this, ""+response.header("id_dokumen"), Toast.LENGTH_SHORT).show();

                            }
                        }

                        /*if (response.isSuccessful()) {
                            Log.d("AA", "Headers :" + response.headers());
                            aUrl = "http://0.0.0.0/api/sign/download/"+response.header("id_dokumen");
                            final String nama_surat_sign = "esign_"+nama_surat;
                            final String file_esign = Environment.getExternalStorageDirectory() + "/" + nama_surat_sign;




                            AndroidNetworking.download(aUrl, "/storage/emulated/0/", nama_surat_sign)
                                    .setTag("downloadTest")
                                    .setPriority(Priority.MEDIUM)
                                    .addHeaders("Authorization", "Basic YnNyZTpzZWN1cmV0cmFuc2FjdGlvbnMhISE=")
                                    .build()
                                    .setDownloadProgressListener(new DownloadProgressListener() {
                                        @Override
                                        public void onProgress(long bytesDownloaded, long totalBytes) {
                                            // do anything with progress

                                        }
                                    })
                                    .startDownload(new DownloadListener() {
                                        @Override
                                        public void onDownloadComplete() {
                                            // do anything after completion


                                            Intent intent = new Intent(PassPhrase.this, DetailSignature.class);
                                            intent.putExtra("nama_tujuan", nama_tujuan);
                                            intent.putExtra("nama_pengirim", nama_pengirim);
                                            intent.putExtra("judul_surat", judul_surat);
                                            intent.putExtra("nomor_surat", nomor_surat);
                                            intent.putExtra("no_agenda_surat_keluar", agenda_nomor_surat_keluar);
                                            intent.putExtra("tanggal_surat", tgl_surat);
                                            intent.putExtra("tanggal_surat_format", tgl_surat_format);
                                            intent.putExtra("jenis_nota_dinas", jenis_nota_dinas);


                                            intent.putExtra("nama_tembusan", tembusan);




                                            intent.putExtra("id_surat", id_surat);
                                            intent.putExtra("id_instansi", id_instansi);
                                            intent.putExtra("token_surat", token_surat);
                                            intent.putExtra("path_file_esign", nama_surat_sign);
                                            startActivity(intent);
                                            progressDialog.dismiss();

                                        }
                                        @Override
                                        public void onError(ANError error) {
                                            // handle error
                                            Log.e("PassPhrase2 ", "Error : " + error.getErrorDetail());
                                         //   Toast.makeText(PassPhrase.this, "A"+ error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        //    Toast.makeText(PassPhrase.this, ""+response.header("id_dokumen"), Toast.LENGTH_SHORT).show();

                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("PassPhrase ", "Error : " + anError.getErrorDetail());
                        Toast.makeText(PassPhrase.this, ""+anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                    }
                })

             ;
     //   Toast.makeText(PassPhrase.this, "File : "+pathFile+"\nImage : "+pathImageTTD, Toast.LENGTH_SHORT).show();

    }
    public void getPdf(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_pdf_konsep, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d(Tag, object.getString("response"));
                    JSONArray jsonArray = object.getJSONArray("response");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String surat_name = jsonObject.getString("surat_name");
                        String surat_link = jsonObject.getString("surat_link");

                        URL_FILE = surat_link;
                        nama_surat = surat_name;

                        pathFile = Environment.getExternalStorageDirectory() + "/" + nama_surat;
                    //    Toast.makeText(PassPhrase.this, ""+nama_surat, Toast.LENGTH_SHORT).show();


                        // Toast.makeText(DetailKonsep.this, ""+URL_FILE, Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            public void run() {
                                DownloadFiles();
                                progressDialog.dismiss();

                            }
                        }).start();


                    }
                    try {
                        footer2();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

    }
    public void footer2() throws IOException, DocumentException {
        Document document = new Document();

        PdfCopy copy = new PdfCopy(document, new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/esign_"+ nama_surat+".pdf")));
        document.open();

        PdfReader reader1 = new PdfReader(pathFile);
        int n1 = reader1.getNumberOfPages();

        PdfImportedPage page;
        PdfCopy.PageStamp stamp;
        Font ffont = new Font(Font.FontFamily.UNDEFINED, 5, Font.ITALIC);

        for (int i = 0; i < n1; ) {
            page = copy.getImportedPage(reader1, ++i);
            stamp = copy.createPageStamp(page);
            ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_CENTER,new Phrase(String.format("Dokumen ini telah ditandatangani secara elektronik menggunakan sertifikat elektronik yang diterbitkan BSrE")),297.5f, 28, 0);
            stamp.alterContents();
            copy.addPage(page);
        }

        document.close();
        reader1.close();
    }
    public void DownloadFiles() {

        try {
            java.net.URL u = new URL(URL_FILE);
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/" + nama_surat));

            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }




        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }




}
