package com.example.kishan.contactslist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactProfile extends MainActivity
{
    private String[] relations;
    private ProfileCustomAdapter profileCustomAdapter;
    private int id;
    private TextView profileName;
    private TextView profileNumber;
    private Map<Integer, Integer> clickPosToIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Holder.setMainContactList((ArrayList<String>) mainContactsList);
        Holder.setMainIds((ArrayList<Integer>) mainIds);
        Holder.setMaindb(mainDb);
        setContentView(R.layout.activity_contact_profile);
        setTitle("Contact Profile");
        centerTitle();

        if(findViewById(R.id.details_land) != null)
        {
            //Toast.makeText(ContactProfile.this, "Profile Landscape!", Toast.LENGTH_SHORT).show();

            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.profile_frag))
                    .commit();

        }

        Intent i = getIntent();
        id = i.getIntExtra("id", -1);


        //Toast.makeText(ContactProfile.this, id + "", Toast.LENGTH_SHORT).show();


        if(i == null || id == -1 || !(id < mainIds.size() && id >= 0))
        {
            Toast.makeText(ContactProfile.this, "Id does not exist!", Toast.LENGTH_SHORT).show();

            return;
        }

        id = mainIds.get(id);

        //Toast.makeText(ContactProfile.this, "Id: " + id, Toast.LENGTH_SHORT).show();



        if(id == -1)
        {
            Toast.makeText(ContactProfile.this, "Id does not exist!", Toast.LENGTH_SHORT).show();

            return;
        }

        Cursor res = mainDb.getRow(id);
        res.moveToNext();


        if(res == null || !(res.getCount() >= 1))
        {
            Toast.makeText(ContactProfile.this, "Id does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }


        String name = res.getString(0);
        String number = res.getString(1);

        if(!res.getString(2).isEmpty())
        {
            relations = res.getString(2).split(",");
        }
        else
        {
            relations = new String[0];
        }

        clickPosToIdx = new HashMap<>();
        int posIdx = 0;
        for(String s : relations)
        {
            //Toast.makeText(ContactProfile.this, s, Toast.LENGTH_SHORT).show();

            clickPosToIdx.put(posIdx++, mainIds.indexOf(Integer.parseInt(s)));
        }

        profileName = findViewById(R.id.profile_name);
        profileNumber = findViewById(R.id.profile_number);

        profileName.setText(name);
        profileNumber.setText(number);

        ListView listView = findViewById(R.id.profile_list);
        profileCustomAdapter = new ProfileCustomAdapter();
        listView.setAdapter(profileCustomAdapter);
        profileCustomAdapter.notifyDataSetChanged();
    }

    public class ProfileCustomAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return relations.length;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            convertView = getLayoutInflater().inflate(R.layout.activity_contact_list2, null);

            TextView name = convertView.findViewById(R.id.list2_name);

            Cursor profilecheck = mainDb.getName(id + "");
            if(profilecheck == null || !(profilecheck.getCount() >= 1))
            {
                profileName.setText("");
                profileNumber.setText("");
                profileCustomAdapter.notifyDataSetChanged();
                return convertView;
            }

            Cursor cursor = mainDb.getName(relations[position]);
            if(cursor == null || !(cursor.getCount() >= 1))
            {
                profileCustomAdapter.notifyDataSetChanged();
                return convertView;
            }
            cursor.moveToNext();

            name.setText(cursor.getString(0));
            name.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Toast.makeText(ContactProfile.this, relations[position] + "", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ContactProfile.this, id + "", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ContactProfile.this, ContactProfile.class);
                    //int pos = (Integer.parseInt(relations[position]) > id) ? position + 1:position;
                    i.putExtra("id", clickPosToIdx.get(position));
                    startActivity(i);
                }
            });

            return convertView;
        }
    }
}
