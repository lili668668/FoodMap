package ballfish.util.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Blob;
import java.util.ArrayList;

/**
 * 資料庫的物件，用以操作資料庫使用
 */

public class Database {
    private String dbName = "";
    private int version = 1;
    private DbOpenHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public static final String LIKE = " like ?";
    public static final String EQUALS = " = ?";
    public static final String MORE = " > ?";
    public static final String LESS = " < ?";
    public static final String NOTEQUALS = " != ?";
    public static final String AND = " and ";
    public static final String OR = " or ";

    /**
     * 連線資料庫，若該資料庫不存在，則會建立該資料庫
     * @param DBName: 資料庫的名字，請輸入XXX.db
     * @param context: 應用程式的context
     * @param version: DB的版本
     * @param tableName: 表單的名稱
     * @param columns: 欄位資料
     */
    public Database(String DBName, Context context, int version, String tableName, Column[] columns) {
        this.dbName = DBName;
        this.version = version;
        this.dbHelper = new DbOpenHelper(context, this.dbName, null, this.version, tableName, columns);
        this.db = dbHelper.getWritableDatabase();
    }

    /**
     * 連線資料庫，若該資料庫不存在，則會建立該資料庫
     * @param DBName: 資料庫的名字，請輸入XXX.db
     * @param context: 應用程式的context
     * @param tableName: 表單的名稱
     * @param columns: 欄位資料
     */
    public Database(String DBName, Context context, String tableName, Column[] columns) {
        this.dbName = DBName;
        this.dbHelper = new DbOpenHelper(context, this.dbName, null, this.version, tableName, columns);
        this.db = dbHelper.getWritableDatabase();
    }

    public void ExeSQL(String exe) {
        db.execSQL(exe);
    }

    public void CreateTable(String tableName, Column[] columns) {
        db.execSQL(DbOpenHelper.CreateTableString(tableName, columns));
    }

    public void DropTable(String tableName) {
        db.execSQL(DbOpenHelper.DropTableString(tableName));
    }

    public long InsertRow(String tableName, ContentValues row) {
        long res = this.db.insertWithOnConflict(tableName, null, row, SQLiteDatabase.CONFLICT_REPLACE);
        return res;
    }

    public long UpdateRow(String tableName, ContentValues row, Column column, String where, String[] clause) {
        int res = this.db.update(tableName,row, column.name + where, clause);
        return res;
    }

    public long DeleteRow(String tableName, Column column, String where, String[] clause) {
        long res = this.db.delete(tableName, column.name + where,clause);
        return res;
    }

    public ContentValues[] SelectRow(String tableName, Column column, String where, String[] clause, Column orderby) {
        ArrayList<ContentValues> rows = new ArrayList<ContentValues>();
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, column.name + where, clause, null, null, orderby.name);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {

                ContentValues v = new ContentValues();
                int index = cursor.getPosition();
                int type = cursor.getType(index);
                switch (type) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        String name = cursor.getColumnName(index);
                        int integer = cursor.getInt(index);
                        v.put(name, integer);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        name = cursor.getColumnName(index);
                        float fl = cursor.getFloat(index);
                        v.put(name, (double)fl);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        name = cursor.getColumnName(index);
                        String string = cursor.getString(index);
                        v.put(name, string);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        name = cursor.getColumnName(index);
                        byte[] blob = cursor.getBlob(index);
                        v.put(name, blob);
                        break;
                }
                if (v != null) {
                    rows.add(v);
                }
            }
        }
        ContentValues[] cv = null;
        cv = rows.toArray(cv);

        return cv;
    }


}
