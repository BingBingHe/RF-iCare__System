package com.impinj.octanesdk.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class Reader {
	public static HashMap<String, Integer> label = new HashMap<String, Integer>();
	public static ArrayList<ArrayList<Double>> tagArray = new ArrayList<ArrayList<Double>>();

	public static ArrayList<ArrayList<Long>> Timestamp = new ArrayList<ArrayList<Long>>();

	public static HashMap<String, Integer> refLabel = new HashMap<String, Integer>();
	public static ArrayList<ArrayList<Double>> refTagArray = new ArrayList<ArrayList<Double>>();
	private static int refNumTag;

	private boolean ReaderOpen = false;
	private static int numTag;
	private static ImpinjReader impinjReader = new ImpinjReader();
	private String hostname = "192.168.1.100";

	public static int containerNum = 1;

	static ArrayList<Container> containerList = new ArrayList<Container>();

	public void tagInit() {
		// 初始化label

		// 定位的参考标签
		refLabel.put("E280 1160 6000 0204 A12B 32D9", 1); // English
		refLabel.put("E280 1160 6000 0204 A12B 32F9", 2); // Chinese
		refNumTag = refLabel.size();
		for (int i = 0; i < refNumTag; i++) {
			refTagArray.add(new ArrayList<Double>());
		}

		// 判断水位的标签
		label.put("E200 9027 6205 0149 1840 56F8", 1);
		label.put("E200 9027 6205 0149 1870 52B3", 2);
		label.put("E200 9027 6205 0147 0380 E69A", 3);
		label.put("FFFF 0000 0000 0000 0000 0122", 4);
		label.put("E280 1160 6000 0204 A135 88EF", 5);

		numTag = label.size();
		// numTag = 100;
		for (int i = 0; i < numTag; i++) {
			tagArray.add(new ArrayList<Double>());
			if (i % 5 == 0) {
				containerList.add(new Container(i, 4));
				Timestamp.add(new ArrayList<Long>());
			}
		}
		containerNum = containerList.size();
	}

	public void readerInit() {

		tagInit();

		impinjReader.setTagReportListener(new TagReportListener() {
			@Override
			public void onTagReported(ImpinjReader reader0, TagReport report0) {
				// TODO Auto-generated method stub
				List<Tag> tags = report0.getTags();
				for (Tag t : tags) {
					// System.out.println(t.getEpc().toString());
					if (refLabel.containsKey(t.getEpc().toString())) {
						// System.out.println(t.getEpc().toString() + " "+
						// t.getPeakRssiInDbm());
						refTagArray.get(refLabel.get(t.getEpc().toString()) - 1).add(t.getPeakRssiInDbm());
					} else {
						if (label.containsKey(t.getEpc().toString())) {
							if (t.isPeakRssiInDbmPresent()) {
								int index = label.get(t.getEpc().toString()) - 1;
								if (index % 5 != 4) {
									tagArray.get(index).add(t.getPeakRssiInDbm());
								}else{								
									tagArray.get(index).add(t.getPhaseAngleInRadians());
									Timestamp.get(index / 5).add(Long.parseLong(t.getFirstSeenTime().ToString()));
//									System.out.println(label.get(t.getEpc().toString());
								}
								// - 1+ " "+
								// t.getPeakRssiInDbm());
							}
						}
					}
				}
			}
		});

		open();

	}

	public void closeReader() {
		try {
			impinjReader.stop();
		} catch (OctaneSdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean open() {

		try {
			if (!ReaderOpen) {

				impinjReader.connect(hostname);

				final Settings settings = impinjReader.queryDefaultSettings();
				ReportConfig report = settings.getReport();
				report.setIncludeAntennaPortNumber(true);
				report.setIncludePeakRssi(true);
				report.setIncludePhaseAngle(true);
				report.setIncludeFirstSeenTime(true);
				report.setMode(ReportMode.Individual);

				settings.setReaderMode(ReaderMode.MaxThroughput);

				AntennaConfigGroup antennas = settings.getAntennas();
				antennas.disableAll();
				antennas.enableById(new short[] { 1 });
				antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
				antennas.getAntenna((short) 1).setIsMaxTxPower(false);
				antennas.getAntenna((short) 1).setTxPowerinDbm(30.0);
				antennas.getAntenna((short) 1).setRxSensitivityinDbm(-80);

				impinjReader.applySettings(settings);
				impinjReader.start();
				ReaderOpen = true;
			}

		} catch (OctaneSdkException e1) {
			e1.printStackTrace();
		}
		return ReaderOpen;

		// ReaderOpen = true;
		// return true;

	}

	public boolean close() {
		try {
			impinjReader.stop();
			impinjReader.disconnect();
			ReaderOpen = false;
		} catch (OctaneSdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return !ReaderOpen;

		// ReaderOpen = false;
		// return true;

	}

	public boolean isReaderOpen() {
		return ReaderOpen;
	}

	public ArrayList<ArrayList<Double>> getTagArray() {
		return tagArray;
	}

	public static HashMap<String, Integer> getRefLabel() {
		return refLabel;
	}

	public static void setRefLabel(HashMap<String, Integer> refLabel) {
		Reader.refLabel = refLabel;
	}

	public ArrayList<ArrayList<Double>> getRefTagArray() {
		return refTagArray;
	}

	public void setRefTagArray(ArrayList<ArrayList<Double>> refTagArray) {
		this.refTagArray = refTagArray;
	}

	public static int getRefNumTag() {
		return refNumTag;
	}

	public static void setRefNumTag(int refNumTag) {
		Reader.refNumTag = refNumTag;
	}

	public static ArrayList<ArrayList<Long>> getTimestamp() {
		return Timestamp;
	}

	public static void setTimestamp(ArrayList<ArrayList<Long>> timestamp) {
		Timestamp = timestamp;
	}

	public void clearCache() {
		for (int i = 0; i < numTag; i++) {
			tagArray.get(i).clear();
		}
		for (int i = 0; i < Timestamp.size(); i++) {
			Timestamp.get(i).clear();
		}
	}

	public void clearRefCache() {
		for (int i = 0; i < refNumTag; i++) {
			refTagArray.get(i).clear();
		}
	}

}
