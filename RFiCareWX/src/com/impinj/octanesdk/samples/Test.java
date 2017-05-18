package com.impinj.octanesdk.samples;

import java.util.Hashtable;

public class Test {

	public static Hashtable<Integer, String> ht = new Hashtable<>();
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread t = new Thread(new Server());
		t.start();
		int count = 1000;
		while(count > 0){
			Thread.sleep(3000);
			for(int i= 0 ; i < ht.size() ; i++){
				System.out.println(ht.get(i));
			}
			System.out.println("====");
			count--;
		}
			
		
	}

}
