package com.blackdoor.gumap;


import java.util.Map;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.view.View;
import android.widget.EditText;



public class GUBuildingInfoWindowAdapter implements InfoWindowAdapter {
	MainActivity containingActivity;
	public GUBuildingInfoWindowAdapter(MainActivity containingActivity){
		this.containingActivity = containingActivity;
	}
	@Override
	public View getInfoContents(Marker arg0) {
		Map markerMap = containingActivity.getMarkers();
		GUBuildingMarker marker = markerMap.get(arg0.getTitle());
		EditText newText = new EditText(null);
		newText.setText(marker.getName()+"\n"+marker.getDescription()+marker.getHours()+"\n"+marker.getDescription());
		return newText;
	}
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
