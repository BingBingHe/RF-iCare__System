package com.impinj.octanesdk.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.BitPointers;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.MemoryBank;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagFilter;
import com.impinj.octanesdk.TagFilterMode;
import com.impinj.octanesdk.TagFilterOp;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class Reader {

	// label 存取已经登进进入的标签
	public static HashMap<String, Integer> label = new HashMap<String, Integer>();
	// tagArray 存储的是label中每个标签对应的RSSI值
	public static ArrayList<ArrayList<Double>> tagArray = new ArrayList<ArrayList<Double>>();
	// tagPhase 存储的是label中每个标签对应的相位值
	public static ArrayList<ArrayList<Double>> tagPhase = new ArrayList<ArrayList<Double>>();
	// labelAntenna 存储的是，label中每个标签对应的天线号
	public static HashMap<String, Short> labelAntenna = new HashMap<String, Short>(); //

	private boolean ReaderOpen = false;
	private static int numTag; // 初始话标签数目

	private static ImpinjReader impinjReader = new ImpinjReader();

	// 阅读器设置
	private static Settings settings;

	// 表示当前已经登记进入的标签数目
	public static int curTagNum = 0;

	// 100个输液瓶，初始创建
	static ArrayList<Container> containerList = new ArrayList<Container>();

	public static void tagInit() {
		// 初始化label
		numTag = 100;
		curTagNum = 0;
		for (int i = 0; i < numTag; i++) {
			tagArray.add(new ArrayList<Double>()); // 预先创建100个容器 && tagArray存储数据
			tagPhase.add(new ArrayList<Double>());
			containerList.add(new Container(i));
		}

		// addCont("2017 1220 0000 0000 0000 0001", "2017 1220 0000 0000 0000
		// 0002", 0, 1, "110", 2);
		addCont("2018 0110 0000 0000 0000 0001", "2018 0110 0000 0000 0000 0002", 0, 1, "220", 2);
	}

	// 添加三标签输液袋模具
	public static void addCont(String tag1, String tag2, int readerCode, int port, String bedNum, int length) {
		Container cont = containerList.get(curTagNum);
		// cont.setPresent(true);
		cont.setTag1(tag1);
		cont.setTag2(tag2);
		// cont.setTag3(tag3);
		cont.setArea(port);
		cont.setBedNum(bedNum);
		cont.setLength(length);

		cont.rssiLog.add(new ArrayList<Double>());
		cont.rssiLog.add(new ArrayList<Double>());
		// cont.rssiLog.add(new ArrayList<Double>());
		cont.rateLog.add(new ArrayList<Double>());
		cont.rateLog.add(new ArrayList<Double>());
		// cont.rateLog.add(new ArrayList<Double>());
		cont.phaseLog.add(new ArrayList<Double>());
		cont.phaseLog.add(new ArrayList<Double>());
		// cont.phaseLog.add(new ArrayList<Double>());

		label.put(tag1, curTagNum++);
		label.put(tag2, curTagNum++);
		// label.put(tag3, curTagNum++);
		labelAntenna.put(tag1, (short) port);
		labelAntenna.put(tag2, (short) port);
		// labelAntenna.put(tag3, (short) port);
	}

	// 配置文件初始化
	public static void settingsInit() {

		settings = impinjReader.queryDefaultSettings();
		ReportConfig report = settings.getReport();
		report.setIncludeAntennaPortNumber(true);
		report.setIncludePeakRssi(true);
		report.setIncludePhaseAngle(true);
		report.setMode(ReportMode.Individual);

		// settings.setReaderMode(ReaderMode.MaxThroughput);
		settings.setReaderMode(ReaderMode.AutoSetDenseReader);

		// 添加 Mask
		String targetEpc = "20180110";

		TagFilter t1 = settings.getFilters().getTagFilter1();
		t1.setBitCount(32);
		t1.setBitPointer(BitPointers.Epc);
		t1.setMemoryBank(MemoryBank.Epc);
		t1.setFilterOp(TagFilterOp.Match);
		t1.setTagMask(targetEpc);

		settings.getFilters().setMode(TagFilterMode.OnlyFilter1);
		System.out.println("Matching 1st 16 bits of epc " + targetEpc);

		double txPowerinDbm = start.thrPower;
		int rxSensitivityinDbm = -70;
		AntennaConfigGroup antennas = settings.getAntennas();
		antennas.disableAll();
		try {
			antennas.enableById(new short[] { 1 });
			antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
			antennas.getAntenna((short) 1).setIsMaxTxPower(false);
			antennas.getAntenna((short) 1).setTxPowerinDbm(txPowerinDbm);
			antennas.getAntenna((short) 1).setRxSensitivityinDbm(rxSensitivityinDbm);

			// antennas.enableById(new short[] { 2 });
			// antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
			// antennas.getAntenna((short) 2).setIsMaxTxPower(false);
			// antennas.getAntenna((short) 2).setTxPowerinDbm(txPowerinDbm);
			// antennas.getAntenna((short)
			// 2).setRxSensitivityinDbm(rxSensitivityinDbm);

			// antennas.enableById(new short[] { 3 });
			// antennas.getAntenna((short) 3).setIsMaxRxSensitivity(false);
			// antennas.getAntenna((short) 3).setIsMaxTxPower(false);
			// antennas.getAntenna((short) 3).setTxPowerinDbm(txPowerinDbm);
			// antennas.getAntenna((short)
			// 3).setRxSensitivityinDbm(rxSensitivityinDbm);

		} catch (OctaneSdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
					if (label.containsKey(t.getEpc().toString())) {

						if (t.getPeakRssiInDbm() > start.thrCome) {
							Container cont = Reader.containerList.get(label.get(t.getEpc().toString()) / 2 * 2);
							if (System.currentTimeMillis() - cont.lastLeaveTime > start.thrComeLeaveTime) {
								cont.setPresent(true);
							}
						}

						if (t.isPeakRssiInDbmPresent()
								&& labelAntenna.get(t.getEpc().toString()) == t.getAntennaPortNumber()) {
							tagArray.get(label.get(t.getEpc().toString())).add(t.getPeakRssiInDbm());
							tagPhase.get(label.get(t.getEpc().toString())).add(t.getPhaseAngleInRadians());
							// System.out.println(t.getEpc().toString());
						}
					}
					// else {
					// if (t.getPeakRssiInDbm() > start.thrCome) {
					// Container cont = Reader.containerList.get(curTagNum);
					// cont.setPresent(true);
					// cont.setEPC(t.getEpc().toString());
					// cont.setArea(t.getAntennaPortNumber());
					// label.put(t.getEpc().toString(), curTagNum++);
					// labelAntenna.put(t.getEpc().toString(),
					// t.getAntennaPortNumber());
					// }
					// }
				}
			}
		});

		open();

	}

	// 修改阅读器配置
	public void settingsModify(double power, int port, String tag1) {

		try {
			impinjReader.stop();
			AntennaConfigGroup antennas = settings.getAntennas();
			antennas.getAntenna((short) port).setTxPowerinDbm(power);

			// 添加 Mask
			// String targetEpc = epc;

			// TagFilter t1 = settings.getFilters().getTagFilter1();
			// t1.setBitCount(96);
			// t1.setBitPointer(BitPointers.Epc);
			// t1.setMemoryBank(MemoryBank.Epc);
			// t1.setFilterOp(TagFilterOp.Match);
			// t1.setTagMask(targetEpc);

			// String targetEpc = "20171220";// 输液瓶
			//
			// TagFilter t1 = settings.getFilters().getTagFilter1();
			// t1.setBitCount(32);
			// t1.setBitPointer(BitPointers.Epc);
			// t1.setMemoryBank(MemoryBank.Epc);
			// t1.setFilterOp(TagFilterOp.Match);
			// t1.setTagMask(targetEpc);
			//
			// settings.getFilters().setMode(TagFilterMode.OnlyFilter1);
			// System.out.println("Matching epc " + targetEpc);

			impinjReader.applySettings(settings);
			impinjReader.start();

			System.out.println("antenna " + port + " power to " + power);
		} catch (OctaneSdkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

				impinjReader.connect(start.hostname);
				settingsInit();
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
	}

	public void clearCache() {
		for (int i = 0; i < numTag; i++) {
			tagArray.get(i).clear();
			tagPhase.get(i).clear();
		}
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

	public static int getNumTag() {
		return numTag;
	}

	public static void setNumTag(int numTag) {
		Reader.numTag = numTag;
	}

	public static ImpinjReader getImpinjReader() {
		return impinjReader;
	}

	public static void setImpinjReader(ImpinjReader impinjReader) {
		Reader.impinjReader = impinjReader;
	}

	public static int getCurTagNum() {
		return curTagNum;
	}

	public static void setCurTagNum(int curTagNum) {
		Reader.curTagNum = curTagNum;
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

	public static ArrayList<ArrayList<Double>> getTagPhase() {
		return tagPhase;
	}

	public static void setTagPhase(ArrayList<ArrayList<Double>> tagPhase) {
		Reader.tagPhase = tagPhase;
	}

}
