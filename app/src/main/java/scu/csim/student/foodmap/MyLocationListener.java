package scu.csim.student.foodmap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (map != null) {
            map.clear();
            LatLng nowLat = new LatLng(latitude, longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(nowLat).zoom(13).build();
            map.addMarker(new MarkerOptions()
                    .position(nowLat)
                    .title("Now Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
