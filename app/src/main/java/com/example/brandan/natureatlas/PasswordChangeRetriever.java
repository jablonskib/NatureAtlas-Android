package com.example.brandan.natureatlas;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Brandan on 4/18/2016.
 */
//Sends a message to the server to send an email to the client to change their password.
public class PasswordChangeRetriever
{
    private JSONArray passwordJSON;

    public PasswordChangeRetriever(String username,  Context context) {


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



        HttpClient client = new DefaultHttpClient();



        String connect = "http://natureatlas.org/appServices/mailPasswordReset.php";
        HttpPost httpPost = new HttpPost(connect);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("email", username));

        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try
        {
            HttpResponse response = client.execute(httpPost);
            HttpEntity get = response.getEntity();

            if (get != null)
            {
                String ret = EntityUtils.toString(get);
                passwordJSON = new JSONArray(ret);



            }




        } catch (IOException e) {
            Log.d("IO", e.toString());
            e.printStackTrace();
        }  catch (Throwable t) {
            Log.d("throwable", t.toString());

        }
    }



    public JSONArray GetJSON()
    {
        return passwordJSON;
    }
}
