package com.gsu.android.contactlistassignment;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

public class ContactActivity extends FragmentActivity implements DatePickerDialog.SaveDateListener {

    private Contact currentContact;
    private ContactAddress newContactAddress;
    private static final int CAMERA_REQUEST = 1888;
    SensorManager sensorManager;
    Sensor proximitySensor;
    TextView textProximity;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Bundle extra = getIntent().getExtras();
        if(extra != null) {
            initContact(extra.getInt("contactid"));
        }
        else {
            currentContact = new Contact();
        }

        initListButton();
        initMapButton();
        initSettingsButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();
        initCallFunction();
        initCallIconVisiblity();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximitySensor != null) {
            sensorManager.registerListener(mySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else {
            Toast.makeText(this, "Sensor not found.", Toast.LENGTH_LONG).show();
        }
        textProximity = (TextView)findViewById(R.id.textProximity);
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                textProximity.setText(String.valueOf(event.values[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void initListButton() {
        ImageButton list = (ImageButton)findViewById(R.id.imageButtonList);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton map = (ImageButton)findViewById(R.id.imageButtonMap);
        map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactMapActivity.class);
                if(currentContact.getContactID() == -1) {
                    Toast.makeText(getBaseContext(), "Contact must be saved before it can be mapped", Toast.LENGTH_LONG).show();
                }
                else {
                    intent.putExtra("contactid", currentContact.getContactID());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton setting = (ImageButton)findViewById(R.id.imageButtonSettings);
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this, ContactSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editName = (EditText)findViewById(R.id.editName);
        EditText editAddress = (EditText)findViewById(R.id.editAddress);
        EditText editCity = (EditText)findViewById(R.id.editCity);
        EditText editState = (EditText)findViewById(R.id.editState);
        EditText editZipcode = (EditText)findViewById(R.id.editZipcode);
        EditText editPhone = (EditText)findViewById(R.id.editHome);
        EditText editCell = (EditText)findViewById(R.id.editCell);
        EditText editEmail = (EditText)findViewById(R.id.editEMail);
        Button btnChange = (Button)findViewById(R.id.btnBirthday);
        Button btnSave = (Button)findViewById(R.id.buttonSave);
        ImageButton ib = (ImageButton)findViewById(R.id.imageContact);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipcode.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        btnChange.setEnabled(enabled);
        btnSave.setEnabled(enabled);

        if(enabled) {
            editName.requestFocus();
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
        }
        else {
            ScrollView s = (ScrollView)findViewById(R.id.scrollView1);
            s.fullScroll(ScrollView.FOCUS_UP);
            s.clearFocus();
            ib.setOnClickListener(null);

        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = (EditText)findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editAddress = (EditText)findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
        EditText editCity = (EditText)findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(editCity.getWindowToken(), 0);
        EditText editState = (EditText)findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(), 0);
        EditText editZipCode = (EditText)findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editZipCode.getWindowToken(), 0);
        EditText editHome = (EditText)findViewById(R.id.editHome);
        imm.hideSoftInputFromWindow(editHome.getWindowToken(), 0);
        EditText editCell = (EditText)findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(editCell.getWindowToken(), 0);
        EditText editEmail = (EditText)findViewById(R.id.editEMail);
        imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
    }

    @Override
    public void didFinishDatePickerDialog(Time selectedTime) {
        TextView birthday = (TextView)findViewById(R.id.textBirthday);
        birthday.setText(DateFormat.format("MM/dd/yyyy", selectedTime.toMillis(false)).toString());
        currentContact.setBirthday(selectedTime);
    }

    private void initChangeDateButton() {
        Button changeDate = (Button)findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(getFragmentManager(), "DatePick");
            }
        });
    }

    private void initTextChangedEvents() {
        final EditText contactName = (EditText)findViewById(R.id.editName);
        contactName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(contactName.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText streetAddress = (EditText)findViewById(R.id.editAddress);
        streetAddress.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(streetAddress.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText city = (EditText)findViewById(R.id.editCity);
        city.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setCity(city.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText state = (EditText)findViewById(R.id.editState);
        state.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setState(state.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText zipCode = (EditText)findViewById(R.id.editZipcode);
        zipCode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(zipCode.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText phoneNumber = (EditText)findViewById(R.id.editHome);
        phoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(phoneNumber.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText cellNumber = (EditText)findViewById(R.id.editCell);
        cellNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(cellNumber.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText email = (EditText)findViewById(R.id.editEMail);
        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentContact.seteMail(email.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        cellNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initSaveButton() {
        Button save = (Button)findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(ContactActivity.this);
                ds.open();

                boolean wasSuccessful = false;
                if(currentContact.getContactID() == -1) {
                    wasSuccessful = ds.insertContact(currentContact);
                    int newId = ds.getLastContactId();
                    currentContact.setContactID(newId);
                }
                else if(currentContact.getContactName() == null && currentContact.getStreetAddress() != null) {
                    newContactAddress = new ContactAddress();
                    try {
                        Long rowId = Long.valueOf(currentContact.getContactID());
                        newContactAddress.setStreetAddress(currentContact.getStreetAddress());
                        newContactAddress.setCity(currentContact.getCity());
                        newContactAddress.setState(currentContact.getState());
                        newContactAddress.setZipCode(currentContact.getZipCode());

                        wasSuccessful = ds.updateContactAddress(newContactAddress, rowId);
                    }
                    catch(Exception e) {
                    }
                }
                else {
                    wasSuccessful = ds.updateContact(currentContact);
                }
                ds.close();

                if(wasSuccessful) {
                    ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    private void initContact(int id) {
        ContactDataSource ds = new ContactDataSource(ContactActivity.this);
        ds.open();
        currentContact = ds.getSpecificContact(id);
        ds.close();

        EditText name = (EditText)findViewById(R.id.editName);
        EditText address = (EditText)findViewById(R.id.editAddress);
        EditText city = (EditText)findViewById(R.id.editCity);
        EditText state = (EditText)findViewById(R.id.editState);
        EditText zipcode = (EditText)findViewById(R.id.editZipcode);
        EditText phoneNumber = (EditText)findViewById(R.id.editHome);
        EditText cellNumber = (EditText)findViewById(R.id.editCell);
        EditText email = (EditText)findViewById(R.id.editEMail);
        TextView birthday = (TextView)findViewById(R.id.textBirthday);
        ImageButton picture = (ImageButton)findViewById(R.id.imageContact);

        name.setText(currentContact.getContactName());
        address.setText(currentContact.getStreetAddress());
        city.setText(currentContact.getCity());
        state.setText(currentContact.getState());
        zipcode.setText(currentContact.getZipCode());
        phoneNumber.setText(currentContact.getPhoneNumber());
        cellNumber.setText(currentContact.getCellNumber());
        email.setText(currentContact.geteMail());
        birthday.setText(DateFormat.format("MM/dd/yyyy", currentContact.getBirthday().toMillis(false)).toString());
        if(currentContact.getPicture() != null) {
            picture.setImageBitmap(currentContact.getPicture());
        }
        else {
            picture.setImageResource(R.drawable.photoicon);
        }
    }

    private void initCallIconVisiblity() {
        ImageButton callButton1 = (ImageButton)findViewById(R.id.imageButtonCall);
        ImageButton callButton2 = (ImageButton)findViewById(R.id.imageButtonCallHome);
        if(currentContact.getContactID() == -1) {
            callButton1.setVisibility(View.INVISIBLE);
            callButton1.setVisibility(View.INVISIBLE);
        }
        else if (currentContact.getPhoneNumber() == null && currentContact.getCellNumber() == null) {
            callButton1.setVisibility(View.INVISIBLE);
            callButton2.setVisibility(View.INVISIBLE);
        }
        else if(currentContact.getPhoneNumber() != null && currentContact.getCellNumber() != null) {
            callButton1.setVisibility(View.VISIBLE);
            callButton2.setVisibility(View.VISIBLE);
        }
        else if(currentContact.getPhoneNumber() != null && currentContact.getCellNumber() == null) {
            callButton1.setVisibility(View.VISIBLE);
            callButton2.setVisibility(View.INVISIBLE);
        }
        else {
            callButton1.setVisibility(View.INVISIBLE);
            callButton2.setVisibility(View.VISIBLE);
        }
    }

    private void initCallFunction() {
        ImageButton phone = (ImageButton) findViewById(R.id.imageButtonCall);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContact(currentContact.getCellNumber());
            }
        });

        ImageButton cell = (ImageButton) findViewById(R.id.imageButtonCallHome);
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callContact(currentContact.getPhoneNumber());
            }
        });
    }

    private void callContact(String n){
        number = n;
        ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 2);

    }

    public void takePhoto() {
        ActivityCompat.requestPermissions(ContactActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults) {
        switch(requestCode) {
            case 1: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                return;
            }
            case 2: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    try {
                        startActivity(intent);
                    }
                    catch(SecurityException e){
                    }
                }
                return;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST) {
            if(resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap)data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 350, 350, true);
                ImageButton imageContact = (ImageButton)findViewById(R.id.imageContact);
                imageContact.setImageBitmap(scaledPhoto);
                currentContact.setPicture(scaledPhoto);
            }
        }
    }
}