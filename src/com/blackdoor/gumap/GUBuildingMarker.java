package com.blackdoor.gumap;

import com.blackdoor.gumap.MainActivity.Zoom;
import com.google.android.gms.maps.model.*;

import android.os.Parcel;
import android.os.Parcelable;

public class GUBuildingMarker implements Parcelable{
	Marker	closeMarker;
	Marker	mediumMarker;
	private MarkerOptions buildingOptions;
	private String name, description, hours, services, dining, contactInfo;
	private LatLng coordinates;
	private Zoom zoomLevel = Zoom.MEDIUM;
	private MainActivity containingActivity;
	private BitmapDescriptor iconClose;
	private BitmapDescriptor iconMedium;

	public GUBuildingMarker(MainActivity containingActivity, String newName,
			LatLng newCoords, String newDescription, String newHours,
			String newServices, String newDining, String newContactInfo) {
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
		buildingOptions = new MarkerOptions();
		buildingOptions.draggable(false).position(newCoords).title(name);// .icon(iconMedium);
		buildingOptions.snippet(description);
		addMarkers();		
	}
	public GUBuildingMarker(MainActivity containingActivity) {
		this.containingActivity = containingActivity;
		name = "";
		coordinates = new LatLng(0,0);
		description = "";
		hours = "";
		services = "";
		dining = "";
		contactInfo = "";
		//iconClose = BitmapDescriptorFactory.fromAsset(name + "_CLOSE" + ".png");
		//iconMedium = BitmapDescriptorFactory.fromAsset(name + "_MEDIUM" + ".png");
		buildingOptions = new MarkerOptions();
		buildingOptions.draggable(false);// .icon(iconMedium);
		//buildingOptions.snippet(description);
		//mediumMarker = containingActivity.guMap.addMarker(buildingOptions.icon(iconMedium).visible(true));
		//closeMarker = containingActivity.guMap.addMarker(buildingOptions.icon(iconClose).visible(false));
		
	}
	
	private void addMarkers(){
		try {
			mediumMarker = containingActivity.guMap.addMarker(buildingOptions
					.icon(iconMedium).visible(true));
			closeMarker = containingActivity.guMap.addMarker(buildingOptions
					.icon(iconClose).visible(false));
		} catch (Exception e) {
			System.err.println(e);
			mediumMarker = containingActivity.guMap.addMarker(buildingOptions
					.icon(null).visible(true));
			closeMarker = containingActivity.guMap.addMarker(buildingOptions
					.icon(null).visible(false));
		}
	}
	/**
	 * @deprecated dont freaking use this
	 */
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
            case CLOSE:  	
            	closeMarker.setVisible(true);
            	mediumMarker.setVisible(false);
                     break;
            case MEDIUM:	
            	closeMarker.setVisible(false);
            	mediumMarker.setVisible(true);
                     break;
            case FAR:		
            	closeMarker.setVisible(false);
            	mediumMarker.setVisible(false);
            		break;
            default: closeMarker.setVisible(false);
        			 mediumMarker.setVisible(true);
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
	
///////////////////////////////////////////////////////////////////////////
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	//actually fuckit, we don't need to set the zoomLevel for each marker individually
	// public void setZoom(Zoom zLevel)
	// {
		// this.zoomLevel = zLevel;
		//whats the point of changing the zoomLevel if we don't update the icon?
		// updateIcon();
	// }
}
