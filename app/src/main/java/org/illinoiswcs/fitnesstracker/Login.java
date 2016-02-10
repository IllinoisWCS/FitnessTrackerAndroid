package org.illinoiswcs.fitnesstracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.util.Map;

public class Login extends FirebaseLoginBaseActivity{
    Firebase myFirebaseRef;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://techteam201516.firebaseio.com/");
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Registration");

        Button mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFirebaseLoginPrompt();
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = showFirebaseRegisterPrompt();
                dialog.show();
            }
        });
    }

    private AlertDialog showFirebaseRegisterPrompt(){
        LayoutInflater inflater = this.getLayoutInflater();
        final View register = inflater.inflate(R.layout.dialog_registration, null);
        builder.setView(register);

        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = ((EditText)register.findViewById(R.id.emailText)).getText().toString();
                String password = ((EditText) register.findViewById(R.id.passwordText)).getText().toString();
                String check = ((EditText) register.findViewById(R.id.checkPassword)).getText().toString();
                if (check.equals(password)) {
                    myFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Toast.makeText(Login.this, "Search Something", Toast.LENGTH_LONG).show();
                            startProfile();
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(Login.this, "Try again later", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                }
                else
                {
                    Toast.makeText(Login.this, "Passwords don't matched. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setEnabledAuthProvider(AuthProviderType.PASSWORD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Firebase getFirebaseRef() {
        return myFirebaseRef;
    }

    @Override
    public void onFirebaseLoginProviderError(FirebaseLoginError firebaseError) {
        Toast.makeText(Login.this, "Try Again!" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFirebaseLoginUserError(FirebaseLoginError firebaseError) {
        Toast.makeText(Login.this, "Some sort of server Error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        Toast.makeText(Login.this, "Yay! You logged in!", Toast.LENGTH_LONG).show();
        startProfile();
    }

    private void startProfile ()
    {
        startActivity(new Intent(this, Profile.class));
    }

    @Override
    public void onFirebaseLoggedOut() {
        Toast.makeText(Login.this, "You logged out!", Toast.LENGTH_LONG).show();
    }
}
