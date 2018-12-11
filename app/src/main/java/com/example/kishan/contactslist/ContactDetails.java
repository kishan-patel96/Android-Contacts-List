package com.example.kishan.contactslist;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactDetails extends MainActivity
{
    private Button addPerson;
    public CustomAdapter detailsCustomAdapter;
    public DatabaseActivity detailsDb;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Holder.setMainContactList((ArrayList<String>) mainContactsList);
        Holder.setMainIds((ArrayList<Integer>) mainIds);
        Holder.setMaindb(mainDb);
        setContentView(R.layout.activity_contact_details);
        setTitle("Contact Details");
        centerTitle();

        if(findViewById(R.id.details_land) != null)
        {
            //Toast.makeText(ContactDetails.this, "Details Landscape!", Toast.LENGTH_SHORT).show();

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.details_frag))
                    .commit();

        }

        detailsDb = mainDb;
        //Intent myIntent = getIntent();
        //myIntent.get

        addPerson = findViewById(R.id.details_Add);

        listView = findViewById(R.id.detail_list);
        detailsCustomAdapter = new CustomAdapter(this, mainContactsList);
        listView.setAdapter(detailsCustomAdapter);
        detailsCustomAdapter.notifyDataSetChanged();

        addPerson();
        goToProfile2();
    }

    public void addPerson()
    {
        addPerson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(ContactDetails.this, "Contact Added!", Toast.LENGTH_SHORT).show();

                EditText name = findViewById(R.id.details_name);
                EditText number = findViewById(R.id.details_number);

                //Check name && number
                if(name.getText().toString().isEmpty())
                {
                    Toast.makeText(ContactDetails.this, "Invalid Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                name.setText(name.getText().toString().toLowerCase());
                String[] names = name.getText().toString().split(" ");
                String n = "";
                for(int i = 0; i < names.length; i++)
                {
                    names[i] = Character.toUpperCase(names[i].charAt(0)) + names[i].substring(1, names[i].length());
                    n += names[i] + " ";
                }
                name.setText(n.trim());

                if(!number.getText().toString().equals("") && !number.getText().toString().matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}"))
                {
                    Toast.makeText(ContactDetails.this, "Invalid Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean res = mainDb.addData(name.getText().toString(), number.getText().toString());
                if(res)
                {
                    Toast.makeText(ContactDetails.this, "Contact Added!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ContactDetails.this, "Failed to Add Contact!", Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(ContactDetails.this, detailsCustomAdapter.ids.size() + "", Toast.LENGTH_SHORT).show();
                initializeList();
                for(int id : detailsCustomAdapter.ids)
                {
                    //Toast.makeText(ContactDetails.this, mainIds.get(id - 1) + "", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ContactDetails.this, mainIds.get(mainContactsList.size() - 1) + "", Toast.LENGTH_SHORT).show();

                    //Cursor cursor = mainDb.getRelationship(mainIds.get(id - 1));
                    //cursor.moveToNext();
                    //Toast.makeText(ContactDetails.this, cursor.getString(0), Toast.LENGTH_SHORT).show();

                    mainDb.updateRelationship(mainIds.get(id - 1), mainIds.get(mainContactsList.size() - 1) + "");
                    mainDb.updateRelationship(mainIds.get(mainContactsList.size() - 1), mainIds.get(id - 1) + "");

                    /*
                    if(mainDb.updateRelationship(mainIds.get(id - 1), mainIds.get(mainContactsList.size() - 1) + "") &&
                    mainDb.updateRelationship(mainIds.get(mainContactsList.size() - 1), mainIds.get(id - 1) + ""))
                    {
                        Toast.makeText(ContactDetails.this, "Updated Relationship!", Toast.LENGTH_SHORT).show();
                    }
                    */
                }

                Intent i = new Intent(ContactDetails.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public void goToProfile2()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(ContactDetails.this, mainIds.get(position) + "", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ContactDetails.this, ContactProfile.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }
}
