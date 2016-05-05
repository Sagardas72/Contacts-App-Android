package com.gsu.android.contactlistassignment;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by dassa on 17-04-2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact>{
    private ArrayList<Contact> items;
    private Context adapterContext;

    public ContactAdapter(Context context, ArrayList<Contact> items) {
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Contact contact = items.get(position);
            if(v == null) {
                LayoutInflater vi = (LayoutInflater)adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView contactName = (TextView)v.findViewById(R.id.textContactName);
            TextView phoneNumber = (TextView)v.findViewById(R.id.textPhoneNumber);
            TextView cellNumber = (TextView)v.findViewById(R.id.textCellNumber);
            TextView address = (TextView)v.findViewById(R.id.textAddress);
            ImageButton b = (ImageButton)v.findViewById(R.id.buttonDeleteContact);
            if(position % 2 == 0){
                contactName.setTextColor(Color.parseColor("#ff0000"));
            }
            else {
                contactName.setTextColor(Color.parseColor("#0000ff"));
            }
            contactName.setText(contact.getContactName());
            phoneNumber.setText("Home:" + contact.getPhoneNumber());
            cellNumber.setText("Cell:" + contact.getCellNumber());
            address.setText(contact.getStreetAddress()+", "+contact.getCity()+", "+contact.getState()+", "+contact.getZipCode());
            b.setVisibility(View.INVISIBLE);
        }
        catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return v;
    }

    public void showDelete(final int position, final View convertView, final Context context, final Contact contact) {
        View v = convertView;
        final ImageButton b = (ImageButton) v.findViewById(R.id.buttonDeleteContact);

        if(b.getVisibility() == View.INVISIBLE) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView, context);
                    items.remove(contact);
                    deleteOption(contact.getContactID(), context);
                }
            });
        }
        else {
            hideDelete(position, convertView, context);
        }
    }

    private void deleteOption(int contactToDelete, Context context) {
        ContactDataSource ds = new ContactDataSource(context);
        ds.open();
        ds.deleteContact(contactToDelete);
        ds.close();
        this.notifyDataSetChanged();
    }

    private void hideDelete(int position, View convertView, Context context) {
        final ImageButton b = (ImageButton) convertView.findViewById(R.id.buttonDeleteContact);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }
}
