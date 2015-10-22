package com.pifss.myway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DriverDestenationList extends Activity {
	static ArrayList<Driver> driverDesList = new ArrayList<Driver>();
	TextView tvDestination;
	//	LocationManager lm ;
//	Location location ;
//	Calendar cal ;
//	Date currentLocalTime ;
//	SimpleDateFormat date ;
	Context context;

	public static String dStartLat;
	public static String dStartLon;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_destenation_list);

		context = this;

//	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//	Intent batteryStatus = context.registerReceiver(null, ifilter);
//	int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//	int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//	float batteryPct = level / (float)scale;

		new postDriverDashBoardTask().execute("");
		new RetrieveDestinationsTask().execute();
		if (!isFinishing()){

			new postDriverReportTask().execute("");
		}



		tvDestination = (TextView) findViewById(R.id.DestinationLocationDriver);
		tvDestination.setText("Press Here To Show Your Destenation");
		tvDestination.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
// TODO Auto-generated method stub
				Intent i = new Intent(DriverDestenationList.this, MapDriverList.class);

				Log.d("bader", "lat is: " + dStartLat + " lng is: " + dStartLon);

				i.putExtra("myCoorLat", dStartLat);
				i.putExtra("myCoorLng", dStartLon);

				startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.driver_profile, menu);
		return true;
	}

//	public float getBatteryLevel() {
//	Intent batteryIntent = getApplicationContext().registerReceiver(null,
//	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//	int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//	int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//	// Error checking that probably isn't needed but I added just in case.
//	if (level == -1 || scale == -1) {
//	return 50.0f;
//	}
//
//	return ((float) level / (float) scale) * 100.0f;
//	}


	class postDriverDashBoardTask extends AsyncTask<String, Integer, String> {

		ProgressDialog dialog = new ProgressDialog(DriverDestenationList.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.setTitle("Posting Request");
			dialog.setMessage("Posting....");
			dialog.show();


		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				URI u = new URI("http://54.88.107.56:80/MyWayWeb/setDriverDashBoard");
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(u);
				ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();

				final InformationManager imm;
				imm = new InformationManager(context);
				JSONObject userJson;
				userJson = imm.getUserInformation();
				String tempUsername = "";
				try {
					tempUsername = userJson.getString("username");
				} catch (Exception e) {

				}



				String battreyPrec = "60";
				String lon = "44.22";
				//String.valueOf(location.getLongitude());
				String lat = "22.43";
				//String.valueOf(location.getLatitude());

				urlparameters.add(new BasicNameValuePair("currentLat", lat));
				urlparameters.add(new BasicNameValuePair("currentLon", lon));
				urlparameters.add(new BasicNameValuePair("battaryStatus", battreyPrec));
				urlparameters.add(new BasicNameValuePair("userName",tempUsername));

				post.setEntity(new UrlEncodedFormEntity(urlparameters));
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);

				return data;


			} catch (MalformedURLException e) {
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
		protected void onPostExecute(String data) {
			// TODO Auto-generated method stub
			super.onPostExecute(data);

			dialog.dismiss();

			finish();
		}


	}

	class postDriverReportTask extends AsyncTask<String, Integer, String> {
		//		String localTime = date.format(currentLocalTime);



		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
//			lm =  (LocationManager) context.getSystemService(LOCATION_SERVICE);
//			location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//			cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
//			currentLocalTime = cal.getTime();
//			date = new SimpleDateFormat("HH:mm a");
			try {
				URI u = new URI("http://54.88.107.56:80/MyWayWeb/postDriverReport");
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post  = new HttpPost(u);
				ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();

				final InformationManager imm;
				imm = new InformationManager(context);
				JSONObject userJson;
				userJson = imm.getUserInformation();
				String tempUsername = "";
				try {
					tempUsername = userJson.getString("username");
				} catch (Exception e) {

				}

				String lon = "22.34";
				//String.valueOf(location.getLongitude());
				String lat = "32.34";
				//String.valueOf(location.getLatitude());

				String overSpeed = "Overspeed";
				String stops = "Stopped";
				for (int i=0;i<3;i++){
					if(i==0){
						urlparameters.add(new BasicNameValuePair("report_reason", overSpeed));
					}else{
						urlparameters.add(new BasicNameValuePair("report_reason", stops));
					}
				}
				urlparameters.add(new BasicNameValuePair("report_lat", lat));
				urlparameters.add(new BasicNameValuePair("report_lon", lon));
				urlparameters.add(new BasicNameValuePair("username",tempUsername));
				urlparameters.add(new BasicNameValuePair("report_time", "10:01:10"));
				urlparameters.add(new BasicNameValuePair("report_date", "2015-6-6"));


				post.setEntity(new UrlEncodedFormEntity(urlparameters));

				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String data = EntityUtils.toString(entity);


				return data;

			} catch (MalformedURLException e) {
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
		protected void onPostExecute(String data) {
			// TODO Auto-generated method stub
			super.onPostExecute(data);




		}

	}

	class RetrieveDestinationsTask extends AsyncTask<Void, Integer, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}


		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				ArrayList<BasicNameValuePair> urlparameters=new ArrayList<BasicNameValuePair>();

				URI u=new URI("http://54.88.107.56:80/MyWayWeb/getDestination");
				DefaultHttpClient client=new DefaultHttpClient();
				final InformationManager imm;
				imm = new InformationManager(context);
				JSONObject userJson;
				userJson = imm.getUserInformation();
				String tempUsername = "";
				try {
					tempUsername = userJson.getString("username");
				} catch (Exception e) {

				}
				HttpPost post = new HttpPost(u);
				urlparameters.add(new BasicNameValuePair("driverUserName", tempUsername));
				post.setEntity(new UrlEncodedFormEntity(urlparameters));

				HttpResponse response=client.execute(post);
				HttpEntity entity=response.getEntity();
				String data=EntityUtils.toString(entity);

				return data;

			} catch (MalformedURLException e) {
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
		protected void onPostExecute(String data) {

			// TODO Auto-generated method stub
			super.onPostExecute(data);
			//convert the json string to arraylist of events

			try {
				JSONObject jsnobject = new JSONObject(data);
				JSONArray jsonArray = new JSONArray(jsnobject.getString("result_data"));

//			    for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject desObject = jsonArray.getJSONObject(0);
				String dStartLat = desObject.getString("destination_Lat");

				String dStartLon = desObject.getString("destination_Lon");



//				Driver d = new Driver();
//				d.setStartlat(dStartLat);
//				d.setStartlon(dStartLon);
				Log.d("bader", dStartLat);

				//Toast.makeText(context, "Lat = " + dStartLat, Toast.LENGTH_LONG).show();

//			    }

				Intent i = new Intent(DriverDestenationList.this, MapDriverList.class);

				i.putExtra("myCoorLat", dStartLat);
				i.putExtra("myCoorLng", dStartLon);

				startActivity(i);

			}catch(JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}
	}



}



