package ballfish.util.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 資料庫的物件，用以操作資料庫使用
 */

public class Database {
    private String dbName = "";
    private int version = 1;
    private DbOpenHelper dbHelper = null;
    private SQLiteDatabase db = null;

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

    public long UpdateRow(String tableName, ContentValues row) {
        return 0;
    }


}
