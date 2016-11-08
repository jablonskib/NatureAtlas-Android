package com.example.brandan.natureatlas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }


    public void SendChange(View view)
    {
        EditText user = (EditText)findViewById(R.id.changeUsert);


        if(user.getText().toString().equals(""))
        {
            Toast.makeText(this, "The username field is blank. Please fill out all fields.", Toast.LENGTH_SHORT).show();

        }
        else
        {
            PasswordChangeRetriever passwordChangeRetriever = new PasswordChangeRetriever(user.getText().toString(), this);
            try
            {
                if(passwordChangeRetriever.GetJSON().getJSONObject(0).toString().equals("Success"))
                {
                    Toast.makeText(this, "A password reset link has been sent to your email.", Toast.LENGTH_SHORT).show();
                }
                else if(passwordChangeRetriever.GetJSON().getJSONObject(0).toString().equals("Invalid Email"))
                {
                    Toast.makeText(this, "The email address you entered was invalid. Please try again.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
