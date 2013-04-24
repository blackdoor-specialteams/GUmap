package com.blackdoor.gumap;


import java.util.Map;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.view.View;
import android.widget.TextView;


/**
 * adapter that will display the details of the calling marker in the info bubble
 * @author nfischer3
 *
 */
public class GUBuildingInfoWindowAdapter implements InfoWindowAdapter {
	MainActivity containingActivity;
	public GUBuildingInfoWindowAdapter(MainActivity containingActivity){
		this.containingActivity = containingActivity;
	}
	public void setContainer(MainActivity containingActivity){
		this.containingActivity = containingActivity;
	}
	/**
	 * returns a View that will be displayed in the info bubble 
	 * view contains basic textual detail
	 */
	@Override
	public View getInfoContents(Marker arg0) {
		Map<String, GUBuildingMarker> markerMap = containingActivity.getMarkers();
		GUBuildingMarker marker = markerMap.get(arg0.getTitle());
		TextView newText = new TextView(containingActivity);
		newText.setText(marker.getName()+"\n"+marker.getDescription()+marker.getHours()+"\n"+marker.getDescription());
		return newText;
	}
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
