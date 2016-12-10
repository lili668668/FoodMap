package scu.csim.student.foodmap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ballfish.util.map.Directions;

/**
 * Created by yin on 2016/12/10.
 */

public class SetCard {
    private SetCard() {}

    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static void show(Activity activity) {
        sp = activity.getSharedPreferences("FoodMap", Context.MODE_PRIVATE);
        editor = sp.edit();

        LayoutInflater li = activity.getLayoutInflater();
        View set_layout = li.inflate(R.layout.set_layout, (ViewGroup) activity.findViewById(R.id.set_layout));

        String str = sp.getString("line_option", Directions.MODE_WALKING);
        int i = 0;
        switch (str) {
            case Directions.MODE_WALKING:
                i = R.id.radio_walk;
                break;
            case Directions.MODE_TRANSIT:
                i = R.id.radio_transit;
                break;
            case Directions.MODE_DRIVING:
                i = R.id.radio_car;
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(set_layout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        RadioGroup radioGroup = (RadioGroup) set_layout.findViewById(R.id.line_option_radio);

        if (i != 0) {
            radioGroup.check(i);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String str = "line_option";
                switch (checkedId) {
                    case R.id.radio_walk:
                        editor.putString(str, Directions.MODE_WALKING);
                        break;
                    case R.id.radio_transit:
                        editor.putString(str, Directions.MODE_TRANSIT);
                        break;
                    case R.id.radio_car:
                        editor.putString(str, Directions.MODE_DRIVING);
                        break;
                }
                editor.apply();
            }
        });
    }
}
