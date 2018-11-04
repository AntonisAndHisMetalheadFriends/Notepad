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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

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


        final User user = new User(Name,username);








        if(Name.trim().isEmpty())
        {
           RealName.setError("Set A Name");
           RealName.requestFocus();
           return;
        }


        if(username.trim().isEmpty()==true)
        {
            Username.setError("Set a username");
            Username.requestFocus();
            return;
        }

        if(email.trim().isEmpty()==true)
        {
            Email.setError("Set An Email");
            Email.requestFocus();
            return;
        }
        if(password.trim().isEmpty()==true)
        {
            Pass.setError("Set a Password");
            Pass.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            Pass.setError("Password Must Be 6 Characters long");
            Pass.requestFocus();
            return;
        }

        if(retype.trim().isEmpty()==true)
        {
            Retype.setError("please retype your password");
            Retype.requestFocus();
            return;
        }

        if(!password.matches(retype))
        {
            Toast.makeText(RegisterActivity.this,"The Password And Retype Password Must Be The Same",Toast.LENGTH_LONG).show();
            return;
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
                                            sendVerificationEmail();
                                            Toast.makeText(RegisterActivity.this,"Registration Successfull check your email address for verification ",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));

                                        }

                                        else if(!task.isSuccessful())
                                        {

                                        }
                                    }
                                });
                    }

                    else if(!task.isSuccessful())
                    {
                        try
                        {
                            throw  task.getException();
                        }
                        catch(FirebaseAuthUserCollisionException MailExists)
                        {
                            Email.setError("Mail Already Exists In Database");
                            Email.requestFocus();
                            Email.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                            return;
                            //startActivity(new Intent(RegisterActivity.this,RegisterActivity.class));

                        }
                        catch (Exception e)
                        {

                        }
                    }
                }
            });
        }


    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
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
