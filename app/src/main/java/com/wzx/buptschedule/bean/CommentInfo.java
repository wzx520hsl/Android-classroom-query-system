package com.wzx.buptschedule.bean;

import com.avos.avoscloud.AVObject;


public class CommentInfo extends AVObject {

    static final String MY_CLASS = "CommentInfo";

    private static final String COMMENT_CONTENT = "comment_content";

    private static final String COMMENT_TIME = "comment_time";

    private static final String COMMENT_USER = "comment_user";


    public String getCommentContent() {
            return this.getString(COMMENT_CONTENT);
    }

    public void setCommentContent(String commentContent) {
        this.put(COMMENT_CONTENT, commentContent);
    }


    public String getCommentTime() {
        return this.getString(COMMENT_TIME);
    }

    public void setCommentTime(String commentTime) {
        this.put(COMMENT_TIME, commentTime);
    }


    public UserInfo getCommentUserInfo() {
        try {
            return this.getAVObject(COMMENT_USER, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCommentUserInfo(UserInfo userInfo) {
        this.put(COMMENT_USER, userInfo);
    }
}
