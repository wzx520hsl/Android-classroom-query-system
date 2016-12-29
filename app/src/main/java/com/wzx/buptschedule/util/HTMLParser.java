package com.wzx.buptschedule.util;

import com.wzx.buptschedule.bean.FreeRoom;
import com.wzx.buptschedule.bean.FreeRooms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析网页获取空闲教室信息
 */
public class HTMLParser{

	public static List<FreeRooms> getTodayFreeRooms(){
		List<FreeRooms> mList = new ArrayList<>();
		Document doc;
		try {
			doc = Jsoup.connect("http://jwxt.bupt.edu.cn/hfxqDtKxJas.jsp/").get();
//			String title = doc.title();
			FreeRooms freeClassRooms;
			FreeRoom freeClassRoom ;
			Elements tbsElements = doc.getElementsByAttributeValue("class", "sortable");
			for(Element info: tbsElements){
				freeClassRooms = new FreeRooms();
				Elements status =  doc.getElementsByAttributeValue("valign", "middle");
				freeClassRooms.setStatus(status.text());
//				System.out.println("标题, status : " + status.text());
				//设置上课时间
				freeClassRooms.setTime(info.text());
				Element nextInfo = info.nextElementSibling();//获取其后的元素
				Elements classroom = nextInfo.getElementsByAttributeValue("align", "left");
				String classRoomStr = classroom.text();
				String one ="";
				String two="";
				if(classRoomStr.contains("宏福一号楼")&&classRoomStr.contains("宏福二号楼")) {
					one = classRoomStr.substring(classRoomStr.indexOf("宏福一号楼") + 6, classRoomStr.indexOf("宏福二号楼"));
					two = classRoomStr.substring(classRoomStr.indexOf("宏福二号楼")+6);
				}else if(!classRoomStr.contains("宏福一号楼")&&classRoomStr.contains("宏福二号楼")){
					//只有宏福二号楼有空教室
					two = classRoomStr.substring(classRoomStr.indexOf("宏福二号楼")+6);
				}else if(classRoomStr.contains("宏福一号楼")&&!classRoomStr.contains("宏福二号楼")){
					//只有宏福一号楼有空教室
					one = classRoomStr.substring(classRoomStr.indexOf("宏福一号楼") + 6);
				}
				String[] ones = one.split(",");
				for(int i =0;i<ones.length;i++){
					if(!ones[i].trim().equals("")){
						freeClassRoom = new FreeRoom();
						freeClassRoom.setBuilding("宏福一号楼");
						freeClassRoom.setRoom(ones[i].trim());
						freeClassRooms.addRoom(freeClassRoom);
					}
				}

				String[] twos = two.split(",");
				for(int i =0;i<twos.length;i++){
					if(!twos[i].trim().equals("")){
						freeClassRoom = new FreeRoom();
						freeClassRoom.setBuilding("宏福二号楼");
						freeClassRoom.setRoom(twos[i].trim());
						freeClassRooms.addRoom(freeClassRoom);
					}
				}
				mList.add(freeClassRooms);
			}
		/*	for(FreeRooms info: mList){
				System.out.println("教室时间 : " + info.getTime());
				List<FreeRoom> mFrees = info.getRooms();
				for(FreeRoom free: mFrees){
					System.out.println("教学楼: " + free.getBuilding()+" ;教室: "+free.getRoom());
				}
				System.out.println("");
				System.out.println("");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mList;
	}






 
    public static void main(String args[]) {
    	List<FreeRooms> mList = new ArrayList<FreeRooms>();
        Document doc;
        try {
            doc = Jsoup.connect("http://jwxt.bupt.edu.cn/hfxqDtKxJas.jsp/").get();
            String title = doc.title();
            FreeRooms freeClassRooms;
            FreeRoom freeClassRoom ;
            Elements tbsElements = doc.getElementsByAttributeValue("class", "sortable");
            for(Element info: tbsElements){
//            	System.out.println("Afte parsing, info : " + info.toString());
            	freeClassRooms = new FreeRooms();
                Elements status =  doc.getElementsByAttributeValue("valign", "middle");
                freeClassRooms.setStatus(status.text());
                System.out.println("标题, status : " + status.text());
            	//设置上课时间
            	freeClassRooms.setTime(info.text());
            	Element nextInfo = info.nextElementSibling();//获取其后的元素
            	Elements classroom = nextInfo.getElementsByAttributeValue("align", "left");
            	String classRoomStr = classroom.text();
            	String one = classRoomStr.substring(classRoomStr.indexOf("宏福一号楼")+6, classRoomStr.indexOf("宏福二号楼"));
            	String[] ones = one.split(",");
            	for(int i =0;i<ones.length;i++){
            		if(!ones[i].trim().equals("")){
            			freeClassRoom = new FreeRoom();
                		freeClassRoom.setBuilding("宏福一号楼");
                		freeClassRoom.setRoom(ones[i].trim());
                		freeClassRooms.addRoom(freeClassRoom);
            		}
            	}
            	String two = classRoomStr.substring(classRoomStr.indexOf("宏福二号楼")+6);
            	String[] twos = two.split(",");
            	for(int i =0;i<twos.length;i++){
            		if(!twos[i].trim().equals("")){
            		freeClassRoom = new FreeRoom();
            		freeClassRoom.setBuilding("宏福二号楼");
            		freeClassRoom.setRoom(twos[i].trim());
            		freeClassRooms.addRoom(freeClassRoom);
            		}
            	}
            	mList.add(freeClassRooms);
            	/*System.out.println("Afte parsing, one info : " + one);
            	System.out.println("Afte parsing, two info : " + two);
            	System.out.println("Afte parsing, info *****************" );*/
            }
            System.out.println("Jsoup Can read HTML page from URL, title : " + title);
            for(FreeRooms info: mList){
            	System.out.println("教室时间 : " + info.getTime());
            	List<FreeRoom> mFrees = info.getRooms();
            	 for(FreeRoom free: mFrees){
            		 System.out.println("教学楼: " + free.getBuilding()+" ;教室: "+free.getRoom());
            	 }
            	 System.out.println("");
            	 System.out.println("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
}
