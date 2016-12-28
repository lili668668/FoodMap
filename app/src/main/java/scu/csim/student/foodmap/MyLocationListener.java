package scu.csim.student.foodmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ballfish.util.map.Directions;

/**
 * GPS實作
 */

public class MyLocationListener implements android.location.LocationListener {
    private Context context;
    private GoogleMap map = null;
    private CameraPosition cameraPosition;
    private Marker nowMarker;
    private LatLng needToDraw;
    private Directions directions;
    private SharedPreferences sp;

    public MyLocationListener(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("FoodMap", Context.MODE_PRIVATE);
        Directions.lineWidth = 20;
        Directions.lineColor = Color.RED;
        directions = Directions.getInstance();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        if (map != null) {

            if (nowMarker != null) {
                nowMarker.remove();
                nowMarker = null;
            }

            final LatLng nowLat = new LatLng(latitude, longitude);

            nowMarker = map.addMarker(new MarkerOptions()
                    .position(nowLat)
                    .title("Now Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass)));

            if (cameraPosition == null) {
                cameraPosition = new CameraPosition.Builder()
                        .target(nowLat)
                        .zoom(13)
                        .build();
                map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            draw();
        }
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

    public void setNeedToDraw(LatLng needToDraw) {
        this.needToDraw = needToDraw;
        draw();
    }

    private void draw() {
        if (nowMarker != null && needToDraw != null && this.map != null) {
            directions.draw(context, nowMarker.getPosition(), needToDraw, map, sp.getString("line_option", Directions.MODE_WALKING));
        }
    }
}
