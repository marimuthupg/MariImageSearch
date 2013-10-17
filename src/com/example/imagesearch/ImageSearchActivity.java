package com.example.imagesearch;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchActivity extends Activity {

	private static final int SETTINGS_REQUEST_CODE = 1;
	private EditText etSearch;
	private GridView gvImages;
	private List<ImageResult> imageResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;
	private SearchOptions options;
	private AsyncHttpClient client = new AsyncHttpClient();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setupViews();
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvImages.setAdapter(imageAdapter);
        options = null;
        gvImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
					ImageResult clickedImageResult = imageResults.get(position);
					
					Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
					i.putExtra("imageResult", clickedImageResult);
					startActivity(i);
			}
        	
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_search, menu);
        return true;
    }
    
    private void setupViews() {
    	etSearch = (EditText) findViewById(R.id.etSearch);
    	gvImages = (GridView) findViewById(R.id.gvImages);
    }
    
    public void onImageSearch(View v) {
    	String query = etSearch.getText().toString();
    	if (query.trim().isEmpty()) {
    		Toast.makeText(getApplicationContext(), "Please enter search query", Toast.LENGTH_LONG);
    		return;
    	}
    	Toast.makeText(this, "Search text entered: " + query, Toast.LENGTH_SHORT).show();
    	
    	String url = UrlBuilder.buildUrl(query, this.options);
    	Log.d("DEBUG", "url="+url);
    	client.get( url, 
    				new JsonHttpResponseHandler() {
    		
    		@Override
    		public void onSuccess(JSONObject response) {
    			JSONArray imageJsonResults = null;
    			try {
    				imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
    				imageResults.clear();
    				imageResults.addAll(ImageResult.fromJSONArray(imageJsonResults));
    				imageAdapter.notifyDataSetChanged();
    			} catch(JSONException e) {
    				Log.d("DEBUG", "Error: " + e.getMessage());
    				Toast.makeText(getApplicationContext(), "Error retrieving search results", Toast.LENGTH_LONG).show();
    			}
    		}
    	});
    }
    
    public void onSettingsAction(MenuItem mi) {
    	Intent i = new Intent(getApplicationContext(), AdvancedSearchOptionsActivity.class);
    	i.putExtra("options", this.options);
    	
    	startActivityForResult(i, SETTINGS_REQUEST_CODE);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == SETTINGS_REQUEST_CODE) {
    		if (resultCode == RESULT_OK) {
    			options = (SearchOptions) data.getSerializableExtra("options");
    			Log.d("DEBUG", "intent data = " + options);
    		}
    	}
    }
    
}
