package io.com.databasetask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.com.databasetask.database.DBHandler;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_download)
    Button download;

    @BindView(R.id.tv_time)
    TextView timeTaken;

    @BindView(R.id.btn_store_db)
    Button storeDB;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String[] mTestArray;
    private HashMap<String, String> mapDictionary = new HashMap<>();
    private DBHandler db;
    private String TAG = MainActivity.class.getSimpleName();
    HTMLPageDownloader.HTMLPageDownloaderListener listener = new HTMLPageDownloader.HTMLPageDownloaderListener() {
        @Override
        public void completionCallBack(String keyLetter, String html) {
            Log.d(TAG, "HTML Result: " + html);
            mapDictionary.put(keyLetter, html);
            if (keyLetter.equalsIgnoreCase("z")) {
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTestArray = getResources().getStringArray(R.array.charecters);
        mapDictionary.clear();
        db = new DBHandler(this);
        timeTaken.setVisibility(View.GONE);

    }

    @OnClick(R.id.btn_download)
    void download() {
        progressBar.setVisibility(View.VISIBLE);
        mapDictionary.clear();
        for (String alphabit :
                mTestArray) {
            new HTMLPageDownloader(alphabit, listener).execute();
        }
    }

    @OnClick(R.id.btn_store_db)
    void storeIntoDB() {
        if (mapDictionary != null && mapDictionary.size() != 0) {
            long startTime = System.nanoTime();
            Log.d(TAG, "startTime:" + startTime);
            for (Map.Entry<String, String> item : mapDictionary.entrySet()) {
                db.addLetterContent(item.getKey(), item.getValue());
            }
            long endTime = System.nanoTime();
            Log.d(TAG, "endTime:" + endTime);
            timeTaken.setVisibility(View.VISIBLE);
            long elapsedTime = endTime - startTime;
            double seconds = (double) elapsedTime / 1000000000.0;
            timeTaken.setText("Time taken for DB insert in nano seconds : " + seconds);

        } else {
            Toast.makeText(this, "Please click on Download button to start downloading files", Toast.LENGTH_SHORT).show();
        }
    }


}
