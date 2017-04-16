package com.impinj.octanesdk.samples;

import java.util.ArrayList;

// test for git
public class start {
	// test for GIT
	static int sleepTime = 3000; // 滑动窗口的大小，ms为单位
	static int varThreshold = 100; // 方差阈值

	static int slideNumforPosition = 4;
	final static double thrLeave = -30.0; // 离开的阈值
	final static double thrCome = -60.0; // 进入的阈值
	final static String[] bedName = { "1", "2" };

	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException {

		// 绘制初始化界面
		UItest ui = new UItest();
		ui.init();

		// 初始化阅读器
		rd.readerInit();

		int count = 0;
		// 不断地动态扫描，改变状态
		while (true) {
			System.out.println("——————");
			// 读取的时延：3秒
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4);
				Thread.sleep(sleepTime);
				// for (Container cont : Reader.containerList) {
				for (int h = 0; h < Reader.containerNum; h++) {

					Container cont = Reader.containerList.get(h);
					int beginTmp = cont.getBegin();
					int lengthTmp = cont.getLength();
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, lengthTmp);
					double rssiMean[] = new double[lengthTmp];
					for (int r = 0; r < lengthTmp; r++) {
						rssiMean[r] = rssiMeansTmp[r];
					}
					double rssiMax = rssiMeansTmp[lengthTmp];
					double rssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp, lengthTmp, rssiMean);

					// if (h == 0 && count == 1) {
					// rssiMean[0] = -53;
					// rssiMean[1] = -53;
					// rssiMean[2] = -53;
					// rssiMean[3] = -70;
					// rssiMax = -59;
					// }
					//
					// if (h == 0 && count > 1) {
					// rssiMean[0] = -53;
					// rssiMean[1] = -53;
					// rssiMean[2] = -53;
					// rssiMean[3] += 1* count ;
					// rssiMax = rssiMean[3];
					// }
					//
					// if (h == 1 && count == 1) {
					// rssiMean[0] = -53;
					// rssiMean[1] = -53;
					// rssiMean[2] = -53;
					// rssiMean[3] = -70;
					// rssiMax = -59;
					// }
					//
					// if (h == 1 && count > 1) {
					// rssiMean[0] = -53;
					// rssiMean[1] = -53;
					// rssiMean[2] = -53;
					// rssiMean[3] += 0.5* count ;
					// rssiMax = rssiMean[3];
					// }

					// if (h == 0 && count > 5) {
					// rssiMean[0] = -50;
					// rssiMax = -53;
					// }

					// 判断是否在场 && 离开
					if (cont.getState().isEmpty() && !cont.isPresent()) {
						// System.out.println(rssiMeans);
						if (rssiMax > thrCome) {
							cont.setPresent(true);
							ui.addState(cont);
							numContainerList++;
						}
					}
					if (!cont.isPresent()) {
						System.out.println();
						continue;
					}

					// 获取相位信号时间序列,从而判断滴速
					ArrayList<Double> phaseListTmp = rd.getTagArray().get(beginTmp + lengthTmp);
					// System.out.println(Reader.getTimestamp().size()+"~~~~~~~~~~");
					ArrayList<Long> timestamp = Reader.getTimestamp().get(beginTmp / 5);
					FlowrateCal.addPhase(phaseListTmp, timestamp);
					
					int flowrate = (int) (FlowrateCal.getFrequency()); 
					// 这个Frequancy
																		// 映射到1分钟，滴多少下。

					System.out.print("吊瓶号： " + beginTmp + "，  均值：" + rssiMean[0] + ", " + rssiMean[1] + ", "
							+ rssiMean[2] + ", " + rssiMean[3] + "\n最大值： " + rssiMax + "，  状态： " + cont.isPresent()
							+ "，  方差：" + rssiVarSum);

					if (rssiVarSum < 200) {
						System.out.println("   流速流速流速： " + flowrate);
						double empty = DataProcess.getEmptyRssi(rssiMean);
						int waterLevel = (new WaterLevelCalculate()).Cal(rssiMean, empty);
						// System.out.println("， 水位： "+ waterLevel);
						cont.setWaterLevel(waterLevel);
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println(", base:" + empty + "，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println(
									", base:" + empty + "，  水位：" + waterLevel + " (" + cont.getState().size() + ")");
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
							System.out.println(", base:    " + "，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println(", base:    " + "，  水位： -1" + " (" + cont.getState().size() + ")");
						}
					}

					boolean present = !new TimeCalculate().canLeave(cont.getState(), cont);
					cont.setPresent(present);
					if (!cont.isPresent()) {
						ui.removeWarn(cont);
						ui.removeState(cont);
					}

					int iTmp = cont.getState().size();
					if (iTmp > 0 && iTmp < slideNumforPosition + 1) {
						for (int g = 0; g < Reader.getRefNumTag(); g++) {
							cont.bedArrayList.get(g).addAll(rd.getRefTagArray().get(g));
						}
						if (iTmp == slideNumforPosition) {
							double[] varS = new double[Reader.getRefNumTag()];
							for (int g = 0; g < Reader.getRefNumTag(); g++) {
								varS[g] = DataProcess.getVarForRef(cont.bedArrayList, g);
							}
							System.out.print("***定位方差比较：  一、" + varS[0] + "  二、" + varS[1]);
							if (varS[0] > varS[1]) {
								System.out.println("    ---> 1号床");
								ui.setBedNumAuto(cont, bedName[0]);
							} else {
								System.out.println("    ---> 2号床");
								ui.setBedNumAuto(cont, bedName[1]);
							}
						}
					}

				}

				rd.clearCache();
				rd.clearRefCache();
			} else {
				ui.initSign();
			}
		}
	}

}
