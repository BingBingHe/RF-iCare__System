package com.impinj.octanesdk.samples;

import java.util.ArrayList;

public class DataProcess {
	// 计算均值
	public static double[] getMeans(ArrayList<ArrayList<Double>> tagArray, int begin, int length) {
		double[] rssiMeans = new double[length + 1 + length];

		for (int i = 0; i < length; i++) {
			if (tagArray.get(begin + i).isEmpty()) {
				rssiMeans[i] = -80.0;
				rssiMeans[length] = -80.0;
				rssiMeans[i + 1 + length] = 0;
			} else {
				double tmpSum = 0;
				double tmpSize = tagArray.get(begin + i).size();
				double max = -100.0;
				for (int j = 0; j < tmpSize; j++) {
					double tmp = tagArray.get(begin + i).get(j);
					// System.out.print(tmp+" ");
					if (tmp > max) {
						max = tmp;
					}
					tmpSum += tmp;
				}
				rssiMeans[length] = max;
				rssiMeans[i] = tmpSum / tmpSize;
				rssiMeans[i + 1 + length] = tmpSize;
			}
		}

		return rssiMeans;
	}

	// 计算方差
	public static double getVar(ArrayList<ArrayList<Double>> tagArray, int begin, int length) {
		double rssiVarSum = 0;
		int countVar = 0;
		double[] rssiMeans = DataProcess.getMeans(tagArray, begin, length);
		for (int i = 0; i < length; i++) {
			if (!tagArray.get(i).isEmpty()) {
				double tmpSize = tagArray.get(begin + i).size();
				for (int j = 0; j < tmpSize; j++) {
					rssiVarSum += Math.pow((tagArray.get(begin + i).get(j) - rssiMeans[i]), 2);
					countVar++;
				}
			}
		}
		if (countVar == 0) {
			rssiVarSum = 0;
		} else {
			rssiVarSum /= countVar;
			rssiVarSum *= 100;
		}
		return rssiVarSum;
	}
	
	// 获取phase
	public static double[] getPhase(ArrayList<ArrayList<Double>> tagPhase, int begin, int length) {
		double phase[] = new double[length];
		
		for (int i = 0; i < length; i++) {
			if (tagPhase.get(begin + i).isEmpty()) {
				phase[i] = 0.0;
			} else {				
				phase[i] = tagPhase.get(begin + i).get(0);
			}
		}
		return phase;
	}

	// 根据自然规律修正RSSI值
	public static double[] modifyRssiMeans(double[] rssiMeans) {

		// 屏幕输出而已：初始均值
		String strRssiMeans = "";
		for (int i = 0; i < rssiMeans.length; i++) {
			strRssiMeans += rssiMeans[i] + "  ";
		}
		System.out.println("  初始均值：" + strRssiMeans);

		// 修正过程

		for (int i = rssiMeans.length - 1; i > 0; i--) {
			if (rssiMeans[i] > rssiMeans[i - 1]) {
				if (rssiMeans[i] > -65.0) {
					if (i - 2 >= 0) {
						rssiMeans[i - 1] = rssiMeans[i - 2];
					}
				} else {
					rssiMeans[i - 1] = rssiMeans[i];
				}
			}
		}

		if (rssiMeans[1] < -63.0 & rssiMeans[2] < -63.0) {
			rssiMeans[3] = -70.0;
		}

		// 屏幕输出而已：修正均值
		strRssiMeans = "";
		for (int i = 0; i < rssiMeans.length; i++) {
			strRssiMeans += rssiMeans[i] + "  ";
		}
		System.out.println("  修正均值：" + strRssiMeans);

		return rssiMeans;
	}

	// 获取空水杯状态下的RSSI最高值，用于模板的调整。
	public static double getEmptyRssi(double[] rssiMeans) {

		double rssiEmpty = -60.0;
		boolean tmpFlag1 = true;
		for (int i = 1; i < rssiMeans.length; i++) {
			if (rssiMeans[i] > -63.0) {
				tmpFlag1 = false;
				break;
			}
		}
		if (!tmpFlag1) {
			rssiEmpty = rssiMeans[0];
		}
		System.out.println(" rssiEmpty:" + rssiEmpty);
		return rssiEmpty;
	}

}
