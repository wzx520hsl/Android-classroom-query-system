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
 * Created by cdl on 2016/4/22 0022.
 */
public class HTML{

    public static List<FreeRooms> getTodayFreeRooms(){
        List<FreeRooms> mList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect("http://jwxt.bupt.edu.cn/zxqDtKxJas.jsp").get();
            FreeRooms freeClassRooms;
            FreeRoom freeClassRoom ;
            Elements tbsElements = doc.getElementsByAttributeValue("class", "sortable");
            for(Element info: tbsElements){
                freeClassRooms = new FreeRooms();
                Elements status =  doc.getElementsByAttributeValue("valign", "middle");
                freeClassRooms.setStatus(status.text());
                freeClassRooms.setTime(info.text());
                Element nextInfo = info.nextElementSibling();
                Elements classroom = nextInfo.getElementsByAttributeValue("align", "left");
                String classRoomStr = classroom.text();
                String one ="";
                String two="";
                String three="";
                String four="";
                String five="";
                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one= classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two= classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two= classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf(" 图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    four = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }
                String[] ones = one.split(",");
                for(int i =0;i<ones.length;i++){
                    if(!ones[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教一楼");
                        freeClassRoom.setRoom(ones[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] twos = two.split(",");
                for(int i =0;i<twos.length;i++){
                    if(!twos[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教二楼");
                        freeClassRoom.setRoom(twos[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] threes = three.split(",");
                for(int i =0;i<threes.length;i++){
                    if(!threes[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教三楼");
                        freeClassRoom.setRoom(threes[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] fours = four.split(",");
                for(int i =0;i<fours.length;i++){
                    if(!fours[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教四楼");
                        freeClassRoom.setRoom(fours[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] fives = five.split(",");
                for(int i =0;i<fives.length;i++){
                    if(!fives[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("图书馆");
                        freeClassRoom.setRoom(fives[i].trim());
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
        List<FreeRooms> mList = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect("http://jwxt.bupt.edu.cn/zxqDtKxJas.jsp").get();
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
                String three="";
                String four="";
                String five="";
                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one= classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two= classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two= classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf(" 图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    four = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three= classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&& !classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four= classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(!classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&!classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&!classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&!classRoomStr.contains("教四楼")&&classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("图书馆"));
                    five= classRoomStr.substring(classRoomStr.indexOf("图书馆") + 4);
                }

                if(classRoomStr.contains("教一楼")&&classRoomStr.contains("教二楼")&&classRoomStr.contains("教三楼")&&classRoomStr.contains("教四楼")&&!classRoomStr.contains("图书馆")) {
                    one = classRoomStr.substring(classRoomStr.indexOf("教一楼") + 4, classRoomStr.indexOf("教二楼"));
                    two = classRoomStr.substring(classRoomStr.indexOf("教二楼") + 4, classRoomStr.indexOf("教三楼"));
                    three = classRoomStr.substring(classRoomStr.indexOf("教三楼") + 4, classRoomStr.indexOf("教四楼"));
                    four = classRoomStr.substring(classRoomStr.indexOf("教四楼") + 4);
                }

                String[] ones = one.split(",");
                for(int i =0;i<ones.length;i++){
                    if(!ones[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教一楼");
                        freeClassRoom.setRoom(ones[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] twos = two.split(",");
                for(int i =0;i<twos.length;i++){
                    if(!twos[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教二楼");
                        freeClassRoom.setRoom(twos[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] threes = three.split(",");
                for(int i =0;i<threes.length;i++){
                    if(!threes[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教三楼");
                        freeClassRoom.setRoom(threes[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] fours = four.split(",");
                for(int i =0;i<fours.length;i++){
                    if(!fours[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("教四楼");
                        freeClassRoom.setRoom(fours[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }

                String[] fives = five.split(",");
                for(int i =0;i<fives.length;i++){
                    if(!fives[i].trim().equals("")){
                        freeClassRoom = new FreeRoom();
                        freeClassRoom.setBuilding("图书馆");
                        freeClassRoom.setRoom(fives[i].trim());
                        freeClassRooms.addRoom(freeClassRoom);
                    }
                }
                mList.add(freeClassRooms);
            }
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

