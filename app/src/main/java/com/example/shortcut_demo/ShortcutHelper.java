package com.example.shortcut_demo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

public class ShortcutHelper {
    private FragmentActivity parent;
    private String id;
    private int icon;
    private String shortLabel;

    private ShortcutHelper(FragmentActivity fragmentActivity) {
        this.parent = fragmentActivity;
    }

    public static ShortcutHelper getInstance(FragmentActivity fragmentActivity) {
        return new ShortcutHelper(fragmentActivity);
    }

    private boolean isVersionCodeSupportShortcutService() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public ShortcutHelper setShortcutInfoId(String id) {
        this.id = id;
        return this;
    }

    public ShortcutHelper setIcon(int drawableRes) {
        this.icon = drawableRes;
        return this;
    }

    public ShortcutHelper setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
        return this;
    }

    public void build() {
        if (isVersionCodeSupportShortcutService()) {
            ShortcutManager shortcutManager = parent.getSystemService(ShortcutManager.class);
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                boolean isRepeat = false;
                for (ShortcutInfo shortcutInfo : shortcutManager.getPinnedShortcuts()) {
                    if (shortcutInfo.getId().equals(id)) {
                        isRepeat = true;
                        break;
                    }
                }
                if (!isRepeat) {
                    Intent shortcutInfoIntent = new Intent(parent, MainActivity.class);
                    shortcutInfoIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    shortcutInfoIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shortcutInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    shortcutInfoIntent.putExtra("shortcutId", id);
                    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(parent, id)
                            .setIcon(Icon.createWithResource(parent, icon))
                            .setShortLabel(shortLabel)
                            .setIntent(shortcutInfoIntent)
                            .build();
                    PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(parent, 0,
                            shortcutInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    shortcutManager.requestPinShortcut(shortcutInfo,
                            shortcutCallbackIntent.getIntentSender());
                } else {
                    Toast.makeText(parent, parent.getString(R.string.shortcutCreated),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.w("ShortcutManager", "RequestPinShortcut is unsupported");
            }
        } else {
            Intent shortcutInfoIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(parent, icon));
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortLabel);
            Intent addIntent = new Intent(parent, MainActivity.class);
            addIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            addIntent.putExtra("shortcutId", id);
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, addIntent);
            parent.sendBroadcast(shortcutInfoIntent);
        }
    }

}