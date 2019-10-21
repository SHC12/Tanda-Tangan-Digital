package com.egov.win10.simpelv.TTD;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.egov.win10.simpelv.R;
import com.egov.win10.simpelv.TU.DetailSuratMasukTU;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadSign extends AppCompatActivity {


    String id_dokumen;

    TextView tvId_dokumen;

    private String URL_FILE_PDF = "http://0.0.0.0/api/sign/download/";


    String URL_PDF = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_sign);

        progressDialog = new ProgressDialog(DownloadSign.this);
        progressDialog.setMessage("Mohon Tunggu....");

        progressDialog.show();
        tvId_dokumen = (TextView) findViewById(R.id.tvId_dokumen);

        id_dokumen = getIntent().getStringExtra("id_dokumen");
        Toast.makeText(this, ""+id_dokumen, Toast.LENGTH_SHORT).show();

        tvId_dokumen.setText(id_dokumen);

        URL_PDF  = URL_FILE_PDF + id_dokumen;
        new Thread(new Runnable() {
            public void run() {
                DownloadFilesSign();



            }
        }).start();
        a(URL_PDF);
    }

    public void a(String aUrl){
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

    public void DownloadFilesSign() {

        try {
            java.net.URL u = new URL(URL_PDF);
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/coba.pdf"));

            while ((length = dis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            progressDialog.dismiss();



        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }
    }
}
