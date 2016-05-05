package com.gsu.android.contactlistassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dassa on 15-04-2016.
 */
public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open()throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertContact(Contact c) {
        boolean didSucceed = false;
        try{
            ContentValues initialValues = new ContentValues();
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.geteMail());
            initialValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

            byte[] photo = null;

            if(c.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                photo = baos.toByteArray();
            }
            initialValues.put("contactphoto", photo);

            didSucceed = database.insert("contact", null, initialValues) > 0;
        }
        catch(Exception e) {

        }
        return didSucceed;
    }

    public boolean updateContact(Contact c) {
        boolean didSucceed = false;
        try{
            Long rowId = Long.valueOf(c.getContactID());
            ContentValues updateValues = new ContentValues();
            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.geteMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().toMillis(false)));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] photo = baos.toByteArray();

            updateValues.put("contactphoto", photo);

            didSucceed = database.update("contact", updateValues, "_id="+rowId, null) > 0;
        }
        catch(Exception e) {

        }
        return didSucceed;
    }

    public boolean updateContactAddress(ContactAddress ca, Long rowId) {
        boolean didSucceed = false;
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put("streetaddress", ca.getStreetAddress());
            updateValues.put("city", ca.getCity());
            updateValues.put("state", ca.getState());
            updateValues.put("zipcode", ca.getZipCode());

            didSucceed = database.update("contact", updateValues, "_id="+rowId, null) > 0;
        }
        catch(Exception e) {
        }
        return didSucceed;
    }

    public int getLastContactId() {
        int lastID = -1;
        try{
            String query = "Select Max(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch(Exception e) {
            lastID = -1;
        }
        return lastID;
    }

    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<String>();
        try{
            String query = "SELECT contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                contactNames.add(cursor.toString());
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e) {
            contactNames = new ArrayList<String>();
        }
        return contactNames;
    }

    public ArrayList<Contact> getContacts(String sortField, String sortOrder) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try{
            String query = "SELECT * FROM contact ORDER BY "+ sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);
            Contact newContact;
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.seteMail(cursor.getString(8));
                Time t = new Time();
                t.set(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(t);

                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch(Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

    public boolean deleteContact(int contactID) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("contact", "_id="+contactID, null) > 0;
        }
        catch(Exception e) {
        }
        return didDelete;
    }

    public Contact getSpecificContact(int contactId) {
        Contact contact = new Contact();
        String query = "SELECT * FROM contact WHERE _id ="+contactId;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.seteMail(cursor.getString(8));
            Time t = new Time();
            t.set(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(t);

            if(cursor.getBlob(10) != null) {
                byte[] photo = cursor.getBlob(10);
                if(photo != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(photo);
                    Bitmap thePicture = BitmapFactory.decodeStream(bais);
                    contact.setPicture(thePicture);
                }
            }
            cursor.close();
        }
        return contact;
    }
}

