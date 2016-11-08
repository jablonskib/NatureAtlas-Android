package com.example.brandan.natureatlas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class FilterOptions extends AppCompatActivity {

    private Spinner radius, type;
    private EditText dateField;
    private DatePicker datePicker;
    private CheckBox currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_options);
        datePicker = (DatePicker)findViewById(R.id.datePicker);
        radius = (Spinner)findViewById(R.id.areaSpinner);
        type = (Spinner)findViewById(R.id.typeSpinner);
        dateField = (EditText)findViewById(R.id.dateField);
        currentUser = (CheckBox)findViewById(R.id.currentUser);

    }


    public void FilterBack(View view)
    {
        Intent resultIntent = new Intent();

        resultIntent.putExtra("radius", radius.getSelectedItem().toString());
        resultIntent.putExtra("type", type.getSelectedItem().toString());
        resultIntent.putExtra("dateObserved", dateField.getText().toString());
        resultIntent.putExtra("currentUser", Boolean.toString(currentUser.isChecked()));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    public void ChooseDate(View view)
    {
        dateField.setText(Integer.toString(datePicker.getYear()) + "-" +
                Integer.toString(datePicker.getMonth()) + "-" + Integer.toString(datePicker.getDayOfMonth()));
    }
}
