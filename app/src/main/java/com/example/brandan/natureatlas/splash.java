package com.example.brandan.natureatlas;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;




public class splash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), com.example.brandan.natureatlas.SightingSubmit.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        Globals g = Globals.getInstance();


        //Initialize s3.
        CognitoCachingCredentialsProvider cccp = new CognitoCachingCredentialsProvider(getApplicationContext(),
                "us-east-1:c1b10d66-496c-4e5f-8b82-7361e2183dfe", Regions.US_EAST_1);
        AmazonS3 s3 = new AmazonS3Client(cccp);
        g.SetS3Instance(s3);


    }



}
