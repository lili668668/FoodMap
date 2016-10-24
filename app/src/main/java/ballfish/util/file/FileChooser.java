package ballfish.util.file;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.List;

public class FileChooser {
    public static final int ACTIVITY_FILE_CHOOSER = 143;
    private Activity activity;
    private boolean choosing = false;
    private File[] chooseFiles;
    private Object otherBind;

    public FileChooser(Activity activity) {
        this.activity = activity;
    }

    public boolean showFileChooser(String mimeType, String chooserTitle, boolean allowMultiple, Object otherBind) {
        if (mimeType == null || choosing) {
            return false;
        }

        choosing = true;

        PackageManager packageManager = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (resolveInfos.size() > 0) {
            Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
            picker.setType(mimeType);
            picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
            picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

            this.otherBind = otherBind;

            Intent destIntent = Intent.createChooser(picker, chooserTitle);
            activity.startActivityForResult(destIntent, ACTIVITY_FILE_CHOOSER);
            return true;
        } else {
            return false;
        }
    }

    public boolean showFileChooser(String mimeType, String title, boolean allowMultiple) {
        return showFileChooser(mimeType, title, allowMultiple, null);
    }

    public boolean showFileChooser(String mimeType, String title) {
        return showFileChooser(mimeType, title, false);
    }

    public boolean showFileChooser(String mimeType, String title, Object otherBind) {
        return showFileChooser(mimeType, title, false, otherBind);
    }

    public boolean showFileChooser(String mimeType) {
        return showFileChooser(mimeType, null);
    }

    public boolean showFileChooser() {
        return showFileChooser("*/*");
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_FILE_CHOOSER) {
            choosing = false;
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    chooseFiles = FileFinder.getFilesFromUris(activity, uri);
                    return true;
                } else if (Build.VERSION.SDK_INT >= 16) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        int count = clipData.getItemCount();
                        if (count > 0) {
                            Uri[] uris = new Uri[count];
                            for (int cnt = 0;cnt < count;cnt++) {
                                uris[cnt] = clipData.getItemAt(cnt).getUri();
                            }
                            chooseFiles = FileFinder.getFilesFromUris(activity, uris);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public File[] getChooseFiles() {
        return this.chooseFiles;
    }

    public Object getOtherBind() {
        return this.otherBind;
    }
}
