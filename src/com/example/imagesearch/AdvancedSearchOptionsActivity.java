package com.example.imagesearch;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class AdvancedSearchOptionsActivity extends Activity {

	private Spinner spImageSize;
	private Spinner spColorFilter;
	private Spinner spImageType;
	private EditText etSiteFilter;
	public static final String DEFAULT_VALUE = "All";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_search_options);
		setupViews();
		Serializable options = getIntent().getSerializableExtra("options");
		SearchOptions searchOptions = null;
		if (options == null) {
			// set Default values
			searchOptions = new SearchOptions();
			searchOptions.setImageSize(DEFAULT_VALUE);
			searchOptions.setColorFilter(DEFAULT_VALUE);
			searchOptions.setImageType(DEFAULT_VALUE);
			searchOptions.setSiteFilter("");
		} else {
			searchOptions = (SearchOptions) options;
		}
		
		setSavedOptions(searchOptions);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.advanced_search_options, menu);
		return true;
	}
	
	public void onOptionsSave(View v) {
		
		SearchOptions options = new SearchOptions();
		if (spImageSize.getSelectedItem() != null) {
			options.setImageSize(spImageSize.getSelectedItem().toString());
		} else {
			options.setImageSize(DEFAULT_VALUE);
		}
		
		if (spColorFilter.getSelectedItem() != null) {
			options.setColorFilter(spColorFilter.getSelectedItem().toString());
		} else {
			options.setColorFilter(DEFAULT_VALUE);
		}
		
		if (spImageType.getSelectedItem() != null) {
			options.setImageType(spImageType.getSelectedItem().toString());
		} else {
			options.setImageType(DEFAULT_VALUE);
		}
		
		options.setSiteFilter(etSiteFilter.getText().toString());
		
		Intent returnIntent = new Intent();
		returnIntent.putExtra("options", options);
		setResult(RESULT_OK, returnIntent);
		finish();
		
	}
	
	private void setSavedOptions(SearchOptions options) {
		setSpinnerToValue(spImageSize, options.getImageSize());
		setSpinnerToValue(spColorFilter, options.getColorFilter());
		setSpinnerToValue(spImageType, options.getImageType());
		etSiteFilter.setText(options.getSiteFilter());
	}
	
	private void setSpinnerToValue(Spinner spinner, String value) {
		SpinnerAdapter adapter = spinner.getAdapter();
		
		for(int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(value)) {
				spinner.setSelection(i);
				break;
			}
		}
	}
	
	private void setupViews() {
		spImageSize = (Spinner) findViewById(R.id.spImageSize);
		spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
		spImageType = (Spinner) findViewById(R.id.spImageType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);	
	}

}
