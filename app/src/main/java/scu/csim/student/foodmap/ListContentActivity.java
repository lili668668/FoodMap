package scu.csim.student.foodmap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ballfish.util.restaurant.RestaurantAPI;

//從checklist上點進來後的店家資訊，有店名、店家、圖片介紹
public class ListContentActivity extends AppCompatActivity {

    Context context;

    LinearLayout PhotosList;
    TextView restTitle;
    TextView resdetail;
    TextView resaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        context = getApplicationContext();

        PhotosList = (LinearLayout) findViewById(R.id.photos_view);

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

            // 迴圈跑每個圖片檔案
            for(int cnt = 0;cnt < rest_photos_arr.length;cnt++) {
                // 新增一個ImageView
                ImageView im = new ImageView(context);
                PhotosList.addView(im);

                // 圖片的網址
                String url = RestaurantAPI.HOST + RestaurantAPI.PHOTO_DIR + rest_photos_arr[cnt];

                // 下載圖片並且傳送給ImageView
                Picasso.with(context)
                        .load(url)
                        .placeholder(R.drawable.pic_no_pix)
                        .error(R.drawable.pic_no_pix)
                        .into(im);
            }
        }

        restTitle.setText(rest_name);
        resdetail.setText(rest_detail);
        resaddress.setText(rest_address);

    }
}
