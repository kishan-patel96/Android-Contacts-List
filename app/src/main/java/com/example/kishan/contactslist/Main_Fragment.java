package com.example.kishan.contactslist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Main_Fragment extends Fragment {

    private ArrayList<String> fragContactList;
    private ArrayList<Integer> fragIds;
    private DatabaseActivity fragDb;
    private Button add;
    private Button delete;
    private ListView listView;
    private CustomAdapter fragCustomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_, container, false);

        fragContactList = Holder.getMainContactList();
        fragIds = Holder.getMainIds();
        fragDb = Holder.getMaindb();

        /*
        Toast.makeText(getContext(), l.size()+"", Toast.LENGTH_SHORT).show();

        for(String s : l)
        {
            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        }
        */

        listView = view.findViewById(R.id.frag_list);
        fragCustomAdapter = new CustomAdapter(getContext(), fragContactList);
        listView.setAdapter(fragCustomAdapter);

        add = view.findViewById(R.id.frag_Add);
        delete = view.findViewById(R.id.frag_Delete);

        goToDetails();
        goToProfile();
        deleteContacts();

        return view;
    }

    public void goToDetails()
    {
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(), "Clicked Add in Fragment", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getContext(), ContactDetails.class);
                startActivity(i);
            }
        });
    }

    public void goToProfile()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //Toast.makeText(getContext(), fragIds.get(position) + "", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getContext(), ContactProfile.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }

    public void deleteContacts()
    {
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int removed = 0;
                for(int id : fragCustomAdapter.ids)
                {
                    fragDb.deleteData(fragIds.get(id - 1 - removed));
                    fragContactList.remove(id - 1 - removed);
                    fragIds.remove(id - 1 - removed);
                    removed++;
                }
                fragCustomAdapter.notifyDataSetChanged();
                fragCustomAdapter.ids.clear();
            }
        });
    }
}
