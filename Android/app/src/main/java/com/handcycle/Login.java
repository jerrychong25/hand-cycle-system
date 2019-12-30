package com.handcycle;

// Import Android API
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.toolbox.Volley;

// Import Firebase API
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

// Import Firebase API 2
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText EmailText = (EditText) findViewById(R.id.EmailField);
        final EditText PasswordText = (EditText) findViewById(R.id.PasswordField);

        final Button LoginButton = (Button) findViewById(R.id.LoginButton);

        // Modify Status Bar Colour
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark)); //status bar or the time bar at the top
        }

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Toast.makeText(Login.this, "User have signed in", Toast.LENGTH_LONG).show();
                } else {
                    // User is signed out
//                    Toast.makeText(Login.this, "User have signed out", Toast.LENGTH_LONG).show();
                }
            }
        };

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = EmailText.getText().toString();
                String Password = PasswordText.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    EmailText.setError("Please enter your email");
                    Toast.makeText(Login.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(Password)) {
                    PasswordText.setError("Please enter your password");
                    Toast.makeText(Login.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    mAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if(task.isSuccessful()){
                                        //display some message here
                                        Toast.makeText(Login.this, "Login success", Toast.LENGTH_LONG).show();

                                        EmailText.getText().clear();
                                        PasswordText.getText().clear();

                                        Intent displayIntent = new Intent(Login.this, Display.class);
                                        startActivity(displayIntent);
                                    }
                                    else{
//                                       Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        Toast.makeText(Login.this, "Login failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
