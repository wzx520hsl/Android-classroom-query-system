package com.wzx.buptschedule.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;


@AVClassName(MarkInfo.MARK_CLASS)
public class MarkInfo extends AVObject implements Comparable<Object> {

	static final String MARK_CLASS = "MarkInfo";


	private static final String MARKER = "marker";

	private static final String MARKERMENU = "mark_menu";

	private static final String MARKRATE = "mark_rate";

	private static final String MARKCONTENT = "mark_content";

	public String getMarker() {
		return this.getString(MARKER);
	}

	public void setMarker(String content) {
		this.put(MARKER, content);
	}

	public String getMarkerMenu() {
		return this.getString(MARKERMENU);
	}

	public void setMarkerMenu(String content) {
		this.put(MARKERMENU, content);
	}

	public String getMarkRate() {
		return this.getString(MARKRATE);
	}

	public void setMarkRate(String content) {
		this.put(MARKRATE, content);
	}

	public String getMarkContent() {
		return this.getString(MARKCONTENT);
	}

	public void setMarkContent(String content) {
		this.put(MARKCONTENT, content);
	}

	@Override
	public int compareTo(Object o) {
		MarkInfo sdto = (MarkInfo) o;
		String rating = sdto.getMarkRate();
		return this.getMarkRate().compareTo(rating);
	}

}
