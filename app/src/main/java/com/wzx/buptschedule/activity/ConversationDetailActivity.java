package com.wzx.buptschedule.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMOnlineClientsCallback;
import com.avoscloud.leanchatlib.activity.AVBaseActivity;
import com.avoscloud.leanchatlib.adapter.HeaderListAdapter;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationHelper;
import com.avoscloud.leanchatlib.controller.RoomsTable;
import com.avoscloud.leanchatlib.model.ConversationType;
import com.avoscloud.leanchatlib.utils.Constants;
import com.avoscloud.leanchatlib.utils.ConversationManager;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.event.ConversationMemberClickEvent;
import com.wzx.buptschedule.util.SingleToast;
import com.wzx.buptschedule.util.UserCacheUtils;
import com.wzx.buptschedule.widget.ConversationDetailItemHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ConversationDetailActivity extends AVBaseActivity {
  private static final int ADD_MEMBERS = 0;
  private static final int INTENT_NAME = 1;

  @Bind(R.id.activity_conv_detail_rv_list)
  RecyclerView recyclerView;

  GridLayoutManager layoutManager;
  HeaderListAdapter<UserInfo> listAdapter;

  View quitLayout;

  private AVIMConversation conversation;
  private ConversationType conversationType;
  private ConversationManager conversationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversation_detail_activity);
    String conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
    conversation = AVIMClient.getInstance(ChatManager.getInstance().getSelfId()).getConversation(conversationId);

    View footerView = getLayoutInflater().inflate(R.layout.conversation_detail_footer_layout, null);

    quitLayout = footerView.findViewById(R.id.quit_layout);
    quitLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        quitGroup();
      }
    });

    layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL,false);
    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return (listAdapter.getItemViewType(position) == HeaderListAdapter.FOOTER_ITEM_TYPE ? layoutManager.getSpanCount(): 1);
      }
    });
    listAdapter = new HeaderListAdapter<>(ConversationDetailItemHolder.class);
    listAdapter.setFooterView(footerView);

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(listAdapter);

    initData();
    setTitle(R.string.conversation_online_members);
    setViewByConvType(conversationType);
  }

  private void setViewByConvType(ConversationType conversationType) {
    if (conversationType == ConversationType.Single) {
      quitLayout.setVisibility(View.GONE);
    } else {
      //都设置为不可见，因为不是群组，是聊天室，都退出会引发问题
      quitLayout.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    refresh();
  }




  private void refresh() {
    UserCacheUtils.fetchUsers(conversation.getMembers(), new UserCacheUtils.CacheUserCallback() {
      @Override
      public void done(final List<UserInfo> userList, Exception e) {
        List<String> mlist = new ArrayList<>();
        for(UserInfo userInfo:userList){
          mlist.add(userInfo.getObjectId());
        }
        final AVIMClient client = AVIMClient.getInstance(ChatManager.getInstance().getSelfId());
        client.getOnlineClients(mlist, new AVIMOnlineClientsCallback() {
          @Override
          public void done(List<String> list, AVIMException e) {
            List<UserInfo> onLineUserList = new ArrayList<>();

            for (String info : list) {
              for(UserInfo userInfo:userList){
               if(userInfo.getObjectId().equals(info)){
                 onLineUserList.add(userInfo);
               }
              }
            }

            listAdapter.setDataList(onLineUserList);
            listAdapter.notifyDataSetChanged();
          }
        });
      }
    });
  }

  private void initData() {
    conversationManager = ConversationManager.getInstance();
    conversationType = ConversationHelper.typeOfConversation(conversation);
  }

  public void onEvent(ConversationMemberClickEvent clickEvent) {
      gotoPersonalActivity(clickEvent.memberId);
  }

  private void gotoPersonalActivity(String memberId) {
    Intent intent = new Intent(this, ContactPersonInfoActivity.class);
    intent.putExtra(Constants.LEANCHAT_USER_ID, memberId);
    startActivity(intent);
  }



  /**
   * 退出聊天室
   */
  private void quitGroup() {
    new AlertDialog.Builder(this).setMessage(R.string.conversation_quit_group_tip)
      .setPositiveButton(R.string.common_sure, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          final String convid = conversation.getConversationId();
          conversation.quit(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
              if (filterException(e)) {
                RoomsTable roomsTable = ChatManager.getInstance().getRoomsTable();
                roomsTable.deleteRoom(convid);
                SingleToast.showToast(ConversationDetailActivity.this,R.string.conversation_alreadyQuitConv,2000);
                setResult(RESULT_OK);
                finish();
              }
            }
          });
        }
      }).setNegativeButton(R.string.chat_common_cancel, null).show();
  }

}
