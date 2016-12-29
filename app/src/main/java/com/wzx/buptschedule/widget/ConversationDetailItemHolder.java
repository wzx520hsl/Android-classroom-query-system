package com.wzx.buptschedule.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avoscloud.leanchatlib.utils.PhotoUtils;
import com.avoscloud.leanchatlib.viewholder.CommonViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wzx.buptschedule.R;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.event.ConversationMemberClickEvent;

import de.greenrobot.event.EventBus;


public class ConversationDetailItemHolder extends CommonViewHolder<UserInfo> {

  ImageView avatarView;
  TextView nameView;
  UserInfo leanchatUser;

  public ConversationDetailItemHolder(Context context, ViewGroup root) {
    super(context, root, R.layout.conversation_member_item);
    avatarView = (ImageView)itemView.findViewById(R.id.avatar);
    nameView = (TextView)itemView.findViewById(R.id.username);

    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EventBus.getDefault().post(new ConversationMemberClickEvent(leanchatUser.getObjectId(), false));
      }
    });

    itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        EventBus.getDefault().post(new ConversationMemberClickEvent(leanchatUser.getObjectId(), true));
        return true;
      }
    });
  }

  @Override
  public void bindData(UserInfo user) {
    leanchatUser = user;
    ImageLoader.getInstance().displayImage(user.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    nameView.setText(user.getUserName());
  }

  public static ViewHolderCreator HOLDER_CREATOR = new ViewHolderCreator<ConversationDetailItemHolder>() {
    @Override
    public ConversationDetailItemHolder createByViewGroupAndType(ViewGroup parent, int viewType) {
      return new ConversationDetailItemHolder(parent.getContext(), parent);
    }
  };
}
