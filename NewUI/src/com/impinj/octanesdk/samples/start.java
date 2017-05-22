package com.impinj.octanesdk.samples;

import java.sql.*;
import java.util.HashMap;

public class start {

	static int sleepTime = 3000; // 滑动窗口的大小，ms为单位
	static int varThreshold = 100; // 方差阈值

	final static double thrLeave = -20.0; // 离开的阈值
	final static double thrCome = -50.0; // 进入的阈值
	final static double thrComeforPatient = -40.0; // 病人进入的阈值

	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {

		sqlOperation.connection();

		// 绘制初始化界面
		UItest ui = new UItest();
		rd.readerInit();
		ui.init();
		Thread t = new Thread(new Server());
		t.start();

		// 初始化阅读器

		int count = 0;
		// 不断地动态扫描，改变状态
		while (count < 28800) {
			System.out.println(" ——————");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // 动态地改变状态显示标志

				// 读取的时延：3秒
				Thread.sleep(sleepTime);

				if (count == 1) {
					Hand.getInfusion_bed().add("E280 1160 6000 0204 A12A 382A;105");
				}

				if (count == 1) {
					Hand.getInfusion_bed().add("E280 1160 6000 0204 A12A 32;103");
				}



				// 检测手持阅读器变化，并添加！
				Hand.solve();

				for (int h = 0; h < Reader.curTagNum; h++) {

					Container cont = Reader.containerList.get(h);

					if (!cont.isPresent()) {
						System.out.print("");
						continue;
					}

					int beginTmp = cont.getBegin();
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, 1);
					double rssiMean = rssiMeansTmp[0];
					double rssiMax = rssiMeansTmp[1];
					double rssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp, 1);

					System.out.print("吊瓶号： " + beginTmp + "， 均值：" + rssiMean + "， 最大值： " + rssiMax + "， 状态： "
							+ cont.isPresent() + "， 方差：" + rssiVarSum);

					// if (!cont.isPresent()) {
					// System.out.println();
					// continue;
					// }
					
					rssiMean = -45;
					if (count > 15) {
						rssiMean = -80;
					}

					if (rssiVarSum < 200) {
						int waterLevel = (new WaterLevelCalculate()).CalSingle(rssiMean);
						cont.setWaterLevel(waterLevel);
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
					cont.setPresent(present);
					if (!cont.isPresent()) {
						ui.removeWarn(cont);
						ui.removeState(cont);
					}
					sqlOperation.add(cont);
				}
				rd.clearCache();
			} else {
				ui.initSign();
			}
		}
		sqlOperation.close();
	}

}
