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
	private MainActivity containingActivity;
	private BitmapDescriptor iconClose;
	private BitmapDescriptor iconMedium;
	private BitmapDescriptor iconFar;	
	public GUBuildingMarker(MainActivity containingActivity, String newName, LatLng newCoords, String newDescription, String newHours, String newServices, String newDining, String newContactInfo)
	{
		this.containingActivity = containingActivity;
		name = newName;
		coordinates = newCoords;
		description = newDescription;
		hours = newHours;
		services = newServices;
		dining = newDining;
		contactInfo = newContactInfo;
		iconClose = BitmapDescriptorFactory.fromAsset(name + "_CLOSE" + ".png");
		iconMedium = BitmapDescriptorFactory.fromAsset(name + "_MEDIUM" + ".png");
		iconFar = BitmapDescriptorFactory.fromAsset(name + "_FAR" + ".png");
		buildingOptions.draggable(false).position(coordinates).title(name).icon(iconMedium);
	}
	
	public void updateBuildingOptions()
	{
		buildingOptions.draggable(false).position(coordinates).title(name);
		updateIcon();
	}
	
	// much better
	public void updateIcon()
	{
		zoomLevel = containingActivity.getZoom();
		switch (zoomLevel) {
            case CLOSE:  	buildingOptions.icon(iconClose);
                     break;
            case MEDIUM:	buildingOptions.icon(iconMedium);
                     break;
            case FAR:		buildingOptions.icon(iconFar);
            default: buildingOptions.icon(iconMedium);
                     break;
        }
		//buildingOptions.icon(BitmapDescriptorFactory.fromAsset(name + "_" + zoomLevel + ".png"));		
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
	
	public MarkerOptions getBuildingOptions()
	{
		return(buildingOptions);
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
	
	//actually fuckit, we don't need to set the zoomLevel for each marker individually
	// public void setZoom(Zoom zLevel)
	// {
		// this.zoomLevel = zLevel;
		//whats the point of changing the zoomLevel if we don't update the icon?
		// updateIcon();
	// }
}
