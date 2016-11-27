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
    public AfterGetListExecute execute;

    public void getList(AfterGetListExecute execute) throws IOException {
        this.execute = execute;
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute();
    }

    private String getAllRestaurantString() throws IOException {
        String urlstr = HOST + "all_restaurant";
        System.out.println(urlstr);
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

        System.out.println(data);

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
            AsyncTask<String, Integer, ArrayList<Restaurant>> {
        @Override
        protected ArrayList<Restaurant> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            ArrayList<Restaurant> list = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                RestaurantJSONParser parser = new RestaurantJSONParser();
                // 解析JSON資料
                list = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
        @Override
        protected void onPostExecute(ArrayList<Restaurant> result) {
            super.onPostExecute(result);
            execute.execute(result);
        }
    }
}
