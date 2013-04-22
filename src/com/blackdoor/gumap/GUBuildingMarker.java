package com.blackdoor.gumap;

import java.util.Locale;

import com.blackdoor.gumap.MainActivity.Zoom;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

public class GUBuildingMarker {
	
	private MarkerOptions buildingOptions;
	private String name, description, hours, services, dining, contactInfo;
	private LatLng coordinates;
	private Zoom zoomLevel = Zoom.MEDIUM;
	
		
	public GUBuildingMarker(String newName, LatLng newCoords, String newDescription, String newHours, String newServices, String newDining, String newContactInfo)
	{
		this.name = newName;
		this.coordinates = newCoords;
		this.description = newDescription;
		this.hours = newHours;
		this.services = newServices;
		this.dining = newDining;
		this.contactInfo = newContactInfo;
		
		buildingOptions.draggable(false).position(coordinates).title(name).icon(BitmapDescriptorFactory.fromAsset(name + "_" + zoomLevel + ".png"));
	}
	
	public void updateIcon()
	{
		buildingOptions.icon(BitmapDescriptorFactory.fromAsset(name + "_" + zoomLevel + ".png"));		
	}
	
	public String getName()
	{
		return(name);
	}
	
	public LatLng getCoordinates()
	{
		return(coordinates);
	}
	
	public String getDescription()
	{
		return(description);
	}

	public String getHours()
	{
		return(hours);
	}
	
	public String getServices()
	{
		return(services);
	}
	
	public String getDining()
	{
		return(dining);
	}
	
	public String getContactInfo()
	{
		return(contactInfo);
	}
	
	public Zoom getZoom()
	{
		return(zoomLevel);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setCoordinates(LatLng coords)
	{
		this.coordinates = coords;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setHours(String hours)
	{
		this.hours = hours;
	}
	
	public void setServices(String services)
	{
		this.services = services;
	}
	
	public void setDining(String dining)
	{
		this.dining = dining;
	}
	
	public void setContactInfo(String contactInfo)
	{
		this.contactInfo = contactInfo;
	}
	
	public void setZoom(Zoom zLevel)
	{
		this.zoomLevel = zLevel;
	}
}
