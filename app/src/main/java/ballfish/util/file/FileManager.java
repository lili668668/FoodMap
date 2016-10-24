package ballfish.util.file;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    // 手機內部資料夾的根節點
    public static final String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 建立資料夾在手機內部，如果本來就有的資料夾，不會覆蓋就有資料夾
     * 使用方式 Mkdir("directory");
     * 或 Mkdir("directory/subdirectory");
     * @param path :資料夾名稱或相對路徑
     * @return 建立是否成功，若資料夾存在，會回傳false
     * */
    public static boolean Mkdir(String path) {
        File dir = new File(ROOTPATH, path);
        if (dir.exists()) {
            return false;
        }
        dir.mkdir();
        return true;
    }

    /**
     * 讀檔
     * @param path :檔案路徑
     * @return 回傳內容，若檔案不存在或不能讀，回傳null
     * */
    public static String Read(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(ROOTPATH, path)));
            String buffer;
            String text = "";
            do {
                buffer = br.readLine();
                text += buffer;
            } while (buffer != null);
            return text;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 寫檔，如果檔案存在就會覆蓋
     * 使用方式 Write("text.txt", "Hello World!");
     * @param path: 路徑(要加副檔名)
     * @param content: 資料內容(文字或二進位檔)
     * @return 寫檔成功會回傳 true
     * */
    public static boolean Write(String path, String content) {
        File f = new File(ROOTPATH, path);
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 刪除檔案
     * @param path: 路徑
     * @return 是否刪除成功，如果檔案不存在，回傳 false
     * */
    public static boolean Delete(String path) {
        File f = new File(ROOTPATH, path);
        if (!f.exists()) {
            return false;
        }
        return f.delete();

    }

    /**
     * 重新命名檔案或移動檔案
     * @param oldPath: 舊的路徑
     * @param newPath: 新的路徑
     * @return 回傳是否成功，如果檔案不存在，回傳 false
     * */
    public static boolean Rename(String oldPath, String newPath) {
        File f = new File(ROOTPATH, oldPath);
        if (!f.exists()) {
            return false;
        }
        File newFile = new File(ROOTPATH, newPath);
        return f.renameTo(newFile);
    }
}
