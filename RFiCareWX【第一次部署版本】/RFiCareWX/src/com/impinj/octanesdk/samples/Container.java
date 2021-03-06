package com.impinj.octanesdk.samples;

import java.util.ArrayList;
import javax.swing.JPanel;

public class Container {

	String bedNum = "未知";
	int bedSign = 0;
	ArrayList<ArrayList<Double>> bedArrayList = new ArrayList<>();
	// 容器的构建
	JPanel displayUnit = new JPanel();
	JPanel taskUnit = new JPanel();
	boolean hasWarn = false;
	String EPC = "";

	// 标签的头号索引和长度
	int begin = -1;
	int length = 1;

	// 容器的状态、时间戳记录情况
	ArrayList<Integer> state = new ArrayList<Integer>(); // 固定时间窗口判断得到的水位
	ArrayList<Integer> timestamp = new ArrayList<Integer>(); // 每种状态的获取时间戳

	// 容器是否存在现场的标记
	boolean present = false;
	boolean canLeave = false;
	// 容器的液面状态
	int waterLevel = 2;
	// 剩余时间
	int timeRemainCal = 0;
	int timeRemainCur = 0;
	private String name;

	public boolean isCanLeave() {
		return canLeave;
	}

	public void setCanLeave(boolean canLeave) {
		this.canLeave = canLeave;
	}

	public Container(int begin) {
		super();
		this.begin = begin;
		// String[] bedNumArr = { "101", "105", "201", "205", "301", "305",
		// "102", "106", "202", "206", "302", "306",
		// "103", "107", "203", "207", "303", "307", "104", "108", "204", "208",
		// "304", "308" };

		
		String[] bedNumArr = { "15", "19", "23", "27", "31", "35", "14", "18", "22", "26", "30", "34",
				"13", "17", "21", "25", "29", "33", "12", "16", "20", "24", "28", "32" };
		if (begin < 24) {
			this.bedNum = bedNumArr[begin];
		}

	}

	public String getEPC() {
		return EPC;
	}

	public void setEPC(String ePC) {
		EPC = ePC;
	}

	public boolean isHasWarn() {
		return hasWarn;
	}

	public void setHasWarn(boolean hasWarn) {
		this.hasWarn = hasWarn;
	}

	public JPanel getTaskUnit() {
		return taskUnit;
	}

	public void setTaskUnit(JPanel taskUnit) {
		this.taskUnit = taskUnit;
	}

	public JPanel getDisplayUnit() {
		return displayUnit;
	}

	public void setDisplayUnit(JPanel displayUnit) {
		this.displayUnit = displayUnit;
	}

	public int getTimeRemainCal() {
		return timeRemainCal;
	}

	public void setTimeRemainCal(int timeRemainCal) {
		this.timeRemainCal = timeRemainCal;
	}

	public int getTimeRemainCur() {
		return timeRemainCur;
	}

	public void setTimeRemainCur(int timeRemainCur) {
		this.timeRemainCur = timeRemainCur;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public String getBedNum() {
		return bedNum;
	}

	public void setBedNum(String bedNum) {
		this.bedNum = bedNum;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ArrayList<Integer> getState() {
		return state;
	}

	public void setState(ArrayList<Integer> state) {
		this.state = state;
	}

	public ArrayList<Integer> getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ArrayList<Integer> timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
		if (!present) {
			getState().clear();
			getTimestamp().clear();
			setCanLeave(false);
			setHasWarn(false);
			if (Reader.getLabel().containsKey(EPC)) {
				Reader.getLabel().remove(EPC);
			}

			if (Reader.getLabelAntenna().containsKey(EPC)) {
				Reader.getLabelAntenna().remove(EPC);
			}
			setEPC("");
		}
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
