package com.impinj.octanesdk.samples;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class start {

	static int sleepTime = 3000; // 滑动窗口的大小，ms为单位
	static int varThreshold = 100; // 方差阈值
	
	
	static int slideNumforPosition = 6;
	final static double thrLeave = -33.0; // 离开的阈值
	final static double thrCome = -55.0; // 进入的阈值
	final static String[] bedName = {"321","323"};
	
	
 	static int numContainerList = 0;
	static Reader rd = new Reader();

	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
		String url = "114.212.84.243";
		String user = "javatest";
		String password = "1234";
		Connection conn = DriverManager.getConnection("jdbc:mysql://"+ url +"/hebb", user, password);
		Statement stmt = conn.createStatement();
		
		// 绘制初始化界面
		UItest ui = new UItest();
		ui.init();

		// 初始化阅读器
		rd.readerInit();

		int count = 0;
		// 不断地动态扫描，改变状态
		while (true) {
			System.out.println("    ——————");
			// 读取的时延：3秒
			if (rd.isReaderOpen()) {
				count++;
				ui.detectingSign(count % 4);
				Thread.sleep(sleepTime);
				// for (Container cont : Reader.containerList) {
				for (int h = 0; h < Reader.curTagNum; h++) {

					Container cont = Reader.containerList.get(h);
					int beginTmp = cont.getBegin();
					double rssiMeansTmp[] = DataProcess.getMeans(rd.getTagArray(), beginTmp, 1);
					double rssiMean = rssiMeansTmp[0];
					double rssiMax = rssiMeansTmp[1];
					double rssiVarSum = DataProcess.getVar(rd.getTagArray(), beginTmp, 1);

					
					// 判断是否在场 && 离开
					if(h == 0 && count >1 ){
						rssiMean = -60;
						rssiMax = -60;
					}
					
					if(h == 0 && count >3 ){
						rssiMean = -50;
						rssiMax = -53;
					}

					if (cont.getState().isEmpty() && !cont.isPresent()) {
						// System.out.println(rssiMeans);
						if (rssiMax > thrCome) {
							cont.setPresent(true);
							ui.addState(cont);
							numContainerList++;
						}
					}

					System.out.print("吊瓶号： " + beginTmp + "，  均值：" + rssiMean + "，  最大值： " + rssiMax + "，  状态： "
							+ cont.isPresent() + "，  方差：" + rssiVarSum);

					if (!cont.isPresent()) {
						System.out.println();
						continue;
					}

					int iTmp = cont.getState().size();
					if (iTmp > 0 && iTmp < slideNumforPosition + 1) {
						for (int g = 0; g < Reader.getRefNumTag(); g++) {
							cont.bedArrayList.get(g).addAll(rd.getRefTagArray().get(g));							
						}
						if (iTmp == slideNumforPosition) {
							double[] varS = new double[Reader.getRefNumTag()];
							for (int g = 0; g < Reader.getRefNumTag(); g++) {
								varS[g] = DataProcess.getVarForRef(cont.bedArrayList, g);
							}
							System.out.println("    " +   varS[0] + "  " + varS[1]);
//							if (varS[0] > varS[1]) {
//								System.out.println("---> 1");
//								ui.setBedNumAuto(cont, bedName[0]);
//							} else {
//								System.out.println("---> 2");
//								ui.setBedNumAuto(cont, bedName[1]);
//							}
						}
					} 

					if (rssiVarSum < 200) {
						int waterLevel = (new WaterLevelCalculate()).CalSingle(rssiMean);
						// System.out.println("， 水位： "+ waterLevel);
						cont.setWaterLevel(waterLevel);
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(waterLevel);
							System.out.println("，  水位：" + waterLevel + " (" + cont.getState().size() + ")");
						}
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));
						// 刷新相应的UI状态
						ui.changeState(cont);
						if (!cont.isHasWarn()) {
							ui.addWarn(cont);
						}

					} else {

						ui.nurseState(cont);
						cont.getTimestamp().add((int) (System.currentTimeMillis() % 100000000));
						if (rssiMax > thrLeave) {
							cont.getState().add(-2);
							System.out.println("，  水位： -2" + " (" + cont.getState().size() + ")");
						} else {
							cont.getState().add(-1);
							System.out.println("，  水位： -1" + " (" + cont.getState().size() + ")");
						}
					}

					boolean present = !new TimeCalculate().canLeave(cont.getState(), cont);
					cont.setPresent(present);
					if (!cont.isPresent()) {
						ui.removeWarn(cont);
						ui.removeState(cont);
					}
					
					
					StringBuilder sql = new StringBuilder();
					sql.append("insert into waterlevel values (");
					sql.append("'jack'");
					sql.append(",");
					sql.append("'"+cont.getBedNum()+"'");
					sql.append(",");
					sql.append("" + cont.getWaterLevel() * 12.5 );
					sql.append(",");
					if(cont.isHasWarn()){
						sql.append("1");
					}else{
						sql.append("0");						
					}
					sql.append(",");
					sql.append("40");
					sql.append(",");
					sql.append("120");
					sql.append(",");
					sql.append("'NaCl'");
					sql.append(",");
					sql.append("04192008");
					sql.append(");");
					
					
					stmt.executeUpdate(sql.toString());

//					ResultSet ret = stmt.executeQuery("select * from waterlevel");
//					while (ret.next()) {
//						System.out.print(ret.getString("name") + " ");
//						System.out.println(ret.getString("info"));
//					}
					
//					String filename = "RFiCare";
//					String fileFolderName = "E:\\ReallyMySQL";
//					int fileCount = 1;
//					int step = 1;
//					File fileFind = new File(fileFolderName);
//					File[] fileArray = fileFind.listFiles();
//					for (int i = 0; i < fileArray.length; i++) {
//						if (fileArray[i].isFile() && fileArray[i].getName().startsWith(filename + "_")) {
//							fileCount += step;
//						}
//					}
//
//					
//					File file = new File(fileFolderName + "\\" + filename + "_" + fileCount + ".csv");
//					
//					
//					StringBuilder outPut = new StringBuilder();
//					outPut.append(""+cont.bedNum);
//					outPut.append(",");
//					outPut.append(""+cont.getWaterLevel() * 12.5);
//					outPut.append(",");
//					if(cont.isHasWarn()){
//						outPut.append("yes");
//					}else{
//						outPut.append("no");						
//					}
//					outPut.append(",");
//					outPut.append("40");
//					outPut.append(",");
//					outPut.append("120");
//					outPut.append(",");
//					outPut.append("NaCl");
//					outPut.append(",");
//					outPut.append("Jack");
//					outPut.append(",");
//					outPut.append("2017.04.08");
//					
//					BufferedWriter bw;
//					try {
//						bw = new BufferedWriter(new FileWriter(file));
//						bw.write(outPut.toString());
//						bw.flush();
//						bw.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
				}
				rd.clearCache();
				rd.clearRefCache();
			} else {
				ui.initSign();
			}
		}
	}

}
