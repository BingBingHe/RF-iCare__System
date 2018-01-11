package com.impinj.octanesdk.samples;

import java.sql.*;

import com.impinj.octanesdk.OctaneSdkException;

public class start {

	static String hostname = "192.168.100.169";

	static int sleepTime = 1000; // 滑动窗口的大小，ms为单位
	static int varThreshold = 500; // 方差阈值

	final static double thrLeave = -30.0; // 离开的阈值
	final static double thrCome = -70.0; // 进入的阈值
	final static long thrComeLeaveTime = 10000; // 离开的阈值
	final static double thrPower = 32.0; // 阅读器功率
	final static double maxRDrate = 30.0; // 最高阅读率
	final static double lowFolder = 0.10; // 最低界限
	final static double highFolder = 0.70; // 最高界限
	final static double thrRDrate = maxRDrate * highFolder; // 阅读率测水位的的阈值
	final static double rssiDiffer = 10.0; // rssi差值阈值
	final static String path = "C:/Users/RFID/Desktop/level/";
//	final static String path = "/Users/redup/develop/RFID/temp/";

	final static int delay = 0; // 初始延迟的时间
	static int delayNow = delay; // 当前的延迟时间

	static int numContainerList = 0;
	static Reader rd = new Reader();

	final static double plus = 0; // Ref标签的激活功率 + plus dBm
	final static double minus = 2; // Sensor标签的激活功率 - minus dBm
	
	final static int beginEnterCount = 20;
	final static int canPowerStepping1 = 150; //  last Time 20 m
	final static int canPowerStepping2 = 100;

	public static void main(String[] args)
			throws InterruptedException, ClassNotFoundException, SQLException, OctaneSdkException {

		// sqlOperation.connection();

		// 绘制初始化界面
		UItest ui = new UItest();
		rd.readerInit();
		ui.init();
		// Thread t = new Thread(new Server());
		// t.start();

		// 初始化阅读器
		int count = 0;
		// 不断地动态扫描，改变状态
		while (count < Integer.MAX_VALUE) {
			System.out.println(" ==================== ");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // 动态地改变状态显示标志

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

					// 计算各种参数值
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, lengthTmp);
					double rssiMean[] = new double[lengthTmp]; // 获取均值
					double rdrate[] = new double[lengthTmp]; // 获取阅读次数
					for (int i = 0; i < lengthTmp; i++) {
						rssiMean[i] = rssiMeansTmp[i];
						rdrate[i] = rssiMeansTmp[i + 1 + lengthTmp] / sleepTime * 1000;
					}
					double rssiMax = rssiMeansTmp[lengthTmp]; // 获取最大值

					double rssiVarSum = 0.0; // 取多个标签里方差最大的值
					for (int i = 0; i < lengthTmp; i++) {
						double tmpRssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp + i, 1);
						// System.out.println(tmpRssiVarSum);
						if (tmpRssiVarSum > rssiVarSum) {
							rssiVarSum = tmpRssiVarSum;
						}
					}
					double phase[] = DataProcess.getPhase(rd.getTagPhase(), beginTmp, lengthTmp); // 获取相位

					System.out.println("吊瓶号： " + beginTmp + "， 状态： " + cont.isPresent() + "， RSSI最大值： " + rssiMax
							+ "， 方差：" + rssiVarSum);
					for (int i = 0; i < lengthTmp; i++) {
						System.out.println(
								"标签号：" + i + "，RSSI均值：" + rssiMean[i] + "，阅读次数：" + rdrate[i] + "，相位：" + phase[i]);
					}

					rd.clearCache();

					for (int i = 0; i < lengthTmp; i++) {
						cont.rssiLog.get(i).add(rssiMean[i]);
						cont.rateLog.get(i).add(rdrate[i]);
						cont.phaseLog.get(i).add(phase[i]);
					}

					// 第一次PowerStepping，找到上面那个标签的最佳功率

					if (cont.powerFlag == false && cont.canPowerStepping()) {
						System.out.println("##   第一次PowerStepping：   ");
						if (rdrate[1] >= maxRDrate * lowFolder) {
							cont.bestPower -= 0.5;
						} else {
							cont.bestPower -= 2;
							System.out.println("##   第一次PowerStepping结束，参考标签功率为 ： " + cont.bestPower);
							cont.powerFlag = true;
							cont.powerBeginCounter = 0;
						}
					}

					// 第二次找到最佳功率
					if (cont.powerFlag && cont.SecondPowerFlag == false && cont.canPowerStepping()) {
						System.out.println("##   第二次PowerStepping：   ");
						if (!cont.SecondPowerFlagRef) {
							if (rdrate[1] < maxRDrate * lowFolder) {
								cont.bestPowerRef = cont.bestPower + plus;
								System.out.println("##   第二次PowerStepping，参考标签功率为 ： " + cont.bestPowerRef);
								cont.SecondPowerFlagRef = true;
							}
						}

						if (!cont.SecondPowerFlagSensor) {
							if (rdrate[0] < maxRDrate * lowFolder) {
								cont.bestPowerSensor = cont.bestPower - minus;
								System.out.println("##   第二次PowerStepping，感知标签功率为 ： " + cont.bestPowerSensor);
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
							System.out.println("##   第二次PowerStepping结束，最终功率为 ： " + cont.bestPower);
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
							System.out.println("， 水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println("， 水位：" + waterLevel + " (" + cont.getState().size() + ")");
						}
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));

						// 刷新相应的UI状态
						ui.changeState(cont);
						if (!cont.isHasWarn()) {
							ui.addWarn(cont);
						}

					} else {
						ui.nurseState(cont);
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));
						if (rssiMax > thrLeave || cont.isCanLeave()) {
							cont.getState().add(-2);
							System.out.println("， 水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println("， 水位： -1" + " (" + cont.getState().size() + ")");
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
