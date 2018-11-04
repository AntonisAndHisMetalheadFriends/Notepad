package aahmf.notepad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText RealName;
    private EditText Username;
    private EditText Email;
    private EditText Pass;
    private EditText Retype;
    private TextView Cancel;
    private Button Rgstr;
    private ProgressBar progressBar;


    public void RegisterUser()
    {
        String Name = RealName.getText().toString();
        String username = Username.getText().toString();
        String email = Email.getText().toString();
        String password = Pass.getText().toString();
        String retype = Retype.getText().toString();




        //uservars.setName(Name);
        //uservars.setUsername(username);

        final User user = new User(Name,username);








        if(Name.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please Enter Full Name",Toast.LENGTH_LONG).show();
        }

        if(email.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please Enter a valid email",Toast.LENGTH_LONG).show();
        }

        if(username.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please Enter a username",Toast.LENGTH_LONG).show();
        }

        if(password.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please Enter a password",Toast.LENGTH_LONG).show();
        }

        if(password.length()<6)
        {
            Toast.makeText(RegisterActivity.this,"Enter A Password Which Contains At Least 6 Characters or Numbers",Toast.LENGTH_LONG).show();
        }

        if(retype.isEmpty())
        {
            Toast.makeText(RegisterActivity.this,"Please Retype The Password You Entered",Toast.LENGTH_LONG).show();
        }

        if(!password.matches(retype))
        {
            Toast.makeText(RegisterActivity.this,"The Password And Retype Password Must Be The Same",Toast.LENGTH_LONG).show();
        }


        else
        {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {

                        progressBar.setVisibility(View.INVISIBLE);



                        mUsers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(RegisterActivity.this,"Registration Successfull Welcome to Our App ",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

                                        }
                                    }
                                });
                    }
                }
            });
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();


    };

    @Override
    protected void onStart() {
        super.onStart();

        progressBar.setVisibility(View.INVISIBLE);

        Rgstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });
    }

    private void setupUIViews(){
        Username = (EditText)findViewById(R.id.etUserName);
        RealName = (EditText)findViewById(R.id.etRealName);
        Email = (EditText)findViewById(R.id.etEmail);
        Pass = (EditText) findViewById(R.id.etUserpass);
        Retype = (EditText)findViewById(R.id.etRetypepass);
        Cancel = (TextView) findViewById(R.id.tvCancel);
        Rgstr = (Button) findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            }
        });
    }





};
