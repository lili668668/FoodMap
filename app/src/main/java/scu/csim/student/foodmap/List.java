package scu.csim.student.foodmap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//checklist的列表，上面有店家名稱
public class List extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    // 給雅鈴: context 要在onCreate裡面抓喔
    Context context;
    ListView list_view;
    ArrayAdapter<String> adapter;
    String[] food;

    // 慈吟：在雅鈴的清單裡加入側邊欄
    private DrawerLayout navLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_drawer);

        // 給雅鈴: context 要在onCreate裡面抓喔
        context = getApplicationContext();

        // 慈吟：在雅鈴的清單裡加入側邊欄
        navLayout = (DrawerLayout) findViewById(R.id.activity_list_with_drawer);
        navView = (NavigationView) findViewById(R.id.nav_list_view);
        navView.setNavigationItemSelectedListener(this);

        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setEmptyView(findViewById(R.id.empty_view));
        food = getResources().getStringArray(R.array.list_content);
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,food);
        list_view.setAdapter(adapter);
        /*list_view.setOnClickListener(new AdapterView.OnItemClickListener() {

            //AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            //int position = menuInfo.position;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position , Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    // 慈吟：在雅鈴的清單裡加入側邊欄
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // 美食地圖
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            this.finish();

            // 美食清單
        } else if (id == R.id.nav_gallery) {
            navLayout.closeDrawer(GravityCompat.START);
            return true;

            // 今天吃什麼？
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(context, DiceActivity.class);
            startActivity(intent);
            finish();

            // 設定
        } else if (id == R.id.nav_manage) {

        }

        navLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 慈吟：在雅鈴的清單裡加入側邊欄
    @Override
    public void onBackPressed() {
        if (navLayout.isDrawerOpen(GravityCompat.START)) {
            navLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
