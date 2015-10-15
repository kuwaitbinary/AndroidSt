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

public class DriverDestenationList extends Activity {
	static ArrayList<Driver> driverDesList = new ArrayList<Driver>();
	TextView tvDestination;
	//	LocationManager lm ;
//	Location location ;
//	Calendar cal ;
//	Date currentLocalTime ;
//	SimpleDateFormat date ;
	Context context;
	int i = 0;
	public static String dStartLat;
	public static String dStartLon;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver_destenation_list);
//	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//	Intent batteryStatus = context.registerReceiver(null, ifilter);
//	int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
//	int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
//
//	float batteryPct = level / (float)scale;

		new postDriverReportTask().execute();
		//new RetrieveDestinationsTask().execute();

		getJSON();

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
//	dialog.setTitle("Sending Request");
//	dialog.setMessage("Sending....");
//	dialog.show();
//
		}

		@Override
		protected String doInBackground(String... params) {
// TODO Auto-generated method stub

			try {
				URI u = new URI("http://192.168.8.102:8080/MyWayWeb/setDriverDashBoard");
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(u);
				ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();


				String tempUsername = "bader";
				String battreyPrec = "22";
				String lon = "44,22";
//String.valueOf(location.getLongitude());
				String lat = "22,43";
//String.valueOf(location.getLatitude());
				urlparameters.add(new BasicNameValuePair("currentLat", lat));
				urlparameters.add(new BasicNameValuePair("currentLon", lon));
				urlparameters.add(new BasicNameValuePair("batteryStatus", battreyPrec));
				urlparameters.add(new BasicNameValuePair("userName", tempUsername));
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
		protected void onPostExecute(String result) {
// TODO Auto-generated method stub
			super.onPostExecute(result);
//	dialog.dismiss();
			finish();
		}

	}

	class postDriverReportTask extends AsyncTask<String, Integer, String> {
//	String localTime = date.format(currentLocalTime);
//	ProgressDialog dialog = new ProgressDialog(DriverDestenationList.this);

		@Override
		protected void onPreExecute() {
// TODO Auto-generated method stub
			super.onPreExecute();
//	dialog.setTitle("Sending Request");
//	dialog.setMessage("Sending....");
//	dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
// TODO Auto-generated method stub
//	lm =  (LocationManager) context.getSystemService(LOCATION_SERVICE);
//	location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//	cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
//	currentLocalTime = cal.getTime();
//	date = new SimpleDateFormat("HH:mm a");
			try {
				URI u = new URI("http://192.168.8.102:8080/MyWayWeb/postDriverReport");
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(u);
				ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();

				String tempUsername = "bader";
				String lon = "22,34";
//String.valueOf(location.getLongitude());
				String lat = "32,34";
//String.valueOf(location.getLatitude());

				String overSpeed = "overSpeed";
				String stops = "stop";
				for (i = 0; i < 3; i++) {
					if (i == 1) {
						urlparameters.add(new BasicNameValuePair("report_reason", overSpeed));
					} else {
						urlparameters.add(new BasicNameValuePair("report_reason", stops));
					}
				}
				urlparameters.add(new BasicNameValuePair("report_lat", lat));
				urlparameters.add(new BasicNameValuePair("report_lon", lon));
				urlparameters.add(new BasicNameValuePair("username", tempUsername));
				urlparameters.add(new BasicNameValuePair("report_time", "10:33pm"));
				urlparameters.add(new BasicNameValuePair("report_date", "22/10/2015"));
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
		protected void onPostExecute(String result) {
// TODO Auto-generated method stub
			super.onPostExecute(result);
//	dialog.dismiss();
			finish();
		}

	}


	private void getJSON() {



		AsyncTask<String, Integer, String> stringVoidVoidAsyncTask = new AsyncTask<String, Integer, String>() {
            public void bkalb(){
				System.out.println("BADER AY SHY");
				Log.d("bader", "getJSON is RUNINNG");
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

			}

			@Override
			protected String doInBackground(String... params) {
// TODO Auto-generated method stub
				bkalb();
				try {

					Log.d("bader", "DO IN BACKGROUND RUNNING");

					ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();
					URI u = new URI("http://192.168.8.102:8080/MyWayWeb/getDestination");
					DefaultHttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(u);
					urlparameters.add(new BasicNameValuePair("driverUserName", "omar"));
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
			protected void onPostExecute(String result) {
// TODO Auto-generated method stub
				super.onPostExecute(result);
//convert the json string to arraylist of events
				System.out.print(result);

				Log.d("bader", "BADER RESULT" + result);

				try {
					JSONObject jsnobject = new JSONObject(result);
					JSONArray jsonArray = new JSONArray(jsnobject.getString("result_data"));

					Log.d("bader", "Strings: " + jsonArray);

					System.out.print(jsonArray);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject desObject = jsonArray.getJSONObject(i);
//	        String userN = desObject.getString("userName");
//	        String driverdate = desObject.getString("date");
//	        String dTime = desObject.getString("Time");
						Log.d("bader", "Strings: " + desObject);

						DriverDestenationList.dStartLat = desObject.getString("destination_Lat");
						DriverDestenationList.dStartLon = desObject.getString("destination_Lon");
//					System.out.print(dStartLon);
//
//					//Log.d("test:", desObject.toString());
//					Driver d = new Driver();
//					d.setStartlat(dStartLat);
//					d.setStartlon(dStartLon);
//	      d.setUserName(userN);
//
//	      d.setDay(driverdate);
//	      d.setTime(dTime);
					}

				} catch (JSONException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}
            finish();
			}

		};
		stringVoidVoidAsyncTask.execute();
	}

}



