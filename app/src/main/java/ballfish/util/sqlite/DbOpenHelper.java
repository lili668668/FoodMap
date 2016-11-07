package ballfish.util.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用以連線資料庫的類別，底層類別
 */

public class DbOpenHelper extends SQLiteOpenHelper {
    private String dbName = "";
    private String tableName = "";
    private Column[] columns = null;
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, String tableName, Column[] columns) {
        super(context, name, factory, version);
        dbName = name;
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String exe = CreateTableString(this.tableName, this.columns);
        db.execSQL(exe);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DropTableString(this.tableName));
        onCreate(db);
    }

    public static String CreateTableString(String tableName, Column[] columns) {
        String res = "";
        final String CREATE = "create table ";
        final String IS_KEY = "primary key autoincrement";
        final String NOT_NULL = "not null";
        final String INTEGER = "integer";
        final String DOUBLE = "real";
        final String TEXT = "text";
        final String BLOB = "blob";

        res = CREATE + tableName + "(";
        boolean flag = true;
        for (Column column : columns) {
            if (flag) {
                flag = false;
            } else {
                res += ", ";
            }
            res += column.name + " ";
            switch (column.type) {
                case Integer:
                    res += INTEGER + " ";
                    break;
                case Double:
                    res += DOUBLE + " ";
                    break;
                case Text:
                    res += TEXT + " ";
                    break;
                case Blob:
                    res += BLOB + " ";
                    break;
            }

            if (column.isKey) {
                res += IS_KEY + " ";
            }

            if (column.isNull) {
                res += NOT_NULL + " ";
            }
        }

        res += ");";

        return res;
    }

    public static String DropTableString(String tableName) {
        final String DROP = "drop table if exists";
        return DROP + tableName;
    }
}
