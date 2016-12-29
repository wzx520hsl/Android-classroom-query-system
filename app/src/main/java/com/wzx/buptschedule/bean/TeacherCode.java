package com.wzx.buptschedule.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(TeacherCode.MANAGER_CLASS)
public class TeacherCode extends AVObject{

	static final String MANAGER_CLASS = "TeacherCode";


	private static final String CODE = "teacher_code";

	public String getCode() {
		return this.getString(CODE);
	}

	public void setCode(String content) {
		this.put(CODE, content);
	}


}
