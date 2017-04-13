package css.cis3334.firebaseauthentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    private TextView textViewStatus;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonGoogleLogin;
    private Button buttonCreateLogin;
    private Button buttonSignOut;

    private FirebaseAuth mAuth; //Firebas authorization object
    private FirebaseAuth.AuthStateListener mAuthListener; //Firebas authorization listener object

    /**
     * onCreate method creates everything needed for the app to run. Specifically to Firebase a couple
     * of instances needed to be initialized such as of the FirebaseAuth instance and AuthStateListener method
     * so tracking of when a user signs in or out can occur.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonGoogleLogin = (Button) findViewById(R.id.buttonGoogleLogin);
        buttonCreateLogin = (Button) findViewById(R.id.buttonCreateLogin);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //old log that said it was an ormal log in
                signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonCreateLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //old log saying the account has been created
                createAccount(editTextEmail.getText().toString(), editTextPassword.getText().toString());
            }
        });

        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("CIS3334", "Google login ");
                googleSignIn();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //old log saying signed out
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance(); //Initialize FirebaseAuth instance so it can track when a user signs in or out

        mAuthListener = new FirebaseAuth.AuthStateListener() { //Initialized AuthStateListener method so it can track when a user signs in or out

            /**
             * onAuthStateChanged method will create a Firebase user object so it caan be used to check if
             * there is an user signed in. The if statement is what actually checks is a user is signed in
             * by simply checking if there is a user or not when firebaseAuth.getCurrentUser is called. If it
             * returns null then the user is signed out and if it is not null then a user is signed in.
             *
             * @param firebaseAuth
             */

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Old log call that logged a User is signed in along with the Users ID

                } else {
                    // Old log call that there isn't a user signed in

                }

            }
        };
    }

    /**
     * onStart is attaching the listener for the authorization on this method.
     */
    @Override
    public void onStart() { //method header of no type and returns void
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); //FirebaseAuth object to access addAuthStateListener method
                                                   // to add a authorization state listener using FirebaseAuth.AuthStateListener
                                                   // as the parameter for the method call
    }

    /**
     * onStop removes the attached listener for the authorization
     */
    @Override
    public void onStop() { //method header of no type and returns void
        super.onStop();
        if (mAuthListener != null) { //As long as  FirebaseAuth.AuthStateListener object is not null...
            mAuth.removeAuthStateListener(mAuthListener); //FirebaseAuth object to access removeAuthStateListener method
                                                          // to remove a authorization state listener using FirebaseAuth.AuthStateListener
                                                          // as the parameter for the method call
        }
    }

    /**
     * createAccount method creates an user account on the FireBase by putting in the users email and password in the database
     *
     * @param email
     * @param password
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password) //FirebaseAuth object to access createUserWithEmailAndPassword method
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //FirebaseAuth object to access addOnCompleteListener method

                    /**
                     * onComplete method sets the Text View object to inform the account was created successfully.
                     * Otherwise in the If statement if the task failed then it would display a toast message saying so.
                     * Updated it now displays the authentication failed in the text view.
                     *
                     * @param task
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Old log that logged that the task was successful
                        textViewStatus.setText("Status: User Created!"); //Updated the Text View to let user know the account has been created

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            textViewStatus.setText("Authentication failed."); //Updated the Text View to let user know Authentication Failed
                            //old toast message here
                        }


                    }
                });
    }

    /**
     * signIn method signs the user in by connecting to the database to make sure the credential match with what is in the database.
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password) //FirebaseAuth object to access signInWithEmailAndPassword method
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //FirebaseAuth object to access addOnCompleteListener method

                    /**
                     * onComplete method sets the Text View object to inform the account was signed in successfully.
                     * Otherwise in the If statement if the task failed then it would display a toast message saying so.
                     * Updated it now displays the authentication failed in the text view.
                     * @param task
                     */

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //old log message saying the sign in was successful
                        textViewStatus.setText("Status: Signed In!"); //Updated the Text View to let user know they are signed in

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Old log message saying there was an exception
                            textViewStatus.setText("Authentication failed."); //Updated the Text View to let user know Authentication Failed
                            //old toast message here
                        }
                    }
                });
    }

    /**
     * signOut simply signs the user out by using the FirebaseAuth object to access signOut method.
     * It also changes the text view to updat let the user know they have been signed out.
     */
    private void signOut () {
        textViewStatus.setText("Status: Signed Out!"); //Updated the Text View to let user know they are signed out
        mAuth.signOut(); //FirebaseAuth object to access signOut method
    }

    private void googleSignIn() {

    }




}
