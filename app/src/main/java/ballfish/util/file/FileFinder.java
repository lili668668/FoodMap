package ballfish.util.file;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class FileFinder {
    public static final String EXTRA_STORAGE = "com.android.externalstorage.documents";
    public static final String DOWNLOAD_DIRECTYORY = "com.android.providers.downloads.documents";
    public static final String MEDIA_TYPE = "com.android.providers.media.documents";
    public static final String PUBLIC_DOWNLOAD_URI = "content://downloads/public_downloads";

    public static String[] getAbsolutePathsFromUris(Context context, Uri... uris) {
        File[] files = getFilesFromUris(context, uris);
        String[] paths = new String[files.length];
        int filesLen = files.length;
        for (int cnt = 0;cnt < filesLen;cnt++) {
            paths[cnt] = files[cnt].getAbsolutePath();
        }
        return paths;
    }

    public static File[] getFilesFromUris(Context context, Uri... uris) {
        ArrayList<File> files = new ArrayList<File>();
        for (Uri u : uris) {
            File f = getFileFromUri(context, u);
            if (f != null) {
                files.add(f);
            }
        }

        File[] tmp = new File[files.size()];
        files.toArray(tmp);
        return tmp;
    }

    public static String getAbsolutePathFromUri(Context context, Uri uri) {
        File f = getFileFromUri(context, uri);
        if (f != null) {
            return f.getAbsolutePath();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static File getFileFromUri(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }

        String authority = uri.getAuthority();

        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (authority.equals(EXTRA_STORAGE)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] tmp = docId.split(":");
                String type = tmp[0];

                if (type.equals("primary")) {
                    String path = Environment.getExternalStorageDirectory() + "/" + tmp[1];
                    return getFileFromPath(path);
                }
            } else if (authority.equals(DOWNLOAD_DIRECTYORY)) {
                String docId = DocumentsContract.getDocumentId(uri);
                Uri downlaodUri = ContentUris.withAppendedId(Uri.parse(PUBLIC_DOWNLOAD_URI), Long.parseLong(docId));
                String path = queryAbsolutePathFromUri(context, downlaodUri);
                return getFileFromPath(path);
            } else if (authority.equals(MEDIA_TYPE)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] tmp = docId.split(":");
                String type = tmp[0];
                Uri mediaUri = null;
                if (type.equals("image")) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("video")) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (type.equals("audio")) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }

                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(tmp[1]));
                String path = queryAbsolutePathFromUri(context, mediaUri);
                return getFileFromPath(path);
            }
        } else {
            String scheme = uri.getScheme();
            String path = null;
            if (scheme.equals("content")) {
                path = queryAbsolutePathFromUri(context, uri);
            } else if (scheme.equals("file")) {
                path = uri.getPath();
            }
            return getFileFromPath(path);
        }
        return null;
    }

    public static File getFileFromPath(String path) {
        if (path != null) {
            try {
                File file = new File(path);

                file.setReadable(true);
                if (!file.canRead()) {
                    return null;
                }
                return file.getAbsoluteFile();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static String queryAbsolutePathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cusor = null;
        try {
            cusor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cusor != null && cusor.moveToFirst()) {
                int index = cusor.getColumnIndexOrThrow(projection[0]);
                return cusor.getString(index);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (cusor != null) {
                cusor.close();
            }
        }
        return null;
    }
}
