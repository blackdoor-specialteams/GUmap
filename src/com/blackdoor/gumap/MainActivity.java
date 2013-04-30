package com.blackdoor.gumap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListMap;

import blackdoor.util.CSV;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	// extends FragmentActivity implements

	// ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	// SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private static final LatLng NEBOUND = new LatLng(47.671781, -117.393352);
	private static final LatLng SWBOUND = new LatLng(47.661283, -117.411052);
	private static final LatLngBounds MAPBOUNDARY = new LatLngBounds(SWBOUND, NEBOUND);
	private LatLng lastCenter = new LatLng(47.667454, -117.402309);
	private LatLng preLastCenter = new LatLng(47.667454, -117.402309);
	ViewPager mViewPager;
	GoogleMap guMap;
	private MapFragment guMapFragment;
	private ConcurrentSkipListMap<String, GUBuildingMarker> markers;
	private Zoom zoom = Zoom.MEDIUM;
	private Handler mHandler;
	private Spinner bldgSpinner;

	public static enum Zoom {
		CLOSE, MEDIUM, FAR
	}

	// protected void onResume(Bundle savedInstanceState){
	// super.onResume(savedInstanceState);
	//
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		addMapFragment();
		bldgSpinner = (Spinner) findViewById(R.id.bldgSpinner);
	}

	protected void onStart() {
		super.onStart();
		// get the map here
		setUpMapIfNeeded();
		postStartSetup();

	}
	private void populateSpinner(){
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
		
		List<String> bldgArray = new ArrayList<String>();
		bldgArray.add("Select Building");
		for(Entry<String, GUBuildingMarker> entry : markers.entrySet()){
			bldgArray.add(entry.getValue().getName());
		}
		adapter.addAll(bldgArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bldgSpinner.setAdapter(adapter);
	}
	private void setSpinnerListener(){
		bldgSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				if (parent.getItemAtPosition(pos) != "Select Building") {
					GUBuildingMarker marker = markers.get(parent
							.getItemAtPosition(pos));
					LatLng dest = marker.getCoordinates();
					guMap.animateCamera(CameraUpdateFactory.newLatLng(dest));
					if (zoom == Zoom.CLOSE)
						marker.closeMarker.showInfoWindow();
					if (zoom == Zoom.MEDIUM)
						marker.mediumMarker.showInfoWindow();
				}
			}

		    public void onNothingSelected(AdapterView<?> parent) {

		    }
		});
	}
	
	private void checkBoundaries(){
		LatLng tempCenter = guMap.getCameraPosition().target;
		LatLngBounds visibleBounds = guMap.getProjection().getVisibleRegion().latLngBounds;
        if(!MAPBOUNDARY.contains(visibleBounds.northeast) || !MAPBOUNDARY.contains(visibleBounds.southwest)){
            guMap.moveCamera(CameraUpdateFactory.newLatLng(lastCenter));
            //guMap.animateCamera(CameraUpdateFactory.newLatLng(lastCenter));
        }
        else
            lastCenter = tempCenter;
	}
	
	private void setUpHandler(){
		mHandler = new Handler(){
				public void handleMessage(Message msg){
					checkBoundaries();
					sendEmptyMessageDelayed(0, 5);
				}
			};
	}

	/**
	 * 
	 * @return the map structure with all the markers in it
	 */
	public Map<String, GUBuildingMarker> getMarkers() {
		return markers;
	}

	// make this work
	// bounds of the desired area
	// var allowedBounds = new google.maps.LatLngBounds(
	// new google.maps.LatLng(70.33956792419954, 178.01171875),
	// new google.maps.LatLng(83.86483689701898, -88.033203125)
	// );
	// var lastValidCenter = map.getCenter();
	//
	// google.maps.event.addListener(map, 'center_changed', function() {
	// if (allowedBounds.contains(map.getCenter())) {
	// // still within valid bounds, so save the last valid position
	// lastValidCenter = map.getCenter();
	// return;
	// }
	//
	// // not valid anymore => return to last valid position
	// map.panTo(lastValidCenter);
	// });

	/**
	 * Gathers building information from CSV file, creates building markers,
	 * stores them in ConcurrentSkipListMap
	 * 
	 * @param fileName
	 *            in assets folder --> CSV file (GPS_Coords.csv)
	 * @return ConcurrentSkipListMap containing GUBuildingMarkers
	 */
	public ConcurrentSkipListMap<String, GUBuildingMarker> gatherBuildingData(
			String assetName) {
		ConcurrentSkipListMap<String, GUBuildingMarker> buildingDataMap = new ConcurrentSkipListMap<String, GUBuildingMarker>();
		StringTokenizer tokenizer;
		String columnHeaders;
		AssetManager assetManager = getAssets();
		InputStream input;

		try {
			// BufferedReader fileReader = new BufferedReader(new
			// FileReader(fileName));
			input = assetManager.open(assetName);
			Scanner fileReader = new Scanner(input);
			columnHeaders = fileReader.nextLine();// .readLine();
			while (fileReader.hasNext())// .ready())
			{
				String line = fileReader.nextLine();// .readLine();
				tokenizer = new StringTokenizer(line);

				String name = tokenizer.nextToken(",");
				LatLng coords = new LatLng(Double.parseDouble(tokenizer
						.nextToken(",")), Double.parseDouble(tokenizer
						.nextToken(",")));
				String hours = tokenizer.nextToken(",");
				String services = tokenizer.nextToken(",");
				String dining = tokenizer.nextToken(",");
				String contactInfo = tokenizer.nextToken(",");
				String abbrev = tokenizer.nextToken(",");

				String description = fileReader.nextLine();// .readLine();
				description = description
						.substring(0, description.length() - 8);
				System.out
						.println("name: " + name + "\n latlng: "
								+ coords.toString() + "\n hours: " + hours
								+ "\n serv: " + services + "\n din: " + dining
								+ "\n cont: " + contactInfo + "\n desc: "
								+ description);

				buildingDataMap.put(name, new GUBuildingMarker(this, name, abbrev,
						coords, description, hours, services, dining,
						contactInfo));
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: File " + assetName + "not found");
			System.exit(0);
		} catch (IOException e) {
			System.err.println("Error: Input from file mishandled");
			System.exit(0);
		}
		return buildingDataMap;
	}

	/**
	 * move the camera up one zoom level
	 * 
	 * @param view
	 */
	public void zoomOut(View view) {

		if (zoom == Zoom.MEDIUM) {

			guMap.animateCamera(CameraUpdateFactory.zoomTo(16));
			zoom = Zoom.FAR;
		} else if (zoom == Zoom.CLOSE) {
			guMap.animateCamera(CameraUpdateFactory.zoomTo(17));
			zoom = Zoom.MEDIUM;
		}
		for (Iterator<Entry<String, GUBuildingMarker>> iterator = markers
				.entrySet().iterator(); iterator.hasNext();) {
			iterator.next().getValue().updateIcon();
		}
	}

	/**
	 * move the camera down one zoom level
	 * 
	 * @param view
	 */
	public void zoomIn(View view) {

		if (zoom == Zoom.MEDIUM) {
			guMap.animateCamera(CameraUpdateFactory.zoomTo(19));
			zoom = Zoom.CLOSE;
		} else if (zoom == Zoom.FAR) {
			guMap.animateCamera(CameraUpdateFactory.zoomTo(17));
			zoom = Zoom.MEDIUM;
		}
		for (Iterator<Entry<String, GUBuildingMarker>> iterator = markers
				.entrySet().iterator(); iterator.hasNext();) {
			iterator.next().getValue().updateIcon();
		}
	}

	/**
	 * associate guMap with the MapFragment on the layout
	 */
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (guMap == null) {
			guMap = guMapFragment.getMap();
			// Check if we were successful in obtaining the map.
			if (guMap != null) {
				// The Map is verified. It is now safe to manipulate the map.

			}
		}
	}

	/**
	 * any setup that needs to happen after all other methods in OnSetup have
	 * been called is called last in onSetup
	 */
	private void postStartSetup() {
		guMap.setMyLocationEnabled(true);
		setupIWCL();
		// GUBuildingMarker test = new GUBuildingMarker(this, "Goller", new
		// LatLng(47.669199,-117.400174), "a building full of nerds", "24/7",
		// "oral", "none", "asdf");
		// guMap.addMarker(test.getBuildingOptions());
		addMarkers();
		setUpHandler();
		mHandler.sendEmptyMessage(0);
		guMap.setInfoWindowAdapter(new GUBuildingInfoWindowAdapter(this));
		populateSpinner();
		setSpinnerListener();
	}

	/**
	 * 
	 * @return the options to create the MapFragment with
	 */
	private GoogleMapOptions loadMapOptions() {
		GoogleMapOptions options = new GoogleMapOptions();
		options.camera(new CameraPosition(new LatLng(47.667454, -117.402309),
				17, 0, 0));// coords, zoom, tilt, bearing
		options.zoomGesturesEnabled(false);
		options.zoomControlsEnabled(false);
		options.rotateGesturesEnabled(false);
		return options;
	}

	/**
	 * adds a map fragment to the activity_main with some preset values
	 */
	private void addMapFragment() {
		guMapFragment = MapFragment.newInstance(loadMapOptions());
		FragmentTransaction fragmentTransaction = getFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.mapContainer, guMapFragment);
		fragmentTransaction.commit();
	}

	private void addMarkers() {
		markers = gatherBuildingData("building-info/GPS_Coords.csv");
	}

	private void setupIWCL() {
		guMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			public void onInfoWindowClick(Marker marker) {

				GUBuildingMarker info = markers.get(marker.getTitle());

				// Captain Jean-luc Picard of the USS Enterprise
				Bundle b = new Bundle();
				b.putParcelable("key_build", info);

				Intent infointent = new Intent(MainActivity.this,
						BuildingInfo.class);
				infointent.putExtras(b);
				startActivity(infointent);
			}
		});
	}

	/**
	 * 
	 * @return the current zoom level
	 */
	public Zoom getZoom() {
		return zoom;
	}
	// // Set up the action bar.
	// final ActionBar actionBar = getActionBar();
	// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	//
	// // Create the adapter that will return a fragment for each of the three
	// // primary sections of the app.
	// mSectionsPagerAdapter = new SectionsPagerAdapter(
	// getSupportFragmentManager());
	//
	// // Set up the ViewPager with the sections adapter.
	// mViewPager = (ViewPager) findViewById(R.id.pager);
	// mViewPager.setAdapter(mSectionsPagerAdapter);
	//
	// // When swiping between different sections, select the corresponding
	// // tab. We can also use ActionBar.Tab#select() to do this if we have
	// // a reference to the Tab.
	// mViewPager
	// .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
	// @Override
	// public void onPageSelected(int position) {
	// actionBar.setSelectedNavigationItem(position);
	// }
	// });
	//
	// // For each of the sections in the app, add a tab to the action bar.
	// for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	// // Create a tab with text corresponding to the page title defined by
	// // the adapter. Also specify this Activity object, which implements
	// // the TabListener interface, as the callback (listener) for when
	// // this tab is selected.
	// actionBar.addTab(actionBar.newTab()
	// .setText(mSectionsPagerAdapter.getPageTitle(i))
	// .setTabListener(this));
	// }
	// // }
	// //
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }
	//
	// @Override
	// public void onTabSelected(ActionBar.Tab tab,
	// FragmentTransaction fragmentTransaction) {
	// // When the given tab is selected, switch to the corresponding page in
	// // the ViewPager.
	// mViewPager.setCurrentItem(tab.getPosition());
	// }
	//
	// @Override
	// public void onTabUnselected(ActionBar.Tab tab,
	// FragmentTransaction fragmentTransaction) {
	// }
	//
	// @Override
	// public void onTabReselected(ActionBar.Tab tab,
	// FragmentTransaction fragmentTransaction) {
	// }
	//
	// /**
	// * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	// * one of the sections/tabs/pages.
	// */
	// public class SectionsPagerAdapter extends FragmentPagerAdapter {
	//
	// public SectionsPagerAdapter(FragmentManager fm) {
	// super(fm);
	// }
	//
	// @Override
	// public Fragment getItem(int position) {
	// // getItem is called to instantiate the fragment for the given page.
	// // Return a DummySectionFragment (defined as a static inner class
	// // below) with the page number as its lone argument.
	// Fragment fragment = new DummySectionFragment();
	// Bundle args = new Bundle();
	// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
	// fragment.setArguments(args);
	// return fragment;
	// }
	//
	// @Override
	// public int getCount() {
	// // Show 3 total pages.
	// return 3;
	// }
	//
	// @Override
	// public CharSequence getPageTitle(int position) {
	// Locale l = Locale.getDefault();
	// switch (position) {
	// case 0:
	// return getString(R.string.title_section1).toUpperCase(l);
	// case 1:
	// return getString(R.string.title_section2).toUpperCase(l);
	// case 2:
	// return getString(R.string.title_section3).toUpperCase(l);
	// }
	// return null;
	// }
	// }
	//
	// /**
	// * A dummy fragment representing a section of the app, but that simply
	// * displays dummy text.
	// */
	// public static class DummySectionFragment extends Fragment {
	// /**
	// * The fragment argument representing the section number for this
	// * fragment.
	// */
	// public static final String ARG_SECTION_NUMBER = "section_number";
	//
	// public DummySectionFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_main_dummy,
	// container, false);
	// TextView dummyTextView = (TextView) rootView
	// .findViewById(R.id.section_label);
	// dummyTextView.setText(Integer.toString(getArguments().getInt(
	// ARG_SECTION_NUMBER)));
	// return rootView;
	// }
	// }

}
