package com.impinj.octanesdk.samples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Hand {

	public static ArrayList<String> infusion_bed = new ArrayList<>();
	public static String[] bedNumArr = { "101", "105", "201", "205", "301", "305", "102", "106", "202", "206", "302",
			"306", "103", "107", "203", "207", "303", "307", "104", "108", "204", "208", "304", "308" };
	public static List<String> bedNumList = Arrays.asList(bedNumArr);

	public static void solve() {
		for (int i = 0; i < infusion_bed.size(); i++) {
			String[] tmp = infusion_bed.get(i).split(";");
			String EPC = tmp[0];
			String bedNum = tmp[1];

			if (bedNum != "#") {
				Container cont = Reader.containerList.get(bedNumList.indexOf(bedNum));
				Reader.getLabelAntenna().put(EPC, (short) (Integer.parseInt(bedNum) / 100));
				Reader.getLabel().put(EPC, bedNumList.indexOf(bedNum));
				cont.setPresent(true);
				cont.setEPC(EPC);
			} else {
				if (Reader.getLabel().containsKey(EPC)) {
					Container cont = Reader.containerList.get(Reader.getLabel().get(EPC));
					cont.setPresent(false);
				}
			}
		}
		infusion_bed.clear();
	}

	public static ArrayList<String> getInfusion_bed() {
		return infusion_bed;
	}

	public static void setInfusion_bed(ArrayList<String> infusion_bed) {
		Hand.infusion_bed = infusion_bed;
	}

}
