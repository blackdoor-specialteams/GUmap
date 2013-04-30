package com.blackdoor.gumap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildingInfo extends Activity {
	//Local Stuff
	private String name;
	private String des;
	private String hours;
	private String services;
	private String dining;
	private String contact;
	//We be needing this
	private String csv_null = "null";
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
		//**********************************************
		//parceable attempt
		//Bundle extras = getIntent().getExtras();
		//building =  extras.getParcelable("key_build");
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		des = intent.getStringExtra("description");
		hours = intent.getStringExtra("hours");
		services = intent.getStringExtra("services");
		dining = intent.getStringExtra("dining");
		contact = intent.getStringExtra("contact");
		//building.setSymbol(intent.getExtras().getString("symbol"));
	}
	
	/*/////////////////////////////////////////////////
	 * Main Function:
	 *  checks to see if fields are empty,
	 * decides to change the visiblity or the content of a
	 * box/textview. Also sets the ICON.
	 *\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */
	public void PopulateText() {
		//setBuidlingICON();
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
		buildingName.setText(name);
	}
	//DESCRIPTION
	private void setBuildingDescriptionTEXT() {
		buildingDes = (TextView) findViewById(R.id.Bdescription);
		buildingDes.setText(des);
	}
	//HOURS
	private void setBuildingHoursTEXT() {
		buildingHours = (TextView) findViewById(R.id.Bdining);
		if (!hours.equals(csv_null)) {
			buildingHours.setText(hours);
		} else {
			divHours = (TextView) findViewById(R.id.divider_hours);
			buildingHours.setVisibility(View.GONE);
			divHours.setVisibility(View.GONE);
		}
	}
	//SERVICES
	private void setBuildingServicesTEXT() {
		buildingServices = (TextView) findViewById(R.id.Bservice);
		if (!services.equals(csv_null)) {

			buildingServices.setText(services);
		} else {
			divServices = (TextView) findViewById(R.id.divider_service);
			buildingServices.setVisibility(View.GONE);
			divServices.setVisibility(View.GONE);
		}
	}
	//DINING
	private void setBuildingdiningTEXT() {
		buildingDining = (TextView) findViewById(R.id.Bdining);
		if (!dining.equals(csv_null)) {

			buildingDining.setText(dining);
		} else {
			divDining = (TextView) findViewById(R.id.divider_contact);
			buildingDining.setVisibility(View.GONE);
			divDining.setVisibility(View.GONE);
		}
	}
	//CONTACT
	private void setBuildingContactTEXT() {
		buildingContact = (TextView) findViewById(R.id.Bcontact);
		if (!contact.equals(csv_null)) {
			buildingContact.setText(contact);
		} else {
			divContact = (TextView) findViewById(R.id.divider_contact);
			divContact.setVisibility(View.GONE);
			buildingContact.setVisibility(View.GONE);
		}
	}
////////////ICON//////////////////////////////////////////////
	private void setBuidlingICON() {
		int imageResource = getResources().getIdentifier(name,
				null, getPackageName());

		Bicon = (ImageView) findViewById(R.id.BIcon);
		Drawable image = getResources().getDrawable(imageResource);
		Bicon.setImageDrawable(image);
	}
}
