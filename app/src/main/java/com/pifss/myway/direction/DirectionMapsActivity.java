package com.pifss.myway.direction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pifss.myway.Favorite;
import com.pifss.myway.MapPointActivity;
import com.pifss.myway.MyLocation;
import com.pifss.myway.R;
import com.pifss.myway.findPlace.PlaceProvider;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class DirectionMapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderCallbacks<Cursor> {

    private GoogleMap mMap;

    Double lat= null;
    Double lng= null;

    ArrayList<Favorite> favoritesList = new ArrayList<Favorite>();
    public final static String PREF_NAME = "userInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.




        handleIntent(getIntent());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);

//        Navigator nav = new Navigator(mMap,new LatLng(29.2786573,48.0506411),new LatLng(29.289244,48.058152));
//        nav.findDirections(false);

//        mMap.addMarker(new MarkerOptions().position(nav.getStartPoint()).title("Start Point"));
//        mMap.addMarker(new MarkerOptions().position(nav.getEndPoint()).title("End Point"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(29.2786573, 48.0506411)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11.0f));

       // getPlace("Kuwait+Towers"); //Cause an error

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker arg0) {
//                if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
//                    Toast.makeText(MainActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        //Got the location!

                        Navigator nav = new Navigator(mMap, new LatLng(location.getLatitude(), location.getLongitude()), arg0.getPosition());
                        nav.findDirections(true);

                    }
                };
                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(DirectionMapsActivity.this, locationResult);


                return true;
            }

        });


    }


    private  void geocodeAddress(String address) {

        //MASH TEST


        AsyncTask<String, Void, Void> stringVoidVoidAsyncTask = new AsyncTask<String, Void, Void>() {



            BufferedReader in;

            @Override
            protected Void doInBackground(String... strings) {

                String url = "";
                if (strings.length > 0) {
                    url = strings[0];
                } else {
                    return null;
                }
                try {
                    HttpClient httpClient = new DefaultHttpClient();// Client
                    HttpGet getRequest = new HttpGet();

                    getRequest.setURI(new URI(url));
                    HttpResponse response = httpClient.execute(getRequest);


                    in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String page = sb.toString();
                    JSONObject jsonObject = new JSONObject(page);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                    if (jsonArray.length() > 0) {
                        jsonObject = (JSONObject) jsonArray.get(0);
                        jsonObject = (JSONObject) jsonObject.get("geometry");
                        JSONObject location = (JSONObject) jsonObject.get("location");
                         lat = (Double) location.get("lat");
                         lng = (Double) location.get("lng");
                        System.out.println("lat - " + lat + " , lon - " + lng);
                        Log.d("mash", "LAT & LNG: " + lat + "," + lng);



                    }
                    System.out.println(page);
                    Log.d("mash","PAGE: "+ page);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        };

        address = address.replaceAll(" ", "+");

        stringVoidVoidAsyncTask.execute("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&region=kw");



        //./MASH TEST

    }



    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())){

            String query = intent.getStringExtra(SearchManager.QUERY);

           // doSearch(intent.getStringExtra(SearchManager.QUERY));

            geocodeAddress(query);
            doSearch(query);

        }else if (Intent.ACTION_VIEW.equals(intent.getAction())){
            getPlace(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
        }

      //  if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        //    String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

           // Log.d("mash","QUERY: " + query);
            //doSearch(query);
           // geocodeAddress(query);

//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_direction_activity, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.findPlace).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(item.getItemId()){
            case R.id.findPlace:
                onSearchRequested();
                break;
//            case R.id.menu_getDirection:
//
//                break;
            case R.id.menu_onFav:
             //   Log.d("mash", "Where is Favourite");

                new RetrieveFavsTask().execute();

                for (int i = 0; i<favoritesList.size(); i++) {
                    Favorite temp = favoritesList.get(i);
                    double lati = Double.parseDouble(temp.getLatitude());
                    double longi = Double.parseDouble(temp.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lati, longi)).title(temp.getName()).icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_media_play)));
                }
                break;
            case R.id.menu_onTraffic:
                if ( mMap.isTrafficEnabled() ) {
                mMap.setTrafficEnabled(false);
            } else {
                mMap.setTrafficEnabled(true);
            }
                break;
            case R.id.menu_navigate:

                Uri gmmIntentUri = Uri.parse("geo:29.3697,47.9783");
						Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
						mapIntent.setPackage("com.google.android.apps.maps");
						if (mapIntent.resolveActivity(getPackageManager()) != null) {
						    startActivity(mapIntent);
						}
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void doSearch(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(0, data, this);
    }

    private void getPlace(String query){
        Bundle data = new Bundle();
        data.putString("query", query);
        getSupportLoaderManager().restartLoader(1, data, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle query) {
        CursorLoader cLoader = null;
        if(arg0==0)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.SEARCH_URI, null, null, new String[]{ query.getString("query") }, null);
        else if(arg0==1)
            cLoader = new CursorLoader(getBaseContext(), PlaceProvider.DETAILS_URI, null, null, new String[]{ query.getString("query") }, null);
        return cLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor c) {

       //  Log.d("mash", "I AM PRINTING: " + c.getCount());

        showLocations(c);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }

    private void showLocations(Cursor c) {
        MarkerOptions markerOptions = null;
        LatLng position = null;
        mMap.clear();
        while (c.moveToNext()) {
            markerOptions = new MarkerOptions();
            position = new LatLng(Double.parseDouble(c.getString(1)), Double.parseDouble(c.getString(2)));
            markerOptions.position(position);
            markerOptions.title(c.getString(0));
            mMap.addMarker(markerOptions);

            if (lat != null && lng != null) {
                //WAS FOR GEOCODING
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("my Marker").icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_media_pause)));
            }
        }
        if (position != null) {
           //  CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
           // mMap.animateCamera(cameraPosition);
        }

    }




    class RetrieveFavsTask extends AsyncTask<String, Integer, String> {

        ProgressDialog dialog = new ProgressDialog(DirectionMapsActivity.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog.setTitle("Loading Favorites");
            dialog.setMessage("Loading....");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {

                    ArrayList<BasicNameValuePair> urlparameters = new ArrayList<BasicNameValuePair>();

                    URI u = new URI("http://mobile.comxa.com/fav/all_favs.json");
                    DefaultHttpClient client = new DefaultHttpClient();

                    HttpPost post = new HttpPost(u);

                    SharedPreferences pref = getSharedPreferences(PREF_NAME,
                            MODE_APPEND);
                    String userObj = pref.getString("user", "ERROR");
                    String tempUsername = "";
                    JSONObject userJson;
                    try {
                        userJson = new JSONObject(userObj);
                        tempUsername = userJson.getString("username");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    urlparameters.add(new BasicNameValuePair("username",
                            tempUsername));

                    post.setEntity(new UrlEncodedFormEntity(urlparameters));

                    HttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    // Log.d("test:", data);
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
            Log.d("test: ", result);
            // convert the json string to arraylist of favorites

            try {
                JSONObject jsnobject = new JSONObject(result);
                JSONArray jsonArray = jsnobject.getJSONArray("result_data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject FavObject = jsonArray.getJSONObject(i);
                    String Fname = FavObject.getString("name");
                    String Flat = FavObject.getString("latitude");
                    String Flong = FavObject.getString("longitude");
                    Favorite tempFav = new Favorite(Fname, Flat, Flong);
                    favoritesList.add(tempFav);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }



            dialog.dismiss();

        }

    }




    }

