package scu.csim.student.foodmap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import ballfish.util.map.Directions;
import ballfish.util.map.Helper;
import ballfish.util.restaurant.Restaurant;
import ballfish.util.restaurant.RestaurantAPI;

/**
 * GPS實作
 */

public class MyLocationListener implements android.location.LocationListener {
    private Context context;
    private GoogleMap map = null;
    private CameraPosition cameraPosition;
    private Marker nowMarker;

    private LatLng mySchool;

    private boolean flag = true;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    public MyLocationListener(Context context, LatLng test) {
        this.context = context;
        mySchool = test;
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

            LatLng nowLat = new LatLng(latitude, longitude);

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

            if (flag) {
                flag = false;
                RestaurantAPI api = RestaurantAPI.getInstance();
                ArrayList<Restaurant> list = new ArrayList<Restaurant>();
                try {
                    list = api.getList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng test;
                if (list.size() == 0) {
                    test = Helper.getLatLngByAddress("100台北市中正區貴陽街一段56號");
                } else {
                    test = Helper.getLatLngByAddress(list.get(0).address);
                }

                Directions.lineColor = Color.RED;
                Directions.lineWidth = 10;
                Directions.getInstance().draw(context, nowLat, test, map, Directions.MODE_TRANSIT);
            }
        }

        String str = "緯度" + latitude + " 經度" + longitude + " 標高" + location.getAltitude() + " 方位" + location.getBearing();
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
