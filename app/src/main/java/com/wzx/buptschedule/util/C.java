package com.wzx.buptschedule.util;

import android.os.Environment;


public final class C {

	public static final class SP {
	// 记住密码
	public static final String remember_login = "shared_remember_login";
	// 记住密码：账号
	public static final String account = "shared_key_accout";
	// 记住密码：密码
	public static final String pwd = "shared_key_pwd";
	}
	
	public static final class INTENT_TYPE {
		//数据修改模式(int) 0:增加 1:删除 2:修改
		public static final String DATA_DATATYPE = "data_datatype";
		//更新的对象信息
		public static final String DATA_OBJ = "data_obj";

		//教室名字(str)
		public static final String DATA_MENUTYPE_NAME = "data_menutype_name";
	}
	
	public static final class NORMAL {
		public static final String pic_path = Environment
				.getExternalStorageDirectory().getPath() + "/TAS/Pic/";
		
		public static final String pic_temp = Environment
				.getExternalStorageDirectory().getPath() + "/TAS/Pic/temp.png";
	}
	
	
	/**
	 * 用户类型
	 * @author sjy
	 *
	 */
	public static final class USER {
		
		//用户名字
		public static final String USER_NAME = "user_name";
		// 用户的推送id
		public static final String USER_PUSHID = "user_pushid";
		
	}
	
	public static final class PushType {
		//通知消息
		public static final String notify_action_notify = "notify_action_notify";
		//求助消息
		public static final String notify_action_help = "notify_action_help";
		//回答消息
		public static final String notify_action_answer = "notify_action_answer";
		
		public static final String notify_action_normal = "notify_action_normal";
	}


}
