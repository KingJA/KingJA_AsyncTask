package com.kingja.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView image;

    private String url="http://img.my.csdn.net/uploads/201407/26/1406383291_6518.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        image = (ImageView) findViewById(R.id.image);

    }

    public void start(View view) {
        new DownloadTask().execute(url);
    }

    class DownloadTask extends AsyncTask<String, Integer, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            byte[] result = null;
            ByteArrayOutputStream output = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); //允许输入流，即允许下载
                conn.setDoOutput(true); //允许输出流，即允许上传
                conn.setUseCaches(false); //不使用缓冲
                conn.setRequestMethod("GET"); //使用get请求
                conn.connect();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("getResponseCode", "Server returned HTTP " + conn.getResponseCode()
                            + " " + conn.getResponseMessage());
                }
                int fileLength = conn.getContentLength();
                Log.e("fileLength", "fileLength: "+fileLength );
                // download the file
                InputStream input = conn.getInputStream();
                output = new ByteArrayOutputStream();
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
//                        if (isCancelled()) {
//                            input.close();
//                            return null;
//                        }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) {
                        // only if total length is known
                        int progress = (int) (total * 100 / fileLength);
                        publishProgress(progress);
                        Log.e("progress", "" + progress);
                    }

                    output.write(data, 0, count);
                }
                result = output.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(byte[] result) {
            super.onPostExecute(result);
            Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
            image.setImageBitmap(bitmap);
        }
    }

}
