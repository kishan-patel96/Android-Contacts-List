package com.example.kishan.contactslist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public DatabaseActivity mainDb;
    public CustomAdapter mainCustomAdapter;
    public List<String> mainContactsList;
    public List<Integer> mainIds;
    private Button add;
    private Button delete;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainDb = new DatabaseActivity(this);

        setTitle("Contacts");
        centerTitle();

        if(findViewById(R.id.main_land) != null)
        {
            //Toast.makeText(MainActivity.this, "Main Landscape!", Toast.LENGTH_SHORT).show();

            Holder.setMainContactList((ArrayList<String>)mainContactsList);
            Holder.setMainIds((ArrayList<Integer>) mainIds);
            Holder.setMaindb(mainDb);
        }

        mainIds = new ArrayList<>();
        mainContactsList = new ArrayList<>();
        listView = findViewById(R.id.main_list);
        mainCustomAdapter = new CustomAdapter(this, mainContactsList);
        listView.setAdapter(mainCustomAdapter);

        add = findViewById(R.id.main_Add);
        delete = findViewById(R.id.main_Delete);

        initializeList();
        goToDetails();
        goToProfile();
        deleteContacts();
    }

    public void initializeList()
    {
        mainIds.clear();
        mainContactsList.clear();

        Cursor res = mainDb.getId_Names();
        while(res.moveToNext())
        {
            mainIds.add(Integer.parseInt(res.getString(0)));
            mainContactsList.add(res.getString(1));
        }
        mainCustomAdapter.notifyDataSetChanged();
    }

    public void goToDetails()
    {
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, ContactDetails.class);
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
                //Toast.makeText(MainActivity.this, mainIds.get(position) + "", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this, ContactProfile.class);
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
                for(int id : mainCustomAdapter.ids)
                {
                    mainDb.deleteData(mainIds.get(id - 1 - removed));
                    mainContactsList.remove(id - 1 - removed);
                    mainIds.remove(id - 1 - removed);
                    removed++;
                }
                mainCustomAdapter.notifyDataSetChanged();
                mainCustomAdapter.ids.clear();
            }
        });
    }

    public void centerTitle()
    {
        ArrayList<View> textViews = new ArrayList<>();
        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }
}
