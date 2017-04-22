package com.impinj.octanesdk.samples;

import java.awt.Label;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Container {


	String bedNum = "δ֪";
	int bedSign = 0;
	ArrayList<ArrayList<Double>> bedArrayList = new ArrayList<>();
	// �����Ĺ���
	JPanel displayUnit = new JPanel();
	JPanel taskUnit = new JPanel();
	boolean hasWarn = false;
	
	// ��ǩ��ͷ�������ͳ���
	int begin = -1;
	int length = 1;

	// ������״̬��ʱ�����¼���
	ArrayList<Integer> state = new ArrayList<Integer>(); // �̶�ʱ�䴰���жϵõ���ˮλ
	ArrayList<Integer> timestamp = new ArrayList<Integer>(); // ÿ��״̬�Ļ�ȡʱ���

	// �����Ƿ�����ֳ��ı��
	boolean present = false;
	// ������Һ��״̬
	int waterLevel = 2;
	// ʣ��ʱ��
	int timeRemainCal = 0;
	int timeRemainCur = 0;
	private String name;

	
	
	public Container(int begin) {
		super();
		this.begin = begin;
		for(int i = 0; i < Reader.getRefNumTag() ; i++){
			bedArrayList.add(new ArrayList<Double>());
		}
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
