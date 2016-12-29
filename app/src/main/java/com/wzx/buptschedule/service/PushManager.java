package com.wzx.buptschedule.service;

import android.content.Context;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzx on 15/11/11.
 */
public class PushManager {
  public final static String AVOS_ALERT = "alert";

  private final static String AVOS_PUSH_ACTION = "action";
  public static final String INSTALLATION_CHANNELS = "channels";
  private static PushManager pushManager;
  private Context context;

  public synchronized static PushManager getInstance() {
    if (pushManager == null) {
      pushManager = new PushManager();
    }
    return pushManager;
  }

  public void init(Context context) {
    this.context = context;
    subscribeCurrentUserChannel();
  }

  private void subscribeCurrentUserChannel() {
  /*  String currentUserId = LeanchatUser.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.subscribe(context, currentUserId, EntrySplashActivity.class);
    }*/
  }

  public void unsubscribeCurrentUserChannel() {
    /*String currentUserId = LeanchatUser.getCurrentUserId();
    if (!TextUtils.isEmpty(currentUserId)) {
      PushService.unsubscribe(context, currentUserId);
    }*/
  }

  public void pushMessage(String userId, String message, String action) {
    AVQuery query = AVInstallation.getQuery();
    query.whereContains(INSTALLATION_CHANNELS, userId);
    AVPush push = new AVPush();
    push.setQuery(query);

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(AVOS_ALERT, message);
    dataMap.put(AVOS_PUSH_ACTION, action);
    push.setData(dataMap);
    push.sendInBackground();
  }
}