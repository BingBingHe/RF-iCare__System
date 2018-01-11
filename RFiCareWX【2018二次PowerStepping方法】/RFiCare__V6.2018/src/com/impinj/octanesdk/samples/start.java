package com.impinj.octanesdk.samples;

import java.sql.*;

import com.impinj.octanesdk.OctaneSdkException;

public class start {

	static String hostname = "192.168.100.169";

	static int sleepTime = 1000; // �������ڵĴ�С��msΪ��λ
	static int varThreshold = 500; // ������ֵ

	final static double thrLeave = -30.0; // �뿪����ֵ
	final static double thrCome = -70.0; // �������ֵ
	final static long thrComeLeaveTime = 10000; // �뿪����ֵ
	final static double thrPower = 32.0; // �Ķ�������
	final static double maxRDrate = 30.0; // ����Ķ���
	final static double lowFolder = 0.10; // ��ͽ���
	final static double highFolder = 0.70; // ��߽���
	final static double thrRDrate = maxRDrate * highFolder; // �Ķ��ʲ�ˮλ�ĵ���ֵ
	final static double rssiDiffer = 10.0; // rssi��ֵ��ֵ
	final static String path = "C:/Users/RFID/Desktop/level/";
//	final static String path = "/Users/redup/develop/RFID/temp/";

	final static int delay = 0; // ��ʼ�ӳٵ�ʱ��
	static int delayNow = delay; // ��ǰ���ӳ�ʱ��

	static int numContainerList = 0;
	static Reader rd = new Reader();

	final static double plus = 0; // Ref��ǩ�ļ���� + plus dBm
	final static double minus = 2; // Sensor��ǩ�ļ���� - minus dBm
	
	final static int beginEnterCount = 20;
	final static int canPowerStepping1 = 150; //  last Time 20 m
	final static int canPowerStepping2 = 100;

	public static void main(String[] args)
			throws InterruptedException, ClassNotFoundException, SQLException, OctaneSdkException {

		// sqlOperation.connection();

		// ���Ƴ�ʼ������
		UItest ui = new UItest();
		rd.readerInit();
		ui.init();
		// Thread t = new Thread(new Server());
		// t.start();

		// ��ʼ���Ķ���
		int count = 0;
		// ���ϵض�̬ɨ�裬�ı�״̬
		while (count < Integer.MAX_VALUE) {
			System.out.println(" ==================== ");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // ��̬�ظı�״̬��ʾ��־

				Thread.sleep(1000);

				for (int h = 0; h < Reader.curTagNum;) {
					System.out.println();
					System.out.println("--------");
					System.out.println();
					Container cont = Reader.containerList.get(h);
					int beginTmp = cont.getBegin();
					int lengthTmp = cont.getLength();
					h += lengthTmp;

					if (!cont.isPresent()) {
						System.out.print("");
						continue;
					}

					if (!cont.hasUI && cont.getState().isEmpty() && cont.isPresent()) {
						ui.addState(cont);
						cont.hasUI = true;
					}

//					rd.settingsModify(24.0, cont.area, cont.getTag1());
					rd.settingsModify(cont.bestPower, cont.area, cont.getTag1());
					rd.clearCache();

					Thread.sleep(sleepTime);

					// ������ֲ���ֵ
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, lengthTmp);
					double rssiMean[] = new double[lengthTmp]; // ��ȡ��ֵ
					double rdrate[] = new double[lengthTmp]; // ��ȡ�Ķ�����
					for (int i = 0; i < lengthTmp; i++) {
						rssiMean[i] = rssiMeansTmp[i];
						rdrate[i] = rssiMeansTmp[i + 1 + lengthTmp] / sleepTime * 1000;
					}
					double rssiMax = rssiMeansTmp[lengthTmp]; // ��ȡ���ֵ

					double rssiVarSum = 0.0; // ȡ�����ǩ�﷽������ֵ
					for (int i = 0; i < lengthTmp; i++) {
						double tmpRssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp + i, 1);
						// System.out.println(tmpRssiVarSum);
						if (tmpRssiVarSum > rssiVarSum) {
							rssiVarSum = tmpRssiVarSum;
						}
					}
					double phase[] = DataProcess.getPhase(rd.getTagPhase(), beginTmp, lengthTmp); // ��ȡ��λ

					System.out.println("��ƿ�ţ� " + beginTmp + "�� ״̬�� " + cont.isPresent() + "�� RSSI���ֵ�� " + rssiMax
							+ "�� ���" + rssiVarSum);
					for (int i = 0; i < lengthTmp; i++) {
						System.out.println(
								"��ǩ�ţ�" + i + "��RSSI��ֵ��" + rssiMean[i] + "���Ķ�������" + rdrate[i] + "����λ��" + phase[i]);
					}

					rd.clearCache();

					for (int i = 0; i < lengthTmp; i++) {
						cont.rssiLog.get(i).add(rssiMean[i]);
						cont.rateLog.get(i).add(rdrate[i]);
						cont.phaseLog.get(i).add(phase[i]);
					}

					// ��һ��PowerStepping���ҵ������Ǹ���ǩ����ѹ���

					if (cont.powerFlag == false && cont.canPowerStepping()) {
						System.out.println("##   ��һ��PowerStepping��   ");
						if (rdrate[1] >= maxRDrate * lowFolder) {
							cont.bestPower -= 0.5;
						} else {
							cont.bestPower -= 2;
							System.out.println("##   ��һ��PowerStepping�������ο���ǩ����Ϊ �� " + cont.bestPower);
							cont.powerFlag = true;
							cont.powerBeginCounter = 0;
						}
					}

					// �ڶ����ҵ���ѹ���
					if (cont.powerFlag && cont.SecondPowerFlag == false && cont.canPowerStepping()) {
						System.out.println("##   �ڶ���PowerStepping��   ");
						if (!cont.SecondPowerFlagRef) {
							if (rdrate[1] < maxRDrate * lowFolder) {
								cont.bestPowerRef = cont.bestPower + plus;
								System.out.println("##   �ڶ���PowerStepping���ο���ǩ����Ϊ �� " + cont.bestPowerRef);
								cont.SecondPowerFlagRef = true;
							}
						}

						if (!cont.SecondPowerFlagSensor) {
							if (rdrate[0] < maxRDrate * lowFolder) {
								cont.bestPowerSensor = cont.bestPower - minus;
								System.out.println("##   �ڶ���PowerStepping����֪��ǩ����Ϊ �� " + cont.bestPowerSensor);
								cont.SecondPowerFlagSensor = true;
							}
						}

						cont.bestPower -= 0.5;
						if (cont.SecondPowerFlagRef && cont.SecondPowerFlagSensor) {
							if (cont.bestPowerRef > cont.bestPowerSensor) {
								cont.bestPower = cont.bestPowerSensor - 2.0;
							} else {
								cont.bestPower = cont.bestPowerSensor
										- (int) (cont.bestPowerSensor - cont.bestPowerRef) / 2.0;
							}
							cont.SecondPowerFlag = true;
							System.out.println("##   �ڶ���PowerStepping���������չ���Ϊ �� " + cont.bestPower);
						}
					}

					if (!cont.powerFlag) {
						// ui.changeState(cont);
						cont.addPowerBeginCounter();
						continue;
					}

					if (cont.powerFlag && cont.SecondPowerFlag == false && cont.canPowerStepping()) {
						continue;
					}

					if (rssiVarSum < varThreshold) {
						int waterLevel = (new WaterLevelCalculate()).CalDouble(rdrate, rssiMean, cont);
						cont.setWaterLevel(waterLevel);
						if (!cont.SecondPowerFlag && waterLevel == 1) {
							cont.addPowerBeginCounter();
							if (cont.canPowerStepping()) {
								cont.bestPower = 32.0;
								cont.refTagFlag = true;
							}
						}

						if (rssiMax > thrLeave || cont.isCanLeave()) {
							cont.getState().add(-2);
							System.out.println("�� ˮλ�� -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println("�� ˮλ��" + waterLevel + " (" + cont.getState().size() + ")");
						}
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));

						// ˢ����Ӧ��UI״̬
						ui.changeState(cont);
						if (!cont.isHasWarn()) {
							ui.addWarn(cont);
						}

					} else {
						ui.nurseState(cont);
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));
						if (rssiMax > thrLeave || cont.isCanLeave()) {
							cont.getState().add(-2);
							System.out.println("�� ˮλ�� -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println("�� ˮλ�� -1" + " (" + cont.getState().size() + ")");
						}
					}

					boolean present = !new TimeCalculate().canLeave(cont.getState(), cont);
					if (present != cont.isPresent()) {
						cont.setPresent(present);
					}
					if (!cont.isPresent()) {
						ui.removeWarn(cont);
						ui.removeState(cont);
						cont.reset();
					}
				}

			} else {
				ui.initSign();
			}
		}
	}
}
