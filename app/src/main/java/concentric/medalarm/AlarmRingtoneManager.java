package concentric.medalarm;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 7/17/15.
 */
public class AlarmRingtoneManager {

    private static AlarmRingtoneManager ourInstance;
    private static List<String> toneNameList;
    private static List<Uri> URI_List;

    /**
     * The AlarmRingtoneManager
     *
     * @param context takes the context
     */
    private AlarmRingtoneManager(Context context) {
        toneNameList = new ArrayList<>();
        URI_List = new ArrayList<>();

        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(RingtoneManager.TYPE_ALL);
        Cursor cursor = ringtoneManager.getCursor();

        while (cursor.moveToNext()) {
            String toneTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            if (toneTitle.compareToIgnoreCase(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE))) == 0) {
                Log.i("Tone Found: ", toneTitle);
                //URI_List.add(cursor.getString(RingtoneManager.URI_COLUMN_INDEX));
                Uri partUri = Uri.parse(cursor.getString(RingtoneManager.URI_COLUMN_INDEX));
                Uri finalUri = ringtoneUriBuilder(partUri, context, toneTitle);
                toneVerifyer(toneTitle, finalUri, context);
            }
        }
    }

    public static List<String> getToneNameList() {
        return toneNameList;
    }

    public static List<Uri> getURI_List() {
        return URI_List;
    }

    public static AlarmRingtoneManager getInstance(Context someContext) {

        if (ourInstance == null) {
            synchronized (AlarmRingtoneManager.class) {
                ourInstance = new AlarmRingtoneManager(someContext);
            }
        }
        return ourInstance;
    }

    private Uri ringtoneUriBuilder(Uri partUri, Context context, String toneTitle) {

        RingtoneManager rm = new RingtoneManager(context);
        Cursor c = rm.getCursor();
        Uri file = null;
        while (c.moveToNext()) {
            if (toneTitle.compareToIgnoreCase(c.getString(c.getColumnIndex(MediaStore.MediaColumns.TITLE))) == 0) {
                int ringtoneID = c.getInt(c.getColumnIndex(MediaStore.MediaColumns._ID));
                file = Uri.withAppendedPath(partUri, "" + ringtoneID);
                Log.i("URI generated: ", Uri.withAppendedPath(partUri, "" + ringtoneID).toString());
                c.close();
                break;
            }
            c.moveToNext();
        }
        c.close();
        return file;
    }

    private void toneVerifyer(String toneTitle, Uri toneUri, Context context) {
        Cursor cursor = null;

        if (toneUri == null) {
            return;
        }

        try {
            String[] projections = {MediaStore.MediaColumns.DATA};
            CursorLoader loader = new CursorLoader(context, toneUri, projections, null, null, null);
            cursor = loader.loadInBackground();
            if (cursor != null) {
                cursor.moveToFirst();

                int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

                String filePath = cursor.getString(index);

                if (new File(filePath).exists()) {
                    toneNameList.add(toneTitle);
                    URI_List.add(toneUri);
                } else {
                    Log.e("Tone Missing:", toneTitle);
                    Log.e("Tone URI:", toneUri.toString());
                }
            } else {
                Log.e("toneVerifier", toneTitle + " was null!");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
}
