package cn.chci.hmcs.automate.accessibility.device;

import android.app.Activity;
import android.graphics.Rect;

public class DisplayDevice {
    private Activity mActivity;
    private Rect size;

    public DisplayDevice(Activity mActivity) {
        this.mActivity = mActivity;
        size = new Rect();
        mActivity.getWindowManager().getDefaultDisplay().getRectSize(size);
    }

    public Rect getDeviceSize() {
        return size;
    }
}
