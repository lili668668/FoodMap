package scu.csim.student.foodmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//從checklist上點進來後的店家資訊，有店名、店家、圖片介紹
public class ListContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);
    }
}
