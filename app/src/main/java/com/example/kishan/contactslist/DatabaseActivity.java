package com.example.kishan.contactslist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseActivity extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "contactslist.db";
    public static final String TABLE_NAME = "contactslist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "PHONE_NUMBER";
    public static final String COl4 = "RELATIONSHIPS";
    Context mContext;

    public DatabaseActivity(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, PHONE_NUMBER TEXT, RELATIONSHIPS TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean addData(String name, String number)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);
        contentValues.put(COL3, number);
        contentValues.put(COl4, "");

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean deleteData(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Toast.makeText(mContext, id + "", Toast.LENGTH_SHORT).show();

        int res = db.delete(TABLE_NAME, "ID = ?", new String[]{id + ""});
        if(res > 0)
        {
            //Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            //Toast.makeText(mContext, "Failed!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean updateRelationship(int id, String relation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor prevrelation = getRow(id);
        prevrelation.moveToNext();

        //Toast.makeText(mContext, id + "", Toast.LENGTH_SHORT).show();
        //Toast.makeText(mContext, relation, Toast.LENGTH_SHORT).show();
        //Toast.makeText(mContext, prevrelation.getString(0), Toast.LENGTH_SHORT).show();
        //Toast.makeText(mContext, prevrelation.getString(1), Toast.LENGTH_SHORT).show();
        //Toast.makeText(mContext, prevrelation.getString(2), Toast.LENGTH_SHORT).show();

        String relationships = prevrelation.getString(2);
        if(relationships.isEmpty())
        {
            relationships += relation;
        }
        else
        {
            relationships += "," + relation;
        }

        contentValues.put(COL2, prevrelation.getString(0));
        contentValues.put(COL3, prevrelation.getString(1));
        contentValues.put(COl4, relationships);

        long result = db.update(TABLE_NAME, contentValues, COL1 + " = ?", new String[] {id + ""});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor getListContents()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor getId_Names()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL1 + ", " + COL2 + " FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor getName(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL2 + " FROM " + TABLE_NAME + " WHERE " +
                COL1 + " = ?", new String[] {id});
        return data;
    }

    public Cursor getRow(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL2 + ", " + COL3 + ", " + COl4 + " FROM "
                + TABLE_NAME + " WHERE " + COL1 + " = ?", new String[] {id + ""});
        return data;
    }

    public Cursor getRelationship(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COl4 + " FROM " + TABLE_NAME +
                " WHERE " + COL1 + " = ?", new String[] {id + ""});
        return data;
    }
}
