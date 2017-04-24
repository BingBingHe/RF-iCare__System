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
	public static HashMap<String, Short> labelAntenna = new HashMap<String, Short>();

	private static int numTag;

	public static HashMap<String, Integer> refLabel = new HashMap<String, Integer>();
	public static ArrayList<ArrayList<Double>> refTagArray = new ArrayList<ArrayList<Double>>();
	private static int refNumTag;

	private boolean ReaderOpen = false;

	private static ImpinjReader impinjReader = new ImpinjReader();
	private String hostname = "192.168.1.100";
	public static int curTagNum = 0;

	static ArrayList<Container> containerList = new ArrayList<Container>();

	public void tagInit() {
		// 初始化label
		refLabel.put("E280 1160 6000 0204 A12B 1", 101); // English
		refLabel.put("E280 1160 6000 0204 A12C 2", 102); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 3", 103); // English
		refLabel.put("E280 1160 6000 0204 A12C 4", 104); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 5", 105); // English
		refLabel.put("E280 1160 6000 0204 A12C 6", 106); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 7", 107); // English
		refLabel.put("E280 1160 6000 0204 A12C 8", 108); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 9", 201); // English
		refLabel.put("E280 1160 6000 0204 A12C 10", 202); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 11", 203); // English
		refLabel.put("E280 1160 6000 0204 A12C 12", 204); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 13", 205); // English
		refLabel.put("E280 1160 6000 0204 A12C 14", 206); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 15", 207); // English
		refLabel.put("E280 1160 6000 0204 A12C 16", 208); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 17", 301); // English
		refLabel.put("E280 1160 6000 0204 A12C 18", 302); // Chinese
		refLabel.put("E280 1160 6000 0204 A12B 19", 303); // English
		refLabel.put("E280 1160 6000 0204 A12C 20", 304); // Chinese
		refLabel.put("E280 1160 6000 0204 A12C 21", 305);
		refLabel.put("E280 1160 6000 0204 A12C 22", 306);
		refLabel.put("E280 1160 6000 0204 A12C 23", 307);
		refLabel.put("E280 1160 6000 0204 A12C 24", 308);

		refNumTag = refLabel.size();
		for (int i = 0; i < refNumTag; i++) {
			refTagArray.add(new ArrayList<Double>());
		}

		// label.put("1B1B 1B1B 0000 0000 0000 0001", 1);
		// label.put("1B1B 1B1B 0000 0000 0000 0002", 2);
		// label.put("3008 33B2 DDD9 0140 0000 0000", 3);
		// label.put("3008 33B2 DDD9 0140 0000 0001", 4);
		// numTag = label.size();

		numTag = 100;

		for (int i = 0; i < numTag; i++) {
			tagArray.add(new ArrayList<Double>());
			containerList.add(new Container(i));
		}

		// 容器集合

	}

	public void readerInit() {

		tagInit();

		impinjReader.setTagReportListener(new TagReportListener() {
			@Override
			public void onTagReported(ImpinjReader reader0, TagReport report0) {
				// TODO Auto-generated method stub
				List<Tag> tags = report0.getTags();
				for (Tag t : tags) {
					if (refLabel.containsKey(t.getEpc().toString())) {
						refTagArray.get(refLabel.get(t.getEpc().toString()) - 1).add(t.getPeakRssiInDbm());
					} else {
						if (label.containsKey(t.getEpc().toString())
								&& labelAntenna.get(t.getEpc().toString()) == t.getAntennaPortNumber()) {
							if (t.isPeakRssiInDbmPresent()) {
								tagArray.get(label.get(t.getEpc().toString())).add(t.getPeakRssiInDbm());
							}
						} else {
							if (t.getPeakRssiInDbm() > start.thrCome) {
								label.put(t.getEpc().toString(), curTagNum++);
								labelAntenna.put(t.getEpc().toString(), t.getAntennaPortNumber());
								System.out.println("******    " + t.getEpc().toString() + " 进入，目前标签个数为：" + curTagNum);
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

//		try {
//			if (!ReaderOpen) {
//
//				impinjReader.connect(hostname);
//
//				final Settings settings = impinjReader.queryDefaultSettings();
//				ReportConfig report = settings.getReport();
//				report.setIncludeAntennaPortNumber(true);
//				report.setIncludePeakRssi(true);
//				report.setIncludePhaseAngle(true);
//				report.setMode(ReportMode.Individual);
//
//				settings.setReaderMode(ReaderMode.AutoSetDenseReader);
//
//				double txPowerinDbm = 32.0;
//				int rxSensitivityinDbm = -80;
//				AntennaConfigGroup antennas = settings.getAntennas();
//				antennas.disableAll();
//				antennas.enableById(new short[] { 1 });
//				antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
//				antennas.getAntenna((short) 1).setIsMaxTxPower(false);
//				antennas.getAntenna((short) 1).setTxPowerinDbm(txPowerinDbm);
//				antennas.getAntenna((short) 1).setRxSensitivityinDbm(rxSensitivityinDbm);
//
//				antennas.enableById(new short[] { 2 });
//				antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
//				antennas.getAntenna((short) 2).setIsMaxTxPower(false);
//				antennas.getAntenna((short) 2).setTxPowerinDbm(txPowerinDbm);
//				antennas.getAntenna((short) 2).setRxSensitivityinDbm(rxSensitivityinDbm);
//
//				antennas.enableById(new short[] { 3 });
//				antennas.getAntenna((short) 3).setIsMaxRxSensitivity(false);
//				antennas.getAntenna((short) 3).setIsMaxTxPower(false);
//				antennas.getAntenna((short) 3).setTxPowerinDbm(txPowerinDbm);
//				antennas.getAntenna((short) 3).setRxSensitivityinDbm(rxSensitivityinDbm);
//
//				impinjReader.applySettings(settings);
//				impinjReader.start();
//				ReaderOpen = true;
//			}
//
//		} catch (OctaneSdkException e1) {
//			e1.printStackTrace();
//		}
//		return ReaderOpen;

		 curTagNum++;
		 ReaderOpen = true;
		 return true;
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

	public void clearCache() {
		for (int i = 0; i < numTag; i++) {
			tagArray.get(i).clear();
		}
	}

	public void clearRefCache() {
		for (int i = 0; i < refNumTag; i++) {
			refTagArray.get(i).clear();
		}
	}

}
