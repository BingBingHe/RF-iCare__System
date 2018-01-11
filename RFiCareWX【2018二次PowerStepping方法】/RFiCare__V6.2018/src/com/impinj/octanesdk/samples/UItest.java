package com.impinj.octanesdk.samples;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class UItest {

	final JFrame f = new JFrame("RF-ICare（尔福康）输液系统  V4.0");
	final JPanel[] displayPanel = new JPanel[2];
	final JScrollPane[] displayJSP = new JScrollPane[2];
	final JPanel taskPanel = new JPanel();
	final JScrollPane taskJSP = new JScrollPane();
	final Font font = new Font("Georgia", Font.BOLD, 15);
	final ArrayList<JPanel> displayUnits = new ArrayList<>();
	final ArrayList<JPanel> taskUnits = new ArrayList<>();
	final JLabel sign = new JLabel();
	final Label indication = new Label();

	public void init() {

		Toolkit t = Toolkit.getDefaultToolkit();
		try {
			f.setIconImage(t.getImage(this.getClass().getResource("transfusion/hospital.png").toURI().getPath()));
		} catch (URISyntaxException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		f.setBounds(40, 5, 1210, 640);
		f.setLayout(null);
		Font fTmp = new Font("宋体", Font.BOLD, 20);

		displayPanel[0] = new JPanel();
		displayPanel[1] = new JPanel();
		displayJSP[0] = new JScrollPane();
		displayJSP[1] = new JScrollPane();

		/////////////

		displayPanel[0].setSize(500, 650);
		displayPanel[0].setLayout(new FlowLayout(FlowLayout.LEFT, 2, 4));

		// 给面板加个滚动条
		displayJSP[0].setViewportView(displayPanel[0]);
		displayJSP[0].setLocation(480, 20);
		displayJSP[0].setSize(360, 580);
		TitledBorder tbTmp = new TitledBorder("一区");

		tbTmp.setTitleFont(fTmp);
		tbTmp.setTitleColor(Color.GRAY);
		tbTmp.setTitleJustification(TitledBorder.CENTER);

		displayJSP[0].setBorder(tbTmp);
		displayJSP[0].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		displayJSP[0].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel[0].setPreferredSize(new Dimension(360, 2000));// 这是关键的2句
		displayJSP[0].setPreferredSize(new Dimension(360, 20000));

		displayPanel[1].setSize(500, 650);
		displayPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT, 2, 4));

		// 给面板加个滚动条
		displayJSP[1].setViewportView(displayPanel[1]);
		displayJSP[1].setLocation(840, 20);
		displayJSP[1].setSize(360, 580);
		TitledBorder tbTmp2 = new TitledBorder("二区");

		tbTmp2.setTitleFont(fTmp);
		tbTmp2.setTitleColor(Color.GRAY);
		tbTmp2.setTitleJustification(TitledBorder.CENTER);

		displayJSP[1].setBorder(tbTmp2);
		displayJSP[1].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		displayJSP[1].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel[1].setPreferredSize(new Dimension(360, 2000));// 这是关键的2句
		displayJSP[1].setPreferredSize(new Dimension(360, 20000));

		// 增加任务提醒队列
		// 面板
		taskPanel.setSize(660, 450);
		taskPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// 给面板加个滚动条
		taskJSP.setViewportView(taskPanel);
		taskJSP.setLocation(20, 150);
		taskJSP.setSize(450, 450);

		TitledBorder tbTmpTask = new TitledBorder("任务提醒窗口");
		tbTmpTask.setTitleFont(fTmp);
		tbTmpTask.setTitleColor(Color.GRAY);
		tbTmpTask.setTitleJustification(TitledBorder.CENTER);
		taskJSP.setBorder(tbTmpTask);

		taskJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		taskJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskPanel.setPreferredSize(new Dimension(400, 2000));// 这是关键的2句
		taskJSP.setPreferredSize(new Dimension(400, 20000));

		Button openLog = new Button("设置延迟");
		openLog.setBounds(370, 110, 100, 30);
		openLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String inputValue = JOptionPane.showInputDialog(null, "输入报警需要延迟的时间（秒）",
						"设置延迟(当前：" + start.delayNow + "秒)", JOptionPane.DEFAULT_OPTION);
				if (inputValue == null) {
					for (Container cont : Reader.containerList) {
						cont.setDelay(start.delay);
					}
				} else {
					if (isNumeric(inputValue)) {
						for (Container cont : Reader.containerList) {
							cont.setDelay(Integer.parseInt(inputValue));
						}
						start.delayNow = Integer.parseInt(inputValue);
					}
				}
			}
		});

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("transfusion/logo.png")));
		logo.setBounds(50, 20, 130, 120);

		Label systemName = new Label();
		systemName.setBounds(170, 40, 300, 30);
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
		f.add(displayJSP[0]);
		f.add(displayJSP[1]);
		f.add(taskJSP);
		f.add(openLog);
		f.add(sign);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				start.rd.closeReader();
				System.exit(0);
			}
		});

		f.setVisible(true);
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
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
		displayUnit.setSize(160, 150);
		setBedNumAuto(cont, cont.getBedNum());
		displayUnit.setLayout(null);
		displayUnit.setPreferredSize(new Dimension(160, 150));// 这是关键的2句

		displayUnit.add(new JLabel());
		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
		jLabel.setBounds(20, 20, 50, 90);

		displayUnit.add(new Label());
		Label label = ((Label) (displayUnit.getComponent(1)));
		label.setBounds(70, 35, 70, 22);
		label.setAlignment(1);
		label.setFont(font);
		label.setText("安全水位");

		displayUnit.add(new Button("设置床号"));
		Button button2 = ((Button) (displayUnit.getComponent(2)));
		button2.setBounds(30, 120, 80, 20);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String inputValue = JOptionPane.showInputDialog(null, "输入当前输液瓶所处位置", "设置床号",
						JOptionPane.DEFAULT_OPTION);
				if (inputValue != null) {
					cont.setBedNum(inputValue);
					setBedNumAuto(cont, inputValue);
				}
			}
		});

		// setBedNumAuto(cont, cont.getArea() + "区");

		displayUnit.add(new JLabel());
		JLabel allert = ((JLabel) (displayUnit.getComponent(3)));
		allert.setBounds(60, 50, 50, 50);

		displayUnit.add(new Button("x"));
		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(115, 120, 20, 20);
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cont.setPresent(false);
				removeState(cont);
				removeWarn(cont);
			}
		});

		displayUnit.updateUI();
		displayUnits.add(displayUnit);
		int area = cont.getArea() - 1;
		displayPanel[area].add(displayUnit);
		displayPanel[area].repaint();
		displayPanel[area].updateUI();
	}

	// 不断更新状态
	public void changeState(Container cont) {

		JPanel displayUnit = cont.getDisplayUnit();
		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));
		JLabel allert = ((JLabel) (displayUnit.getComponent(3)));
		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(115, 120, 20, 20);
		jLabel.setBounds(20, 20, 50, 90);

		boolean ct = cont.isNeedWarn();
		cont.setNeedWarn(ct);

		if (ct && cont.getState().size() > 5 || cont.isHasWarn() && label.getText().equals("请稍等...")) {
			jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/empty.png")));
			label.setText("警戒水位");
			label.setBackground(Color.red);
			allert.setIcon(new ImageIcon(this.getClass().getResource("transfusion/Allert.png")));

		} else {
			if (!cont.isHasWarn()) {
				jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
				label.setText("安全水位");
				label.setBackground(null);
				allert.setIcon(null);
			}
		}
		displayUnit.updateUI();

		int area = cont.getArea() - 1;
		displayPanel[area].repaint();
		displayPanel[area].updateUI();
	}

	// 护士状态
	public void nurseState(Container cont) {
		JPanel displayUnit = cont.getDisplayUnit();

		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));
		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(115, 120, 20, 20);

		jLabel.setBounds(10, 30, 50, 90);
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/nurse.png")));

		label.setText("请稍等...");
		label.setBackground(null);

		displayUnit.updateUI();
		int area = cont.getArea() - 1;
		displayPanel[area].repaint();
		displayPanel[area].updateUI();
	}

	// 把显示单元删除掉
	public void removeState(Container cont) {

		JPanel displayUnit = cont.getDisplayUnit();

		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));
		JLabel allert = ((JLabel) (displayUnit.getComponent(3)));

		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(115, 10, 0, 0);

		jLabel.setIcon(null);

		label.setText(null);
		label.setBackground(null);
		allert.setIcon(null);
		int area = cont.getArea() - 1;
		displayPanel[area].remove(cont.getDisplayUnit());
		displayPanel[area].validate();
		displayPanel[area].updateUI();
		displayUnits.remove(cont.getDisplayUnit());
		cont.reset();

	}

	// 添加提醒
	public void addWarn(Container cont) {
		boolean ct = cont.getNeedWarn();
		if (ct && cont.getState().size() > 5) {
			cont.setHasWarn(true);
			JPanel taskUnit = cont.getTaskUnit();
			taskUnit.setSize(410, 50);
			TitledBorder tb = BorderFactory.createTitledBorder("");
			taskUnit.setBorder(tb);
			taskUnit.setLayout(null);
			taskUnit.setPreferredSize(new Dimension(410, 50));// 这是关键的2句

			taskUnit.add(new Label());
			Label label = ((Label) (taskUnit.getComponent(0)));
			label.setBounds(20, 10, 200, 30);
			label.setAlignment(1);
			label.setFont(font);

			String warn = cont.getArea() + "区有警报！";
			if (!cont.getBedNum().equals("未知")) {
				warn = "" + cont.getBedNum() + "号即将输液完毕" + "";
			}

			if (taskUnits.size() < 3) {
				label.setText(warn);
				label.setBackground(Color.orange);

				Runnable doWorkRunnable = new Runnable() {
					public void run() {

						String warn = cont.getArea() + "区有警报！";
						if (!cont.getBedNum().equals("未知")) {
							warn = "" + cont.getBedNum() + "号即将输液完毕" + "";
						}
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String time = "【时间：" + formatter.format(curDate) + "】";

						URL codebase = null;
						codebase = this.getClass().getResource("transfusion/haha.wav");
						AudioClip audio1 = Applet.newAudioClip(codebase);
						audio1.play();

						JOptionPane.showMessageDialog(null, warn + "\n" + time, "提醒窗口",
								JOptionPane.INFORMATION_MESSAGE);

						audio1.stop();
					}
				};
				SwingUtilities.invokeLater(doWorkRunnable);
			} else {
				label.setText(warn);
				label.setBackground(Color.white);
			}

			taskUnit.add(new Button("已处理"));
			Button button1 = ((Button) (taskUnit.getComponent(1)));
			button1.setBounds(255, 10, 80, 30);
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
						removeState(cont);
						removeWarn(cont);
					}
				}
			});

			taskUnit.add(new Button("忽略"));
			Button button2 = ((Button) (taskUnit.getComponent(2)));
			button2.setBounds(330, 10, 60, 30);
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

			for (JPanel jpTmp : taskUnits) {
				if (taskUnits.indexOf(jpTmp) < 3) {
					Label labelTmp = (Label) (jpTmp.getComponent(0));
					labelTmp.setText("" + labelTmp.getText() + "");
					labelTmp.setBackground(Color.orange);
				}
			}

			taskUnit.updateUI();
			taskUnit.setVisible(true);
			taskUnits.add(taskUnit);
			taskPanel.add(taskUnit);
			taskPanel.updateUI();
		}
	}

	// 设置床号
	public void setBedNumAuto(Container cont, String bedNum) {
		cont.setBedNum(bedNum);

		TitledBorder tbtmp = null;
		if (cont.getBedNum().equals("未知")) {
			tbtmp = BorderFactory.createTitledBorder(cont.getArea() + "区");
		} else {
			tbtmp = BorderFactory.createTitledBorder("" + bedNum + "号");
		}

		JPanel jpTmp = cont.getDisplayUnit();
		jpTmp.setBorder(tbtmp);
	}

	// 把taskPanel中的提醒给删除掉
	public void removeWarn(Container cont) {

		cont.setHasWarn(false);
		taskPanel.remove(cont.getTaskUnit());
		taskPanel.validate();
		taskPanel.updateUI();
		taskUnits.remove(cont.getTaskUnit());
		// 保证前三个待处理的任务，背景为橘色。
		for (JPanel jpTmp : taskUnits) {
			if (taskUnits.indexOf(jpTmp) < 3) {
				Label labelTmp = (Label) (jpTmp.getComponent(0));
				labelTmp.setText("" + labelTmp.getText() + "");
				labelTmp.setBackground(Color.orange);
			}
		}
	}

}
