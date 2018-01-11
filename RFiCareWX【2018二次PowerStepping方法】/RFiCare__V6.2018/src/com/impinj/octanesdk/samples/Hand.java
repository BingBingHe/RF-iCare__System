package com.impinj.octanesdk.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hand {

//	public static ArrayList<String> infusion_bed = new ArrayList<>();
//
//	// 在无锡医院使用的位置号
//	public static String[] bedNumArr = { "101", "105", "201", "205", "301", "305", "102", "106", "202", "206", "302",
//			"306", "103", "107", "203", "207", "303", "307", "104", "108", "204", "208", "304", "308" };
//	public static String[] bedNumArrReal = { "15", "19", "23", "27", "31", "35", "14", "18", "22", "26", "30", "34",
//			"13", "17", "21", "25", "29", "33", "12", "16", "20", "24", "28", "32" };
//
//	public static List<String> bedNumList = Arrays.asList(bedNumArr);
//	public static List<String> bedNumListReal = Arrays.asList(bedNumArrReal);
//
//	// 在主线程的while循环中重复调用，监测根据infusion_bed中传输的新数据来判断是否有新输液瓶进入
//	public static void solve() {
//		for (int i = 0; i < infusion_bed.size(); i++) {
//			String[] tmp = infusion_bed.get(i).split(";");
//			String EPC = tmp[0];
//			String bedNum = tmp[1];
//			if (!bedNum.equals("#")) {
//				Container cont = Reader.containerList.get(Reader.curTagNum);
//				Reader.getLabelAntenna().put(EPC, (short) (Integer.parseInt(bedNum) / 100));
//				Reader.getLabel().put(EPC, Reader.curTagNum++);
//				cont.setPresent(true);
//				cont.setBedNum(bedNum);
//				cont.setEPC(EPC);
//				cont.setArea(1);
//				System.out.println(EPC + " " + " 进入！！！");
//			} else {
//				if (Reader.getLabel().containsKey(EPC)) {
//					Container cont = Reader.containerList.get(Reader.getLabel().get(EPC));
//					cont.setCanLeave(true);
//					System.out.println(EPC + " " + " 离开！！！");
//				}
//			}
//		}
//		infusion_bed.clear();
//	}
//
//	public static ArrayList<String> getInfusion_bed() {
//		return infusion_bed;
//	}
//
//	public static void setInfusion_bed(ArrayList<String> infusion_bed) {
//		Hand.infusion_bed = infusion_bed;
//	}

}
