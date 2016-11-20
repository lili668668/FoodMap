package scu.csim.student.foodmap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * GPS實作
 */

public class MyLocationListener implements android.location.LocationListener {
    private Context context;
    private GoogleMap map = null;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latittude = location.getLatitude();
        double longitude = location.getLongitude();
        if (map != null) {
            map.clear();
            LatLng nowLat = new LatLng(latittude, longitude);
            map.addMarker(new MarkerOptions().position(nowLat).title("Now Location"));
            map.moveCamera(CameraUpdateFactory.newLatLng(nowLat));
        }

        String str = "緯度" + location.getLatitude() + " 經度" + location.getLongitude() + " 標高" + location.getAltitude() + " 方位" + location.getBearing();
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }
}
