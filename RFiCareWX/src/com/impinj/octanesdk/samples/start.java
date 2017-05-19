package com.impinj.octanesdk.samples;

import java.sql.*;
import java.util.HashMap;

public class start {

	static int sleepTime = 3000; // 滑动窗口的大小，ms为单位
	static int varThreshold = 100; // 方差阈值

	final static double thrLeave = -30.0; // 离开的阈值
	final static double thrCome = -50.0; // 进入的阈值
	final static double thrComeforPatient = -40.0; // 病人进入的阈值

	final static String[] bedName = { "321", "323" };

	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {

		sqlOperation.connection();

		// 绘制初始化界面
		UItest ui = new UItest();
		ui.init();
		Thread t = new Thread(new Server());
		t.start();

		// 初始化阅读器
		rd.readerInit();

		int count = 0;
		// 不断地动态扫描，改变状态
		while (count < 28800) {
			System.out.println("    ——————");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // 动态地改变状态显示标志

				// 读取的时延：3秒
				Thread.sleep(sleepTime);
				System.out.println(Reader.patient_curTagNum + "    =====    ");
				String new_patient = "Unknown";
				// System.out.println(Reader.patient_curTagNum + "#####");
				if (Reader.patient_curTagNum >= 2 && Reader.patient_curTagNum % 2 == 0) {
					// 是否属于病人标签
					HashMap<String, Integer> map = Reader.getPatient_label();
					String last1 = "";
					String last2 = "";

					for (String getKey : map.keySet()) {
						if (map.get(getKey) == (Reader.patient_curTagNum - 1)) {
							last1 = getKey;

						} else if (map.get(getKey) == (Reader.patient_curTagNum - 2)) {
							last2 = getKey;
						}
					}

					if (Reader.isBed(last1) && !Reader.isBed(last2)) {
						if (!Reader.getLabel().containsKey(last2)) {
							Reader.getLabel().put(last2, Reader.curTagNum++);
							new_patient = last1;
							map.clear();
							Reader.patient_curTagNum = 0;
						} else {
							map.remove(last2);
							Reader.patient_curTagNum--;
						}

					} else if (Reader.isBed(last2) && !Reader.isBed(last1)) {
						if (!Reader.getLabel().containsKey(last1)) {
							Reader.getLabel().put(last1, Reader.curTagNum++);
							new_patient = last2;
							map.clear();
							Reader.patient_curTagNum = 0;
						} else {
							map.remove(last1);
							Reader.patient_curTagNum--;
						}
					} else if (!Reader.isBed(last2) && !Reader.isBed(last1)) {
						if (Reader.getLabel().containsKey(last1)) {
							map.remove(last1);
							Reader.patient_curTagNum--;
						}
						if (Reader.getLabel().containsKey(last2)) {
							map.remove(last2);
							Reader.patient_curTagNum--;
						}
						if (!Reader.getLabel().containsKey(last2) && !Reader.getLabel().containsKey(last1)) {
							map.clear();
							Reader.patient_curTagNum = 0;
						}
					} else {
						map.clear();
						Reader.patient_curTagNum = 0;
					}
				}

				for (int h = 0; h < Reader.curTagNum; h++) {

					Container cont = Reader.containerList.get(h);
					int beginTmp = cont.getBegin();
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, 1);
					double rssiMean = rssiMeansTmp[0];
					double rssiMax = rssiMeansTmp[1];
					double rssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp, 1);

					if (cont.getState().isEmpty() && !cont.isPresent()) {
						cont.setPresent(true);
						ui.addState(cont);
						ui.setBedNumAuto(cont, Reader.patient_bed.get(new_patient));
						HashMap<String, Short> labelAntenna = Reader.getLabelAntenna();
						labelAntenna.put(new_patient, (short) (Integer.parseInt(cont.getBedNum()) / 100));
						numContainerList++;
						// }
					}

					System.out.print("吊瓶号： " + beginTmp + "，  均值：" + rssiMean + "，  最大值： " + rssiMax + "，  状态： "
							+ cont.isPresent() + "，  方差：" + rssiVarSum);

					if (!cont.isPresent()) {
						System.out.println();
						continue;
					}

					if (rssiVarSum < 200) {
						int waterLevel = (new WaterLevelCalculate()).CalSingle(rssiMean);
						cont.setWaterLevel(waterLevel);
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println("，  水位：" + waterLevel + " (" + cont.getState().size() + ")");
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
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println("，  水位： -1" + " (" + cont.getState().size() + ")");
						}
					}

					boolean present = !new TimeCalculate().canLeave(cont.getState(), cont);
					cont.setPresent(present);
					if (!cont.isPresent()) {
						ui.removeWarn(cont);
						ui.removeState(cont);
					}
					sqlOperation.add(cont);
					// sqlOperation.select();
				}
				rd.clearCache();
			} else {
				ui.initSign();
			}
		}
		sqlOperation.close();
	}

}
