package com.example.android.pollutioninfo;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    TextView textView;
    Button button;
    EditText editText;
    TextView instruction;
    TextView heading;
    TextView aqi_name;
    String Pollution_Url="";
    View loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingIndicator = findViewById(R.id.loading_indicator);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        instruction = findViewById(R.id.instructions);
        heading = findViewById(R.id.heading);
        aqi_name = findViewById(R.id.aqi_name);
        loadingIndicator.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingIndicator.setVisibility(View.VISIBLE);
                String mycity = editText.getText().toString().toLowerCase().trim();
                Pollution_Url = "https://api.waqi.info/feed/" + mycity + "/?token=9690654bfbd281ec21269e560fc42c8e71070267";
                textView.setVisibility(View.VISIBLE);
                aqi_name.setVisibility(View.VISIBLE);
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo !=null && networkInfo.isConnected()) {
                    loadercall();
                }
                else
                {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    heading.setText("No internet connection");
                }
            }
        });
    }

    @Override
    public Loader<String> onCreateLoader(int id,Bundle args) {
        return new PollutionAsyncTask(this, Pollution_Url);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        loadingIndicator.setVisibility(View.GONE);
        if (s == null) {
            textView.setText("");
        }
        textView.setText(s);
        int magnitude = Integer.valueOf(s);
        GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
        if (magnitude < 0) {
            textView.setText("No data found for this city.");
            magnitudeCircle.setColor(getResources().getColor(R.color.white));
            textView.setTextColor(getResources().getColor(R.color.black));
            instruction.setText("Enter correct city name or try entering another city name.");
            aqi_name.setVisibility(View.INVISIBLE);
            heading.setText("");
        } else if (magnitude >= 0 && magnitude <= 50) {
            magnitudeCircle.setColor(getResources().getColor(R.color.Good));
            heading.setText("Good");
            instruction.setText(R.string.Good);
        } else if (magnitude > 50 && magnitude <= 100) {
            magnitudeCircle.setColor(getResources().getColor(R.color.Satisfactory));
            heading.setText("Satisfactory");
            instruction.setText(R.string.Satisfactory);
        } else if (magnitude > 100 && magnitude <= 150) {
            magnitudeCircle.setColor(getResources().getColor(R.color.Moderate));
            heading.setText("Moderate");
            instruction.setText(R.string.Moderate);
        } else if (magnitude > 150 && magnitude <= 200) {
            magnitudeCircle.setColor(getResources().getColor(R.color.Unhealthy));
            heading.setText("Unhealthy");
            instruction.setText(R.string.Unhealthy);
        } else if (magnitude > 200 && magnitude <= 300) {
            magnitudeCircle.setColor(getResources().getColor(R.color.very_Unhealthy));
            heading.setText("Very Unheathy");
            instruction.setText(R.string.Very_Unhealthy);
        } else {
            magnitudeCircle.setColor(getResources().getColor(R.color.Hazardous));
            heading.setText("Hazardous");
            instruction.setText(R.string.Hazardous);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }
    static class PollutionAsyncTask extends AsyncTaskLoader<String> {
        String mUrl;
        public PollutionAsyncTask(Context context,String url) {
            super(context);
            mUrl = url;
        }
        @Override
        protected void onStartLoading() {
            forceLoad();
        }
        @Override
        public String loadInBackground() {
            if(mUrl==null){
                return Pollutioninfo.fetchPollutiondata("");
            }
            return Pollutioninfo.fetchPollutiondata(mUrl);
        }
    }
    public void loadercall(){
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(1, null, this);
    }
}