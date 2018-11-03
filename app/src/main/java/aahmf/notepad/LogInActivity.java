package aahmf.notepad;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.makeText;

public class LogInActivity extends AppCompatActivity {


    private EditText Name;
    private EditText Password;
    private TextView FrgtPassword;
    private Button LogIn;
    private Button Register;
    private FirebaseAuth mAuth;
    private String email,password;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPsswrd);
        FrgtPassword = (TextView)findViewById(R.id.tvfrgtpass);
        LogIn = (Button)findViewById(R.id.btnLogIn);
        Register = (Button)findViewById(R.id.btnRgstr);
        mAuth = FirebaseAuth.getInstance();


        Register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            }
        });

        LogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Name.getText().toString();
                password = Password.getText().toString();
                signIn(email,password);

            }
        });
    }


    private void signIn(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in succes
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LogInActivity.this, RegisterActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Context context = getApplicationContext();
                            CharSequence text = "Incorrect email/password";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toastOne = makeText(context, text, duration);
                            toastOne.show();

                        }


                    }
                });
    }

}




