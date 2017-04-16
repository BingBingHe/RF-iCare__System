package com.impinj.octanesdk.samples;

import java.util.ArrayList;

public class FlowrateCal {

	public static ArrayList<ArrayList<Double>> phaseAll = new ArrayList<ArrayList<Double>>();
	public static ArrayList<Double> phaseList = new ArrayList<Double>();
	public static int time = 4;
	public static int highPass = 5;

	public static ArrayList<ArrayList<Long>> timestampAll = new ArrayList<ArrayList<Long>>();
	public static ArrayList<Long> timestampList = new ArrayList<Long>();

	// 添加相位，组成6*3s的总时间序列
	public static void addPhase(ArrayList<Double> phaseListTmp, ArrayList<Long> timestamp) {

		addTimestamp(timestamp);

		while (phaseAll.size() >= time) {
			phaseAll.remove(0);
		}
		ArrayList<Double> phaseListClone = new ArrayList<Double>();
		int tmpSize = phaseListTmp.size();
		for (int j = 0; j < tmpSize; j++) {
			phaseListClone.add(phaseListTmp.get(j));
		}
		phaseAll.add(phaseListClone);
		phaseList.clear();
		for (int i = 0; i < phaseAll.size(); i++) {
			ArrayList<Double> t = phaseAll.get(i);
			int size = t.size();
			System.out.println("【" + i + " " + size + " 】");
			for (int j = 0; j < size; j++) {
				phaseList.add(t.get(j));
				// System.out.print(t.get(j) + " ");
			}
			// System.out.println();
		}
		System.out.println(phaseAll.size() + "@@@@@@@");
	}

	public static void addTimestamp(ArrayList<Long> timestamp) {

		while (timestampAll.size() >= time) {
			timestampAll.remove(0);
		}
		ArrayList<Long> timeListClone = new ArrayList<Long>();
		int tmpSize = timestamp.size();
		for (int j = 0; j < tmpSize; j++) {
			timeListClone.add(timestamp.get(j));
		}
		timestampAll.add(timeListClone);
		timestampList.clear();
		for (int i = 0; i < timestampAll.size(); i++) {
			ArrayList<Long> t = timestampAll.get(i);
			int size = t.size();
			for (int j = 0; j < size; j++) {
				timestampList.add(t.get(j));
			}
		}
	}

	public static double getFrequency() {

		// 集成在其中，并拍摄一个视频！！！！！！
		int size = phaseList.size();
		System.out.print((size * 1.0) + "  " + timestampList.size());
		int N = (int) (Math.pow(2, (int) ((Math.log(size * 1.0) / (Math.log(2.0))))));
		// N = 512;
		System.out.println(N);

		FFT fft = new FFT(N);

		double[] window = fft.getWindow();
		double[] re = new double[N];
		double[] im = new double[N];
		double[] amplitude = new double[N];
		Long[] time = new Long[N];

		double[] mostLikely = new double[N];

		int begin = 0;
		int count = 0;
		long meanTime = 0;
		while (size - N - begin >= 0 && timestampList.size() - N - begin >= 0) {
			count++;
			int tsize = timestampList.size();
			for (int i = 0; i < N; i++) {
				re[i] = phaseList.get(size - N + i - begin);
				im[i] = 0;
				amplitude[i] = 0;
				time[i] = timestampList.get(tsize - N + i - begin);
			}
			meanTime += (timestampList.get(tsize - 1 - begin) - timestampList.get(tsize - N - begin));

			fft.fft(re, im);
			for (int i = 0; i < amplitude.length; i++) {
				amplitude[i] = (Math.pow(re[i], 2) + Math.pow(im[i], 2));
				amplitude[i] = (int) (amplitude[i] * 1000) / 1000.0;
			}

			// System.out.print("Re: [");
			// for (int i = 0; i < re.length; i++)
			// System.out.print(((int) (re[i] * 1000) / 1000.0) + " ");
			//
			// System.out.print("]\nIm: [");
			// for (int i = 0; i < im.length; i++)
			// System.out.print(((int) (im[i] * 1000) / 1000.0) + " ");
			// System.out.println("]");
			//
			// System.out.print("Am: [");
			// for (int i = 0; i < amplitude.length; i++) {
			// System.out.print(amplitude[i] + " ");
			// }
			// System.out.println("]");

			double[] max5 = findFrequeacy(amplitude);
			// index

			// for (int i = 0; i < 5; i++) {
			// System.out.print(max5[i] + " ");
			// }
			// System.out.println();
			for (int i = 0; i < 5; i++) {
				// System.out.print(max5[5 + i] + " ");
				mostLikely[(int) max5[i]] += max5[5 + i];
			}
			begin += 20;
		}

		double maxNumDouble = 0.0;
		double maxIndexDouble = 0.0;
		int slideWindow = 3;
		for (int j = highPass; j < 70 - slideWindow; j++) {
			double sum = 0.0;
			for (int h = 0; h < slideWindow; h++) {
				sum += mostLikely[j + h];
			}
			if (sum > maxNumDouble) {
				maxNumDouble = sum;
				double t_up = 0.0;
				double t_down = 0.0;
				for (int k = 0; k < slideWindow; k++) {
					t_up += (mostLikely[j + k] * (j + k));
					t_down += (mostLikely[j + k] * 1.0);
				}

				maxIndexDouble = t_up / t_down;
			}
		}
		
		double nFolder = 60000000.0 / (meanTime / count);
		double result = maxIndexDouble * nFolder;
		
		double maxNum = 0.0;
		int maxIndex = -1;
		for (int j = highPass; j < 70; j++) {
			System.out.println("[" + mostLikely[j] + " " + j + "]");
			if (mostLikely[j] > maxNum) {
				maxNum = mostLikely[j];
				maxIndex = j;
			}
		}
		
		System.out.println("==" + maxIndexDouble + "," + maxNumDouble);
		mostLikely[maxIndex] = 0;
		System.out.println("=" + maxIndex + "," + maxNum);

		// double result = maxIndex;

		maxNum = 0.0;
		maxIndex = -1;
		for (int j = highPass; j < 70; j++) {

			if (mostLikely[j] > maxNum) {
				maxNum = mostLikely[j];
				maxIndex = j;
			}
		}

		System.out.println("=" + maxIndex + "," + maxNum);
		System.out.println("时间" + meanTime / 1000000.0 / count);

		// 根据索引得到时间戳，计算频率。
		return result;
	}

	public static double[] findFrequeacy(double[] amplitude) {
		double[] max5 = new double[5 * 2];
		for (int i = 0; i < 5; i++) {
			double maxNum = 0.0;
			int maxIndex = -1;
			for (int j = highPass; j < 70; j++) {
				if (amplitude[j] > maxNum) {
					maxNum = amplitude[j];
					maxIndex = j;
				}
			}

			max5[i] = maxIndex;
			max5[i + 5] = maxNum;
			amplitude[maxIndex] = 0.0;
		}
		return max5;
	}
}
