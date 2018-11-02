package aahmf.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private EditText RealName;
    private EditText Username;
    private EditText Email;
    private EditText Pass;
    private EditText Retype;
    private TextView Cancel;
    private Button Rgstr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();


    };


    private void setupUIViews(){
        Username = (EditText)findViewById(R.id.etUserName);
        RealName = (EditText)findViewById(R.id.etRealName);
        Email = (EditText)findViewById(R.id.etEmail);
        Pass = (EditText) findViewById(R.id.etUserpass);
        Retype = (EditText)findViewById(R.id.etRetypepass);
        Cancel = (TextView) findViewById(R.id.tvCancel);
        Rgstr = (Button) findViewById(R.id.btnRegister);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });
    }





};
