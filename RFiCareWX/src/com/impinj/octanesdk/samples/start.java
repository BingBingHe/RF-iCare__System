package com.impinj.octanesdk.samples;

import java.sql.*;
import java.util.HashMap;

public class start {

	static int sleepTime = 3000; // �������ڵĴ�С��msΪ��λ
	static int varThreshold = 100; // ������ֵ

	final static double thrLeave = -30.0; // �뿪����ֵ
	final static double thrCome = -50.0; // �������ֵ
	final static double thrComeforPatient = -40.0; // ���˽������ֵ

	final static String[] bedName = { "321", "323" };

	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {

		sqlOperation.connection();

		// ���Ƴ�ʼ������
		UItest ui = new UItest();
		ui.init();
		Thread t = new Thread(new Server());
		t.start();

		// ��ʼ���Ķ���
		rd.readerInit();

		int count = 0;
		// ���ϵض�̬ɨ�裬�ı�״̬
		while (count < 28800) {
			System.out.println("    ������������");
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4); // ��̬�ظı�״̬��ʾ��־

				// ��ȡ��ʱ�ӣ�3��
				Thread.sleep(sleepTime);
				System.out.println(Reader.patient_curTagNum + "    =====    ");
				String new_patient = "Unknown";
				// System.out.println(Reader.patient_curTagNum + "#####");
				if (Reader.patient_curTagNum >= 2 && Reader.patient_curTagNum % 2 == 0) {
					// �Ƿ����ڲ��˱�ǩ
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

					System.out.print("��ƿ�ţ� " + beginTmp + "��  ��ֵ��" + rssiMean + "��  ���ֵ�� " + rssiMax + "��  ״̬�� "
							+ cont.isPresent() + "��  ���" + rssiVarSum);

					if (!cont.isPresent()) {
						System.out.println();
						continue;
					}

					if (rssiVarSum < 200) {
						int waterLevel = (new WaterLevelCalculate()).CalSingle(rssiMean);
						cont.setWaterLevel(waterLevel);
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("��  ˮλ�� -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println("��  ˮλ��" + waterLevel + " (" + cont.getState().size() + ")");
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
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("��  ˮλ�� -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println("��  ˮλ�� -1" + " (" + cont.getState().size() + ")");
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
