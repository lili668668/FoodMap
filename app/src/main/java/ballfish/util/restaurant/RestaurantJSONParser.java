package ballfish.util.restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yin on 2016/11/21.
 */

public class RestaurantJSONParser {

    public ArrayList<Restaurant> parse(JSONObject jObject) {

        ArrayList<Restaurant> list =
                new ArrayList<Restaurant>();
        JSONArray jRestaurants = null;

        try {

            jRestaurants = jObject.getJSONArray("restaurants");

            for (int cnt = 0; cnt < jRestaurants.length(); cnt++) {
                JSONObject row = (JSONObject) jRestaurants.get(cnt);
                String name = row.getString("name");
                String detail = row.getString("detail");
                String address = row.getString("address");

                Restaurant rest = new Restaurant(name, detail, address);
                list.add(rest);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return list;
    }
}
