package edu.hust.structuretree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.plaf.ColorUIResource;

//import edu.stanford.nlp.ling.CoreAnnotations;
//import edu.stanford.nlp.pipeline.Annotation;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.trees.Tree;
//import edu.stanford.nlp.trees.TreeCoreAnnotations;
//import edu.stanford.nlp.util.CoreMap;

public class Main {	
	private JFrame f = new JFrame("δ����");
	private TreePanel t =new TreePanel();
	private JScrollPane paintjsp;
	private JPanel editPanel = new JPanel();
	private TextArea editArea0= new TextArea(2,80);
	private TextArea editArea= new TextArea(8,80);
	private JButton start = new JButton("���ɽṹ��");
	private JButton treeToText = new JButton("�������ļ�");
	private JButton rePaint = new JButton("�ػ��ṹ��");
	private JButton updateExpression = new JButton("���±��ʽ");
	private JButton parse = new JButton("�䷨����");
	private JButton importExpressionFromText = new JButton("������ʽ");
	private JFileChooser fileChooser = new JFileChooser(); 
	private TxtFileFilter txtFileFilter = new TxtFileFilter() ;
	private Vector<TreePanelNode> nodes = new Vector<TreePanelNode>();
	private ArrayList<TreePanelNode> treeLists = new ArrayList<TreePanelNode>();
	private HashMap<String,Boolean> hasModeified = new HashMap<String,Boolean>();//��¼�ı��Ƿ񱻸ı�
	private TreeAtTxt treeAtTxt = new TreeAtTxt();//��ʾ��ǰ����ϵ����ű��ʽ
	private ArrayList<TreeAtTxt> allTreesAtTxt = new ArrayList<TreeAtTxt>();//��ʾ�����ļ��е����ű��ʽ
	private static int RIGHT = 0,LEFT = -1;//��ʾ���һ���
//	private StanfordCoreNLP pipeline;
//	private Annotation annotation;
	public void init() {
		
//		coreNLPInit();
		treeAtTxt=new TreeAtTxt(treeLists);
		allTreesAtTxt.add(treeAtTxt);
		hasModeified.put(null, Boolean.FALSE);
		t.setHasModeified(hasModeified);
		t.setTreeAtTxt(treeAtTxt);
		t.setNodes(nodes);
		t.setTreeLists(treeLists);
		
		t.setPreferredSize(new Dimension(10000,10000));
		paintjsp=new JScrollPane(t);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(parse);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(start);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(rePaint);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(updateExpression);
		buttonPanel.add(Box.createVerticalStrut(8));
//		buttonPanel.add(importExpressionFromText);	
//		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(treeToText);
		
		//�����˵�
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("�ļ�");
		JMenuItem jmi_new = new JMenuItem("�½�");
		JMenuItem jmi_open = new JMenuItem("��");
		JMenuItem jmi_save = new JMenuItem("����");
		JMenuItem jmi_saveAs = new JMenuItem("���Ϊ");
		jm.add(jmi_new);
		jm.add(jmi_open);
		jm.add(jmi_save);
		jm.add(jmi_saveAs);
		jmb.add(jm);
		
		
		
		JScrollPane editjsp0 = new JScrollPane(editArea0);//����δ����ľ���
		JScrollPane editjsp = new JScrollPane(editArea);//�������ű��ʽ
		JPanel editTextPanel = new JPanel();
		editTextPanel.setLayout(new BorderLayout());
		editTextPanel.add(editjsp);
		editTextPanel.add(editjsp0, BorderLayout.NORTH);
		
		editPanel.setLayout(new BorderLayout());
		editPanel.add(jmb, BorderLayout.NORTH);
		editPanel.add(editTextPanel);		
		editPanel.add(buttonPanel, BorderLayout.EAST);

		
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BoxLayout(functionPanel,BoxLayout.Y_AXIS));
		JButton add = new JButton("����");
		JButton delete = new JButton("ɾ��");
//	    JButton modify = new JButton("�޸�");
		JButton combine = new JButton("����");
		JButton selectRoot = new JButton("root");
		JButton clearPanel = new JButton("���");
		JButton leftTree = new JButton("<����");
		JButton rightTree = new JButton("����>");
		
		
		functionPanel.add(add);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(delete);
		functionPanel.add(Box.createVerticalStrut(8));
//		functionPanel.add(modify);
//		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(combine);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(selectRoot);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(clearPanel);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(leftTree);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(rightTree);
		
		
		t.setAdd(add);
		t.setDelete(delete);
//		t.setModify(modify);
		t.setCombine(combine);
		t.setSelectRoot(selectRoot);
		System.out.println(222222);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(paintjsp);
		panel.add(functionPanel, BorderLayout.WEST);
		panel.add(editPanel, BorderLayout.NORTH);
		
		
		double width = Toolkit.getDefaultToolkit().getScreenSize().width; // �õ���ǰ��Ļ�ֱ��ʵĸ�
		double height = Toolkit.getDefaultToolkit().getScreenSize().height-45;// �õ���ǰ��Ļ�ֱ��ʵĿ�
		f.setSize((int) width, (int) height);// ���ô�С
		f.setLocation(0, 0); // ���ô��������ʾ
		f.setResizable(false);// ������󻯰�ť
		f.setLocationRelativeTo(null);
		f.add(panel);

		f.setJMenuBar(jmb);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

//����
		//Ϊ�ļ��˵���ӵ���¼�
		jmi_new.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int returnValue = JOptionPane.YES_OPTION;
				String currentTxtPath = t.getTreeAtTxt().getTxtPath();
				if (!hasModeified.get(currentTxtPath).booleanValue()) {// txtPath��Ӧ�ļ�û���޸�
					//�½�
					resetTreePanel();
				} else {
					returnValue = JOptionPane.showConfirmDialog(f, "�Ƿ�Ҫ����,��ǰҳ������ݡ�", "ȷ�϶Ի���",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (returnValue == JOptionPane.YES_OPTION) {
						ArrayList<TreePanelNode> treesOfSameTxt = new ArrayList<TreePanelNode>();
						if (t.getTreeLists().size() == 1) {// ��ǰ�����ֻ��һ�����ǲſ��Ա���
							if (currentTxtPath == null) {//�½�����壬����һ��txt��ֻ��һ����
								if (t.getTreeLists().get(0).examTheTree() && t.getTreeLists().get(0) != null) {
									try {
										SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
										if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {//ȷ������
											currentTxtPath = fileChooser.getSelectedFile().toString();
											FileWriter fw = new FileWriter(currentTxtPath);
											for (String word : t.getTreeLists().get(0).changeIntoText())
												fw.write(word);

											hasModeified.remove(null);// ��û������λ�ú��ļ�����ɾ������Ϊ�Ѿ�Ϊ�����浽��ָ���ļ���
											hasModeified.put(currentTxtPath, Boolean.FALSE);
											t.setHasModeified(hasModeified);
											t.getTreeAtTxt().setTxtPath(currentTxtPath);// �޸��ļ���·����֮ǰ��null

											fw.close();
											
											//�½�
											resetTreePanel();
											
										}else {//�����ȡ�����˳��Ի���ʲôҲ������
											
										}
										
										
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								}else {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								}
								
								
							}else {//�޸��˴򿪵��ļ��е�ĳ����
								for (TreeAtTxt tat : allTreesAtTxt) {
									if(tat.getTxtPath().equals(currentTxtPath))
										treesOfSameTxt.add(tat.getTreeListWithOneTree().get(0));
								}
							}
							
							if(!treesOfSameTxt.isEmpty()) {//ִ���������else
								boolean correctFormat = true;
								for (TreePanelNode treeRoot : treesOfSameTxt) {
									if (treeRoot.examTheTree() && treeRoot != null) {
										
									}else {
										correctFormat = false;
										break;
									}
								}
								if(correctFormat == false) {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									try {
										FileWriter fw = new FileWriter(currentTxtPath);
										for (TreePanelNode treeRoot : treesOfSameTxt) {
											for (String word : treeRoot.changeIntoText())
												fw.write(word);
											fw.write("\r\n");
										}
										hasModeified.put(currentTxtPath, Boolean.FALSE);
										t.setHasModeified(hasModeified);
										fw.close();
										//�½�
										resetTreePanel();
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								
							}
							
							
						}else {//��ǰ��岻ֹһ����/����û����
							JOptionPane.showMessageDialog(f, "������ֻ��һ����ʱ���ܱ��档", "��Ϣ��ʾ��",
									JOptionPane.INFORMATION_MESSAGE);
						}

					} else if (returnValue == JOptionPane.NO_OPTION) {
						//�½�
						resetTreePanel();
					} else {//�����ȡ�������˳�
						resetButtonstatus();
						t.initCombineNodes();
						t.setSelectedNodes(-1);
						t.repaint();
					}
				}
				resetButtonstatus();
				t.initCombineNodes();
				t.setSelectedNodes(-1);
				t.repaint();
			}
		});
		
		jmi_open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int returnValue = JOptionPane.YES_OPTION;
				String currentTxtPath = t.getTreeAtTxt().getTxtPath();
				if (!hasModeified.get(currentTxtPath).booleanValue()) {// txtPath��Ӧ�ļ�û���޸�
					//��
					try {
						openTxts();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					returnValue = JOptionPane.showConfirmDialog(f, "�Ƿ�Ҫ����,��ǰҳ������ݡ�", "ȷ�϶Ի���",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (returnValue == JOptionPane.YES_OPTION) {
					
						ArrayList<TreePanelNode> treesOfSameTxt = new ArrayList<TreePanelNode>();
						if (t.getTreeLists().size() == 1) {// ��ǰ�����ֻ��һ�����ǲſ��Ա���
							if (currentTxtPath == null) {//�½�����壬����һ��txt��ֻ��һ����
								if (t.getTreeLists().get(0).examTheTree() && t.getTreeLists().get(0) != null) {
									try {
										SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
										if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {//ȷ������
											currentTxtPath = fileChooser.getSelectedFile().toString();
											FileWriter fw = new FileWriter(currentTxtPath);
											for (String word : t.getTreeLists().get(0).changeIntoText())
												fw.write(word);

											hasModeified.remove(null);// ��û������λ�ú��ļ�����ɾ������Ϊ�Ѿ�Ϊ�����浽��ָ���ļ���
											hasModeified.put(currentTxtPath, Boolean.FALSE);
											t.setHasModeified(hasModeified);
											t.getTreeAtTxt().setTxtPath(currentTxtPath);// �޸��ļ���·����֮ǰ��null
											f.setTitle(new File(currentTxtPath).getName());
											fw.close();
											
											//��
											try {
												openTxts();
											} catch (IOException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											
										}else {//�����ȡ�����˳��Ի���ʲôҲ������
											
										}
										
										
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								}else {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								}
								
								
							}else {//�޸��˴򿪵��ļ��е�ĳ����
								for (TreeAtTxt tat : allTreesAtTxt) {
									if(tat.getTxtPath().equals(currentTxtPath))
										treesOfSameTxt.add(tat.getTreeListWithOneTree().get(0));
								}
							}
							
							if(!treesOfSameTxt.isEmpty()) {//ִ���������else
								boolean correctFormat = true;
								for (TreePanelNode treeRoot : treesOfSameTxt) {
									if (treeRoot.examTheTree() && treeRoot != null) {
										
									}else {
										correctFormat = false;
										break;
									}
								}
								if(correctFormat == false) {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									try {
										FileWriter fw = new FileWriter(currentTxtPath);
										for (TreePanelNode treeRoot : treesOfSameTxt) {
											for (String word : treeRoot.changeIntoText())
												fw.write(word);
											fw.write("\r\n");
										}
										hasModeified.put(currentTxtPath, Boolean.FALSE);
										t.setHasModeified(hasModeified);
										fw.close();
										//��
										try {
											openTxts();
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}							
							}				
						}else {//��ǰ��岻ֹһ����/����û����
							JOptionPane.showMessageDialog(f, "������ֻ��һ����ʱ���ܱ��档", "��Ϣ��ʾ��",
									JOptionPane.INFORMATION_MESSAGE);
						}

					} else if (returnValue == JOptionPane.NO_OPTION) {
						//��
						try {
							openTxts();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {//�����ȡ�������˳�
						
					}
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}});
			
		jmi_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(t.getHasModeified().get(t.getTreeAtTxt().getTxtPath())) {//�ļ����޸Ĺ�
					if (t.getTreeLists().size() != 1) {
						JOptionPane.showMessageDialog(f, "����ʱ�������ֻ��һ����", "��Ϣ��ʾ��", JOptionPane.INFORMATION_MESSAGE);
					} else {//Ҫ����������ֻ��һ����
						if (t.getTreeLists().get(0).examTheTree() && t.getTreeLists().get(0) != null) {//��ʽ��ȷ
							if(t.getTreeAtTxt().getTxtPath() == null ) {//��û��Ϊ��ǰ�ļ�����·��������
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");							
								fileChooser.setSelectedFile(new File(sdf.format(new Date()) + ".txt"));
								if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
									String currentTxtPath = fileChooser.getSelectedFile().toString();
									try {
										FileWriter fw = new FileWriter(currentTxtPath);
										for (String word : t.getTreeLists().get(0).changeIntoText())
											fw.write(word);

										hasModeified.remove(null);// ��û������λ�ú��ļ�����ɾ������Ϊ�Ѿ�Ϊ�����浽��ָ���ļ���
										hasModeified.put(currentTxtPath, Boolean.FALSE);
										t.setHasModeified(hasModeified);
										t.getTreeAtTxt().setTxtPath(currentTxtPath);// �޸��ļ���·����֮ǰ��null
										resetButtonstatus();
										t.setSelectedNodes(-1);
										t.initCombineNodes();
										f.setTitle(fileChooser.getSelectedFile().getName());	
										t.repaint();
										fw.close();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}else {//���ļ��Ǵ����е��ı�
								String currentTxtPath = t.getTreeAtTxt().getTxtPath();
								ArrayList<TreePanelNode> treesOfSameTxt = new ArrayList<TreePanelNode>();
								for (TreeAtTxt tat : allTreesAtTxt) {
									if(tat.getTxtPath().equals(currentTxtPath))
										treesOfSameTxt.add(tat.getTreeListWithOneTree().get(0));
								}
								if(!treesOfSameTxt.isEmpty()) {
									boolean correctFormat = true;
									for (TreePanelNode treeRoot : treesOfSameTxt) {//������ĸ�ʽ�Ƿ���ȷ
										if (treeRoot.examTheTree() && treeRoot != null) {
											
										}else {
											correctFormat = false;
											break;
										}
									}
									if(correctFormat == false) {
										JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
												JOptionPane.INFORMATION_MESSAGE);
									} else {
										try {
											FileWriter fw = new FileWriter(currentTxtPath);
											for (TreePanelNode treeRoot : treesOfSameTxt) {
												for (String word : treeRoot.changeIntoText())
													fw.write(word);
												fw.write("\r\n");
											}
											//�޸����Ĳ���״̬
											hasModeified.put(currentTxtPath, Boolean.FALSE);
											t.setHasModeified(hasModeified);
											resetButtonstatus();
											t.setSelectedNodes(-1);
											t.initCombineNodes();
											t.repaint();
											fw.close();
											
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
									
								}
							}
						} else{//��ʽ����ȷ
							JOptionPane.showMessageDialog(f, "���ĸ�ʽ����ȷ��", "���ĸ�ʽ����ȷ", JOptionPane.INFORMATION_MESSAGE);
							resetButtonstatus();
							t.setSelectedNodes(-1);
							t.initCombineNodes();
							t.repaint();
						}
					}
				}else {//�ļ�û�б��޸�
					//��ʱ��Ϊ���ù�
					resetButtonstatus();
					t.setSelectedNodes(-1);
					t.initCombineNodes();
					t.repaint();
				}
			}

		});
		
		jmi_saveAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeLists = t.getTreeLists();
				nodes = TreePanelNode.nodesOfAllTrees(treeLists);
				int returnValue = JOptionPane.YES_OPTION;
				for (TreePanelNode root : treeLists) {
					if (root.examTheTree() && root != null) {//�ж�ɭ���е����Ƿ񶼷��ϸ�ʽ
						System.out.println(returnValue);
					} else {

						returnValue = JOptionPane.showConfirmDialog(f, "���ű��ʽ��ʽ�д���,�Ƿ�Ҫ���档", "ȷ�϶Ի���",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

						break;
					}

				}

				if (returnValue == JOptionPane.YES_OPTION && treeLists != null && nodes != null && !treeLists.isEmpty() && !nodes.isEmpty()) {
					try {
						int count = 0;
						SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
						System.out.println(date.format(new Date()) + ".txt");
						fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
						if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {// ȷ������						
							String filePath = fileChooser.getSelectedFile().toString();
							FileWriter treeToText = new FileWriter(filePath);
							count = 0;
							for (TreePanelNode root : treeLists) {
								count++;
								if (root.examTheTree() && root != null)
									for (String word : root.changeIntoText())
										treeToText.write(word);
								else
									treeToText.write("��" + count + "�����ű��ʽ��ʽ����");

								treeToText.write("\r\n");
							}
							treeToText.close();
							
						}
						resetButtonstatus();
						t.initCombineNodes();
						t.setSelectedNodes(-1);
						t.repaint();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}});
		
//		parse.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				String text = editArea0.getText();
//				if (text.trim().length() != 0) {//���޸�ʽ�����ű��ʽת��Ϊ�и�ʽ�ı��ʽ
//					annotation = new Annotation(text);
//					pipeline.annotate(annotation);
//					List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
//					ArrayList<TreePanelNode> treeLists = new ArrayList<TreePanelNode>();
//					String expressionofAlltrees = "";
//					for (CoreMap sentence : sentences) {
//						Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
//						String eachSentenceofOneLine = tree.toString();
//					    expressionofAlltrees = expressionofAlltrees +eachSentenceofOneLine;
//					}
//					System.out.println(expressionofAlltrees);	
//					treeLists = new TreePanelNode().fromTextToTree(expressionofAlltrees);
//					String sentencesofMultLines = "";
//						int count = 0;
//						for (TreePanelNode root : treeLists) {
//							count++;
//							if (root.examTheTree() && root != null)
//								for (String word : root.changeIntoText())
//									sentencesofMultLines = sentencesofMultLines + word;
//							else
//								sentencesofMultLines = sentencesofMultLines + "��" + count + "�����ű��ʽ��ʽ����";
//
//							sentencesofMultLines = sentencesofMultLines + "\r\n";
//						}
//                      editArea.setText(sentencesofMultLines);
//
//				}
//				resetButtonstatus();
//				t.setSelectedNodes(-1);
//				t.initCombineNodes();
//				t.repaint();
//			}
//		});

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(new TreePanelNode().toOneLine(editArea.getText()));
				System.out.println(new TreePanelNode().format(new TreePanelNode().toOneLine(editArea.getText())));
				ArrayList<TreePanelNode> trees = new TreePanelNode().fromTextToTree(editArea.getText());
				if (trees != null) {
			
					treeLists.clear();
					treeLists = trees;
					treeAtTxt.setTreeListWithOneTree(treeLists);
					nodes = TreePanelNode.nodesOfAllTrees(treeLists);
					for (TreePanelNode node : nodes) {
						node.calculateAngle();
					}
//					System.out.println(new TreePanelNode().toOneLine(editArea.getText()));
//					System.out.println(new TreePanelNode().format(new TreePanelNode().toOneLine(editArea.getText())));
					hasModeified.put(t.getTreeAtTxt().getTxtPath(), Boolean.FALSE);
					t.setTreeAtTxt(treeAtTxt);
					t.changeStatus_PanelModified();
					t.setTreeLists(treeLists);
					t.setNodes(nodes);
					resetButtonstatus();
					t.setSelectedNodes(-1);
					t.initCombineNodes();
					t.repaint();
				}
				else {
					JOptionPane.showMessageDialog(f, "���ű��ʽ��ʽ�д���,���顣", "��Ϣ��ʾ��",
							JOptionPane.INFORMATION_MESSAGE);
					resetButtonstatus();
					t.setSelectedNodes(-1);
					t.initCombineNodes();
					t.repaint();
				}
			}
		});
		treeToText.addActionListener(new ActionListener() {//�����޸�Ϊ���Ϊ

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeLists = t.getTreeLists();
				nodes = TreePanelNode.nodesOfAllTrees(treeLists);
				int returnValue = JOptionPane.YES_OPTION;
				for (TreePanelNode root : treeLists) {
					if (root.examTheTree() && root != null) {//�ж�ɭ���е����Ƿ񶼷��ϸ�ʽ
						System.out.println(returnValue);
					} else {

						returnValue = JOptionPane.showConfirmDialog(f, "���ű��ʽ��ʽ�д���,�Ƿ�Ҫ���档", "ȷ�϶Ի���",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

						break;
					}

				}

				if (returnValue == JOptionPane.YES_OPTION && treeLists != null && nodes != null && !treeLists.isEmpty() && !nodes.isEmpty()) {
					try {
						int count = 0;
						SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
						System.out.println(date.format(new Date()) + ".txt");
						fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
						if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {// ȷ������						
							String filePath = fileChooser.getSelectedFile().toString();
							FileWriter treeToText = new FileWriter(filePath);
							count = 0;
							for (TreePanelNode root : treeLists) {
								count++;
								if (root.examTheTree() && root != null)
									for (String word : root.changeIntoText())
										treeToText.write(word);
								else
									treeToText.write("��" + count + "�����ű��ʽ��ʽ����");

								treeToText.write("\r\n");
							}
							treeToText.close();
							
						}
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();

			}
		});
		
		rePaint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				nodes = t.getNodes();
				treeLists = t.getTreeLists();
				if (treeLists != null && nodes != null && !treeLists.isEmpty() && !nodes.isEmpty()) {
					TreePanelNode.repaintTreeLists(treeLists);
					t.setTreeLists(treeLists);
					t.setNodes(TreePanelNode.nodesOfAllTrees(treeLists));
					
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}});
		updateExpression.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				nodes = t.getNodes();
				treeLists = t.getTreeLists();
				int returnValue = JOptionPane.YES_OPTION;
				for (TreePanelNode root : treeLists) {
					if (root.examTheTree() && root != null) {//�ж�ɭ���е����Ƿ񶼷��ϸ�ʽ
						
					} else {

						returnValue = JOptionPane.showConfirmDialog(f, "���ű��ʽ��ʽ�д���,�Ƿ�Ҫ�������ű��ʽ��", "ȷ�϶Ի���",
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

						break;
					}

				}
				if (returnValue == JOptionPane.YES_OPTION && treeLists != null && nodes != null && !treeLists.isEmpty() && !nodes.isEmpty()) {
					String allExpressionFormatted = "";
					int count = 0;
					for(TreePanelNode root : treeLists) {
						count ++;
						if(root.examTheTree() && root != null) 
							for (String word : root.changeIntoText())
								allExpressionFormatted = allExpressionFormatted + word;
						else 
							allExpressionFormatted = allExpressionFormatted + "��"+count+"�����ű��ʽ��ʽ����";
						allExpressionFormatted = allExpressionFormatted + "\r\n"; 
					}
					editArea.setText(allExpressionFormatted);
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}});
		clearPanel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!treeLists.isEmpty()) {
					if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(f, "�Ƿ�������壿", "ȷ���������",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)) {
						// TODO Auto-generated method stub
						treeLists.clear();
						nodes.clear();
						
						t.setTreeLists(treeLists);
						t.setNodes(nodes);											
                     	treeAtTxt.setTreeListWithOneTree(treeLists);                   	
                     	t.setTreeAtTxt(treeAtTxt);
                     	hasModeified.put(t.getTreeAtTxt().getTxtPath(), Boolean.TRUE);
						t.setHasModeified(hasModeified);
						resetButtonstatus();
						t.setSelectedNodes(-1);
						t.initCombineNodes();
						t.repaint();
						
					}
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}
		});
		combine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t.setCombine_Clicked(!t.isCombine_Clicked());
				t.initCombineNodes();
				t.setSelectedNodes(-1);//��ѡ�нڵ�
				
				if (t.isCombine_Clicked()) {
					combine.setBackground(Color.green);
				}	
				else
					combine.setBackground((Color)new ColorUIResource(238,238,238));
				t.setAdd_Clicked(false);
				add.setBackground((Color)new ColorUIResource(238,238,238));
				t.setDelete_Clicked(false);
				delete.setBackground((Color)new ColorUIResource(238,238,238));
				t.setSelectRoot_Clicked(false);
				selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setModify_Clicked(false);
//				modify.setBackground((Color)new ColorUIResource(238,238,238));
				t.grabFocus();
				t.repaint();
			}
		});
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		

				// TODO Auto-generated method stub
				t.setSelectedNodes(-1);//��ѡ�нڵ�
				t.setAdd_Clicked(!t.isAdd_Clicked());
				if (t.isAdd_Clicked())
					add.setBackground(Color.green);
				else
					add.setBackground((Color)new ColorUIResource(238,238,238));
				t.setCombine_Clicked(false);
				combine.setBackground((Color)new ColorUIResource(238,238,238));
				t.setDelete_Clicked(false);
				delete.setBackground((Color)new ColorUIResource(238,238,238));
				t.setSelectRoot_Clicked(false);
				selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setModify_Clicked(false);
//				modify.setBackground((Color)new ColorUIResource(238,238,238));
				t.grabFocus();
				t.repaint();
			}});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t.setSelectedNodes(-1);//��ѡ�нڵ�
				t.setDelete_Clicked(!t.isDelete_Clicked());
				if (t.isDelete_Clicked())
					delete.setBackground(Color.green);
				else
					delete.setBackground((Color)new ColorUIResource(238,238,238));
				t.setCombine_Clicked(false);
				combine.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setModify_Clicked(false);
//				modify.setBackground((Color)new ColorUIResource(238,238,238));
				t.setAdd_Clicked(false);
				add.setBackground((Color)new ColorUIResource(238,238,238));
				t.setSelectRoot_Clicked(false);
				selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
				t.grabFocus();
				t.repaint();

			}});
		selectRoot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t.setSelectedNodes(-1);//��ѡ�нڵ�
				t.setSelectRoot_Clicked(!t.isSelectRoot_Clicked());
				if (t.isSelectRoot_Clicked())
					selectRoot.setBackground(Color.green);
				else
					selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
				t.setCombine_Clicked(false);
				combine.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setModify_Clicked(false);
//				modify.setBackground((Color)new ColorUIResource(238,238,238));
				t.setAdd_Clicked(false);
				add.setBackground((Color)new ColorUIResource(238,238,238));
				t.setDelete_Clicked(false);
				delete.setBackground((Color)new ColorUIResource(238,238,238));
				t.grabFocus();
				t.repaint();

			}});
		rightTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int returnValue = JOptionPane.YES_OPTION;
				String currentTxtPath = t.getTreeAtTxt().getTxtPath();
				if (!hasModeified.get(currentTxtPath).booleanValue()) {// txtPath��Ӧ�ļ�û���޸�
					//���һ���
					if( !nextTreeLists( Main.RIGHT ) ) //���һ���ʧ��
						JOptionPane.showMessageDialog(null, "�Ѿ������һƪ�ĵ������һ������", "���һ���", JOptionPane.INFORMATION_MESSAGE);		
					else {
						rightTree.setBackground(Color.green);
						rightTree.setBackground((Color)new ColorUIResource(238,238,238));
					}
				} else {
					returnValue = JOptionPane.showConfirmDialog(f, "�����ȱ��浱ǰ�����ϵ�����", "ȷ�϶Ի���",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (returnValue == JOptionPane.YES_OPTION) {
						
						ArrayList<TreePanelNode> treesOfSameTxt = new ArrayList<TreePanelNode>();
						if (t.getTreeLists().size() == 1) {// ��ǰ�����ֻ��һ�����ǲſ��Ա���
							if (currentTxtPath == null) {//�½�����壬����һ��txt��ֻ��һ����
								if (t.getTreeLists().get(0).examTheTree() && t.getTreeLists().get(0) != null) {
									try {
										SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
										if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {//ȷ������
											currentTxtPath = fileChooser.getSelectedFile().toString();
											FileWriter fw = new FileWriter(currentTxtPath);
											for (String word : t.getTreeLists().get(0).changeIntoText())
												fw.write(word);

											hasModeified.remove(null);// ��û������λ�ú��ļ�����ɾ������Ϊ�Ѿ�Ϊ�����浽��ָ���ļ���
											hasModeified.put(currentTxtPath, Boolean.FALSE);
											t.setHasModeified(hasModeified);
											treeAtTxt.setTxtPath(currentTxtPath);											
											t.getTreeAtTxt().setTxtPath(currentTxtPath);// �޸��ļ���·����֮ǰ��null
											f.setTitle(new File(currentTxtPath).getName());
											fw.close();
											
											//���һ���
											if( !nextTreeLists( Main.RIGHT ) ) //���һ���ʧ��
												JOptionPane.showMessageDialog(null, "�Ѿ������һƪ�ĵ������һ������", "���һ���", JOptionPane.INFORMATION_MESSAGE);		
											
										}else {//�����ȡ�����˳��Ի���ʲôҲ������
											
										}
										
										
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								}else {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								}
								
								
							}else {//�޸��˴򿪵��ļ��е�ĳ����
								for (TreeAtTxt tat : allTreesAtTxt) {
									if(tat.getTxtPath().equals(currentTxtPath))
										treesOfSameTxt.add(tat.getTreeListWithOneTree().get(0));
								}
							}
							
							if(!treesOfSameTxt.isEmpty()) {//ִ���������else
								boolean correctFormat = true;
								for (TreePanelNode treeRoot : treesOfSameTxt) {
									if (treeRoot.examTheTree() && treeRoot != null) {
										
									}else {
										correctFormat = false;
										break;
									}
								}
								if(correctFormat == false) {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									try {
										FileWriter fw = new FileWriter(currentTxtPath);
										for (TreePanelNode treeRoot : treesOfSameTxt) {
											for (String word : treeRoot.changeIntoText())
												fw.write(word);
											fw.write("\r\n");
										}
										hasModeified.put(currentTxtPath, Boolean.FALSE);
										t.setHasModeified(hasModeified);
										fw.close();
										//���һ���
										if( !nextTreeLists( Main.RIGHT ) ) //���һ���ʧ��
											JOptionPane.showMessageDialog(null, "�Ѿ������һƪ�ĵ������һ������", "���һ���", JOptionPane.INFORMATION_MESSAGE);		
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								
							}
							
							
						}else {//��ǰ��岻ֹһ����/����û����
							JOptionPane.showMessageDialog(f, "������ֻ��һ����ʱ���ܱ��档", "��Ϣ��ʾ��",
									JOptionPane.INFORMATION_MESSAGE);
						}

					} else if (returnValue == JOptionPane.NO_OPTION) {
						hasModeified.put(currentTxtPath, Boolean.FALSE);
						t.setHasModeified(hasModeified);
						//���һ���
						if( !nextTreeLists( Main.RIGHT ) ) //���һ���ʧ��
							JOptionPane.showMessageDialog(null, "�Ѿ������һƪ�ĵ������һ������", "���һ���", JOptionPane.INFORMATION_MESSAGE);		
					} else {//�����ȡ�������˳�
					}
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}});
		leftTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int returnValue = JOptionPane.YES_OPTION;
				String currentTxtPath = t.getTreeAtTxt().getTxtPath();
				if (!hasModeified.get(currentTxtPath).booleanValue()) {// txtPath��Ӧ�ļ�û���޸�
					//���󻬶�
					if( !nextTreeLists( Main.LEFT ) ) //���󻬶�ʧ��
						JOptionPane.showMessageDialog(null, "�Ѿ�����һƪ�ĵ��ĵ�һ������", "���󻬶�", JOptionPane.INFORMATION_MESSAGE);					
				} else {
					returnValue = JOptionPane.showConfirmDialog(f, "���ȱ��浱ǰҳ������ݡ�", "ȷ�϶Ի���",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (returnValue == JOptionPane.YES_OPTION) {
						ArrayList<TreePanelNode> treesOfSameTxt = new ArrayList<TreePanelNode>();
						if (t.getTreeLists().size() == 1) {// ��ǰ�����ֻ��һ�����ǲſ��Ա���
							if (currentTxtPath == null) {//�½�����壬����һ��txt��ֻ��һ����
								if (t.getTreeLists().get(0).examTheTree() && t.getTreeLists().get(0) != null) {
									try {
										SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
										fileChooser.setSelectedFile(new File(date.format(new Date()) + ".txt"));
										if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {//ȷ������
											currentTxtPath = fileChooser.getSelectedFile().toString();
											FileWriter fw = new FileWriter(currentTxtPath);
											for (String word : t.getTreeLists().get(0).changeIntoText())
												fw.write(word);

											hasModeified.remove(null);// ��û������λ�ú��ļ�����ɾ������Ϊ�Ѿ�Ϊ�����浽��ָ���ļ���
											hasModeified.put(currentTxtPath, Boolean.FALSE);
											t.setHasModeified(hasModeified);
											treeAtTxt.setTxtPath(currentTxtPath);
											t.getTreeAtTxt().setTxtPath(currentTxtPath);// �޸��ļ���·����֮ǰ��null
											f.setTitle(new File(currentTxtPath).getName());
											fw.close();
											
											//���󻬶�
											if( !nextTreeLists( Main.LEFT ) ) //���󻬶�ʧ��
												JOptionPane.showMessageDialog(null, "�Ѿ�����һƪ�ĵ��ĵ�һ������", "���󻬶�", JOptionPane.INFORMATION_MESSAGE);	
											
										}else {//�����ȡ�����˳��Ի���ʲôҲ������
											
										}
										
										
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
								}else {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								}
								
								
							}else {//�޸��˴򿪵��ļ��е�ĳ����
								for (TreeAtTxt tat : allTreesAtTxt) {
									if(tat.getTxtPath().equals(currentTxtPath))
										treesOfSameTxt.add(tat.getTreeListWithOneTree().get(0));
								}
							}
							
							if(!treesOfSameTxt.isEmpty()) {//ִ���������else
								boolean correctFormat = true;
								for (TreePanelNode treeRoot : treesOfSameTxt) {
									if (treeRoot.examTheTree() && treeRoot != null) {
										
									}else {
										correctFormat = false;
										break;
									}
								}
								if(correctFormat == false) {
									JOptionPane.showMessageDialog(f, "�ṹ����ʽ�д���,���顣", "��Ϣ��ʾ��",
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									try {
										FileWriter fw = new FileWriter(currentTxtPath);
										for (TreePanelNode treeRoot : treesOfSameTxt) {
											for (String word : treeRoot.changeIntoText())
												fw.write(word);
											fw.write("\r\n");
										}
										hasModeified.put(currentTxtPath, Boolean.FALSE);
										t.setHasModeified(hasModeified);
										fw.close();
										//���󻬶�
										if( !nextTreeLists( Main.LEFT ) ) //���󻬶�ʧ��
											JOptionPane.showMessageDialog(null, "�Ѿ�����һƪ�ĵ��ĵ�һ������", "���󻬶�", JOptionPane.INFORMATION_MESSAGE);	
										
										
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
								
							}
							
							
						}else {//��ǰ��岻ֹһ����/����û����
							JOptionPane.showMessageDialog(f, "������ֻ��һ����ʱ���ܱ��档", "��Ϣ��ʾ��",
									JOptionPane.INFORMATION_MESSAGE);
						}

					} else if (returnValue == JOptionPane.NO_OPTION) {
						//���󻬶�
						hasModeified.put(currentTxtPath, Boolean.FALSE);
						t.setHasModeified(hasModeified);
						if( !nextTreeLists( Main.LEFT ) ) //���󻬶�ʧ��
							JOptionPane.showMessageDialog(null, "�Ѿ�����һƪ�ĵ��ĵ�һ������", "���󻬶�", JOptionPane.INFORMATION_MESSAGE);	
					} else {//�����ȡ�������˳�
					}
				}
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
			}});
	}

	private void resetButtonstatus() {
		t.setDelete_Clicked(false);
		t.getDelete().setBackground((Color) new ColorUIResource(238, 238, 238));
		t.setAdd_Clicked(false);
		t.getAdd().setBackground((Color) new ColorUIResource(238, 238, 238));
		t.setCombine_Clicked(false);
		t.getCombine().setBackground((Color) new ColorUIResource(238, 238, 238));
		t.setSelectRoot_Clicked(false);
		t.getSelectRoot().setBackground((Color) new ColorUIResource(238, 238, 238));
	}
	
	private void resetTreePanel() {
		treeLists.clear();
		allTreesAtTxt.clear();
		nodes.clear();	
		hasModeified.clear();		
		hasModeified.put(null, Boolean.FALSE);
		treeAtTxt=new TreeAtTxt(treeLists);
		allTreesAtTxt.add(treeAtTxt);
		t.setTreeLists(treeLists);
		t.setNodes(nodes);
        t.setHasModeified(hasModeified);
        t.setTreeAtTxt(treeAtTxt);
		resetButtonstatus();
		t.setSelectedNodes(-1);
		t.initCombineNodes();
		t.repaint();
		f.setTitle("δ����");
		System.out.println("��屻�����");	
	}
	
	private boolean nextTreeLists(int direction) {
		int sizeOfTrees = allTreesAtTxt.size();
		int positionOfTreeAtList = allTreesAtTxt.indexOf(t.getTreeAtTxt());
		System.out.println("positionOfTreeAtList"+positionOfTreeAtList);
		if (direction == Main.LEFT) {//���󻬶�
			if (positionOfTreeAtList != 0) {
				treeLists = allTreesAtTxt.get(positionOfTreeAtList - 1).getTreeListWithOneTree();
				nodes = TreePanelNode.nodesOfAllTrees(treeLists);
				treeAtTxt = allTreesAtTxt.get(positionOfTreeAtList - 1);
				t.setTreeLists(treeLists);
				t.setNodes(nodes);
				t.setTreeAtTxt(treeAtTxt);
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.setTreeLists(treeLists);
				t.setNodes(nodes);
				t.setHasModeified(hasModeified);
				t.setTreeAtTxt(treeAtTxt);
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
				f.setTitle(new File(t.getTreeAtTxt().getTxtPath()).getName());
				return true;
			}else
				return false;
		}else {//���һ���
			if (positionOfTreeAtList != sizeOfTrees - 1) {
				treeLists = allTreesAtTxt.get(positionOfTreeAtList + 1).getTreeListWithOneTree();
				nodes = TreePanelNode.nodesOfAllTrees(treeLists);
				treeAtTxt = allTreesAtTxt.get(positionOfTreeAtList + 1);
				t.setTreeLists(treeLists);
				t.setNodes(nodes);
				t.setTreeAtTxt(treeAtTxt);
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.setTreeLists(treeLists);
				t.setNodes(nodes);
				t.setHasModeified(hasModeified);
				t.setTreeAtTxt(treeAtTxt);
				resetButtonstatus();
				t.setSelectedNodes(-1);
				t.initCombineNodes();
				t.repaint();
				f.setTitle(new File(t.getTreeAtTxt().getTxtPath()).getName());
				return true;
			}else
				return false;
		}
		
	}
	
	private void openTxts() throws IOException {
		fileChooser.addChoosableFileFilter(txtFileFilter);
		fileChooser.setMultiSelectionEnabled(true);
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File[] files =  fileChooser.getSelectedFiles();
			for(int i = 0,j = files.length -1;i<files.length;i++,j--) {//����JFileChooser�Ὣ��ѡ����ļ���򿪣��ʽ�����files��ת
				if( i < j  ) {
					File temp = files[i];
					files[i] = files[j];
					files[j] = temp;
				}else
					break;	
			}
			hasModeified.clear();
			allTreesAtTxt.clear();
			for(File file : files) {
				BufferedReader br = new BufferedReader(new FileReader(file.toString()));
				String strOfaTxt = new String();
				String line = null;
				while ((line = br.readLine()) != null) {
					strOfaTxt +=line; 
				}
				System.out.println(strOfaTxt);
				ArrayList<TreePanelNode> trees = new TreePanelNode().fromTextToTree(strOfaTxt);
				hasModeified.put(file.toString(), Boolean.FALSE);	
				
				for(TreePanelNode tree : trees) {
					TreePanelNode.allocatePosition(tree);
					TreeAtTxt treeAtTxt = new TreeAtTxt(tree, file.toString());
					allTreesAtTxt.add(treeAtTxt);				
				}
			}
			treeLists = allTreesAtTxt.get(0).getTreeListWithOneTree();
			t.setTreeLists(treeLists);
			nodes = TreePanelNode.nodesOfAllTrees(treeLists);
			t.setNodes(nodes);
			treeAtTxt = allTreesAtTxt.get(0);
			t.setTreeAtTxt(treeAtTxt);
			t.setHasModeified(hasModeified);
			resetButtonstatus();
			t.setSelectedNodes(-1);
			t.initCombineNodes();
			t.repaint();
			f.setTitle(files[0].getName());
			
			
		}
	}
	
//	private void coreNLPInit() {
//		Properties props = new Properties();
//		try {
//			props.load(this.getClass().getClassLoader().getResourceAsStream("StanfordCoreNLP-chinese.properties"));
//			props.setProperty("annotators", "tokenize,ssplit,pos,parse");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		pipeline = new StanfordCoreNLP(props);
//		annotation = new Annotation("�䷨������");
//		pipeline.annotate(annotation);
//	}
	
	public static void main (String args[]) {
		new Main().init();	
	}
	
}


















