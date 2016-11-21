package ballfish.util.restaurant;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantAPI {
    private static final String HOST = "http://foodmap-nethw.rhcloud.com/";

    private RestaurantAPI() {}
    private static RestaurantAPI _instance = new RestaurantAPI();
    public static RestaurantAPI getInstance() {
        return _instance;
    }

    private ArrayList<Restaurant> list = new ArrayList<Restaurant>();

    private String getAllRestaurantString() throws IOException {
        String urlstr = HOST + "all_restaurant";
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlstr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            data = stringBuffer.toString();
            bufferedReader.close();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }

            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    // 下載並解析JSON
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                // 取得所下載的資料
                data = getAllRestaurantString();
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            RestaurantAPI.ParserTask parserTask = new RestaurantAPI.ParserTask();
            // 解析JSON
            parserTask.execute(result);
        }
    }
    // 解析JSON格式
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // 解析JSON資料
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            // 走訪所有 result
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                // 得到每一個位置(經緯度)資料
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    // 放置折線點經緯度集合
                    points.add(position);
                }
                // 繪製折線點經緯度集合
                lineOptions.addAll(points);
                lineOptions.width(lineWidth); // 導航路徑寬度
                lineOptions.color(lineColor); // 導航路徑顏色
            }
            if(lineOptions != null) {
                map.addPolyline(lineOptions);
            } else {
                Toast.makeText(context, mode + "模式下無導航路徑 !",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
