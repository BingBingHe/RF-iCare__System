package com.impinj.octanesdk.samples;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyStore.Entry;
import java.text.SimpleDateFormat;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JList;

public class UItest {

	final JFrame f = new JFrame("RF-ICare（尔福康）输液系统  V1.1");
	final JPanel displayPanel = new JPanel();
	final JScrollPane displayJSP = new JScrollPane();
	final JPanel taskPanel = new JPanel();
	final JScrollPane taskJSP = new JScrollPane();
	final Font font = new Font("Georgia", Font.BOLD, 15);
	final ArrayList<JPanel> displayUnits = new ArrayList<>();
	final ArrayList<JPanel> taskUnits = new ArrayList<>();
	final JLabel sign = new JLabel();
	final Label indication = new Label();

	public void init() {

		Toolkit t = Toolkit.getDefaultToolkit();
		// @SuppressWarnings("deprecation")
		// String src1 =
		// this.getClass().getResource("transfusion/hospital.png").getFile();
		// f.setIconImage(t.getImage(src1));
		try {
			f.setIconImage(t.getImage(this.getClass().getResource("transfusion/hospital.png").toURI().getPath()));
		} catch (URISyntaxException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		f.setBounds(20, 20, 1170, 750);
		f.setLayout(null);

		// 面板
		displayPanel.setSize(660, 650);
		displayPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// 给面板加个滚动条
		displayJSP.setViewportView(displayPanel);
		displayJSP.setLocation(480, 20);
		displayJSP.setSize(660, 650);
		TitledBorder tbTmp = new TitledBorder("输液情况显示");
		Font fTmp = new Font("宋体", Font.BOLD, 20);
		tbTmp.setTitleFont(fTmp);
		tbTmp.setTitleColor(Color.GRAY);
		tbTmp.setTitleJustification(TitledBorder.CENTER);

		displayJSP.setBorder(tbTmp);

		displayJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		displayJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel.setPreferredSize(new Dimension(650, 2000));// 这是关键的2句
		displayJSP.setPreferredSize(new Dimension(650, 20000));

		// 增加任务提醒队列
		// 面板
		taskPanel.setSize(660, 650);
		taskPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// 给面板加个滚动条
		taskJSP.setViewportView(taskPanel);
		taskJSP.setLocation(20, 150);
		taskJSP.setSize(430, 470);

		TitledBorder tbTmp2 = new TitledBorder("任务提醒窗口");
		tbTmp2.setTitleFont(fTmp);
		tbTmp2.setTitleColor(Color.GRAY);
		tbTmp2.setTitleJustification(TitledBorder.CENTER);
		taskJSP.setBorder(tbTmp2);

		taskJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		taskJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskPanel.setPreferredSize(new Dimension(400, 2000));// 这是关键的2句
		taskJSP.setPreferredSize(new Dimension(400, 20000));

		Button openLog = new Button("打开日志文件");
		openLog.setBounds(20, 630, 100, 30);
		openLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// try {
				// try {
				// URL uri = this.getClass().getResource("transfusion/log.txt");
				// File logFile = new File(uri.toURI().getPath());
				// Desktop.getDesktop().open(logFile);
				// } catch (URISyntaxException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				// } catch (IOException e2) {
				// // TODO Auto-generated catch block
				// e2.printStackTrace();
				// }
				// try {
				// Runtime.getRuntime().exec("cmd /c log.txt");
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
			}
		});

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("transfusion/logo.png")));
		logo.setBounds(50, 20, 130, 120);

		Label systemName = new Label();
		systemName.setBounds(170, 40, 270, 30);
		systemName.setAlignment(1);
		Font fontName = new Font("Georgia", Font.BOLD, 20);
		systemName.setFont(fontName);
		systemName.setText("RF-iCare（尔福康）输液系统");

		sign.setIcon(new ImageIcon(this.getClass().getResource("transfusion/signRED.png")));
		sign.setBounds(170, 90, 20, 20);

		indication.setBounds(200, 88, 150, 30);
		indication.setAlignment(0);
		Font fontIndication = new Font("Georgia", Font.PLAIN, 15);
		indication.setFont(fontIndication);
		indication.setText("系统正在初始化");

		f.add(systemName);
		f.add(logo);
		f.add(indication);
		f.add(displayJSP);
		f.add(taskJSP);
		// f.add(openLog);
		f.add(sign);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				start.rd.closeReader();
				System.exit(0);
			}
		});

		f.setVisible(true);

	}

	public void initSign() {
		sign.setIcon(new ImageIcon(this.getClass().getResource("transfusion/signRED.png")));
		indication.setText("系统正在初始化");
	}

	public void detectingSign(int count) {

		sign.setIcon(new ImageIcon(this.getClass().getResource("transfusion/signGREEN.png")));
		String drops = "";
		for (int i = 0; i < count; i++) {
			drops = drops.concat("·");
		}
		indication.setText("系统正常工作中 " + drops);
	}

	public void addState(Container cont) {
		// 添加状态
		JPanel displayUnit = cont.getDisplayUnit();
		displayUnit.setSize(206, 230);
		TitledBorder tb = BorderFactory.createTitledBorder("请设置床号");
		displayUnit.setBorder(tb);
		displayUnit.setLayout(null);
		displayUnit.setPreferredSize(new Dimension(206, 230));// 这是关键的2句

		displayUnit.add(new JLabel());
		((JLabel) (displayUnit.getComponent(0)))
				.setIcon(new ImageIcon(this.getClass().getResource("transfusion/500_8.png")));
		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		jLabel.setBounds(0, 20, 115, 200);

		displayUnit.add(new Label());
		Label label = ((Label) (displayUnit.getComponent(1)));
		label.setBounds(115, 40, 70, 30);
		label.setAlignment(1);
		label.setFont(font);
		if (cont.getWaterLevel() < 2) {
			label.setText(12.5 * cont.getWaterLevel() + "%");
			label.setBackground(Color.red);
		} else {
			label.setText(12.5 * cont.getWaterLevel() + "%");
			label.setBackground(null);

		}

		// displayUnit.add(new Button("查看药品详情"));
		// Button button1 = ((Button) (displayUnit.getComponent(2)));
		// button1.setBounds(110, 90, 80, 30);
		// button1.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // TODO Auto-generated method stub
		// // displayPanel.remove(displayUnit);
		// // displayPanel.validate();
		//
		// // Object[] options = { "确认", "取消" };
		// JOptionPane.showMessageDialog(null, "【药品名称】通用名称：尼莫地平片
		// \n【英文名称】Nimodpin Tablets \n【汉语拼音】Nimodiping Pian",
		// "药品详情", JOptionPane.DEFAULT_OPTION);
		// }
		// });

		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = "时间：" + formatter.format(curDate) + "】";

		displayUnit.add(new Button("设置床号"));
		Button button2 = ((Button) (displayUnit.getComponent(2)));
		button2.setBounds(110, 140, 80, 30);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String inputValue = JOptionPane.showInputDialog(null, "输入当前输液瓶所处床号【进入" + time, "设置床号",
						JOptionPane.DEFAULT_OPTION);
				TitledBorder tbtmp = null;
				TitledBorder tb = (TitledBorder) displayUnit.getBorder();
				Character bedNum = tb.getTitle().charAt(0);
				// System.out.println(bedNum);
				if (inputValue == null) {
					if (!Character.isDigit(bedNum)) {
						System.out.println("1111213");
						tbtmp = BorderFactory.createTitledBorder("请设置床号");
						JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
						unknown.setIcon(new ImageIcon(this.getClass().getResource("transfusion/unknown.png")));
					} else {
						tbtmp = BorderFactory.createTitledBorder(tb.getTitle());
						JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
						unknown.setIcon(null);
					}
				} else {
					tbtmp = BorderFactory.createTitledBorder("" + inputValue + "号床");
					JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
					unknown.setIcon(null);
					cont.setBedNum(inputValue);
				}

				displayUnit.setBorder(tbtmp);

				String info = "" + cont.getBedNum() + "号床开始输液。";

				// try {
				// URL uri = this.getClass().getResource("transfusion/log.txt");
				// File logFile = new File(uri.toURI().getPath());
				// BufferedWriter bw = new BufferedWriter(new
				// FileWriter(logFile, true));
				// bw.write("【" + time + info);
				// bw.newLine();
				// bw.flush();
				// bw.close();
				// } catch (IOException | URISyntaxException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
			}
		});

		displayUnit.add(new JLabel());
		((JLabel) (displayUnit.getComponent(3)))
				.setIcon(new ImageIcon(this.getClass().getResource("transfusion/unknown.png")));
		JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
		unknown.setBounds(125, 80, 50, 50);

		displayUnit.add(new Button("x"));
		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(183, 10, 20, 20);
		button3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cont.setPresent(false);
				removeState(cont);
				removeWarn(cont);
			}
		});

		displayUnit.add(new Button("药品信息"));
		Button button4 = ((Button) (displayUnit.getComponent(5)));
		button4.setBounds(110, 180, 80, 30);
		button4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String medicine =null;
				if(cont.getBegin()/4==0){
					medicine = "药品名称：葡萄糖注射液\n剂        量：400ml\n患        者：张三\n日        期：2017年3月7日\n";
				}else if(cont.getBegin()/4==1){
					medicine = "药品名称：氯化钠注射液\n剂        量：400ml\n患        者：李四\n日        期：2017年3月7日\n";
				}
				JOptionPane.showMessageDialog(null, medicine, "药品信息", JOptionPane.NO_OPTION);
			}
		});

		displayUnit.updateUI();
		displayUnits.add(displayUnit);
		displayPanel.add(displayUnit);
		displayPanel.repaint();
		displayPanel.updateUI();
	}

	public void changeState(Container cont) {
		JPanel displayUnit = cont.getDisplayUnit();

		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));

		jLabel.setBounds(0, 20, 115, 200);

		boolean ct = isNeedWarn(cont);

		String tmp = "500_" + cont.getWaterLevel();
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/" + tmp + ".png")));
		label.setText(cont.getWaterLevel() * 12.5 + "%");

		if (ct || cont.isHasWarn() && label.getText().equals("请稍等...")) {
			label.setBackground(Color.red);

		} else {
			if (!cont.isHasWarn()) {
				label.setBackground(null);
			}
		}
		displayUnit.updateUI();
		displayPanel.repaint();
		displayPanel.updateUI();
	}

	public void nurseState(Container cont) {
		JPanel displayUnit = cont.getDisplayUnit();

		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));

		jLabel.setBounds(20, 20, 70, 170);
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/nurse.png")));

		label.setText("请稍等...");
		label.setBackground(null);

		displayUnit.updateUI();
		displayPanel.repaint();
		displayPanel.updateUI();
	}

	public void removeState(Container cont) {

		displayPanel.remove(cont.getDisplayUnit());
		displayPanel.validate();
		displayPanel.updateUI();
		displayUnits.remove(cont.getDisplayUnit());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = "【时间：" + formatter.format(curDate) + "】";
		String info = "" + cont.getBedNum() + "号床结束输液。";
	}

	public boolean isNeedWarn(Container cont) {
		boolean need = false;
//		if (cont.getWaterLevel() < 2 && cont.getState().size() > 15) {
			if (cont.getWaterLevel() < 2) {
			ArrayList<Integer> arrayTmp = cont.getState();
			if(!arrayTmp.isEmpty() && arrayTmp.size() > 5 ){
				if (arrayTmp.get(arrayTmp.size() - 2) < 2 && arrayTmp.get(arrayTmp.size() - 3) < 2) {
					// if (arrayTmp.get(arrayTmp.size() - 2) == 0) {
					need = true;
				}				
			}
		}
		return need;

	}

	public void addWarn(Container cont) {
		// 添加提醒
		boolean ct = isNeedWarn(cont);
//		if (ct && cont.getState().size() > 15) {
			if (ct) {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String time = "【时间：" + formatter.format(curDate) + "】";

			String info = "" + cont.getBedNum() + "号床即将输液完毕，已发送提醒。";
			// try {
			// URL uri = this.getClass().getResource("transfusion/log.txt");
			// File logFile = new File(uri.toURI().getPath());
			// BufferedWriter bw = new BufferedWriter(new FileWriter(logFile,
			// true));
			// bw.write(time + info);
			// bw.newLine();
			// bw.flush();
			// bw.close();
			// } catch (IOException | URISyntaxException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			cont.setHasWarn(true);
			JPanel taskUnit = cont.getTaskUnit();
			taskUnit.setSize(390, 50);
			TitledBorder tb = BorderFactory.createTitledBorder("");
			taskUnit.setBorder(tb);
			taskUnit.setLayout(null);
			taskUnit.setPreferredSize(new Dimension(390, 50));// 这是关键的2句

			taskUnit.add(new Label());
			Label label = ((Label) (taskUnit.getComponent(0)));
			label.setBounds(20, 10, 200, 30);
			label.setAlignment(1);
			label.setFont(font);
			if (taskUnits.size() < 3) {
				label.setText("" + cont.getBedNum() + "号床即将输液完毕" + "");
				label.setBackground(Color.orange);

				Runnable doWorkRunnable = new Runnable() {
					public void run() {

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String time = "【时间：" + formatter.format(curDate) + "】";

						URL codebase = null;
						codebase = this.getClass().getResource("transfusion/haha.wav");
						AudioClip audio1 = Applet.newAudioClip(codebase);
						// audio1.loop();
						audio1.play();

						JOptionPane.showMessageDialog(null, cont.getBedNum() + "号床即将输液完毕!!!\n" + time, "提醒窗口",
								JOptionPane.INFORMATION_MESSAGE);

						audio1.stop();

						// JDialog jDialog =new JDialog(f,"aaa",);
						// java.awt.Container container =
						// jDialog.getContentPane();
						// container.add(new JLabel("这是一个对话框"));
						// jDialog.setSize(300, 100);
						// jDialog.setLocationRelativeTo(null);
						// jDialog.setVisible(true);
						// jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						//// jDialog.setVisible(false);
						// JLabel label = new JLabel("test");
						// label.setBounds(20, 20, 50, 50);
						// JPanel pan=new JPanel();
						// pan.setLayout(null);
						// pan.setBounds(0, 0, 100, 100);
						// pan.add(label);
						// jDialog.add(pan);
						// jDialog.setLayout(null);
						// jDialog.setVisible(true);

						// JDialog dialog = new JDialog();
						// JPanel panel = new JPanel(new
						// FlowLayout(FlowLayout.LEFT));
						// panel.setBorder(BorderFactory.createEmptyBorder(10,
						// 10, 0, 0));
						// panel.setSize(100, 100);
						// JTextField textfield = new JTextField(8);
						// textfield.setText("111111");
						// textfield.setBounds(10, 10, 40, 20);
						// panel.add(textfield);
						// dialog.add(panel);
						// dialog.setSize(200, 100);
						// dialog.setLocationRelativeTo(null);
						// dialog.setVisible(true);
						//
						//
						// try {
						// Thread.sleep(3000);
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
						//
						// jDialog.dispose();

					}
				};
				SwingUtilities.invokeLater(doWorkRunnable);

			} else {
				label.setText("" + cont.getBedNum() + "号床即将输液完毕");
				label.setBackground(Color.white);
			}

			taskUnit.add(new Button("已处理"));
			Button button1 = ((Button) (taskUnit.getComponent(1)));
			button1.setBounds(255, 10, 50, 30);
			button1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub

					Object[] options = { "确定", "取消" };
					int result = JOptionPane.showOptionDialog(null, "         是否确定已处理？", "提醒",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (result == 0) {

						cont.setHasWarn(false);
						cont.setPresent(false);
						// taskPanel.remove(taskUnit);
						// taskPanel.validate();
						// taskUnits.remove(taskUnit);
						// for (JPanel jpTmp : taskUnits) {
						// if (taskUnits.indexOf(jpTmp) < 3) {
						// Label labelTmp = (Label) (jpTmp.getComponent(0));
						// labelTmp.setText("" + labelTmp.getText() + "");
						// labelTmp.setBackground(Color.orange);
						// }
						// }
						removeState(cont);
						removeWarn(cont);
						// Label labelTmp = (Label) (taskUnit.getComponent(0));
						// String[] strTmp = labelTmp.getText().split("号");
						// for (JPanel jpTmp : displayUnits) {
						// TitledBorder tb = (TitledBorder) (jpTmp.getBorder());
						// if (tb.getTitle().startsWith(strTmp[0])) {
						// displayPanel.remove(jpTmp);
						// displayPanel.validate();
						// displayPanel.updateUI();
						// displayUnits.remove(jpTmp);
						// break;
						// }
						// }
						// System.out.println(strTmp[0]);

					}

				}
			});

			for (JPanel jpTmp : taskUnits) {
				if (taskUnits.indexOf(jpTmp) < 3) {
					Label labelTmp = (Label) (jpTmp.getComponent(0));
					labelTmp.setText("" + labelTmp.getText() + "");
					labelTmp.setBackground(Color.orange);
				}
			}

			taskUnit.add(new Button("忽略"));
			Button button2 = ((Button) (taskUnit.getComponent(2)));
			button2.setBounds(315, 10, 50, 30);
			button2.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					cont.setHasWarn(false);
					taskPanel.remove(taskUnit);
					taskPanel.validate();
					taskUnits.remove(taskUnit);
					for (JPanel jpTmp : taskUnits) {
						if (taskUnits.indexOf(jpTmp) < 3) {
							Label labelTmp = (Label) (jpTmp.getComponent(0));
							labelTmp.setText("" + labelTmp.getText() + "");
							labelTmp.setBackground(Color.orange);
						}
					}

				}
			});

			taskUnit.updateUI();
			taskUnit.setVisible(true);
			taskUnits.add(taskUnit);
			taskPanel.add(taskUnit);
			taskPanel.updateUI();

			// Object[] options = { "确认", "取消" };
			// JOptionPane.showMessageDialog(null, "609号床即将输液完毕！", "注意！！！",
			// JOptionPane.DEFAULT_OPTION);
		}
	}

	public void setBedNumAuto(Container cont, String bedNum) {
		JPanel jpTmp = cont.getDisplayUnit();
		TitledBorder tbtmp = null;
		TitledBorder tb = (TitledBorder) jpTmp.getBorder();
		// System.out.println(bedNum);

		tbtmp = BorderFactory.createTitledBorder("" + bedNum + "号床");
		JLabel unknown = ((JLabel) (jpTmp.getComponent(3)));
		unknown.setIcon(null);
		cont.setBedNum(bedNum);
		jpTmp.setBorder(tbtmp);
	}

	public void removeWarn(Container cont) {

		cont.setHasWarn(false);
		taskPanel.remove(cont.getTaskUnit());
		taskPanel.validate();
		taskPanel.updateUI();
		taskUnits.remove(cont.getTaskUnit());
		for (JPanel jpTmp : taskUnits) {
			if (taskUnits.indexOf(jpTmp) < 3) {
				Label labelTmp = (Label) (jpTmp.getComponent(0));
				labelTmp.setText("" + labelTmp.getText() + "");
				labelTmp.setBackground(Color.orange);
			}
		}
		// removeState(cont);
		// Label labelTmp = (Label) (cont.getTaskUnit().getComponent(0));
		// String[] strTmp = labelTmp.getText().split("号");
		// for (JPanel jpTmp : displayUnits) {
		// TitledBorder tb = (TitledBorder) (jpTmp.getBorder());
		// if (tb.getTitle().startsWith(strTmp[0])) {
		// displayPanel.remove(jpTmp);
		// displayPanel.validate();
		// displayPanel.updateUI();
		// displayUnits.remove(jpTmp);
		// break;
		// }
		// }
		// System.out.println(strTmp[0]);

	}

}
