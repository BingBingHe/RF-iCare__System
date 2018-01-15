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

	// ����
	String bedNum = "δ֪";
	// ��Ӧ��EPC
	// String EPC = "";

	// 1�ű�ǩ���£�
	String tag1 = "";
	// 2�ű�ǩ���У�
	String tag2 = "";

	// ����ţ���Ӧ�Ķ������е���һ��������ĳɶ��Ķ��������Ըĳɶ�Ӧ�Ķ��Ķ����е���һ��
	int area = 1;

	// ������UI����
	JPanel displayUnit = new JPanel(); // ��Һƿ��ʾ��Ԫ
	JPanel taskUnit = new JPanel(); // �������ѵ���ʾ��Ԫ
	boolean hasWarn = false; // �Ƿ��Ѵ��ھ���
	boolean needWarn = false; // ���鵱ǰ����Һƿ�Ƿ���ҪWarn
	boolean hasUI = false;

	// ������ʱ�ӣ�Ĭ��
	int delay = start.delay;

	// ��ǩ��ͷ�������ͳ���
	int begin = -1; // ����ǳ�ʼ��100����Һƿ�е���һ����
	int length = 1; // ����ǩ����Һƿ������Ϊ1��

	// ������״̬��ʱ�����¼���
	ArrayList<Integer> state = new ArrayList<Integer>(); // �̶�ʱ�䴰���жϵõ���ˮλ״̬����
	ArrayList<Integer> timestamp = new ArrayList<Integer>(); // ÿ��״̬�Ļ�ȡʱ���
	// ArrayList<Double> phase = new ArrayList<Double>(); // ��λ����
	// ArrayList<Double> rate = new ArrayList<Double>(); // �Ķ�������

	// �洢����log
	ArrayList<ArrayList<Double>> rssiLog = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> rateLog = new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> phaseLog = new ArrayList<ArrayList<Double>>();

	// �����Ƿ�����ֳ��ı��
	boolean present = false; // ����Һƿ�Ƿ����
	boolean canLeave = false; // �жϸ���Һƿ�Ƿ�����뿪�ı��
	// ������Һ��״̬,2����1���ϱ�ǩ������0���±�ǩ����[�ձ���]��-2�����뿪��ֵ��-1����������
	int waterLevel = 2; // ��ǰ��Һƿ��ˮλ״̬
	boolean refTagFlag = false;

	// ʣ��ʱ��
	int timeRemainCal = 0;
	int timeRemainCur = 0;

	// �Ƿ��ҵ���ѹ���
	boolean powerFlag = false;
	boolean SecondPowerFlag = false;
	boolean SecondPowerFlagRef = false;
	boolean SecondPowerFlagSensor = false;

	// ��ѹ���
	double bestPower = start.thrPower;
	double bestPowerRef = start.thrPower;
	double bestPowerSensor = start.thrPower;
	int powerBeginCounter = 0;

	// ��λ�ò���
	int bufferIndex = 0;
	double bufferSum[] = new double[2];
	double phaseLast = 0;

	// ��λ������
	int phaseCounter = 0;

	// ����ʱ��ʼʱ��
	long delayStartTime = 0;

	long lastLeaveTime = System.currentTimeMillis();

	// �������в���
	public void reset() {

		hasWarn = false;
		needWarn = false;
		delay = start.delay;
		state.clear(); // �̶�ʱ�䴰���жϵõ���ˮλ״̬����
		timestamp.clear(); // ÿ��״̬�Ļ�ȡʱ���
		hasUI = false;
		for (int i = 0; i < length; i++) {
			rssiLog.get(i).clear();
			rateLog.get(i).clear();
			phaseLog.get(i).clear();
		}

		present = false; // ����Һƿ�Ƿ����
		canLeave = false; // �жϸ���Һƿ�Ƿ�����뿪�ı��

		waterLevel = 2; // ��ǰ��Һƿ��ˮλ״̬
		refTagFlag = false;
		timeRemainCal = 0;
		timeRemainCur = 0;

		// �Ƿ��ҵ���ѹ���
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

	// �жϸ���Һƿ�Ƿ���Ҫ����
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
		// this.bedNum = "δ֪";
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
