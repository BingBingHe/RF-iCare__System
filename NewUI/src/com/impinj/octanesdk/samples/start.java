package com.impinj.octanesdk.samples;

import java.sql.*;
import java.util.HashMap;

public class start {

	static int sleepTime = 3000; // �������ڵĴ�С��msΪ��λ
	static int varThreshold = 100; // ������ֵ

	final static double thrLeave = -20.0; // �뿪����ֵ
	final static double thrCome = -50.0; // �������ֵ
	final static double thrComeforPatient = -40.0; // ���˽������ֵ

	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {

		sqlOperation.connection();

		// ���Ƴ�ʼ������
		UItest ui = new UItest();
		rd.readerInit();
		ui.init();
		Thread t = new Thread(new Server());
		t.start();

		// ��ʼ���Ķ���

		int count = 0;
		// ���ϵض�̬ɨ�裬�ı�״̬
		while (count < 28800) {
			System.out.println(" ������������");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // ��̬�ظı�״̬��ʾ��־

				// ��ȡ��ʱ�ӣ�3��
				Thread.sleep(sleepTime);

				if (count == 1) {
					Hand.getInfusion_bed().add("E280 1160 6000 0204 A12A 382A;105");
				}

				if (count == 1) {
					Hand.getInfusion_bed().add("E280 1160 6000 0204 A12A 32;103");
				}



				// ����ֳ��Ķ����仯������ӣ�
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

					System.out.print("��ƿ�ţ� " + beginTmp + "�� ��ֵ��" + rssiMean + "�� ���ֵ�� " + rssiMax + "�� ״̬�� "
							+ cont.isPresent() + "�� ���" + rssiVarSum);

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
