package com.wzx.buptschedule.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.List;


@AVClassName(GroupInfo.GROUPINFO_CLASS)
public class GroupInfo extends AVObject{

	static final String GROUPINFO_CLASS = "GroupInfo";
	

	public static final String group_name = "group_name";

	public static final String group_peoples = "group_peoples";

	public static final String group_creator = "group_creator";
	

	public String getCreatorName() {
		return this.getString(group_creator);
	}

	public void setCreatorName(String content) {
		this.put(group_creator, content);
	}
	

	public String getGroupName() {
		return this.getString(group_name);
	}

	public void setGroupName(String content) {
		this.put(group_name, content);
	}
	

	public List<UserInfo> getGroupPeoples() {
		return (List<UserInfo>)this.getList(group_peoples, UserInfo.class);
	}
	public void setGroupPeoples(List<UserInfo> list) {
		this.put(group_peoples, list);
	}
	
}
