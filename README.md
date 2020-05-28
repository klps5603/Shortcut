# Shortcut
這裡簡述如何建立桌面固定捷徑，我們可以透過『拖移』的方式建立桌面捷徑。建立固定捷徑實際流程如以下兩張圖

![image](https://github.com/klps5603/Shortcut/blob/master/app/src/main/res/drawable/%E5%BB%BA%E7%AB%8B%E5%9B%BA%E5%AE%9A%E6%8D%B7%E5%BE%91%E6%88%AA%E5%9C%96.png)

![image](https://github.com/klps5603/Shortcut/blob/master/app/src/main/res/drawable/%E5%9B%BA%E5%AE%9A%E6%8D%B7%E5%BE%91%E6%88%AA%E5%9C%96.png)

# 授權

```
    <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
    <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
```

首先先到 AndroidManifest 加上建立捷徑與刪除捷徑的權限


# 在 android 8.0 以上


```
   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      ShortcutManager shortcutManager = parent.getSystemService(ShortcutManager.class);
      if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
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
                Log.w("ShortcutManager", "RequestPinShortcut is unsupported");
      }
   }
   
```

setAction com.android.launcher.action.INSTALL_SHORTCUT 表示安裝捷徑，建立 shortcutInfoIntent putExtra id 用於識別是哪個捷徑。接著設定捷徑圖示、名字跟 shortcutInfoIntent，最後送出安裝要求

```
  boolean isRepeat = false;
  for (ShortcutInfo shortcutInfo : shortcutManager.getPinnedShortcuts()) {
          if (shortcutInfo.getId().equals(id)) {
                 isRepeat = true;
                 break;
          }
   }
```
為了避免重複建立捷徑，從 getPinnedShortcuts 尋找已經建立的捷徑，如果有找到就不再建立

# 在 android 8.0 以下

```
  Intent shortcutInfoIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(parent, icon));
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortLabel);
            Intent addIntent = new Intent(parent, MainActivity.class);
            addIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            addIntent.putExtra("shortcutId", id);
            shortcutInfoIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, addIntent);
            parent.sendBroadcast(shortcutInfoIntent);
```
android 8.0 以下不支援『拖移』建立捷徑，因此以 sendBroadcast 來建立
           
            
