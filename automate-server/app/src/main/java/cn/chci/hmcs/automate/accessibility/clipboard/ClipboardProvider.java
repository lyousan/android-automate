package cn.chci.hmcs.automate.accessibility.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import cn.chci.hmcs.automate.utils.StringUtils;

public class ClipboardProvider {
    private static final String LOG_TAG = "ClipboardProvider";
    private final Context mContext;

    public ClipboardProvider(Context mContext) {
        this.mContext = mContext;
    }

    public String getClipboardText() {
        String result = "";
        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData.getItemCount() > 0) {
                if (clipData.getItemAt(0) != null && !StringUtils.isEmpty(clipData.getItemAt(0).getText())) {
                    result = clipData.getItemAt(0).getText().toString();
                }
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, "setClipboardText: ", e);
        }
        return result;
    }

    public boolean setClipboardText(String text) {
        try {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("automate", text));
            return true;
        } catch (Exception e) {
            Log.d(LOG_TAG, "setClipboardText: ", e);
        }
        return false;
    }
}
