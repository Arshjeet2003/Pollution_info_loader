package com.example.android.pollutioninfo;

import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    TextView textView;
    Button button;
    EditText editText;
    TextView instruction;
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        instruction = findViewById(R.id.instructions);
        heading = findViewById(R.id.heading);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mycity = editText.getText().toString().toLowerCase().trim();
                String Pollution_Url = "https://api.waqi.info/feed/"+mycity+"/?token=9690654bfbd281ec21269e560fc42c8e71070267";
                PollutionAsyncTask task = new PollutionAsyncTask();
                task.execute(Pollution_Url);
                textView.setVisibility(View.VISIBLE);
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
            int magnitude = Integer.valueOf(s);
            GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
            if(magnitude>=0 && magnitude<=50) {
                magnitudeCircle.setColor(getResources().getColor(R.color.Good));
                heading.setText("Good");
                instruction.setText(R.string.Good);
            }
            else if(magnitude>50 && magnitude<=100) {
                magnitudeCircle.setColor(getResources().getColor(R.color.Satisfactory));
                heading.setText("Satisfactory");
                instruction.setText(R.string.Satisfactory);
            }
            else if(magnitude>100 && magnitude<=150) {
                magnitudeCircle.setColor(getResources().getColor(R.color.Moderate));
                heading.setText("Moderate");
                instruction.setText(R.string.Moderate);
            }
            else if(magnitude>150 && magnitude<=200){
                magnitudeCircle.setColor(getResources().getColor(R.color.Unhealthy));
                heading.setText("Unhealthy");
                instruction.setText(R.string.Unhealthy);
            }
            else if(magnitude>200 && magnitude<=300){
                magnitudeCircle.setColor(getResources().getColor(R.color.very_Unhealthy));
                heading.setText("Very Unheathy");
                instruction.setText(R.string.Very_Unhealthy);
            }
            else{
                magnitudeCircle.setColor(getResources().getColor(R.color.Hazardous));
                heading.setText("Hazardous");
                instruction.setText(R.string.Hazardous);
            }
        }
    }
}