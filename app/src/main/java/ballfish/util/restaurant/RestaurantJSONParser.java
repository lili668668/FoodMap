package ballfish.util.restaurant;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yin on 2016/11/21.
 */

public class RestaurantJSONParser {

    /** 接收一個JSONObject並返回一個列表的列表，包含經緯度 */
    public ArrayList<Restaurant> parse(JSONObject jObject) {

        ArrayList<Restaurant> list =
                new ArrayList<Restaurant>();
        JSONArray jRestaurants = null;

        try {

            jRestaurants = jObject.getJSONArray("restaurants");

            /** Traversing all routes */
            for (int cnt = 0; cnt < jRestaurants.length(); cnt++) {
                JSONObject row = (JSONObject) jRestaurants.get(cnt);
                String name = row.getString("name");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return routes;
    }
}
