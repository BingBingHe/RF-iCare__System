package com.impinj.octanesdk.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;



public class Detector {
	
	public static int bufferSize = 15;
	public static double rateThreshold = start.thrRDrate;
	public static double phaseVarThreshold[] = {0.1,0.15};
	
//	public Detector() throws Exception
//	{
////		flag = false;
//		bufferIndex = 0;
//		
//		File file = new File("/Users/zz/Documents/RFID/鎸傛按/杈撴恫琚媝hase/bag1.csv");
//        if (file.isFile() && file.exists())
//        { // 鍒ゆ柇鏂囦欢鏄惁瀛樺湪
//            InputStreamReader read = new InputStreamReader(
//                    new FileInputStream(file), "GBK");// 鑰冭檻鍒扮紪鐮佹牸寮�
//            BufferedReader bufferedReader = new BufferedReader(read);
//            String lineTxt = null;
//
//            while ((lineTxt = bufferedReader.readLine()) != null)
//            {
//            	String temp[] = lineTxt.split(",");
//            	if (temp.length<3)
//            		continue;
//            	double a = Double.parseDouble(temp[1]);//rate
//            	double b = Double.parseDouble(temp[3]);//phase
////            	System.out.println(a+" "+b);
//            	flag = phaseDetector(a,b);
//            	
//            	if (flag)
//            		System.out.println("Detected True ID: "+temp[0] +"^^^ rate/phase "+a+" "+b);
//            }
//            bufferedReader.close();
//            read.close();
//        }
//	}
	
	public static boolean phaseDetector(Container cont, double rate, double phase) {
		// update the buffer
		int bufferIndex = cont.getBufferIndex();
		double bufferSum[] = cont.getBufferSum();
		double phaseLast = cont.getPhaseLast();
		double bufferP[][] = cont.getBufferP();
		
		bufferSum[0] -= bufferP[0][bufferIndex];
		bufferSum[1] -= bufferP[1][bufferIndex];
		
		// handle phase jump of pi or 2pi
		if (phaseLast == 1000000)
			bufferP[1][bufferIndex] = phase;
		else{
			double phaseGap = (phase - phaseLast);
			while ((phaseGap)>1.57)
				phaseGap -= Math.PI;
			while ((phaseGap)<-1.57)
				phaseGap += Math.PI;
			bufferP[1][bufferIndex] = phaseLast + phaseGap;
		}
//		System.out.println("#@$%\t\t\t"+bufferP[1][bufferIndex]);
		
		bufferP[0][bufferIndex] = rate;
//		bufferP[1][bufferIndex] = phase;
		phaseLast = bufferP[1][bufferIndex];
		bufferSum[0] += bufferP[0][bufferIndex];
		bufferSum[1] += bufferP[1][bufferIndex];
		bufferIndex = (++bufferIndex)%bufferSize;
		
		cont.setBufferIndex(bufferIndex);
		cont.setBufferSum(bufferSum);
		cont.setPhaseLast(phaseLast);
		cont.setBufferP(bufferP);
		
		double meanRate = bufferSum[0]/bufferSize;
//		System.out.println("#"+meanRate);
		if (meanRate >= rateThreshold && rate >= rateThreshold)//large read rate and then test phase variance
		{
			double meanPhase = bufferSum[1]/bufferSize;
			
			
			double stdPhase = 0;
			for (int i=0;i<bufferSize;i++)
			{
				stdPhase += Math.pow(Math.min(Math.abs(bufferP[1][i] - meanPhase),6.28-Math.abs(bufferP[1][i]-meanPhase)),2);
			}
			stdPhase/=bufferSize;
//			System.out.println("*stdPhase/meanRate/phaseGap**"+stdPhase+" "+meanRate+"  "+Math.abs(phaseLast - bufferP[1][bufferIndex]));
			if (phaseVarThreshold[1]>Math.abs(phaseLast - bufferP[1][bufferIndex]) &&
					stdPhase <= phaseVarThreshold[0])
			{
				System.out.print("stdPhase "+stdPhase+"*****");
				return true;
				}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
		
//		return false;
	}

	public static final void main(String args[]){
		try{
			new Detector();
		}catch(Exception e)
		{
			System.out.println(e.toString()+"\terr");
		}
	}

}
