package com.impinj.octanesdk.samples;

import java.util.HashMap;
import java.lang.Math;

//import java.util.*;

public class WaterLevelCalculate {

	public static HashMap<String, String> modelMatch = new HashMap<>();

	static {
		modelMatch.put("15", "101");
		modelMatch.put("14", "102");
		modelMatch.put("13", "103");
		modelMatch.put("12", "104");
		modelMatch.put("19", "105");
		modelMatch.put("18", "106");
		modelMatch.put("17", "107");
		modelMatch.put("16", "108");
		modelMatch.put("23", "201");
		modelMatch.put("22", "202");
		modelMatch.put("21", "203");
		modelMatch.put("20", "204");
		modelMatch.put("27", "205");
		modelMatch.put("26", "206");
		modelMatch.put("25", "207");
		modelMatch.put("24", "208");
		modelMatch.put("31", "301");
		modelMatch.put("30", "302");
		modelMatch.put("29", "303");
		modelMatch.put("28", "304");
		modelMatch.put("35", "305");
		modelMatch.put("34", "306");
		modelMatch.put("33", "307");
		modelMatch.put("32", "308");
	}

	// tags RSSI data in the distance of 100cm

	private static double[][] initData = { { -53.0, -52.0, -54.0, -57.0, 0 }, { -54.0, -53.0, -55.0, -62.0, 1 },
			{ -55.0, -54.0, -55.0, -70.0, 2 }, { -56.0, -56.0, -63.0, -70.0, 3 }, { -57.0, -57.5, -70.0, -70.0, 4 },
			{ -58.0, -60.0, -70.0, -70.0, 5 }, { -58.0, -70.0, -70.0, -70.0, 6 }, { -62.0, -70.0, -70.0, -70.0, 7 },
			{ -70.0, -70.0, -70.0, -70.0, 8 }, };

	// 单个标签的模板
	private static int numTag = initData[0].length - 1;
	private static int numLevel = initData.length;

	// calculate the water level
	public double Cal(double[] TagRssi, double base) {

		double[][] modifyData;
		double waterLevel;

		modifyData = ModifyInitData(base);
		waterLevel = FindSimilarityData(TagRssi, modifyData);

		return waterLevel;
	}

	// 两标签（一参考标签）
	public int CalDouble(double[] rdRate, double[] rssiMean, Container cont) {
		double rateThreshold = start.thrRDrate;

		if (!cont.refTagFlag) {
			if (rdRate[1] >= rateThreshold) {
				return 1;
			} else {
				return 2;
			}
		} else {
			if (rdRate[0] >= rateThreshold) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	// 三标签（增加参考标签设置水位）
	public int CalTrible(double[] rdRate, double[] rssiMean, Container cont) {

		double rateThreshold = start.thrRDrate;
		double rssiDiffer = start.rssiDiffer;

		if (rdRate[0] >= rateThreshold && rdRate[1] >= rateThreshold && rdRate[2] >= rateThreshold
		// && (Math.abs(rssiMean[0] - rssiMean[1]) <= rssiDiffer ||
		// Math.abs(rssiMean[0] - rssiMean[2]) <= rssiDiffer)
				&& (rssiMean[0] >= rssiMean[1] || rssiMean[0] >= rssiMean[2])) {
			return 0;
		} else if (rdRate[0] >= rateThreshold && rdRate[1] >= rateThreshold && rdRate[2] >= rateThreshold) {
			return 1;
		} else if (rdRate[1] >= rateThreshold && rdRate[2] >= rateThreshold) {
			return 2;
		} else if (rdRate[2] >= rateThreshold) {
			return 3;
		}
		return 4;
	}

	// 单标签测水位
	public int CalSingle(double rdrate, double TagRssi, Container cont) {

		double[][] data = { { -69.8, -69.9, -70.0 }, { -65.0, -66.0, -66.1 }, { -63.0, -63.1, -65.0 },
				{ -64.0, -66.0, -70.0 } };
		//////////////////////
		// 阅读率阈值 //
		//////////////////////
		double rddata = start.thrRDrate;

		// -75 , -67
		int v = 2;
		if (WaterLevelCalculate.modelMatch.containsKey(cont.getBedNum())) {
			int bedNum = Integer.parseInt(modelMatch.get(cont.getBedNum()));
			v = (bedNum - 1) % 4;
		}

		double dataSingle[] = data[v];

		double[] dataDistances = new double[3];
		int minIndex = -1;
		double minDistance = 10000;

		for (int i = 0; i < dataSingle.length; i++) {
			dataDistances[i] = Math.abs(dataSingle[i] - TagRssi);
			if (dataDistances[i] < minDistance) {
				minDistance = dataDistances[i];
				minIndex = i;
			}
		}

		int result = minIndex;

		// 阅读率测水位，一刀切代码！
		if (rdrate > rddata) {
			result = 0;
		} else {
			result = 2;
		}

		// if (cont.getPhaseCounter() < 3 && result == 0) {
		// result = 2;
		// }

		return result;
	}

	// modify the initData on the reference of the base
	public double[][] ModifyInitData(double base) {

		double modifyBase = ((base - (-70)) / (initData[0][0] - (-70)));
		double[][] modifyData = new double[numLevel][numTag + 1];

		for (int i = 0; i < numLevel; i++) {
			for (int j = 0; j < numTag; j++) {
				modifyData[i][j] = (-70) + (initData[i][j] - (-70)) * modifyBase;
			}
			modifyData[i][numTag] = initData[i][numTag];
		}

		return modifyData;
	}

	// find the similarityData by the deviation
	public double FindSimilarityData(double[] TagRssi, double[][] modifyData) {

		double[] deviation = new double[numLevel];
		double minData = -1;
		int minIndex = -1;

		for (int i = 0; i < numLevel; i++) {
			deviation[i] = 0;
			for (int j = 0; j < numTag; j++) {
				deviation[i] += Math.pow((TagRssi[j] - modifyData[i][j]), 2);
			}
		}

		for (int i = 0; i < numLevel; i++) {
			if (minData == -1 || minData > deviation[i]) {
				minData = deviation[i];
				minIndex = i;
			}
		}

		return modifyData[minIndex][numTag];
	}
}
