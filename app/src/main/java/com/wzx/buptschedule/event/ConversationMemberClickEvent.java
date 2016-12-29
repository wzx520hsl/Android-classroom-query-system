package com.wzx.buptschedule.event;


public class ConversationMemberClickEvent {
  public String memberId;
  public boolean isLongClick;

  public ConversationMemberClickEvent(String memberId, boolean isLongClick) {
    this.memberId = memberId;
    this.isLongClick = isLongClick;
  }
}
