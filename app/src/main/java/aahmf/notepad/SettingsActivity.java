package aahmf.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView sampleText;
    private Spinner spinnerText;
    private Spinner spinnerColor;
    private Spinner backgroundColor;
    private Button buttonApply,btnBack;
    private static final String[] paths = {"Falling Sky One", "Insomnia Regural", "Falling Sky Two", "Falling Sky Three", "Simple handwriting"};
    private static final String[] colours = {"Black", "White", "Red", "Green", "Yellow"};
    private static final String[] coloursTwo = {"White", "Black", "Red", "Green", "Yellow"};
    int fntClr,bgClr;
    Typeface face;
    private String fontPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sampleText = (TextView) findViewById(R.id.sampleTextView);
        spinnerText = (Spinner) findViewById(R.id.fontSpinner);
        spinnerColor = (Spinner) findViewById(R.id.spinnerColor);
        backgroundColor= (Spinner) findViewById(R.id.bckgroundColor);
        buttonApply= (Button) findViewById(R.id.btnApply);
        btnBack= (Button) findViewById(R.id.btnBack);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerText.setAdapter(adapter);
        spinnerText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        face = Typeface.createFromAsset(getAssets(), "fallingSkyOne.otf");
                        sampleText.setTypeface(face);
                        fontPath = "fallingSkyOne.otf";
                        break;
                    case 1:
                        face = Typeface.createFromAsset(getAssets(), "insomnia_regural.ttf");
                        sampleText.setTypeface(face);
                        fontPath ="insomnia_regural.ttf";
                        break;
                    case 2:
                        face = Typeface.createFromAsset(getAssets(), "fallingSkyTwo.otf");
                        sampleText.setTypeface(face);
                        fontPath = "faillingSkyTwo.otf";
                        break;

                    case 3:
                        face = Typeface.createFromAsset(getAssets(), "fallingSkyThree.otf");
                        sampleText.setTypeface(face);
                        fontPath = "fallingSkyThree.otf";
                        break;
                    case 4:
                        face = Typeface.createFromAsset(getAssets(), "simple_hand.ttf");
                        sampleText.setTypeface(face);
                        fontPath = "simple_hand.ttf";
                        break;

                }
                ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterOne = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_spinner_item, colours);

        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapterOne);
        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //black
                        sampleText.setTextColor(getResources().getColor(R.color.colorBlack));
                        fntClr =getResources().getColor(R.color.colorBlack);
                        break;
                    case 1:
                        //white
                        sampleText.setTextColor(getResources().getColor(R.color.colorWhite));
                        fntClr =getResources().getColor(R.color.colorWhite);

                        break;
                    case 2:
                        //red
                        sampleText.setTextColor(getResources().getColor(R.color.colorRed));
                        fntClr =getResources().getColor(R.color.colorRed);
                        break;
                    case 3:
                        //green
                        sampleText.setTextColor(getResources().getColor(R.color.colorGreen));
                        fntClr =getResources().getColor(R.color.colorGreen);
                        break;
                    case 4:
                        //yellow
                        sampleText.setTextColor(getResources().getColor(R.color.colorYellow));
                        fntClr =getResources().getColor(R.color.colorYellow);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterTwo = new ArrayAdapter<String>(SettingsActivity.this,
                android.R.layout.simple_spinner_item, coloursTwo);

        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundColor.setAdapter(adapterTwo);
        backgroundColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //white
                        sampleText.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        bgClr =getResources().getColor(R.color.colorWhite);
                        break;
                    case 1:
                        //black
                        sampleText.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                        bgClr =getResources().getColor(R.color.colorBlack);
                        break;
                    case 2:
                        //red
                        sampleText.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        bgClr =getResources().getColor(R.color.colorRed);
                        break;
                    case 3:
                        //green
                        sampleText.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        bgClr =getResources().getColor(R.color.colorGreen);
                        break;
                    case 4:
                        //yellow
                        sampleText.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                        bgClr =getResources().getColor(R.color.colorYellow);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mSharedPref = getSharedPreferences("Format", MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPref.edit();
                mEditor.putInt("Fcolor",fntClr);
                mEditor.putInt("Bgcolor",bgClr);
                mEditor.putString("font_path",fontPath);
                mEditor.apply();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,MainMenuActivity.class));
            }
        });


    }
}



