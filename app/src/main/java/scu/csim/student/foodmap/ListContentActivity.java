package scu.csim.student.foodmap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ballfish.util.restaurant.RestaurantAPI;

//從checklist上點進來後的店家資訊，有店名、店家、圖片介紹
public class ListContentActivity extends AppCompatActivity {

    Context context;

    ImageView restPhoto;
    TextView restTitle;
    TextView resdetail;
    TextView resaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        context = getApplicationContext();

        restPhoto = (ImageView) findViewById(R.id.food_imageview);
        restTitle = (TextView) findViewById(R.id.restTitle_textView);
        resdetail = (TextView) findViewById(R.id.detail_textView);
        resaddress = (TextView) findViewById(R.id.address_textView);

        //get bundle(restaurant data)
        Bundle rest_data = getIntent().getExtras();
        String rest_photos = rest_data.getString("photos"); // 取得圖片檔名
        String rest_name = rest_data.getString("name");
        String rest_address = rest_data.getString("address");
        String rest_detail = rest_data.getString("detail");

        // 如果圖片檔名是null就不執行
        if (rest_photos != null) {
            // 很多圖片的檔名是用逗號分隔的
            String[] rest_photos_arr = rest_photos.split(",");

            // 圖片的網址
            String url = RestaurantAPI.HOST + RestaurantAPI.PHOTO_DIR + rest_photos_arr[0];

            // 下載圖片並且傳送給ImageView
            Picasso.with(context).load(url).into(restPhoto);
        }

        restTitle.setText(rest_name);
        resdetail.setText(rest_detail);
        resaddress.setText(rest_address);

    }
}
