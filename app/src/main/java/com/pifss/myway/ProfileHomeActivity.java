package com.pifss.myway;

import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileHomeActivity extends Activity {

//	public final static String PREF_NAME = "userInformation";

	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_home);

		context = this;
		final InformationManager imm = new InformationManager(this);
		////////////////////// To set the user info on the profile ////////////////////////

		JSONObject userJson;
		userJson = imm.getUserInformation();
		String username = "";
		
		try {
			username = userJson.getString("username");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Toast.makeText(this,username,Toast.LENGTH_LONG).show();

		String [] stringArray = {username};
		new GetUserInfo().execute(stringArray);
		
		/*
		TextView tvUsername = (TextView) findViewById(R.id.textViewProfileUsername);
		ImageView userImg = (ImageView) findViewById(R.id.imageViewProfileImage);
		
		try {
			
			tvUsername.setText(username);
			
			Bitmap bm = imm.readImage();
			if (bm != null){
				userImg.setImageBitmap(bm);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		//////////////////////////////// Go to edit profile page /////////////////////////////////////////
		ImageView imIcon = (ImageView) findViewById(R.id.imageViewEditIcon); //edit profile icon

		imIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ProfileHomeActivity.this,
						ProfileEditActivity.class);
				startActivity(i);
				finish();
			}
		});
		//////////////// button to log out /////////////////////////
		
		ImageView imLogOut = (ImageView) findViewById(R.id.imageViewLogout);
		imLogOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.logOut(ProfileHomeActivity.this);
				Intent i = new Intent(ProfileHomeActivity.this, Home.class);
				startActivity(i);
				finish();
			}
		});
		
		//////////////// button to go to the add fav ////////////////////
//		TextView tvAddFav = (TextView) findViewById(R.id.textViewAddFav);
//		tvAddFav.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(ProfileHomeActivity.this, FavoritesActivity.class);
//				startActivity(i);
//				finish();
//			}
//		});
		
		
		//use the following line to add the sliding menu to the current page
		SlidingUtil.SetSliding(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_profile_home, menu);
		return true;
	}
    //to send the user to the home page when pressing the back button
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	Intent i = new Intent(ProfileHomeActivity.this, Home.class);
		startActivity(i);
		finish();
    }
	
	class GetUserInfo extends AsyncTask<String, Void, User> {
		
		@Override
		protected User doInBackground(String... params) {
			// TODO Auto-generated method stub

			User user = new User();

			try {
				URI uri = new URI("http://54.88.107.56:80/MyWayWeb/getUserProfile");

				DefaultHttpClient client = new DefaultHttpClient();

				HttpPost postRequest = new HttpPost(uri);

				ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
				arrayList.add(new BasicNameValuePair("username", params[0]));

				postRequest.setEntity(new UrlEncodedFormEntity(arrayList));

				HttpResponse response = client.execute(postRequest);

				HttpEntity entity = response.getEntity();

				String jsonString = EntityUtils.toString(entity);

				JSONObject jsonData = new JSONObject(jsonString);

				JSONArray userInfoJson = new JSONArray(jsonData.getString("result_data"));

				JSONObject json = new JSONObject(userInfoJson.getString(0));

				user = new User(params[0], json.getString("password"), json.getString("email"), json.getString("profile_picture"));
			} catch (Exception e) {

			}

			return user;
		}
		
		@Override
		protected void onPostExecute(User user) {
			// TODO Auto-generated method stub
			super.onPostExecute(user);

			final InformationManager imm = new InformationManager(context);

			TextView tvUsername = (TextView) findViewById(R.id.textViewProfileUsername);
			ImageView userImg = (ImageView) findViewById(R.id.imageViewProfileImage);

			tvUsername.setText(user.getUsername());
			Toast.makeText(context,"email = " + user.getEmail(), Toast.LENGTH_LONG).show();

			//userImg.setImageBitmap(imm.decodeBase64(user.getProfilePicture()));
			//set email
			
		}
		
	}

}