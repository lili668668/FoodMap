package scu.csim.student.foodmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

//從checklist上點進來後的店家資訊，有店名、店家、圖片介紹
public class ListContentActivity extends AppCompatActivity {

    TextView restTitle;
    TextView resdetail;
    TextView resaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        restTitle = (TextView) findViewById(R.id.restTitle_textView);
        resdetail = (TextView) findViewById(R.id.detail_textView);
        resaddress = (TextView) findViewById(R.id.address_textView);

        //get bundle(restaurant data)
        Bundle rest_data = getIntent().getExtras();
        String rest_name = rest_data.getString("name");
        String rest_address = rest_data.getString("address");
        String rest_detail = rest_data.getString("detail");

        restTitle.setText(rest_name);
        resdetail.setText(rest_detail);
        resaddress.setText(rest_address);

    }
}
