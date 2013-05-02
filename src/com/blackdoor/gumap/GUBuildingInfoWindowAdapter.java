package com.blackdoor.gumap;


import java.util.Map;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
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
		LinearLayout output = new LinearLayout(containingActivity);
		output.setOrientation(LinearLayout.VERTICAL);
		
		TextView name = new TextView(containingActivity);
		name.setTypeface(Typeface.DEFAULT, 1);
		name.setText(marker.getName());
		name.setTextColor(Color.BLACK);
		name.setTextScaleX((float) 1.3);
		
		//TextView description = new TextView(containingActivity);
		//description.setText(marker.getDescription());
		//description.setTextColor(Color.DKGRAY);
		
		TextView hours = new TextView(containingActivity);
		hours.setText(marker.getHours());
		hours.setTypeface(Typeface.DEFAULT_BOLD);
		hours.setTextColor(Color.DKGRAY);
		
		output.addView(name);
		//output.addView(description);
		output.addView(hours);
		
		TextView newText = new TextView(containingActivity);
		newText.setText(marker.getName()+"\n"+marker.getDescription()+"\n" +marker.getHours());
		return output;
	}
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
