package scu.csim.student.foodmap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ballfish.util.restaurant.AfterGetListExecute;
import ballfish.util.restaurant.Restaurant;
import ballfish.util.restaurant.RestaurantAPI;

//checklist的列表，上面有店家名稱
public class List extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // 給雅鈴: context 要在onCreate裡面抓喔
    Context context;
    ListView list_view;
    ArrayAdapter<String> adapter;
    private String[] names = {"cat", "flower"};//list 的內容
    private String [] detail = {"測試一", "測試二"};//店家的圖片
    //String[] food;

    // 慈吟：在雅鈴的清單裡加入側邊欄
    private DrawerLayout navLayout;
    private NavigationView navView;
    private SimpleAdapter simpleAdapter;

    private ArrayList<Restaurant> restaurantList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_with_drawer);

        // 給雅鈴: context 要在onCreate裡面抓喔
        context = getApplicationContext();

        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setEmptyView(findViewById(R.id.empty_view));

        // 慈吟：在雅鈴的清單裡加入側邊欄
        navLayout = (DrawerLayout) findViewById(R.id.activity_list_with_drawer);
        navView = (NavigationView) findViewById(R.id.nav_list_view);
        navView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navLayout, toolbar,  R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navLayout.setDrawerListener(toggle);
        toggle.syncState();

        // 取得店家資料
        RestaurantAPI api = RestaurantAPI.getInstance();
        try {
            api.getList(new AfterGetListExecute() {
                @Override
                public void execute(ArrayList<Restaurant> list) {

                    if (list == null) {
                        return;
                    }

                    ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();

                    for (int cnt = 0; cnt < list.size() ; cnt++) {
                        Restaurant rest = list.get(cnt);
                        HashMap<String, Object> item = new HashMap<String, Object>();
                        item.put("name",rest.name);
                        item.put("detail",rest.detail);
                        items.add(item);
                    }
                    simpleAdapter = new SimpleAdapter(context, items, R.layout.item_layout,new String[]{"name", "detail"},
                            new int[]{R.id.name_text, R.id.detail_text});
                    list_view.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();

                    restaurantList = list;

                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (restaurantList != null) {
                                Restaurant rest = restaurantList.get(position);

                                Intent intent = new Intent(context,ListContentActivity.class);
                                Bundle rest_data = new Bundle();

                                //get value
                                String rest_photos = rest.photos; // 取得很多圖片的檔名
                                String rest_name = rest.name;
                                String rest_address = rest.address;
                                String rest_detail = rest.detail;

                                //package it
                                rest_data.putString("photos", rest_photos); // 將圖片檔名傳過去
                                rest_data.putString("name",rest_name);
                                rest_data.putString("address",rest_address);
                                rest_data.putString("detail",rest_detail);

                                //send
                                intent.putExtras(rest_data);
                                startActivity(intent);
                            }
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            SetCard.show(this);
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
