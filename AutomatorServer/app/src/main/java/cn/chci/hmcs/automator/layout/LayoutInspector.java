package cn.chci.hmcs.automator.layout;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import cn.chci.hmcs.automator.MyAccessibilityService;
import cn.chci.hmcs.automator.layout.NodeInfo;

/**
 * 该类用于获取界面节点信息，从开源项目autojs中截取的部分核心代码，使用该类的时候请确保已经获得了无障碍权限
 */
public class LayoutInspector {
    private static final String LOG_TAG = "hmcs-automator";
    private final Context mContext;
    private static final AtomicInteger count = new AtomicInteger(0);

    public LayoutInspector(Context mContext) {
        this.mContext = mContext;
    }

    public NodeInfo captureCurrentWindow() {
        AccessibilityService service = MyAccessibilityService.instance;
        if (service == null) {
            Log.d(LOG_TAG, "captureCurrentWindow: AccessibilityService is NULL");
            return null;
        }
        AccessibilityNodeInfo root = service.getRootInActiveWindow();
        if (root == null) {
            Log.d(LOG_TAG, "captureCurrentWindow: RootInActiveWindow is NULL");
            return null;
        }
        NodeInfo nodeInfo = capture(mContext, root);
        return nodeInfo;
    }

    private NodeInfo capture(Context context, AccessibilityNodeInfo root) {
        HashMap<String, Resources> resourcesCache = new HashMap<>();
        return capture(resourcesCache, context, root, null);
    }

    private NodeInfo capture(HashMap<String, Resources> resourcesCache, Context context, AccessibilityNodeInfo node, NodeInfo parent) {
        String packageName = node.getPackageName().toString();
        // 这个resources暂时不知道可以干什么，直接从autojs里扣过来的
        Resources resources = null;
        resources = resourcesCache.get(packageName);
        if (resources == null) {
            try {
                resources = context.getPackageManager().getResourcesForApplication(packageName);
                resourcesCache.put(packageName, resources);
            } catch (PackageManager.NameNotFoundException e) {
                // 这个地方也许会经常报错，但是不影响获取界面的节点
                Log.w(LOG_TAG, "capture: 未找到对应的app " + e.getMessage());
            }
        }
        // 深度优先将界面节点重新包装一层构建成树形结构
        NodeInfo nodeInfo = new NodeInfo(node, resources, parent, String.valueOf(count.getAndAdd(1)));
        int childCount = node.getChildCount();
        for (int i = 0; i < childCount; i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                nodeInfo.getChildren().add(capture(resourcesCache, context, child, nodeInfo));
            }
        }
        return nodeInfo;
    }
}
