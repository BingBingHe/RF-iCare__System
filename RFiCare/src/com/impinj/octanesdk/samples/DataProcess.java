package com.impinj.octanesdk.samples;

import java.util.ArrayList;

public class DataProcess {

	// 计算均值
	public static double[] getMeans(ArrayList<ArrayList<Double>> tagArray, int begin, int length) {
		double[] rssiMeans = new double[length + 1];
		double max = -70.0;
		for (int i = 0; i < length; i++) {
			if (tagArray.get(begin + i).isEmpty()) {
				rssiMeans[i] = -70.0;
				// rssiMeans[length] = -80.0;
			} else {
				double tmpSum = 0;
				ArrayList<Double> tagArrayTmp = tagArray.get(begin + i);
				double tmpSize = tagArrayTmp.size();
				for (int j = 0; j < tmpSize; j++) {
					double tmp = tagArrayTmp.get(j);
					// System.out.print(tmp+" ");
					if (tmp > max) {
						max = tmp;
					}
					tmpSum += tmp;
				}
				rssiMeans[i] = tmpSum / tmpSize;
			}
		}
		rssiMeans[length] = max;
		return rssiMeans;
	}

	// 计算方差
	public static double getVar(ArrayList<ArrayList<Double>> tagArray, int begin, int length, double[] rssiMeans) {
		double rssiVarSum = 0;
		int countVar = 0;
		// double[] rssiMeans = DataProcess.getMeans(tagArray, begin, length);
		for (int i = 0; i < length; i++) {
			ArrayList<Double> tagArrayTmp = tagArray.get(begin + i);
			if (!tagArrayTmp.isEmpty()) {
				double tmpSize = tagArrayTmp.size();
				for (int j = 0; j < tmpSize; j++) {
					rssiVarSum += Math.pow((tagArrayTmp.get(j) - rssiMeans[i]), 2);
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

	public static double getVarForRef(ArrayList<ArrayList<Double>> refTagArray, int begin) {
		double rssiVarSum = 0;
		int countVar = 0;
		double rssiMeans = 0;
		int sizeRef = refTagArray.get(begin).size();
		for (int i = 0; i < sizeRef; i++) {

			rssiMeans += (refTagArray.get(begin).get(i) / sizeRef);
		}
		for (int i = 0; i < sizeRef; i++) {
			rssiVarSum += Math.pow((refTagArray.get(begin).get(i) - rssiMeans), 2);
			countVar++;
		}

		if (countVar == 0) {
			rssiVarSum = 0;
		} else {
			rssiVarSum /= countVar;
			rssiVarSum *= 100;
		}
		return rssiVarSum;

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
			if (rssiMeans[i] > -62.0) {
				tmpFlag1 = false;
				break;
			}
		}
		if (!tmpFlag1) {
			rssiEmpty = rssiMeans[0];
		}
		// System.out.println(" rssiEmpty:" + rssiEmpty);
		return rssiEmpty;
	}

}
