package com.avoscloud.leanchatlib.controller;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avoscloud.leanchatlib.model.Room;
import com.avoscloud.leanchatlib.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 该类来负责处理接收消息、聊天服务连接状态管理、查找对话、获取最近对话列表最后一条消息
 * Created by lzw on 15/2/10.
 */
public class ChatManager {
  private static ChatManager chatManager;

  private volatile AVIMClient imClient;
  private volatile String selfId;

  private RoomsTable roomsTable;

  private ChatManager() {}

  public static synchronized ChatManager getInstance() {
    if (chatManager == null) {
      chatManager = new ChatManager();
    }
    return chatManager;
  }

  /**
   * 设置是否打印 leanchatlib 的日志，发布应用的时候要关闭
   * 日志 TAG 为 leanchatlib，可以获得一些异常日志
   *
   * @param debugEnabled
   */
  public static void setDebugEnabled(boolean debugEnabled) {
    LogUtils.debugEnabled = debugEnabled;
  }

  /**
   * 请在应用一启动(Application onCreate)的时候就调用，因为 SDK 一启动，就会去连接聊天服务器
   * 如果没有调用此函数设置 messageHandler ，就可能丢失一些消息
   *
   * @param context
   */
  public void init(Context context) {
    AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));
    AVIMClient.setClientEventHandler(LeanchatClientEventHandler.getInstance());
    //签名
    //AVIMClient.setSignatureFactory(new SignatureFactory());
  }

  /**
   * 设置 AVIMConversationEventHandler，用来处理对话成员变更回调
   *
   * @param eventHandler
   */
  public void setConversationEventHandler(AVIMConversationEventHandler eventHandler) {
    AVIMMessageManager.setConversationEventHandler(eventHandler);
  }

  /**
   * Get the userID for the server
   *
   * @param userId 应用用户系统当前用户的 userId
   */
  public void setupManagerWithUserId(Context context, String userId) {
    this.selfId = userId;
    roomsTable = RoomsTable.getInstanceByUserId(context.getApplicationContext(), userId);
  }

  public String getSelfId() {
    return selfId;
  }

  public void setSelfId(String ID) {
    this.selfId = ID;
  }

  public RoomsTable getRoomsTable() {
    return roomsTable;
  }

  public AVIMClient getImClient() {
    return imClient;
  }

  /**
   * Conneting to the chat server，use the userID to login
   *
   * @param callback AVException 常发生于网络错误、签名错误
   */
  public void openClient(final AVIMClientCallback callback) {
    if (this.selfId == null) {
      throw new IllegalStateException("please call setupManagerWithUserId() first");
    }
    imClient = AVIMClient.getInstance(this.selfId);
    Log.d("ChatManager","this.selfId:"+this.selfId);
    imClient.open(new AVIMClientCallback() {
      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          LeanchatClientEventHandler.getInstance().setConnectAndNotify(false);
          Log.d("ChatManager", "open failed" );
        } else {
          LeanchatClientEventHandler.getInstance().setConnectAndNotify(true);
          Log.d("ChatManager", "open succ");
        }
        if (callback != null) {
          callback.done(avimClient, e);
        }
      }
    });
  }

  /**
   * logout
   *
   * @param callback AVException 常见于网络错误
   */
  public void closeWithCallback(final AVIMClientCallback callback) {
    imClient.close(new AVIMClientCallback() {

      @Override
      public void done(AVIMClient avimClient, AVIMException e) {
        if (e != null) {
          LogUtils.logException(e);
        }
        if (callback != null) {
          callback.done(avimClient, e);
        }
      }
    });
    imClient = null;
    selfId = null;
  }

  /**
   * Get AVIMConversationQuery to query the conversation
   *
   * @return
   */
  public AVIMConversationQuery getConversationQuery() {
    return imClient.getQuery();
  }

  //ChatUser
  public List<Room> findRecentRooms() {
    return ChatManager.getInstance().getRoomsTable().selectRooms();
  }

  List<AVIMTypedMessage> filterTypedMessages(List<AVIMMessage> messages) {
    List<AVIMTypedMessage> resultMessages = new ArrayList<>();
    for (AVIMMessage msg : messages) {
      if (msg instanceof AVIMTypedMessage) {
        resultMessages.add((AVIMTypedMessage) msg);
      } else {
        LogUtils.i("unexpected message " + msg.getContent());
      }
    }
    return resultMessages;
  }


  /**
   * check the last message of the conversation
   *
   * @param conversation
   * @return if seach fail of none history message, return null
   */
  public synchronized AVIMTypedMessage queryLatestMessage(AVIMConversation conversation) throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    final List<AVIMTypedMessage> typeMessages = new ArrayList<>();
    conversation.queryMessages(null, 0, 1, new AVIMMessagesQueryCallback() {
      @Override
      public void done(List<AVIMMessage> list, AVIMException e) {
        if (e == null) {
          typeMessages.addAll(filterTypedMessages(list));
        }
        latch.countDown();
      }
    });
    latch.await();
    if (typeMessages.size() > 0) {
      return typeMessages.get(0);
    } else {
      return null;
    }
  }

}
