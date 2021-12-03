package com.example.android.pollutioninfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    TextView textView;
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mycity = editText.getText().toString().toLowerCase().trim();
                String Pollution_Url = "https://api.waqi.info/feed/"+mycity+"/?token=9690654bfbd281ec21269e560fc42c8e71070267";
                PollutionAsyncTask task = new PollutionAsyncTask();
                task.execute(Pollution_Url);
            }
        });
    }
    private class PollutionAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            return Pollutioninfo.fetchPollutiondata(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null){
                textView.setText("");
            }
            textView.setText(s);
        }
    }
}