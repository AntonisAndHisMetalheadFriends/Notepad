package aahmf.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {


    private EditText Name;
    private EditText Password;
    private TextView FrgtPassword;
    private Button LogIn;
    private Button Register;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPsswrd);
        FrgtPassword = (TextView)findViewById(R.id.tvfrgtpass);
        LogIn = (Button)findViewById(R.id.btnLogIn);
        Register = (Button)findViewById(R.id.btnRgstr);


        Register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            }
        });
    }

}

