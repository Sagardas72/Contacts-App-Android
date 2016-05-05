package com.gsu.android.contactlistassignment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

public class ContactSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_settings);
        initMapButton();
        initListButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initBackgroundColorClick();
    }

    private void initListButton() {
        ImageButton map = (ImageButton) findViewById(R.id.imageButtonList3);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initMapButton() {
        ImageButton map = (ImageButton) findViewById(R.id.imageButtonMap3);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactSettingsActivity.this, ContactMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton settings = (ImageButton)findViewById(R.id.imageButtonSettings3);
        settings.setEnabled(false);
    }

    private void initSettings() {
        String sortby = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortfield","contactname");
        String sortorder = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");
        String backgroundcolor = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("backgroundcolor", "Blue");

        RadioButton rbName = (RadioButton)findViewById(R.id.radioName);
        RadioButton rbCity = (RadioButton)findViewById(R.id.radioCity);
        RadioButton rbBirthDay = (RadioButton)findViewById(R.id.radioBirthday);

        if(sortby.equalsIgnoreCase("contactname")) {
            rbName.setChecked(true);
        }
        else if(sortby.equalsIgnoreCase("city")) {
            rbCity.setChecked(true);
        }
        else {
            rbBirthDay.setChecked(true);
        }

        RadioButton ascending = (RadioButton)findViewById(R.id.radioAscending);
        RadioButton descending = (RadioButton)findViewById(R.id.radioDescending);
        if(sortorder.equalsIgnoreCase("ASC")) {
            ascending.setChecked(true);
        }
        else {
            descending.setChecked(true);
        }

        RadioButton blue = (RadioButton)findViewById(R.id.radioBlue);
        RadioButton green = (RadioButton)findViewById(R.id.radioGreen);
        ScrollView sview = (ScrollView)findViewById(R.id.scrollView2);
        if(backgroundcolor.equalsIgnoreCase("Blue")) {
            blue.setChecked(true);
            sview.setBackgroundResource(R.color.settings1_background);
        }
        else {
            green.setChecked(true);
            sview.setBackgroundResource(R.color.settings2_background);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = (RadioGroup)findViewById(R.id.rGroup1);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbName = (RadioButton)findViewById(R.id.radioName);
                RadioButton rbCity = (RadioButton)findViewById(R.id.radioCity);
                if(rbName.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "contactname").commit();
                }
                else if(rbCity.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "city").commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "birthday").commit();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOder = (RadioGroup)findViewById(R.id.rGroup2);
        rgSortOder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton asc = (RadioButton)findViewById(R.id.radioAscending);
                if(asc.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").commit();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").commit();
                }
            }
        });
    }

    private void initBackgroundColorClick() {
        RadioGroup rgBackgroundColor = (RadioGroup)findViewById(R.id.rGroup3);
        rgBackgroundColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton blue = (RadioButton)findViewById(R.id.radioBlue);
                ScrollView sview = (ScrollView)findViewById(R.id.scrollView2);
                if(blue.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("backgroundcolor", "Blue").commit();
                    sview.setBackgroundResource(R.color.settings1_background);
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit()
                            .putString("backgroundcolor", "Green").commit();
                    sview.setBackgroundResource(R.color.settings2_background);
                }
            }
        });
    }
}
