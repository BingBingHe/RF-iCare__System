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

	final JFrame f = new JFrame("RF-ICare������������Һϵͳ  V4.0");
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
		Font fTmp = new Font("����", Font.BOLD, 20);

		displayPanel[0] = new JPanel();
		displayPanel[1] = new JPanel();
		displayJSP[0] = new JScrollPane();
		displayJSP[1] = new JScrollPane();

		/////////////

		displayPanel[0].setSize(500, 650);
		displayPanel[0].setLayout(new FlowLayout(FlowLayout.LEFT, 2, 4));

		// �����Ӹ�������
		displayJSP[0].setViewportView(displayPanel[0]);
		displayJSP[0].setLocation(480, 20);
		displayJSP[0].setSize(360, 580);
		TitledBorder tbTmp = new TitledBorder("һ��");

		tbTmp.setTitleFont(fTmp);
		tbTmp.setTitleColor(Color.GRAY);
		tbTmp.setTitleJustification(TitledBorder.CENTER);

		displayJSP[0].setBorder(tbTmp);
		displayJSP[0].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		displayJSP[0].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel[0].setPreferredSize(new Dimension(360, 2000));// ���ǹؼ���2��
		displayJSP[0].setPreferredSize(new Dimension(360, 20000));

		displayPanel[1].setSize(500, 650);
		displayPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT, 2, 4));

		// �����Ӹ�������
		displayJSP[1].setViewportView(displayPanel[1]);
		displayJSP[1].setLocation(840, 20);
		displayJSP[1].setSize(360, 580);
		TitledBorder tbTmp2 = new TitledBorder("����");

		tbTmp2.setTitleFont(fTmp);
		tbTmp2.setTitleColor(Color.GRAY);
		tbTmp2.setTitleJustification(TitledBorder.CENTER);

		displayJSP[1].setBorder(tbTmp2);
		displayJSP[1].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		displayJSP[1].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel[1].setPreferredSize(new Dimension(360, 2000));// ���ǹؼ���2��
		displayJSP[1].setPreferredSize(new Dimension(360, 20000));

		// �����������Ѷ���
		// ���
		taskPanel.setSize(660, 450);
		taskPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// �����Ӹ�������
		taskJSP.setViewportView(taskPanel);
		taskJSP.setLocation(20, 150);
		taskJSP.setSize(450, 450);

		TitledBorder tbTmpTask = new TitledBorder("�������Ѵ���");
		tbTmpTask.setTitleFont(fTmp);
		tbTmpTask.setTitleColor(Color.GRAY);
		tbTmpTask.setTitleJustification(TitledBorder.CENTER);
		taskJSP.setBorder(tbTmpTask);

		taskJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		taskJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskPanel.setPreferredSize(new Dimension(400, 2000));// ���ǹؼ���2��
		taskJSP.setPreferredSize(new Dimension(400, 20000));

		Button openLog = new Button("�����ӳ�");
		openLog.setBounds(370, 110, 100, 30);
		openLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String inputValue = JOptionPane.showInputDialog(null, "���뱨����Ҫ�ӳٵ�ʱ�䣨�룩",
						"�����ӳ�(��ǰ��" + start.delayNow + "��)", JOptionPane.DEFAULT_OPTION);
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
		systemName.setText("RF-iCare������������Һϵͳ");

		sign.setIcon(new ImageIcon(this.getClass().getResource("transfusion/signRED.png")));
		sign.setBounds(170, 90, 20, 20);

		indication.setBounds(200, 88, 150, 30);
		indication.setAlignment(0);
		Font fontIndication = new Font("Georgia", Font.PLAIN, 15);
		indication.setFont(fontIndication);
		indication.setText("ϵͳ���ڳ�ʼ��");

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
		indication.setText("ϵͳ���ڳ�ʼ��");
	}

	public void detectingSign(int count) {

		sign.setIcon(new ImageIcon(this.getClass().getResource("transfusion/signGREEN.png")));
		String drops = "";
		for (int i = 0; i < count; i++) {
			drops = drops.concat("��");
		}
		indication.setText("ϵͳ���������� " + drops);
	}

	public void addState(Container cont) {
		// ���״̬
		JPanel displayUnit = cont.getDisplayUnit();
		displayUnit.setSize(160, 150);
		setBedNumAuto(cont, cont.getBedNum());
		displayUnit.setLayout(null);
		displayUnit.setPreferredSize(new Dimension(160, 150));// ���ǹؼ���2��

		displayUnit.add(new JLabel());
		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
		jLabel.setBounds(20, 20, 50, 90);

		displayUnit.add(new Label());
		Label label = ((Label) (displayUnit.getComponent(1)));
		label.setBounds(70, 35, 70, 22);
		label.setAlignment(1);
		label.setFont(font);
		label.setText("��ȫˮλ");

		displayUnit.add(new Button("���ô���"));
		Button button2 = ((Button) (displayUnit.getComponent(2)));
		button2.setBounds(30, 120, 80, 20);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String inputValue = JOptionPane.showInputDialog(null, "���뵱ǰ��Һƿ����λ��", "���ô���",
						JOptionPane.DEFAULT_OPTION);
				if (inputValue != null) {
					cont.setBedNum(inputValue);
					setBedNumAuto(cont, inputValue);
				}
			}
		});

		// setBedNumAuto(cont, cont.getArea() + "��");

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

	// ���ϸ���״̬
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

		if (ct && cont.getState().size() > 5 || cont.isHasWarn() && label.getText().equals("���Ե�...")) {
			jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/empty.png")));
			label.setText("����ˮλ");
			label.setBackground(Color.red);
			allert.setIcon(new ImageIcon(this.getClass().getResource("transfusion/Allert.png")));

		} else {
			if (!cont.isHasWarn()) {
				jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
				label.setText("��ȫˮλ");
				label.setBackground(null);
				allert.setIcon(null);
			}
		}
		displayUnit.updateUI();

		int area = cont.getArea() - 1;
		displayPanel[area].repaint();
		displayPanel[area].updateUI();
	}

	// ��ʿ״̬
	public void nurseState(Container cont) {
		JPanel displayUnit = cont.getDisplayUnit();

		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		Label label = ((Label) (displayUnit.getComponent(1)));
		Button button3 = ((Button) (displayUnit.getComponent(4)));
		button3.setBounds(115, 120, 20, 20);

		jLabel.setBounds(10, 30, 50, 90);
		jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/nurse.png")));

		label.setText("���Ե�...");
		label.setBackground(null);

		displayUnit.updateUI();
		int area = cont.getArea() - 1;
		displayPanel[area].repaint();
		displayPanel[area].updateUI();
	}

	// ����ʾ��Ԫɾ����
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

	// �������
	public void addWarn(Container cont) {
		boolean ct = cont.getNeedWarn();
		if (ct && cont.getState().size() > 5) {
			cont.setHasWarn(true);
			JPanel taskUnit = cont.getTaskUnit();
			taskUnit.setSize(410, 50);
			TitledBorder tb = BorderFactory.createTitledBorder("");
			taskUnit.setBorder(tb);
			taskUnit.setLayout(null);
			taskUnit.setPreferredSize(new Dimension(410, 50));// ���ǹؼ���2��

			taskUnit.add(new Label());
			Label label = ((Label) (taskUnit.getComponent(0)));
			label.setBounds(20, 10, 200, 30);
			label.setAlignment(1);
			label.setFont(font);

			String warn = cont.getArea() + "���о�����";
			if (!cont.getBedNum().equals("δ֪")) {
				warn = "" + cont.getBedNum() + "�ż�����Һ���" + "";
			}

			if (taskUnits.size() < 3) {
				label.setText(warn);
				label.setBackground(Color.orange);

				Runnable doWorkRunnable = new Runnable() {
					public void run() {

						String warn = cont.getArea() + "���о�����";
						if (!cont.getBedNum().equals("δ֪")) {
							warn = "" + cont.getBedNum() + "�ż�����Һ���" + "";
						}
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
						String time = "��ʱ�䣺" + formatter.format(curDate) + "��";

						URL codebase = null;
						codebase = this.getClass().getResource("transfusion/haha.wav");
						AudioClip audio1 = Applet.newAudioClip(codebase);
						audio1.play();

						JOptionPane.showMessageDialog(null, warn + "\n" + time, "���Ѵ���",
								JOptionPane.INFORMATION_MESSAGE);

						audio1.stop();
					}
				};
				SwingUtilities.invokeLater(doWorkRunnable);
			} else {
				label.setText(warn);
				label.setBackground(Color.white);
			}

			taskUnit.add(new Button("�Ѵ���"));
			Button button1 = ((Button) (taskUnit.getComponent(1)));
			button1.setBounds(255, 10, 80, 30);
			button1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Object[] options = { "ȷ��", "ȡ��" };
					int result = JOptionPane.showOptionDialog(null, "         �Ƿ�ȷ���Ѵ���", "����",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
					if (result == 0) {
						cont.setHasWarn(false);
						cont.setPresent(false);
						removeState(cont);
						removeWarn(cont);
					}
				}
			});

			taskUnit.add(new Button("����"));
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

	// ���ô���
	public void setBedNumAuto(Container cont, String bedNum) {
		cont.setBedNum(bedNum);

		TitledBorder tbtmp = null;
		if (cont.getBedNum().equals("δ֪")) {
			tbtmp = BorderFactory.createTitledBorder(cont.getArea() + "��");
		} else {
			tbtmp = BorderFactory.createTitledBorder("" + bedNum + "��");
		}

		JPanel jpTmp = cont.getDisplayUnit();
		jpTmp.setBorder(tbtmp);
	}

	// ��taskPanel�е����Ѹ�ɾ����
	public void removeWarn(Container cont) {

		cont.setHasWarn(false);
		taskPanel.remove(cont.getTaskUnit());
		taskPanel.validate();
		taskPanel.updateUI();
		taskUnits.remove(cont.getTaskUnit());
		// ��֤ǰ��������������񣬱���Ϊ��ɫ��
		for (JPanel jpTmp : taskUnits) {
			if (taskUnits.indexOf(jpTmp) < 3) {
				Label labelTmp = (Label) (jpTmp.getComponent(0));
				labelTmp.setText("" + labelTmp.getText() + "");
				labelTmp.setBackground(Color.orange);
			}
		}
	}

}
