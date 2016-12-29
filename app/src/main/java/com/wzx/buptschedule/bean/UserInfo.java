package com.wzx.buptschedule.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;


@AVClassName(UserInfo.MY_CLASS)
public class UserInfo extends AVObject{

	static final String MY_CLASS = "UserInfo";


	public static final String USERNAME = "user_name";

	public static final String AVATAR = "avatar";

	private static final String USERTYPE = "user_type";

	private static final String NICKNAME = "user_nickname";

	public static final String PHONE = "user_phone";
	

	public static final String S_USER_TYPE = "usertype";

	public static final String S_STUDENT = "student";

	public static final String S_TEACHER = "teacher";

	
	public String getUserName() {
		return this.getString(USERNAME);
	}

	public void setUserName(String content) {
		this.put(USERNAME, content);
	}

	public String getAvatarUrl() {
		AVFile avatar = getAVFile(AVATAR);
		if (avatar != null) {
			return avatar.getUrl();
		} else {
			return null;
		}
	}


	public void saveAvatar(String path, final SaveCallback saveCallback) {
		final AVFile file;
		try {
			file = AVFile.withAbsoluteLocalPath(this.getUserName(), path);
			put(AVATAR, file);
			file.saveInBackground(new SaveCallback() {
				@Override
				public void done(AVException e) {
					if (null == e) {
						saveInBackground(saveCallback);
					} else {
						if (null != saveCallback) {
							saveCallback.done(e);
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String getUserType() {
		return this.getString(USERTYPE);
	}

	public void setUserType(String content) {
		this.put(USERTYPE, content);
	}
	
	public String getNickName() {
		return this.getString(NICKNAME);
	}

	public void setNickName(String content) {
		this.put(NICKNAME, content);
	}
	
	public String getPhone() {
		return this.getString(PHONE);
	}

	public void setPhone(String content) {
		this.put(PHONE, content);
	}
	


}
