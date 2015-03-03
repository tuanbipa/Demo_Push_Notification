package com.example.tuanpham.demo_push_notification;


import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String REGISTRATION_ID = "resgistration_id";
    public static final String SENDER_ID = "971547622722";

    static final String TAG = "MY_TAG";
    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(this);
        new RegisterBackground().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class RegisterBackground extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            try{
                if (gcm == null){
                    gcm = GoogleCloudMessaging.getInstance(context);

                }
                regid = gcm.register(SENDER_ID);

                msg = "Device registered, registration id  = "+regid;
                Log.d(TAG, msg);
                sendRegistrationIdToServer(regid);

            }
            catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

    @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!regid.contentEquals("")){
                Toast.makeText(context, "Register completed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendRegistrationIdToServer(String id){
        //api url
        String url = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("registrationId", id));
        params.add(new BasicNameValuePair("deviceId", android.os.Build.SERIAL));
        params.add(new BasicNameValuePair("userName", "Test"));
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try{
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        }
        catch (UnsupportedEncodingException ex){
            ex.printStackTrace();
        }

        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.d(TAG, "Response: " + httpResponse.toString());

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


