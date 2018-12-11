package com.example.kishan.contactslist;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CustomAdapter extends BaseAdapter
{
    private Context mContext;
    private List<String> contactsList;
    public final Set<Integer> ids;

    public CustomAdapter(Context context, List<String> contactsList)
    {
        this.mContext = context;
        this.contactsList = contactsList;
        ids = new TreeSet<>();
    }

    @Override
    public int getCount()
    {
        return contactsList.size();
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
        convertView = View.inflate(mContext, R.layout.activity_contact_list, null);

        TextView name = convertView.findViewById(R.id.list_name);
        name.setText(contactsList.get(position));

        CheckBox checkBox = convertView.findViewById(R.id.list_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    ids.add(position + 1);
                }
                else
                {
                    ids.remove(position + 1);
                }
            }
        });

        /*
        checkBox.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {

                return true;
            }
        });
        */

        name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(mContext, ContactProfile.class);
                i.putExtra("id", position);
                mContext.startActivity(i);
            }
        });

        return convertView;
    }
}
