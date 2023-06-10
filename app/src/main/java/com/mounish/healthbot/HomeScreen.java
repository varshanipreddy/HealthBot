package com.mounish.healthbot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class HomeScreen extends AppCompatActivity implements AIListener {

    Button button;
    private LinearLayout listenButton;
    private TextView resultTextView;
    private TextView qsTextView;
    private AIService aiService;


    /*To Create the OptionsMenu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/optionsmenu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    /*Putting data and giving functionality to OptionsMenu*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // English Language is Selected
            case R.id.eng:
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_home_screen);

                button = (Button)findViewById(R.id.home_btn1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Chatbot.class);
                        startActivity(intent);
                    }
                });

                break;
            // Hindi Language is Selected
            case R.id.hin:
                languageToLoad = "hi"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_home_screen);


                button = (Button)findViewById(R.id.home_btn1);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Chatbot.class);
                        startActivity(intent);
                    }
                });
                break;
                // Tour is Selected, This will go to the 'WelcomeActivity2' as WelcomeActivity2 can be accessed any number of times even though is not first run of the app
            case R.id.tour:
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity2.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        listenButton = (LinearLayout) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        qsTextView = (TextView)findViewById(R.id.quesTextView);

        /*To establish connection with the Chatbot in the 'HomeScreen' to interact with the user via Speech*/

        final AIConfiguration config = new AIConfiguration("0c20744d179f4fa7abe6d3ae392b5ee8", AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }


    //Intent to go from HomeScreen to ChatBot Activity
        button = (Button)findViewById(R.id.home_btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Chatbot.class);
                startActivity(intent);
            }
        });
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 101);
    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    @Override
    public void onResult(final AIResponse response) {

        Result result = response.getResult();
        /*
        //Get Parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        */

        //Show Results in TextView

        resultTextView.setText("\nHealthBot:" + result.getFulfillment().getSpeech());
        qsTextView.setText("User:  " + result.getResolvedQuery());

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }


    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());

    }

    @Override
    public void onAudioLevel(final float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
