package com.blackdoor.gumap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildingInfo extends Activity {
	//Experimental Stuff
	//Map<String, GUBuildingMarker> markerMap;
	//BUILDING
	private GUBuildingMarker building;
	//Picture Icon 
	private ImageView Bicon;
	//Text Fields 
	private TextView buildingName;
	private TextView buildingDes;
	private TextView buildingHours;
	private TextView buildingServices;
	private TextView buildingDining;
	private TextView buildingContact;
	//Dividers
	private TextView divHours;
	private TextView divServices;
	private TextView divDining;
	private TextView divContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_info);
		

		EstablishBuilding();
		PopulateText();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.building_info, menu);
		return true;
	}
	/*_________________________________________________
	 * Procures the proper building from the Map
	 * 
	 * THIS NEEDS WORK........otherwise we good to go
	 *_________________________________________________
	 */
	public void EstablishBuilding() {
		// not needed
			//markerMap = containingActivity.getMarkers();
		
		//EXAMPLE:::::
		//Intent myIntent = new Intent(mycurentActivity.this, secondActivity.class);
		//myIntent.putExtra("key", myEditText.Text.toString();
		//startActivity(myIntent); 
		
		//get the proper intnet here and the rest falls into place
		//building = markerMap.get(myIntent.getStringExtra("name");;
		
		Bundle extras = getIntent().getExtras();
		building =  extras.getParcelable("key_build");
	}
	
	/*/////////////////////////////////////////////////
	 * Main Function:
	 *  checks to see if fields are empty,
	 * decides to change the visiblity or the content of a
	 * box/textview. Also sets the ICON.
	 *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	public void PopulateText() {
		setBuidlingICON();
		setBuildingNameTEXT();
		setBuildingDescriptionTEXT();
		setBuildingHoursTEXT();
		setBuildingServicesTEXT();
		setBuildingdiningTEXT();
		setBuildingContactTEXT();
	}
	//NAME
	private void setBuildingNameTEXT() {
		buildingName = (TextView) findViewById(R.id.Bname);
		buildingName.setText(building.getName());
	}
	//DESCRIPTION
	private void setBuildingDescriptionTEXT() {
		buildingDes = (TextView) findViewById(R.id.Bdescription);
		buildingDes.setText(building.getDescription());
	}
	//HOURS
	private void setBuildingHoursTEXT() {
		buildingHours = (TextView) findViewById(R.id.Bdining);
		if (!building.getHours().equals("")) {
			buildingHours.setText(building.getHours());
		} else {
			divHours = (TextView) findViewById(R.id.divider_hours);
			buildingHours.setVisibility(View.GONE);
			divHours.setVisibility(View.GONE);
		}
	}
	//SERVICES
	private void setBuildingServicesTEXT() {
		buildingServices = (TextView) findViewById(R.id.Bservice);
		if (!building.getServices().equals("")) {

			buildingServices.setText(building.getServices());
		} else {
			divServices = (TextView) findViewById(R.id.divider_service);
			buildingServices.setVisibility(View.GONE);
			divServices.setVisibility(View.GONE);
		}
	}
	//DINING
	private void setBuildingdiningTEXT() {
		buildingDining = (TextView) findViewById(R.id.Bdining);
		if (!building.getDining().equals("")) {

			buildingDining.setText(building.getDining());
		} else {
			divDining = (TextView) findViewById(R.id.divider_contact);
			buildingDining.setVisibility(View.GONE);
			divDining.setVisibility(View.GONE);
		}
	}
	//CONTACT
	private void setBuildingContactTEXT() {
		buildingContact = (TextView) findViewById(R.id.Bcontact);
		if (!building.getContactInfo().equals("")) {
			buildingContact.setText(building.getContactInfo());
		} else {
			divContact = (TextView) findViewById(R.id.divider_contact);
			divContact.setVisibility(View.GONE);
			buildingContact.setVisibility(View.GONE);
		}
	}
////////////ICON//////////////////////////////////////////////
	private void setBuidlingICON() {
		int imageResource = getResources().getIdentifier(building.getName(),
				null, getPackageName());

		Bicon = (ImageView) findViewById(R.id.BIcon);
		Drawable image = getResources().getDrawable(imageResource);
		Bicon.setImageDrawable(image);
	}

}
