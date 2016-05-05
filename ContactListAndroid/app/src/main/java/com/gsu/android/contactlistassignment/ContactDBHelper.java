package com.gsu.android.contactlistassignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dassa on 15-04-2016.
 */
public class ContactDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "mycontacts1.db";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_TABLE_CONTACT = "create table contact(_id integer primary key autoincrement," +
            "contactname text not null, streetaddress text, city text, state text, zipcode text, phonenumber text," +
            "cellnumber text, email text, birthday text, contactphoto blob);";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CONTACT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDBHelper.class.getName(), "Upgrading database from version" + oldVersion + " to " + newVersion +
                ", which will destroy all old data");
        try {
            db.execSQL("ALTER TABLE contact ADD COLUMN BFF integer");
            db.execSQL("ALTER TABLE contact ADD COLUMN contactphoto blob");
        }
        catch(Exception e){
        }
        //onCreate(db);
    }
}
