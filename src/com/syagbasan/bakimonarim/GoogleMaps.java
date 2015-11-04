package com.syagbasan.bakimonarim;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GoogleMaps extends Activity implements OnClickListener {

	GoogleMap gMap;
	static TextView tvdecimallatitude,tvdecimallongitude,tvlatituderef,tvlongituderef;
	int port=21;
	static double latsql,lonsql;
	static boolean only_one_marker = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapsview);
		
		only_one_marker = true;
		tvdecimallatitude = (TextView) findViewById(R.id.tvDecimalLatitude);
		tvdecimallongitude = (TextView) findViewById(R.id.tvDecimalLongitude);
		
		gMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gMap.setMyLocationEnabled(true);
		
		gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.2570869, 27.4431267), 7));
		
		gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			
			@SuppressWarnings("unused")
			@Override
			public void onMapClick(LatLng position) {

				if(only_one_marker == true){
					
					gMap.addMarker(new MarkerOptions()
							.position(new LatLng(position.latitude, position.longitude))
							.title(""));
					
					only_one_marker = false;
					int num1Lat = (int)Math.floor(position.latitude);
				    int num2Lat = (int)Math.floor((position.latitude - num1Lat) * 60);
				    double num3Lat = (position.latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;
				    
				    int num1Lon = (int)Math.floor(position.longitude);
				    int num2Lon = (int)Math.floor((position.longitude - num1Lon) * 60);
				    double num3Lon = (position.longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;
				  	   
				    tvdecimallatitude.setText("Latitude: "+position.latitude);
				    tvdecimallongitude.setText("Longitude: "+position.longitude);
				    
				    latsql = position.latitude;
				    lonsql = position.longitude;

				    /*
				    if (position.latitude > 0) {
				    	tvlatituderef.setText(" N");
				    } else {
				    	tvlatituderef.setText(" S");
				    }
				    
				    if (position.longitude > 0) {
				    	tvlongituderef.setText(" E");
				    } else {
				    	tvlongituderef.setText(" W");
				    }*/
				}
			}
		}); 

		gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() 
		{	
			@Override
			public boolean onMarkerClick(Marker marker) 
			{

				if (marker != null && only_one_marker == false) 
				{
					marker.remove();
					only_one_marker = true;
					latsql = Double.NaN;
					lonsql = Double.NaN;
					tvdecimallatitude.setText("");
					tvdecimallongitude.setText("");
				}
				return false;
			}	
		});	
	}
	
	public void Geri(View view){
		if(only_one_marker == false){
			MainActivity.haritadanLat.setText("Haritadan seçilen-"+tvdecimallatitude.getText().toString());
			MainActivity.haritadanLon.setText("Haritadan seçilen-"+tvdecimallongitude.getText().toString());
			onBackPressed();
		}

		if(only_one_marker == true){
			AlertDialog ad2 = new AlertDialog.Builder(this)
	 	   	.setIcon(R.drawable.ic_launcher)
	 		.setTitle("Konum güncellemek için pin koymadınız haritadan çıkmak istediğinize emin misiniz?")
	 		.setPositiveButton("Evet", this)
	 		.setNegativeButton("Hayır", this)
	 		.setCancelable(false)
	 		.create();
	 		ad2.show();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {

	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		switch(arg1){
		case DialogInterface.BUTTON_POSITIVE:
			onBackPressed();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			break;
		}
	}
}