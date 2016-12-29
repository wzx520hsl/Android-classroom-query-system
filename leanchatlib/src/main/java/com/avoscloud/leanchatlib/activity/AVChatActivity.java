package com.avoscloud.leanchatlib.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationMemberCountCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avoscloud.leanchatlib.R;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationHelper;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.LogUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wli on 15/9/18.
 */
public class AVChatActivity extends AVBaseActivity {

    private static final String TAG = AVChatActivity.class.getSimpleName();

    protected ChatFragment chatFragment;
    protected AVIMConversation mConversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        initByIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (null != extras) {
            final String mMemberId = extras.getString(Constants.MEMBER_ID);
            final String mConversationId = extras.getString(Constants.CONVERSATION_ID);
            setTitle(mConversationId);
            Log.i(TAG, "initByIntent 111 mConversation:" + mConversationId);
            Log.i(TAG, "initByIntent 111 mMemberId:" + mMemberId);

            final AVIMClient client = AVIMClient.getInstance(ChatManager.getInstance().getSelfId());

            AVIMConversationQuery query = client.getQuery();
            query.whereEqualTo("name", mConversationId);
            //查询是否有这个会话了
            query.findInBackground(new AVIMConversationQueryCallback() {

                @Override
                public void done(List<AVIMConversation> list, AVIMException e) {

                    if (e == null) {

                        if (list != null && !list.isEmpty()) {

                            Log.i(TAG, "initByIntent size:" + list.size());

                            AVIMConversation conversation = list.get(0);
                            Log.i(TAG, "initByIntent mConversation:" + conversation.getConversationId());
                            Log.i(TAG, "initByIntent mConversation getName:" + conversation.getName());

                            mConversation = client.getConversation(conversation.getConversationId());

                            for (String name : mConversation.getMembers()) {
                                Log.i(TAG, "initByIntent mConversation member:" + name);
                            }

                            mConversation.join(new AVIMConversationCallback() {
                                @Override
                                public void done(AVIMException e) {
                                    Log.i(TAG, "initByIntent join e:" + e);
                                    if (e == null) {
                                        //加入成功
                                     /*   Log.i(TAG,"initByIntent join succ mConversationId:"
                                                +mConversationId);*/
                                        Log.i(TAG, "initByIntent join succ mConversationId:" + mConversationId);
                                        chatFragment.setConversation(mConversation);
                                        chatFragment.showUserName(ConversationHelper.typeOfConversation(mConversation) != ConversationType.Single);
                                        initActionBar(ConversationHelper.titleOfConversation(mConversation));
                                    } else {
                                        Log.i(TAG, "initByIntent join failed mConversationId:" + mConversationId);
                                    }
                                }
                            });

                            //获取第一个对话的
                            mConversation.getMemberCount(new AVIMConversationMemberCountCallback() {
                                @Override
                                public void done(Integer count, AVIMException e) {
                                    if (e == null) {
                                        Log.d(TAG, "conversation got " + count + " members");
                                    }
                                }
                            });
                        } else {
                            updateConversation(
                                    client.getConversation(mConversationId),
                                    mMemberId, mConversationId);
                        }
                    }

                }
            });



        }
    }

    protected void initActionBar(String title) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (title != null) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        } else {
            LogUtils.i("action bar is null, so no title, please set an ActionBar style for activity");
        }
    }

    protected void updateConversation(AVIMConversation conversation, String memberId,
                                      String conversationId) {
        Log.d(TAG, "updateConversation conversation:"+conversation);
        if (null != conversation && conversation.getMembers().size() > 0) {
            Log.d(TAG, "updateConversation mConversation exist");
            this.mConversation = conversation;
            chatFragment.setConversation(conversation);
            chatFragment.showUserName(ConversationHelper.typeOfConversation(conversation) != ConversationType.Single);
            initActionBar(ConversationHelper.titleOfConversation(conversation));
        } else if (!TextUtils.isEmpty(conversationId)) {
            Log.d(TAG, "updateConversation mConversation not exist");
            //没有的话就新建
            getConversation(memberId, conversationId);
        }
    }

    /**
     * 获取 mConversation，为了避免重复的创建，此处先 query 是否已经存在只包含该 member 的 mConversation
     * 如果存在，则直接赋值给 ChatFragment，否者创建后再赋值
     */
    private void getConversation(final String memberId, final String conversationId) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(ConversationType.TYPE_KEY, ConversationType.Group.getValue());

        Log.e(TAG, "getConversation clientId:" + ChatManager.getInstance().getImClient().getClientId());

        ChatManager.getInstance().getImClient().createConversation(Arrays.asList(memberId), conversationId, attrs, false, true, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation avimConversation, AVIMException e) {
                if (filterException(e)) {
                    //创建成功
                    ChatManager.getInstance().getRoomsTable().insertRoom(conversationId);
                    updateConversation(avimConversation, memberId, conversationId);
                    Log.d(TAG, "getConversation succ");
                } else {
                    Log.d(TAG, "getConversation fail e:" + e.toString());
                }
            }
        });
    }
}