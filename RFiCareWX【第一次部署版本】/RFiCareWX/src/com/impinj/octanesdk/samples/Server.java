package com.impinj.octanesdk.samples;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server implements Runnable {

	private Socket client;
	private DataInputStream dis;

	public Server() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMsg() {
		byte[] b = new byte[1024];
		String message = "";
		try {

			dis = new DataInputStream(client.getInputStream());
			dis.read(b, 0, b.length);
			message = new String(b, "UTF-8"); // �ؼ������յ����ֽ�����Ҫת��Ϊ�ַ�������ʾ
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message.trim(); // ȥ���ո�
	}

	public void test() throws IOException {
		ServerSocket server = new ServerSocket(8895);
		while (true) {
			client = server.accept();
			System.out.println("connect successfully");
			String str = getMsg();

			Hand.getInfusion_bed().add(str);

			// if (!str.equals("")) {
			// // System.out.println(str.trim());
			// String[] tmp = str.trim().split(";");
			// // �ӿո񣡣���
			// HashMap<String, Integer> patient_label =
			// Reader.getPatient_label();
			// HashMap<String, Long> patient_TimeStamp =
			// Reader.getPatient_TimeStamp();
			// StringBuilder sb = new StringBuilder();
			// for (int i = 0; i < tmp[0].length(); i += 4) {
			// sb.append(tmp[0].substring(i, i + 4));
			// sb.append(' ');
			// }
			// String result = sb.toString().trim();
			// if (Reader.getLabel().containsKey(result)
			// && System.currentTimeMillis() - patient_TimeStamp.get(result) >
			// 10000) {
			// Container cont =
			// Reader.containerList.get(Reader.getLabel().get(result));
			// cont.setCanLeave(true);
			// System.out.println(result + "�����뿪������");
			// continue;
			// }
			// if (!(patient_TimeStamp.containsKey(result)
			// && System.currentTimeMillis() - patient_TimeStamp.get(result) <
			// 10000)) {
			// patient_TimeStamp.put(result, System.currentTimeMillis());
			// patient_label.put(result, Reader.patient_curTagNum++);
			// System.out.println("****** " + result + "���룬�ֳ��Ķ���ɨ�赽�ı�ǩΪ��" +
			// Reader.patient_curTagNum);
			// }
			// }
			client.close();
		}
	}
}
