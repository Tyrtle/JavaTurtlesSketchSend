package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;


public class MainMenu extends ActionBarActivity {

    private final String TAG = "SketchSend";

    private ArrayList<String> userCredentials;
    private ImageButton sketchButton;
    private ImageButton inboxButton;
    private ImageButton contactsButton;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Creating sketchButton, inboxButton, contactsButton, and attaching listeners
        sketchButton = (ImageButton) findViewById(R.id.sketchButton);
        sketchButton.setOnClickListener(sketchMenuButtonHandler);

        inboxButton = (ImageButton) findViewById(R.id.inboxButton);
        inboxButton.setOnClickListener(inboxButtonHandler);

        contactsButton = (ImageButton) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(contactsButtonHandler);

        currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Welcome back " + currentUser.getUsername(), Toast.LENGTH_SHORT);
            toast.show();

        } else {
            //make buttons unclickable until after SignUpCallback
            createNewUser();
        }
//        //do something on Parse.com
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

    }

    public void createNewUser() {
        Log.w(TAG, "Create New User Call");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Create User");
        builder.setMessage("Please enter your Information");
        View dialogCustomView = inflater.inflate(R.layout.three_options_view, null);
        builder.setView(dialogCustomView);
        //Code that requires API 21 or higher
        //builder.setView(R.layout.three_options_view);
        final EditText username = (EditText) dialogCustomView.findViewById(R.id.UsernameEditText);
        final EditText email = (EditText) dialogCustomView.findViewById(R.id.EmailEditText);
        final EditText password = (EditText) dialogCustomView.findViewById(R.id.PasswordEditText);
        //In case it gives you an error for setView(View) try
        builder.setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser = new ParseUser();
                currentUser.setUsername(username.getText().toString());
                currentUser.setEmail(email.getText().toString());
                currentUser.setPassword(password.getText().toString());
                Log.w(TAG, "Credentials from createNewUser(): Username = " + currentUser.getUsername() + "Email = " + currentUser.getEmail());
                finishSignIn();
            }
        });

        builder.setCancelable(false);
        builder.show();

    }

    private void logIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Log In");
        builder.setMessage("Please enter your Login Information");
        View dialogCustomView = inflater.inflate(R.layout.two_options_view, null);
        builder.setView(dialogCustomView);
        //Code that requires API 21 or higher
        //builder.setView(R.layout.three_options_view);
        final EditText username = (EditText) dialogCustomView.findViewById(R.id.UsernameEditText);
        final EditText password = (EditText) dialogCustomView.findViewById(R.id.PasswordEditText);
        //In case it gives you an error for setView(View) try
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ParseUser.logIn(username.getText().toString(), password.getText().toString());
                    currentUser = ParseUser.getCurrentUser();
                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + currentUser.getUsername(), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (ParseException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Login. Please Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                    createNewUser();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    private void finishSignIn() {
        Log.w(TAG, "Credentials from onCreate(): Username = " + currentUser.getUsername() + "Email = " + currentUser.getEmail());
        Toast toast = Toast.makeText(getApplicationContext(), "Please wait while we check those credentials", Toast.LENGTH_SHORT);
        toast.show();
        currentUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + currentUser.getUsername(), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.w(TAG, "" + e.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Combination of Credentials.  Please Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                    createNewUser();
                }
            }
        });


    }

    //Listener for sketchMenuButton
    View.OnClickListener sketchMenuButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, DrawActivity.class);
            startActivity(playGameIntent);
        }
    };

    //Listener for inboxButton
    View.OnClickListener inboxButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, InboxList.class);
            startActivity(playGameIntent);
        }
    };

    //Listener for contactsButton
    View.OnClickListener contactsButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, ContactList.class);
            startActivity(playGameIntent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_user) {
            ParseUser.logOut();
            logIn();
            return true;
        }
        if (id == R.id.create_account) {
            ParseUser.logOut();
            createNewUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}