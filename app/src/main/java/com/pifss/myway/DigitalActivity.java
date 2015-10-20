package com.pifss.myway;
import java.net.URI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DigitalActivity extends Activity {

    Context context;

    EditText etDA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital);
        etDA = (EditText) findViewById(R.id.DAddress);
        Button submit = (Button) findViewById(R.id.SubmitBT);

        context = this;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Hello ommmmmaaaar",Toast.LENGTH_LONG).show();
                new RetrieveDigitalAddressTask().execute();
            }
        });

    }

    class RetrieveDigitalAddressTask extends AsyncTask <Void, Integer, String>{

        @Override
        protected String doInBackground(Void... params) {



            try {
                URI uri = new URI("http://54.88.107.56:80/MyWayWeb/DigitalAddress");//put servlet name here!!
                DefaultHttpClient client = new DefaultHttpClient();

                HttpPost post = new HttpPost(uri);

                ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();
                urlparameters.add(new BasicNameValuePair("digitalAddress", etDA.getText().toString()));

                post.setEntity(new UrlEncodedFormEntity(urlparameters));

                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);

                return data;
            }catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonData = new JSONObject(s);

                JSONArray resultData = new JSONArray(jsonData.getString("result_data"));

                JSONObject location = resultData.getJSONObject(0);



                Intent i = new Intent(DigitalActivity.this, MapPointActivity.class);
                i.putExtra("name", "");
                i.putExtra("lat", location.getString("latitude"));
                i.putExtra("long",location.getString("longitude") );
                startActivity(i);
                finish();
                
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}
