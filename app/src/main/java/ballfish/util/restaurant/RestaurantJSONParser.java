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

            jRestaurants = jObject.getJSONArray("restaurant");

            for (int cnt = 0; cnt < jRestaurants.length(); cnt++) {
                JSONObject row = (JSONObject) jRestaurants.get(cnt);
                String name = row.getString("name");
                String tmp = row.getString("detail");
                String detail = tmp.replace("\\n", "\n");
                String address = row.getString("address");
                String photos = row.getString("photos");
                String _class = row.getString("class");
                String latlng = row.getString("latlng");

                Restaurant rest = new Restaurant(name, detail, address, photos, _class, latlng);
                list.add(rest);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        return list;
    }
}
