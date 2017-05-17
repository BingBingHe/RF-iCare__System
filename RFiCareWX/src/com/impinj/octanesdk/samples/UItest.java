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

	final JFrame f = new JFrame("RF-ICare������������Һϵͳ  V1.1");
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

		// ���
		displayPanel.setSize(660, 650);
		displayPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// �����Ӹ�������
		displayJSP.setViewportView(displayPanel);
		displayJSP.setLocation(480, 20);
		displayJSP.setSize(660, 650);
		TitledBorder tbTmp = new TitledBorder("��Һ�����ʾ");
		Font fTmp = new Font("����", Font.BOLD, 20);
		tbTmp.setTitleFont(fTmp);
		tbTmp.setTitleColor(Color.GRAY);
		tbTmp.setTitleJustification(TitledBorder.CENTER);

		displayJSP.setBorder(tbTmp);

		displayJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		displayJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		displayPanel.setPreferredSize(new Dimension(650, 2000));// ���ǹؼ���2��
		displayJSP.setPreferredSize(new Dimension(650, 20000));

		// �����������Ѷ���
		// ���
		taskPanel.setSize(660, 650);
		taskPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		// �����Ӹ�������
		taskJSP.setViewportView(taskPanel);
		taskJSP.setLocation(20, 150);
		taskJSP.setSize(430, 470);

		TitledBorder tbTmp2 = new TitledBorder("�������Ѵ���");
		tbTmp2.setTitleFont(fTmp);
		tbTmp2.setTitleColor(Color.GRAY);
		tbTmp2.setTitleJustification(TitledBorder.CENTER);
		taskJSP.setBorder(tbTmp2);

		taskJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		taskJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		taskPanel.setPreferredSize(new Dimension(400, 2000));// ���ǹؼ���2��
		taskJSP.setPreferredSize(new Dimension(400, 20000));

		Button openLog = new Button("����־�ļ�");
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
		displayUnit.setSize(206, 200);
		TitledBorder tb = BorderFactory.createTitledBorder("�����ô���");
		displayUnit.setBorder(tb);
		displayUnit.setLayout(null);
		displayUnit.setPreferredSize(new Dimension(206, 200));// ���ǹؼ���2��

		displayUnit.add(new JLabel());
		((JLabel) (displayUnit.getComponent(0)))
				.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
		JLabel jLabel = ((JLabel) (displayUnit.getComponent(0)));
		jLabel.setBounds(0, 20, 105, 170);

		displayUnit.add(new Label());
		Label label = ((Label) (displayUnit.getComponent(1)));
		label.setBounds(115, 40, 70, 30);
		label.setAlignment(1);
		label.setFont(font);
		if (cont.getWaterLevel() == 0) {
			label.setText("����ˮλ");
			label.setBackground(Color.red);
		} else {
			label.setText("��ȫˮλ");
			label.setBackground(null);

		}

		// displayUnit.add(new Button("�鿴ҩƷ����"));
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
		// // Object[] options = { "ȷ��", "ȡ��" };
		// JOptionPane.showMessageDialog(null, "��ҩƷ���ơ�ͨ�����ƣ���Ī��ƽƬ
		// \n��Ӣ�����ơ�Nimodpin Tablets \n������ƴ����Nimodiping Pian",
		// "ҩƷ����", JOptionPane.DEFAULT_OPTION);
		// }
		// });

		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		String time = "ʱ�䣺" + formatter.format(curDate) + "��";

		displayUnit.add(new Button("���ô���"));
		Button button2 = ((Button) (displayUnit.getComponent(2)));
		button2.setBounds(110, 140, 80, 30);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				String inputValue = JOptionPane.showInputDialog(null, "���뵱ǰ��Һƿ�������š�����" + time, "���ô���",
						JOptionPane.DEFAULT_OPTION);
				TitledBorder tbtmp = null;
				TitledBorder tb = (TitledBorder) displayUnit.getBorder();
				Character bedNum = tb.getTitle().charAt(0);
				// System.out.println(bedNum);
				if (inputValue == null) {
					if (!Character.isDigit(bedNum)) {
						System.out.println("1111213");
						tbtmp = BorderFactory.createTitledBorder("�����ô���");
						JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
						unknown.setIcon(new ImageIcon(this.getClass().getResource("transfusion/unknown.png")));
					} else {
						tbtmp = BorderFactory.createTitledBorder(tb.getTitle());
						JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
						unknown.setIcon(null);
					}
				} else {
					tbtmp = BorderFactory.createTitledBorder("" + inputValue + "�Ŵ�");
					JLabel unknown = ((JLabel) (displayUnit.getComponent(3)));
					unknown.setIcon(null);
					cont.setBedNum(inputValue);
				}

				displayUnit.setBorder(tbtmp);

				// String info = "" + cont.getBedNum() + "�Ŵ���ʼ��Һ��";

				// try {
				// URL uri = this.getClass().getResource("transfusion/log.txt");
				// File logFile = new File(uri.toURI().getPath());
				// BufferedWriter bw = new BufferedWriter(new
				// FileWriter(logFile, true));
				// bw.write("��" + time + info);
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

		jLabel.setBounds(0, 20, 105, 170);

		boolean ct = isNeedWarn(cont);

		if (ct && cont.getState().size() > 8 || cont.isHasWarn() && label.getText().equals("���Ե�...")) {
			jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/empty.png")));
			label.setText("����ˮλ");
			label.setBackground(Color.red);

		} else {
			if (!cont.isHasWarn()) {
				jLabel.setIcon(new ImageIcon(this.getClass().getResource("transfusion/full.png")));
				label.setText("��ȫˮλ");
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

		label.setText("���Ե�...");
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

		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
		// Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		// String time = "��ʱ�䣺" + formatter.format(curDate) + "��";
		// String info = "" + cont.getBedNum() + "�Ŵ�������Һ��";
	}

	public boolean isNeedWarn(Container cont) {
		boolean need = false;
		if (cont.getWaterLevel() == 0 && cont.getState().size() > 15) {
			ArrayList<Integer> arrayTmp = cont.getState();
			if (arrayTmp.get(arrayTmp.size() - 2) == 0 && arrayTmp.get(arrayTmp.size() - 3) == 0) {
				// if (arrayTmp.get(arrayTmp.size() - 2) == 0) {
				need = true;
			}
		}
		return need;

	}

	public void addWarn(Container cont) {
		// �������
		boolean ct = isNeedWarn(cont);
		if (ct && cont.getState().size() > 15) {

//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
//			Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��

			// String time = "��ʱ�䣺" + formatter.format(curDate) + "��";
			// String info = "" + cont.getBedNum() + "�Ŵ�������Һ��ϣ��ѷ������ѡ�";
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
			taskUnit.setPreferredSize(new Dimension(390, 50));// ���ǹؼ���2��

			taskUnit.add(new Label());
			Label label = ((Label) (taskUnit.getComponent(0)));
			label.setBounds(20, 10, 200, 30);
			label.setAlignment(1);
			label.setFont(font);
			if (taskUnits.size() < 3) {
				label.setText("" + cont.getBedNum() + "�Ŵ�������Һ���" + "");
				label.setBackground(Color.orange);

				Runnable doWorkRunnable = new Runnable() {
					public void run() {

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
						String time = "��ʱ�䣺" + formatter.format(curDate) + "��";

						URL codebase = null;
						codebase = this.getClass().getResource("transfusion/haha.wav");
						AudioClip audio1 = Applet.newAudioClip(codebase);
						// audio1.loop();
						audio1.play();

						JOptionPane.showMessageDialog(null, cont.getBedNum() + "�Ŵ�������Һ���!!!\n" + time, "���Ѵ���",
								JOptionPane.INFORMATION_MESSAGE);

						audio1.stop();

						// JDialog jDialog =new JDialog(f,"aaa",);
						// java.awt.Container container =
						// jDialog.getContentPane();
						// container.add(new JLabel("����һ���Ի���"));
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
				label.setText("" + cont.getBedNum() + "�Ŵ�������Һ���");
				label.setBackground(Color.white);
			}

			taskUnit.add(new Button("�Ѵ���"));
			Button button1 = ((Button) (taskUnit.getComponent(1)));
			button1.setBounds(255, 10, 50, 30);
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
						// String[] strTmp = labelTmp.getText().split("��");
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

			taskUnit.add(new Button("����"));
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

			// Object[] options = { "ȷ��", "ȡ��" };
			// JOptionPane.showMessageDialog(null, "609�Ŵ�������Һ��ϣ�", "ע�⣡����",
			// JOptionPane.DEFAULT_OPTION);
		}
	}

	public void setBedNumAuto(Container cont, String bedNum) {
		JPanel jpTmp = cont.getDisplayUnit();
		TitledBorder tbtmp = null;
		// TitledBorder tb = (TitledBorder) jpTmp.getBorder();
		// System.out.println(bedNum);

		tbtmp = BorderFactory.createTitledBorder("" + bedNum + "�Ŵ�");
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
		// String[] strTmp = labelTmp.getText().split("��");
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
