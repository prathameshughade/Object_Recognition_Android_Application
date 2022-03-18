package com.example.imageclass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class Trans_4 extends AppCompatActivity {

    TextView tv3,tv4,tv5,tv6,tv7;
    private TextToSpeech mTTS;
    private float pitch = (float) 0.5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
        Button btenglish, btgerman,btfrench,bthindi,btspanish, home;

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Lang Not SUpported");
                    } else {
                        Log.e("TTS", "Lang Not SUpported");
                    }
                } else {
                    Log.e("TTS", "dd");
                }
            }
        });

        btenglish = findViewById(R.id.btenglish);
        btgerman = findViewById(R.id.btgerman);
        btfrench = findViewById(R.id.btFrench);
        btspanish = findViewById(R.id.btspanish);
        bthindi = findViewById(R.id.btHindi);
        home = findViewById(R.id.first_page);


        Intent intent4 = getIntent();
        String message = intent4.getStringExtra(EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        tv3 = findViewById(R.id.textenglish);
        tv4 = findViewById(R.id.textspanish);
        tv5 = findViewById(R.id.textFrench);
        tv6 = findViewById(R.id.textGerman);
        tv7 = findViewById(R.id.textHindi);
        tv3.setText(message);
        String ext = message;

        btenglish.setOnClickListener(v -> {
            String Text = tv3.getText().toString();
            mTTS.setPitch(pitch);
            mTTS.speak(Text, TextToSpeech.QUEUE_FLUSH,null);
        });

        btspanish.setOnClickListener(v -> {
            String Text = tv4.getText().toString();
            mTTS.setPitch(pitch);
            mTTS.speak(Text, TextToSpeech.QUEUE_FLUSH,null);
        });

        btfrench.setOnClickListener(v -> {
            String Text = tv5.getText().toString();
            mTTS.setPitch(pitch);
            mTTS.speak(Text, TextToSpeech.QUEUE_FLUSH,null);
        });

        btgerman.setOnClickListener(v -> {
            String Text = tv6.getText().toString();
            mTTS.setPitch(pitch);
            mTTS.speak(Text, TextToSpeech.QUEUE_FLUSH,null);
        });

        bthindi.setOnClickListener(v -> {
            String Text = tv7.getText().toString();
            mTTS.setPitch(pitch);
            mTTS.speak(Text, TextToSpeech.QUEUE_FLUSH,null);
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Trans_4.this, Main_1.class);
            startActivity(intent);
        });


        /*try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Rahul\\AndroidStudioProjects\\ImageClass\\app\\src\\main\\assets\\new_word.csv"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if ((values[1].toLowerCase()).equals(ext.toLowerCase())) {
                    tv4.setText(values[1]);
                    break;
                }
                br.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("new_word.csv")));//Specify asset file name
            //in open();
            for (; ;) {
                next = reader.readNext();
                if (next != null) {
                    if ((next[1].toLowerCase()).equals(ext.toLowerCase())) {
                        tv4.setText(next[2]);
                        tv5.setText(next[3]);
                        tv6.setText(next[4]);
                        tv7.setText(next[5]);



                    }
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }


}
