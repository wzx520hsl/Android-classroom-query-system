package com.wzx.buptschedule;


import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ConversationEventHandler;
import com.avoscloud.leanchatlib.utils.ThirdPartUserUtils;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wzx.buptschedule.bean.FreeRooms;
import com.wzx.buptschedule.bean.GroupInfo;
import com.wzx.buptschedule.bean.MarkInfo;
import com.wzx.buptschedule.bean.TeacherCode;
import com.wzx.buptschedule.bean.UserInfo;
import com.wzx.buptschedule.service.AVService;
import com.wzx.buptschedule.service.PushManager;
import com.wzx.buptschedule.util.HTML;
import com.wzx.buptschedule.util.HTMLParser;
import com.wzx.buptschedule.util.LeanchatUserProvider;
import com.wzx.buptschedule.util.LogTrace;

import java.util.ArrayList;
import java.util.List;


/**
 * Application entrance
 * regester the sever information
 * 
 * @author sjy
 * 
 */
public class TasApp extends Application {

	private static final String TAG = TasApp.class.getSimpleName();

	private AVUser currentUser;
	private String userId;
	private String userName;
	private UserInfo userInfo = null;

	public static Context ctx;

	//Empty classroom information
	public List<FreeRooms> mFreeRoomsList= new ArrayList<>();

	@Override
	public void onCreate() {
		super.onCreate();
		isInitChat = false;
		ctx = TasApp.this;

		AVOSCloud.useAVCloudCN();
		// U need your AVOS key and so on to run the code.
		AVOSCloud.initialize(this,
				"6NeBbriuRLkn3muLFAVHnEjD-gzGzoHsz",
				"pmIdQaV8NFEMTDPgVwsQLjv0");


		AVOSCloud.setLastModifyEnabled(true);
		PushManager.getInstance().init(TasApp.this);

		// register the subclass
		AVObject.registerSubclass(UserInfo.class);
		AVObject.registerSubclass(TeacherCode.class);
	    AVObject.registerSubclass(GroupInfo.class);
		AVObject.registerSubclass(MarkInfo.class);

		refCurrUser();

		getRooms();

		initImageLoader(TasApp.this);
		//初始化百度地图
		initBaiduMap();

		ThirdPartUserUtils.setThirdPartUserProvider(new LeanchatUserProvider());
	
	}

	private void initBaiduMap() {
		SDKInitializer.initialize(this);
	}

	/**
	 * Initiate the imageLaoder to load picture
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
						//.memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
	}

	boolean isInitChat = false;
	/*i
	 * Initiate the chatManger which is aimed ot control the conversation
	 */
	private void initChatManager() {
		if(!isInitChat){
			final ChatManager chatManager = ChatManager.getInstance();
			chatManager.init(this);
			String currentUserId = userInfo.getObjectId();
			if (!TextUtils.isEmpty(currentUserId)) {
				chatManager.setupManagerWithUserId(this, currentUserId);
			}
			chatManager.setConversationEventHandler(ConversationEventHandler.getInstance());
			ChatManager.setDebugEnabled(true);
			isInitChat = true;
		}
	}

	public void getRooms(){
		if(mFreeRoomsList.isEmpty()) {
			new getTodayFreeRooms().execute();
		}
	}

	/**
	 * Get today's empty room
	 */
	private class getTodayFreeRooms extends AsyncTask<Void, Void, List> {

		@Override
		protected List doInBackground(Void... params) {
			return HTML.getTodayFreeRooms();
		}

		@Override
		protected void onPostExecute(List list) {
			if(list!=null){
				mFreeRoomsList = list;
				LogTrace.d(TAG,"getTodayFreeRooms","list:"+mFreeRoomsList.size());
			}
			super.onPostExecute(list);
		}
	}

	/**
	 * Refresh current user information
	 */
	public void refCurrUser() {
		currentUser = AVUser.getCurrentUser();
		if (currentUser != null) {
			userId = currentUser.getObjectId();
			userName = currentUser.getUsername();
			initPushThing();
			new getUserInfoTask().execute(userName);
		}
	}

	public AVUser getAVUser() {
		return currentUser;
	}
	
	public void setAVUser(AVUser avuser) {
		 currentUser = avuser ;
		 userId = currentUser.getObjectId();
		 userName = currentUser.getUsername();
		 initPushThing();
	}

	/**
	 * get the user ObjectId
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * get username
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * get the account name
	 * 
	 * @return
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo info,AVUser avuser) {
		userInfo = info;
		setAVUser(avuser);
		initChatManager();
	}

	// get the user information
	private class getUserInfoTask extends AsyncTask<String, Void, UserInfo> {
		@Override
		protected UserInfo doInBackground(String... params) {
			UserInfo info = AVService.getUserInfo(params[0]);
			return info;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(UserInfo info) {
			// check succeed
			if (info != null) {
				userInfo = info;
				setUserInfo(info, AVUser.getCurrentUser());
			} else {
				// check fail
			}
		}
	}
	
	
	public void initPushThing(){

	}



}
