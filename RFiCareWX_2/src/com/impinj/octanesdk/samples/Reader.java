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
	public static HashMap<String, Short> labelAntenna = new HashMap<String, Short>(); //

	// 病人epc床号键值对
	public static HashMap<String, String> patient_bed = new HashMap<String, String>();
	public static HashMap<String, Long> patient_TimeStamp = new HashMap<String, Long>();

	// 手持阅读器label组
	public static HashMap<String, Integer> patient_label = new HashMap<String, Integer>();
	public static ArrayList<ArrayList<Double>> patient_tagArray = new ArrayList<ArrayList<Double>>();

	private boolean ReaderOpen = false;
	private static int numTag;
	private static int patient_numTag;

	private static ImpinjReader impinjReader = new ImpinjReader();
	private String hostname = "192.168.1.118";

	public static int curTagNum = 0;
	public static int patient_curTagNum = 0;

	static ArrayList<Container> containerList = new ArrayList<Container>();

	public void tagInit() {
		// 初始化label
		patient_bed.put("E280 1160 6000 0204 A12A 382A", "101");
		patient_bed.put("E280 1160 6000 0204 A12A 38A", "102");
		patient_bed.put("E280 1160 6000 0204 A12A 389A", "103");
		patient_bed.put("E280 1160 6000 0204 A12C 04", "104");
		patient_bed.put("E280 1160 6000 0204 A12B 05", "105");
		patient_bed.put("E280 1160 6000 0204 A12C 06", "106");
		patient_bed.put("E280 1160 6000 0204 A12B 07", "107");
		patient_bed.put("E280 1160 6000 0204 A12C 08", "108");
		patient_bed.put("E280 1160 6000 0204 A12B 09", "201");
		patient_bed.put("E280 1160 6000 0204 A12C 10", "202");
		patient_bed.put("E280 1160 6000 0204 A12A 384A", "203");
		patient_bed.put("E280 1160 6000 0204 A12C 12", "204");
		patient_bed.put("E280 1160 6000 0204 A12B 13", "205");
		patient_bed.put("E280 1160 6000 0204 A12C 14", "206");
		patient_bed.put("E280 1160 6000 0204 A12B 15", "207");
		patient_bed.put("E280 1160 6000 0204 A12C 16", "208");
		patient_bed.put("E280 1160 6000 0204 A12B 17", "301");
		patient_bed.put("E280 1160 6000 0204 A12C 18", "302");
		patient_bed.put("E280 1160 6000 0204 A12B 19", "303");
		patient_bed.put("E280 1160 6000 0204 A12C 20", "304");
		patient_bed.put("E280 1160 6000 0204 A12C 21", "305");
		patient_bed.put("E280 1160 6000 0204 A12C 22", "306");
		patient_bed.put("E280 1160 6000 0204 A12C 23", "307");
		patient_bed.put("E280 1160 6000 0204 A12C 24", "308");
		patient_bed.put("Unknown", "Unknown");

		// label.put("1B1B 1B1B 0000 0000 0000 0001", 1);
		// label.put("1B1B 1B1B 0000 0000 0000 0002", 2);
		// label.put("3008 33B2 DDD9 0140 0000 0000", 3);
		// label.put("3008 33B2 DDD9 0140 0000 0001", 4);
		// numTag = label.size();

		numTag = 100;
		patient_numTag = 100;

		for (int i = 0; i < numTag; i++) {
			tagArray.add(new ArrayList<Double>());
			containerList.add(new Container(i));
		}

		for (int i = 0; i < patient_numTag; i++) {
			patient_tagArray.add(new ArrayList<Double>());
		}

		// 容器集合

	}

	// 单纯的阅读输液瓶上的标签
	// public void readerInit() {
	//
	// tagInit();
	//
	// impinjReader.setTagReportListener(new TagReportListener() {
	// @Override
	// public void onTagReported(ImpinjReader reader0, TagReport report0) {
	// // TODO Auto-generated method stub
	// List<Tag> tags = report0.getTags();
	// for (Tag t : tags) {
	// if (label.containsKey(t.getEpc().toString())
	// && labelAntenna.get(t.getEpc().toString()) == t.getAntennaPortNumber()) {
	// if (t.isPeakRssiInDbmPresent()) {
	// tagArray.get(label.get(t.getEpc().toString())).add(t.getPeakRssiInDbm());
	// }
	// } else {
	// if (t.getPeakRssiInDbm() > start.thrCome) {
	// label.put(t.getEpc().toString(), curTagNum++);
	// labelAntenna.put(t.getEpc().toString(), t.getAntennaPortNumber());
	// System.out.println("****** " + t.getEpc().toString() + " 进入，目前标签个数为：" +
	// curTagNum);
	// }
	// }
	// }
	// }
	// });
	// open();
	// }

	public void readerInit() {

		tagInit();

		impinjReader.setTagReportListener(new TagReportListener() {
			@Override
			public void onTagReported(ImpinjReader reader0, TagReport report0) {
				// TODO Auto-generated method stub
				List<Tag> tags = report0.getTags();
				for (Tag t : tags) {
					// if (t.getAntennaPortNumber() == (short) 1 &&
					// t.getEpc().toString().equals("E200 4125 1704 0175 2110
					// 3A5F")) {
					// System.out.println(t.getAntennaPortNumber() + " " +
					// t.getEpc().toString());
					// System.out.println(t.getPeakRssiInDbm());
					// }
					// if (patient_bed.containsKey(t.getEpc().toString())) {
					// // 如果读到床号标签的话
					// if (patient_label.containsKey(t.getEpc().toString())) {
					// if (t.isPeakRssiInDbmPresent()) {
					// patient_tagArray.get(patient_label.get(t.getEpc().toString()))
					// .add(t.getPeakRssiInDbm());
					// }
					// } else {
					// if (t.getPeakRssiInDbm() > start.thrComeforPatient) {
					// if
					// (!(patient_TimeStamp.containsKey(t.getEpc().toString())
					// && System.currentTimeMillis()
					// - patient_TimeStamp.get(t.getEpc().toString()) < 10000))
					// {
					// patient_TimeStamp.put(t.getEpc().toString(),
					// System.currentTimeMillis());
					// patient_label.put(t.getEpc().toString(),
					// patient_curTagNum++);
					// System.out.println("****** " + t.getEpc().toString() + "
					// 进入，手持阅读器扫描到的标签为："
					// + patient_curTagNum);
					// }
					// }
					// }
					// } else {
					if (label.containsKey(t.getEpc().toString())) {
						// System.out.println(t.getEpc().toString() +" "
						// +t.getAntennaPortNumber() + " " +
						// labelAntenna.get(t.getEpc().toString()));
						// System.out.println(t.getPeakRssiInDbm());
						if (t.isPeakRssiInDbmPresent()
								&& labelAntenna.get(t.getEpc().toString()) == t.getAntennaPortNumber()) {
							tagArray.get(label.get(t.getEpc().toString())).add(t.getPeakRssiInDbm());
						}
					}
					// else {
					// if (t.getPeakRssiInDbm() > start.thrCome) {
					// // label.put(t.getEpc().toString(),
					// // curTagNum++);
					//
					// // System.out.println("****** " +
					// // t.getEpc().toString() + " 进入，目前标签个数为：" +
					// // curTagNum);
					//
					// if
					// (!(patient_TimeStamp.containsKey(t.getEpc().toString())
					// && System.currentTimeMillis()
					// - patient_TimeStamp.get(t.getEpc().toString()) < 10000))
					// {
					//
					// labelAntenna.put(t.getEpc().toString(),
					// t.getAntennaPortNumber());
					// patient_TimeStamp.put(t.getEpc().toString(),
					// System.currentTimeMillis());
					// patient_label.put(t.getEpc().toString(),
					// patient_curTagNum++);
					// System.out.println(
					// "****** " + t.getEpc().toString() + "进入，手持阅读器扫描到的标签为：" +
					// patient_curTagNum);
					// }
					// }
					// }
					// }
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
				report.setMode(ReportMode.Individual);

				settings.setReaderMode(ReaderMode.AutoSetDenseReader);

				double txPowerinDbm = 32.0;
				int rxSensitivityinDbm = -80;
				AntennaConfigGroup antennas = settings.getAntennas();
				antennas.disableAll();
				antennas.enableById(new short[] { 1 });
				antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
				antennas.getAntenna((short) 1).setIsMaxTxPower(false);
				antennas.getAntenna((short) 1).setTxPowerinDbm(txPowerinDbm);
				antennas.getAntenna((short) 1).setRxSensitivityinDbm(rxSensitivityinDbm);

				antennas.enableById(new short[] { 2 });
				antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
				antennas.getAntenna((short) 2).setIsMaxTxPower(false);
				antennas.getAntenna((short) 2).setTxPowerinDbm(txPowerinDbm);
				antennas.getAntenna((short) 2).setRxSensitivityinDbm(rxSensitivityinDbm);

				antennas.enableById(new short[] { 3 });
				antennas.getAntenna((short) 3).setIsMaxRxSensitivity(false);
				antennas.getAntenna((short) 3).setIsMaxTxPower(false);
				antennas.getAntenna((short) 3).setTxPowerinDbm(txPowerinDbm);
				antennas.getAntenna((short) 3).setRxSensitivityinDbm(rxSensitivityinDbm);

				impinjReader.applySettings(settings);
				impinjReader.start();
				ReaderOpen = true;
			}

		} catch (OctaneSdkException e1) {
			e1.printStackTrace();
		}
		return ReaderOpen;

		// curTagNum++;
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

	public static HashMap<String, Integer> getLabel() {
		return label;
	}

	public static void setLabel(HashMap<String, Integer> label) {
		Reader.label = label;
	}

	public static HashMap<String, Short> getLabelAntenna() {
		return labelAntenna;
	}

	public static void setLabelAntenna(HashMap<String, Short> labelAntenna) {
		Reader.labelAntenna = labelAntenna;
	}

	public static HashMap<String, String> getPatient_bed() {
		return patient_bed;
	}

	public static void setPatient_bed(HashMap<String, String> patient_bed) {
		Reader.patient_bed = patient_bed;
	}

	public static HashMap<String, Long> getPatient_TimeStamp() {
		return patient_TimeStamp;
	}

	public static void setPatient_TimeStamp(HashMap<String, Long> patient_TimeStamp) {
		Reader.patient_TimeStamp = patient_TimeStamp;
	}

	public static HashMap<String, Integer> getPatient_label() {
		return patient_label;
	}

	public static void setPatient_label(HashMap<String, Integer> patient_label) {
		Reader.patient_label = patient_label;
	}

	public static ArrayList<ArrayList<Double>> getPatient_tagArray() {
		return patient_tagArray;
	}

	public static void setPatient_tagArray(ArrayList<ArrayList<Double>> patient_tagArray) {
		Reader.patient_tagArray = patient_tagArray;
	}

	public static int getNumTag() {
		return numTag;
	}

	public static void setNumTag(int numTag) {
		Reader.numTag = numTag;
	}

	public static int getPatient_numTag() {
		return patient_numTag;
	}

	public static void setPatient_numTag(int patient_numTag) {
		Reader.patient_numTag = patient_numTag;
	}

	public static ImpinjReader getImpinjReader() {
		return impinjReader;
	}

	public static void setImpinjReader(ImpinjReader impinjReader) {
		Reader.impinjReader = impinjReader;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public static int getCurTagNum() {
		return curTagNum;
	}

	public static void setCurTagNum(int curTagNum) {
		Reader.curTagNum = curTagNum;
	}

	public static int getPatient_curTagNum() {
		return patient_curTagNum;
	}

	public static void setPatient_curTagNum(int patient_curTagNum) {
		Reader.patient_curTagNum = patient_curTagNum;
	}

	public static ArrayList<Container> getContainerList() {
		return containerList;
	}

	public static void setContainerList(ArrayList<Container> containerList) {
		Reader.containerList = containerList;
	}

	public static void setTagArray(ArrayList<ArrayList<Double>> tagArray) {
		Reader.tagArray = tagArray;
	}

	public void setReaderOpen(boolean readerOpen) {
		ReaderOpen = readerOpen;
	}

	public static boolean isBed(String epc) {
		return patient_bed.containsKey(epc);
	}

	public void clearCache() {
		for (int i = 0; i < numTag; i++) {
			tagArray.get(i).clear();
		}
	}

}
