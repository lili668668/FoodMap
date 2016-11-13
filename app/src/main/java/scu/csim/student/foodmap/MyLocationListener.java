package scu.csim.student.foodmap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

/**
 * GPS實作
 */

public class MyLocationListener implements android.location.LocationListener {
    Context context;

    public MyLocationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
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
}
