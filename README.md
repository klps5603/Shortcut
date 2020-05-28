# Shortcut
這裡簡述如何建立桌面固定捷徑
我們可以透過『拖移』的方式建立桌面捷徑。建立固定捷徑實際流程如以下兩張圖

![image](https://github.com/klps5603/Shortcut/blob/master/app/src/main/res/drawable/%E5%BB%BA%E7%AB%8B%E5%9B%BA%E5%AE%9A%E6%8D%B7%E5%BE%91%E6%88%AA%E5%9C%96.png)

![image](https://github.com/klps5603/Shortcut/blob/master/app/src/main/res/drawable/%E5%9B%BA%E5%AE%9A%E6%8D%B7%E5%BE%91%E6%88%AA%E5%9C%96.png)

# 使用
在 sdk version 8.0 以上，google 提供 ShortcutManager 讓我們可以『拖移』捷徑到桌面任何想要的位置。
```
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
```
