package edu.augustana.javaturtles.sketchsend;


import android.app.ListActivity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import java.util.ArrayList;


public class ContactList extends ActionBarActivity {

    private static final String YOUR_CONTACTS = "Your Contacts";

    private EditText newContact;
    private ArrayList<String> contactsList;
    private SharedPreferences savedContacts;
    private int contactNumber;
    private ArrayAdapter<String> adapter;
    private ListView contactsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactsList = new ArrayList<String>();
        newContact = (EditText) findViewById(R.id.contactsEditText);
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        savedContacts = getSharedPreferences(YOUR_CONTACTS, MODE_PRIVATE);

//COMMENTED OUT TO ALLOW COMPILING
      //  adapter = new ArrayAdapter<String>(this, R.layout.list_item, contactsList);
        contactsListView.setAdapter(adapter);

        Button addContactButton = (Button) findViewById(R.id.addContact);
        addContactButton.setOnClickListener(addContactButtonListener);

        initializeContacts();

        contactNumber = contactsList.size();

    }

//    listener for addContactButton
    public View.OnClickListener addContactButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if (newContact.getText().length() > 0 ){
                addContact(newContact.getText().toString());
                newContact.setText("");

                
            }

        }


    };

    public void addContact(String newContact){
        contactsList.add(newContact);
        SharedPreferences.Editor preferencesEditor = savedContacts.edit();
        preferencesEditor.putString("contact" + (contactNumber -1), newContact);
        preferencesEditor.apply();

        adapter.notifyDataSetChanged();

    }

    public void initializeContacts(){
        for( int i = 0; i < contactsList.size(); i ++){
            contactsList.add(savedContacts.getString("contact" + i, "Logan Kruse"));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
