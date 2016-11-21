package ballfish.util.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
}
