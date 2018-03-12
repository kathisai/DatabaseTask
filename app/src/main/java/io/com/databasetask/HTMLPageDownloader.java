package io.com.databasetask;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTMLPageDownloader extends AsyncTask<Void, Void, String> {
    public HTMLPageDownloaderListener listener;
    public String mLetter;
    public HTMLPageDownloader(String letter, HTMLPageDownloaderListener aListener) {
        listener = aListener;
        mLetter = letter;
    }

    @Override
    protected String doInBackground(Void... params) {
        String html = "";
        try {
            //Format URL
            URL url = new URL(String.format("http://unreal3112.16mb.com/wb1913_%s.html", mLetter));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in;
            in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            in.close();
            html = str.toString();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!isCancelled()) {
            listener.completionCallBack(mLetter, result);
        }
    }

    public interface HTMLPageDownloaderListener {
        void completionCallBack(String keyLetter, String html);
    }
}