package scu.csim.student.foodmap;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//checklist的列表，上面有店家名稱
public class List extends AppCompatActivity {

    // 給雅鈴: context 要在onCreate裡面抓喔
    Context context;
    ListView list_view;
    ArrayAdapter<String> adapter;
    String[] food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 給雅鈴: context 要在onCreate裡面抓喔
        context = getApplicationContext();

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
}
