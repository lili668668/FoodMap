package scu.csim.student.foodmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import ballfish.util.map.Helper;
import ballfish.util.restaurant.AfterGetListExecute;
import ballfish.util.restaurant.Restaurant;
import ballfish.util.restaurant.RestaurantAPI;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    // 側邊欄
    private DrawerLayout navLayout;
    private NavigationView navView;

    // 本體
    private Context context;

    // 地圖
    private GoogleMap mMap;

    // GPS
    private android.location.LocationListener locationListener;
    private LocationManager locationManager;
    private static final int REQUEST_GPS = 492;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        navLayout = (DrawerLayout) findViewById(R.id.nav_layout);
        navView = (NavigationView) findViewById(R.id.nav_map_view);
        navView.setNavigationItemSelectedListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(context);
        openGPS();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle rest_data = getIntent().getExtras();
        if (rest_data != null) {
            String address = rest_data.getString("address");
            ((MyLocationListener) locationListener).setNeedToDraw(Helper.getLatLngByAddress(address));
        }
    }

    @Override
    protected void onResume() {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new MyLocationListener(context);
        }

        updateGPS();

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (locationManager != null) {
            removeGPS();
            locationManager = null;
        }
        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // 美食地圖
        if (id == R.id.nav_camera) {
            navLayout.closeDrawer(GravityCompat.START);
            return true;

        // 美食清單
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(context, List.class);
            startActivity(intent);
            this.finish();

        // 今天吃什麼？
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(context, DiceActivity.class);
            startActivity(intent);
            finish();

        // 設定
        } else if (id == R.id.nav_manage) {
            SetCard.show(this);
        }

        navLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (navLayout.isDrawerOpen(GravityCompat.START)) {
            navLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ((MyLocationListener) locationListener).setMap(googleMap);

        RestaurantAPI api = RestaurantAPI.getInstance();
        try {
            api.getList(new AfterGetListExecute() {
                @Override
                public void execute(ArrayList<Restaurant> list) {
                LatLng test;
                if (list == null || list.size() == 0) {
                    Toast.makeText(context, "收不到資料", Toast.LENGTH_SHORT).show();
                    test = Helper.getLatLngByAddress("100台北市中正區貴陽街一段56號");
                } else {
                    for (int cnt = 0;cnt < list.size();cnt++) {
                        Restaurant rest = list.get(cnt);
                        test = Helper.getLatLngByAddress(rest.address);
                        if (test == null) {
                            System.out.println(rest.name + " is null");
                        } else {
                            MarkerOptions tmp = new MarkerOptions()
                                    .position(test)
                                    .title(rest.name)
                                    .snippet(rest.detail);
                            int inttmp = getIconFromClass(rest._class);
                            if (inttmp != 0) {
                                tmp.icon(BitmapDescriptorFactory.fromResource(inttmp));
                            }
                            
                            mMap.addMarker(tmp);
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    ((MyLocationListener) locationListener).setNeedToDraw(marker.getPosition());
                                    marker.showInfoWindow();
                                    return true;
                                }
                            });
                        }
                    }
                }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openGPS() {
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return ;
        } else {
            Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsIntent);
        }
    }

    private void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_GPS);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    private void removeGPS() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_GPS);
        } else {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GPS:
                updateGPS();
                break;
        }

    }

    private int getIconFromClass(String _class) {
        switch (_class) {
            case "noodle":
                return R.drawable.noodle;
            case "rice":
                return R.drawable.rice;
            case "pizza":
                return R.drawable.pizza;
            case "dumpling":
                return R.drawable.dumpling;
            case "hamburger":
                return R.drawable.hamburger;
        }
        return 0;
    }
}
