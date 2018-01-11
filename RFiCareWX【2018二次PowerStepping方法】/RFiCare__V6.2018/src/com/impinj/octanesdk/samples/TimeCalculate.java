package com.impinj.octanesdk.samples;

import java.util.*;
import java.util.Arrays;

/**
 * Created by Murphy on 2016/11/17.
 */

public class TimeCalculate {

	private int stableNumber = 3;

	// 判定离开的条件
	public boolean canLeave(ArrayList<Integer> state, Container cont) {
		if (state.size() < 10) {
			return false;
		}
		if (state.size() == 10) {
			state.clear();
			for (int i = 0; i < 10; i++) {
				state.add(2);
			}
		}
		int recentState1 = state.get(state.size() - 1);
		int recentState2 = state.get(state.size() - 2);
		int recentState3 = state.get(state.size() - 3);
		if (recentState1 == -2) {
			return true;
		}
		// if (recentState1 == 2 && recentState2 == 2 && recentState3 == 2) {
		// for (int i = 6; i > 0; i--) {
		// if (state.get(state.size() - i - 2) == 0 && state.get(state.size() -
		// i - 1) == 0
		// && state.get(state.size() - i) == 0) {
		// return true;
		// }
		// }
		// }
		return false;

	}

	// calculate time，计算剩余的时间。
	public int Cal(ArrayList<Integer> state, ArrayList<Integer> timestamp) {
		double resTime = 0.0;
		double tempTime = 0.0;
		int size = state.size();
		Map<Integer, Integer> map = new HashMap<>();

		for (int i = 0; i < size - stableNumber + 1; i++) {
			boolean flag = true;
			for (int j = i; j < i + stableNumber; j++) {
				if (!state.get(i).equals(state.get(j)) || Integer.parseInt(state.get(i).toString()) == -1) {
					flag = false;
				}
			}
			if (flag) {
				map.putIfAbsent(state.get(i), timestamp.get(i));
			}
		}

		Object[] obj = map.keySet().toArray();
		Arrays.sort(obj);
		if (obj.length < 2) {
			return -1;
		}

		tempTime = map.get(obj[0]) - map.get(obj[1]);
		resTime = (tempTime / (Integer.parseInt(obj[1].toString()) - Integer.parseInt(obj[0].toString())))
				* Integer.parseInt(obj[0].toString());

		int allresTime = (int) (resTime / 1000);
		return allresTime;
	}
}
