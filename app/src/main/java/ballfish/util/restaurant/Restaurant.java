package ballfish.util.restaurant;

/**
 * Created by yin on 2016/11/21.
 */

public class Restaurant {
    public String name;
    public String detail;
    public String address;
    public String photos;
    public String _class;
    public double lat = 0;
    public double lng = 0;

    public Restaurant(String name, String detail, String address, String photos, String _class, String latlng) {
        this.name = name;
        this.detail = detail;
        this.address = address;
        this.photos = photos;
        this._class = _class;

        if (latlng != null) {
            String[] list = latlng.split(",");
            this.lat = Double.parseDouble(list[0].trim());
            this.lng = Double.parseDouble(list[1].trim());
        }

    }
}
