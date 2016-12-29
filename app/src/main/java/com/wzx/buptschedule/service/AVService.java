package com.wzx.buptschedule.service;


import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.wzx.buptschedule.bean.GroupInfo;
import com.wzx.buptschedule.bean.MarkInfo;
import com.wzx.buptschedule.bean.TeacherCode;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.util.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * LeanCloud Service
 */
public class AVService {

	// Reset password
	public static void requestPasswordReset(String email,
			RequestPasswordResetCallback callback) {
		AVUser.requestPasswordResetInBackground(email, callback);
	}

	// Register account
	public static void signUp(String stuid, String name, String password,
			String email, SignUpCallback signUpCallback) {
		AVUser user = new AVUser();
		user.setUsername(stuid);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(signUpCallback);
	}

	// Register account
	public static void signUp(String stuid, String password,
			String email, SignUpCallback signUpCallback) {
		AVUser user = new AVUser();
		user.setUsername(stuid);
		user.setPassword(password);
		user.setEmail(email);
		user.signUpInBackground(signUpCallback);
	}

	// logout
	public static void logout() {
		AVUser.logOut();
	}
	


	public static void updatePushId(String pushId) {
		AVUser user = new AVUser();
		user.setObjectId(AVUser.getCurrentUser().getObjectId());
		user.put(C.USER.USER_PUSHID, pushId);
		user.saveInBackground();
	}


	public static void createTeacherCode(String code, SaveCallback saveCallback) {
		final TeacherCode info = new TeacherCode();
		info.setCode(code);
		info.saveInBackground(saveCallback);
	}


	public static TeacherCode findManagerCode() {
		AVQuery<TeacherCode> query = AVQuery.getQuery(TeacherCode.class);
		query.orderByDescending("updatedAt");
		try {
			List<TeacherCode> list = query.find();
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (AVException exception) {
			return null;
		}
	}

	/**
	 * Get the user information
	 * 
	 * @return
	 */
	public static UserInfo getUserInfo(String username) {
		AVQuery<UserInfo> query = AVQuery.getQuery(UserInfo.class);
		query.whereEqualTo("user_name", username);
		query.orderByDescending("updatedAt");
		try {
			List<UserInfo> list = query.find();
			if (list != null && list.size() > 0) {
				return list.get(0);
			} else {
				return new UserInfo();
			}
		} catch (AVException exception) {
			return new UserInfo();
		}
	}
	
	

	public static List<UserInfo> getUserInfoList(String userType) {
		AVQuery<UserInfo> query = AVQuery.getQuery(UserInfo.class);
		query.whereEqualTo(UserInfo.S_USER_TYPE, userType);
		query.orderByDescending("updatedAt");
		try {
			List<UserInfo> list = query.find();
			return list;
		} catch (AVException exception) {
			return null;
		}
	}
	
	

	public static List<UserInfo> searchUserInfo(String content, int type) {

		// 查询当前子菜单列表
		AVQuery<UserInfo> query = AVQuery.getQuery(UserInfo.class);
		switch (type) {
		case 0:
			query.whereContains(UserInfo.USERNAME, content);
			break;
		case 1:
			query.whereContains(UserInfo.PHONE, content);
			break;
		default:
			break;
		}

		try {
			List<UserInfo> list = query.find();
			return list;
		} catch (AVException exception) {
			return null;
		}
	}
	

	/**
	 * Create or update user information
	 * 
	 * @param objectId
	 * @param userName
	 * @param nickName
	 * @param phone
	 * @param saveCallback
	 */
	public static void createOrUpdateUserInfo(String objectId, String userName,
			String nickName, String phone, String userType,
			SaveCallback saveCallback) {
		final UserInfo userInfo = new UserInfo();
		if (!TextUtils.isEmpty(objectId)) {
			userInfo.setObjectId(objectId);
		}
		userInfo.setUserType(userType);
		userInfo.setPhone(phone);
		userInfo.setNickName(nickName);
		userInfo.setUserName(userName);
		userInfo.saveInBackground(saveCallback);
	}


	/**
	 * Save the group relations
	 * @param groupName
	 * @param creatorName
	 * @param userinfo
	 * @param saveCallback
	 */
	public static void saveGroupInfo(final String groupName,
			final String creatorName, final UserInfo userinfo,final SaveCallback saveCallback) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				AVQuery<GroupInfo> query = AVQuery.getQuery(GroupInfo.class);
				query.whereEqualTo(GroupInfo.group_name, groupName);

				try {
					List<GroupInfo> list = query.find();
					if (list != null && list.size() > 0) {
						saveCallback.done(new AVException(333, "same name group has existed"));
					} else {
						GroupInfo info = new GroupInfo();
						info.setGroupName(groupName);
						info.setCreatorName(creatorName);
						List<UserInfo> userlist = new ArrayList<UserInfo>();
						userlist.add(userinfo);
						info.setGroupPeoples(userlist); 
						info.saveInBackground(saveCallback);
					}
				} catch (AVException exception) {
					if(exception.getCode()==101){
						GroupInfo info = new GroupInfo();
						info.setGroupName(groupName);
						info.setCreatorName(creatorName);
						List<UserInfo> list = new ArrayList<UserInfo>();
						list.add(userinfo);
						info.setGroupPeoples(list); 
						info.saveInBackground(saveCallback);
					}
				}

			}
		}).start();

	}
	
	
	/**
	 * Get the group information
	 * 
	 * @param content
	 *
	 * @return
	 */
	public static List<GroupInfo> searchGroupInfo(String content) {


		AVQuery<GroupInfo> query = AVQuery.getQuery(GroupInfo.class);
		query.whereContains(GroupInfo.group_name, content);
		

		try {
			List<GroupInfo> list = query.find();
			return list;
		} catch (AVException exception) {
			return null;
		}
	}

	// Create comment
	public static void createMarkInfo(String marker, String markmenu,
									  String content, String rate, SaveCallback saveCallback) {
		final MarkInfo markInfo = new MarkInfo();

		markInfo.setMarker(marker);
		markInfo.setMarkerMenu(markmenu);
		markInfo.setMarkContent(content);
		markInfo.setMarkRate(rate);
		// Asynchronized save
		markInfo.saveInBackground(saveCallback);
	}

	public static List<MarkInfo> findMarkInfos(String markmenu) {
		// Query the
		AVQuery<MarkInfo> query = AVQuery.getQuery(MarkInfo.class);
		query.whereEqualTo("mark_menu", markmenu);
		query.orderByDescending("updatedAt");
		try {
			return query.find();
		} catch (AVException exception) {
			Log.e("tag", "Query todos failed.", exception);
			return Collections.emptyList();
		}
	}


}
