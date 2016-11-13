package scu.csim.student.foodmap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout navLayout;
    private NavigationView navView;
    private Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        navLayout = (DrawerLayout) findViewById(R.id.nav_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // 美食地圖
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(context, List.class);
            startActivity(intent);

        // 美食清單
        } else if (id == R.id.nav_gallery) {


        // 今天吃什麼？
        } else if (id == R.id.nav_slideshow) {


        // 設定
        } else if (id == R.id.nav_manage) {

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
}
