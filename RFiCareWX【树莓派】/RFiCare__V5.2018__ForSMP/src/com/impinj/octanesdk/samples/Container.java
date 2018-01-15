package com.impinj.octanesdk.samples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

public class Container {

	// 床号
	String bedNum = "未知";
	// 对应的EPC
	// String EPC = "";

	// 1号标签（下）
	String tag1 = "";
	// 2号标签（中）
	String tag2 = "";

	// 区域号，对应的多天线中的哪一个，如果改成多阅读器，可以改成对应的多阅读器中的哪一个
	int area = 1;

	// 容器的UI构建
	JPanel displayUnit = new JPanel(); // 输液瓶显示单元
	JPanel taskUnit = new JPanel(); // 任务提醒的显示单元
	boolean hasWarn = false; // 是否已存在警报
	boolean needWarn = false; // 检验当前的输液瓶是否需要Warn
	boolean hasUI = false;

	// 报警的时延，默认
	int delay = start.delay;

	// 标签的头号索引和长度
	int begin = -1; // 标记是初始化100个输液瓶中的哪一个。
	int length = 1; // 单标签的输液瓶，长度为1。

	// 容器的状态、时间戳记录情况
	ArrayList<Integer> state = new ArrayList<Integer>(); // 固定时间窗口判断得到的水位状态序列
	ArrayList<Integer> timestamp = new ArrayList<Integer>(); // 每种状态的获取时间戳
	// ArrayList<Double> phase = new ArrayList<Double>(); // 相位序列
	// ArrayList<Double> rate = new ArrayList<Double>(); // 阅读率序列

	// 存储数据log
	ArrayList<ArrayList<Double>> rssiLog = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> rateLog = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> phaseLog = new ArrayList<ArrayList<Double>>();

	// 容器是否存在现场的标记
	boolean present = false; // 该输液瓶是否存在
	boolean canLeave = false; // 判断该输液瓶是否可以离开的标记
	// 容器的液面状态,2满，1最上标签读到，0最下标签读到[空报警]，-2高于离开阈值，-1波动（？）
	int waterLevel = 2; // 当前输液瓶的水位状态
	boolean refTagFlag = false;

	// 剩余时间
	int timeRemainCal = 0;
	int timeRemainCur = 0;

	// 是否找到最佳功率
	boolean powerFlag = false;
	boolean SecondPowerFlag = false;
	boolean SecondPowerFlagRef = false;
	boolean SecondPowerFlagSensor = false;

	// 最佳功率
	double bestPower = start.thrPower;
	double bestPowerRef = start.thrPower;
	double bestPowerSensor = start.thrPower;
	int powerBeginCounter = 0;

	// 相位用参数
	int bufferIndex = 0;
	double bufferSum[] = new double[2];
	double phaseLast = 0;

	// 相位计数器
	int phaseCounter = 0;

	// 倒计时开始时间
	long delayStartTime = 0;

	long lastLeaveTime = System.currentTimeMillis();

	// 重置所有参数
	public void reset() {

		hasWarn = false;
		needWarn = false;
		delay = start.delay;
		state.clear(); // 固定时间窗口判断得到的水位状态序列
		timestamp.clear(); // 每种状态的获取时间戳
		hasUI = false;
		for (int i = 0; i < length; i++) {
			rssiLog.get(i).clear();
			rateLog.get(i).clear();
			phaseLog.get(i).clear();
		}

		present = false; // 该输液瓶是否存在
		canLeave = false; // 判断该输液瓶是否可以离开的标记

		waterLevel = 2; // 当前输液瓶的水位状态
		refTagFlag = false;
		timeRemainCal = 0;
		timeRemainCur = 0;

		// 是否找到最佳功率
		powerFlag = false;
		SecondPowerFlag = false;
		SecondPowerFlagRef = false;
		SecondPowerFlagSensor = false;

		bestPower = start.thrPower;
		bestPowerRef = start.thrPower;
		bestPowerSensor = start.thrPower;
		powerBeginCounter = 0;

		phaseCounter = 0;
		delayStartTime = 0;

		lastLeaveTime = System.currentTimeMillis();

	}

	public int getPowerBeginCounter() {
		return powerBeginCounter;
	}

	public void setPowerBeginCounter(int powerBeginCounter) {
		this.powerBeginCounter = powerBeginCounter;
	}

	public void addPowerBeginCounter() {
		this.powerBeginCounter++;
		System.out.println(" ()()() " + this.powerBeginCounter);
	}

	public boolean canPowerStepping() {
		if (!powerFlag) {
			{
				if (rateLog.get(1).get(rateLog.get(1).size() - 1) > start.thrRDrate
						&& this.powerBeginCounter > start.beginEnterCount)
					return true;
			}
			return this.powerBeginCounter > start.canPowerStepping1;
		} else
			return this.powerBeginCounter > start.canPowerStepping2;
	}

	public Container(int begin) {
		super();
		this.begin = begin;
	}

	public boolean isPowerFlag() {
		return powerFlag;
	}

	public void setPowerFlag(boolean powerFlag) {
		this.powerFlag = powerFlag;
	}

	public double getBestPower() {
		return bestPower;
	}

	public void setBestPower(double bestPower) {
		this.bestPower = bestPower;
	}

	// 判断该输液瓶是否需要提醒
	public boolean isNeedWarn() {
		if (delayStartTime != 0) {
			if ((System.currentTimeMillis() - delayStartTime) / 1000 >= getDelay()) {
				setHasWarn(true);
				return true;
			}
		} else {
			if (getWaterLevel() == 0 && getState().size() > 5) {
				ArrayList<Integer> arrayTmp = getState();
				if (arrayTmp.get(arrayTmp.size() - 2) == 0 && arrayTmp.get(arrayTmp.size() - 3) == 0) {
					delayStartTime = System.currentTimeMillis();
				}
			}
		}
		return isHasWarn();
	}

	public boolean getNeedWarn() {
		return needWarn;
	}

	public void setNeedWarn(boolean needWarn) {
		this.needWarn = needWarn;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public boolean isCanLeave() {
		return canLeave;
	}

	public void setCanLeave(boolean canLeave) {
		this.canLeave = canLeave;
	}

	// public String getEPC() {
	// return EPC;
	// }
	//
	// public void setEPC(String ePC) {
	// EPC = ePC;
	// }

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

		// if (Hand.bedNumListReal.contains(bedNum)) {
		this.bedNum = bedNum;
		// } else {
		// this.bedNum = "未知";
		// }
		// this.bedNum = bedNumArr[begin];

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
			setBestPower((int) start.thrPower);
		}
	}

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	// public ArrayList<Double> getPhase() {
	// return phase;
	// }
	//
	// public void setPhase(ArrayList<Double> phase) {
	// this.phase = phase;
	// }

	public int getBufferIndex() {
		return bufferIndex;
	}

	public void setBufferIndex(int bufferIndex) {
		this.bufferIndex = bufferIndex;
	}

	public double[] getBufferSum() {
		return bufferSum;
	}

	public void setBufferSum(double[] bufferSum) {
		this.bufferSum = bufferSum;
	}

	public double getPhaseLast() {
		return phaseLast;
	}

	public void setPhaseLast(double phaseLast) {
		this.phaseLast = phaseLast;
	}

	public int getPhaseCounter() {
		return phaseCounter;
	}

	public void setPhaseCounter(int phaseCounter) {
		this.phaseCounter = phaseCounter;
	}

	// public ArrayList<Double> getRate() {
	// return rate;
	// }
	//
	// public void setRate(ArrayList<Double> rate) {
	// this.rate = rate;
	// }

	public String getTag1() {
		return tag1;
	}

	public void setTag1(String tag1) {
		this.tag1 = tag1;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	// public String getTag3() {
	// return tag3;
	// }
	//
	// public void setTag3(String tag3) {
	// this.tag3 = tag3;
	// }

	public long getDelayStartTime() {
		return delayStartTime;
	}

	public void setDelayStartTime(long delayStartTime) {
		this.delayStartTime = delayStartTime;
	}

	public ArrayList<ArrayList<Double>> getRssiLog() {
		return rssiLog;
	}

	public void setRssiLog(ArrayList<ArrayList<Double>> rssiLog) {
		this.rssiLog = rssiLog;
	}

	public ArrayList<ArrayList<Double>> getRateLog() {
		return rateLog;
	}

	public void setRateLog(ArrayList<ArrayList<Double>> rateLog) {
		this.rateLog = rateLog;
	}

	public ArrayList<ArrayList<Double>> getPhaseLog() {
		return phaseLog;
	}

	public void setPhaseLog(ArrayList<ArrayList<Double>> phaseLog) {
		this.phaseLog = phaseLog;
	}

}
