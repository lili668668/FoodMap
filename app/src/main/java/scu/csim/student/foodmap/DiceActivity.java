package scu.csim.student.foodmap;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.graphics.drawable.RippleDrawable;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.google.android.gms.maps.OnMapReadyCallback;

public class DiceActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    // 側邊欄
    private DrawerLayout navLayout;
    private NavigationView navView;
    private Context context;

    private ImageView one;  //表示抽起來的籤(抽到時上移,再想想)
    private TextView rest;  //表示抽到的餐廳名
    //Intent intent = new Intent();

    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private double mSpeed;                 //甩動力道數度
    private long mLastUpdateTime;           //觸發時間

    //甩動力道數度設定值
    private static final int SPEED_SHRESHOLD = 3000;

    //觸發間隔時間
    private static final int UPTATE_INTERVAL_TIME = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_with_drawer);

        one = (ImageView) findViewById(R.id.one);
        rest = (TextView) findViewById(R.id.rest);

        // 側邊欄
        navLayout = (DrawerLayout) findViewById(R.id.activity_dice_with_drawer);
        navView = (NavigationView) findViewById(R.id.nav_dice_view);
        navView.setNavigationItemSelectedListener(this);
        context = getApplicationContext();

        //取得體感(Sensor)服務使用權限
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //註冊體感(Sensor)甩動觸發Listener
        mSensorManager.registerListener(SensorListener,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    public SensorEventListener SensorListener = new SensorEventListener()
    {
        // 慈吟：養成好習慣，在Java裡面，需要覆寫的method，需要加@Override
        @Override
        public void onSensorChanged(SensorEvent mSensorEvent)
        {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();

            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;

            //若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME) return;

            mLastUpdateTime = mCurrentUpdateTime;

            //取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];

            //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;

            //若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)
            if (mSpeed >= SPEED_SHRESHOLD)
            {
                //達到搖一搖甩動後要做的事情
                one.layout(one.getLeft(), one.getTop()-10, one.getRight(), one.getBottom());
                RandomNumber();  //隨機抽數字(暫時)

                //Log.d("TAG","搖一搖中...");

                //跳到下一頁(先做不跳頁的)
                //intent.setClass(DiceActivity.this , ResultActivity.class);
                //startActivity(intent);
            }
        }

        public void onAccuracyChanged(Sensor sensor , int accuracy)
        {
        }
    };

    private void RandomNumber(){
        //隨機10個數字
        int r = (int)(Math.random()*10 + 1);

        // 慈吟：型態不對，他Crash了
        // r是int不是string
        rest.setText(r + "");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //在程式關閉時移除體感(Sensor)觸發
        mSensorManager.unregisterListener(SensorListener);
    }

    // 慈吟：在詩堯的清單裡加入側邊欄
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
            Intent intent = new Intent(context, List.class);
            startActivity(intent);
            this.finish();

            // 今天吃什麼？
        } else if (id == R.id.nav_slideshow) {
            navLayout.closeDrawer(GravityCompat.START);
            return true;

            // 設定
        } else if (id == R.id.nav_manage) {

        }

        navLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // 慈吟：在詩堯的清單裡加入側邊欄
    @Override
    public void onBackPressed() {
        if (navLayout.isDrawerOpen(GravityCompat.START)) {
            navLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
