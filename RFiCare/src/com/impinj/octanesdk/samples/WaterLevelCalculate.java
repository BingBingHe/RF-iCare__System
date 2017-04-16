package com.impinj.octanesdk.samples;

import java.util.*;

public class WaterLevelCalculate {

	// tags RSSI data in the distance of 100cm

	private static double[][] initData = {
			{ -53.0, -55.0, -54.0, -54.0, 0 }, 
			{ -54.0, -55.0, -55.0, -60.0, 1 },
			{ -54.0, -55.0, -55.0, -70.0, 2 }, 
			{ -54.0, -55.0, -62.0, -70.0, 3 },
			{ -54.0, -55.5, -70.0, -70.0, 4 },
			{ -54.0, -63.0, -70.0, -70.0, 5 }, 
			{ -54.0, -70.0, -70.0, -70.0, 6 },
			{ -60.0, -70.0, -70.0, -70.0, 7 },
			{ -70.0, -70.0, -70.0, -70.0, 8 }, };

	// 单个标签的模板

	private static int numTag = initData[0].length - 1;
	private static int numLevel = initData.length;

	// calculate the water level
	public int Cal(double[] TagRssi, double base) {

		double[][] modifyData;
		double waterLevel;

		modifyData = ModifyInitData(base);
		waterLevel = FindSimilarityData(TagRssi, modifyData);

		return (int)(waterLevel);
	}

	public int CalSingle(double TagRssi) {

		// -75 ,  -67
		
		double dataSingle[] = { -64.0, -72.0, -80.0 };
		
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
		return minIndex;
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
