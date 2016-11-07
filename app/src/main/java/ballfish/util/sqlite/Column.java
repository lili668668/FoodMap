package ballfish.util.sqlite;

/**
 * 欄位的物件，紀錄欄位的屬性
 */

public class Column {
    public enum Type {Integer, Double, Text, Blob}
    public String name;
    public boolean isKey;
    public boolean isNull;
    public Type type;
}
