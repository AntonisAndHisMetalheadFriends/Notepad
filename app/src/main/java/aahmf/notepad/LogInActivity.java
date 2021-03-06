package aahmf.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import static android.widget.Toast.makeText;

public class LogInActivity extends AppCompatActivity {

    private DatabaseReference mUsers = FirebaseDatabase.getInstance().getReference("Users");
    private EditText Name;
    private EditText Password;
    private TextView FrgtPassword;
    private Button LogIn;
    private Button Register;
    private FirebaseAuth mAuth;
    private String email,password;
    private ProgressBar progressBarLogin;
    private static  FirebaseUser user;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    String fbId = "",fbemail="";
    private static final String TAG = "LogInActivity";
    private FirebaseAuth.AuthStateListener firebaseAuthListner;







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_log_in);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPsswrd);
        FrgtPassword = findViewById(R.id.tvfrgtpass);
        LogIn = findViewById(R.id.btnLogIn);
        Register = findViewById(R.id.btnRgstr);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        FrgtPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, ResetPassword.class));
            }
        });

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
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Cancel",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListner = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

            }
        };

        progressBarLogin = findViewById(R.id.progressBarLogin);

        progressBarLogin.setVisibility(View.INVISIBLE);

    }

    private void checkIfEmailVerified()
    {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            Toast.makeText(LogInActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LogInActivity.this, MainMenuActivity.class));
            finish();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LogInActivity.this,"You Must Verify your account first",Toast.LENGTH_LONG).show();
            progressBarLogin.setVisibility(View.INVISIBLE);
            FirebaseAuth.getInstance().signOut();





        }
    }
    private void handleFacebookAccessToken(AccessToken accessToken)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),R.string.firebase_error_login, Toast.LENGTH_LONG).show();
                }

                else
                {
                    startActivity(new Intent(LogInActivity.this,MainMenuActivity.class));
                }
            }
        });
    }

    private void signIn(String email, String password)
    {
        if(Name.getText().toString().isEmpty())
        {
            Name.setError("Give An Email");
            Name.requestFocus();
            Name.setText("");
            return;
        }

        if(Password.getText().toString().isEmpty())
        {
            Password.setError("Give A Password");
            Password.requestFocus();
            Password.setText("");
            return;
        }

        if(Password.getText().toString().length()<6)
        {
            Password.setError("The password must be 6 Characters long");
            Password.requestFocus();
            Password.setText("");
            return;
        }

        progressBarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful())
                            {
                                // Sign in success


                                progressBarLogin.setVisibility(View.INVISIBLE);
                                checkIfEmailVerified();




                            } else
                            {
                                // If sign in fails, display a message to the user.
                                Context context = getApplicationContext();
                                CharSequence text = "Incorrect email/password";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toastOne = makeText(context, text, duration);
                                toastOne.show();
                                progressBarLogin.setVisibility(View.INVISIBLE);

                            }


                    }
                });
    }

    public static  FirebaseUser getUser() {
        return user;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth.addAuthStateListener(firebaseAuthListner);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListner);
    }


}




