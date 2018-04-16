package edu.hust.structuretree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.plaf.ColorUIResource;

public class Main {	
	private JFrame f = new JFrame("窗口");
	private TreePanel t =new TreePanel();
	private JScrollPane paintjsp;
	private JPanel editPanel = new JPanel();
	private TextArea editArea= new TextArea(8,80);
	private JButton start = new JButton("生成结构树");
	private JButton textToTree = new JButton("导出到文件");
	private JButton rePaint = new JButton("重画结构树");
	private Vector<TreePanelNode> nodes = new Vector<TreePanelNode>();
	private ArrayList<TreePanelNode> treeLists = new ArrayList<TreePanelNode>();
	public void init() {
		//注释
		t.setNodes(nodes);
		t.setTreeLists(treeLists);
		t.setPreferredSize(new Dimension(10000,10000));
		paintjsp=new JScrollPane(t);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));	
		buttonPanel.add(start);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(textToTree);
		buttonPanel.add(Box.createVerticalStrut(8));
		buttonPanel.add(rePaint);
		
		JScrollPane editjsp = new JScrollPane(editArea);
		editPanel.setLayout(new BorderLayout());
		editPanel.add(editjsp);		
		editPanel.add(buttonPanel, BorderLayout.EAST);
		
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BoxLayout(functionPanel,BoxLayout.Y_AXIS));
		JButton add = new JButton("增加");
		JButton delete = new JButton("删除");
//	    JButton modify = new JButton("修改");
		JButton combine = new JButton("连接");
		JButton selectRoot = new JButton("root");
		
		
		
		functionPanel.add(add);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(delete);
		functionPanel.add(Box.createVerticalStrut(8));
//		functionPanel.add(modify);
//		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(combine);
		functionPanel.add(Box.createVerticalStrut(8));
		functionPanel.add(selectRoot);
		selectRoot.setVisible(false);
		t.setAdd(add);
		t.setDelete(delete);
//		t.setModify(modify);
		t.setCombine(combine);
		t.setSelectRoot(selectRoot);
		
		
		f.setBounds(100,100,1000,800);		
		f.setLocationRelativeTo(null);		
		f.add(paintjsp);
		f.add(functionPanel, BorderLayout.WEST);
		f.add(editPanel, BorderLayout.NORTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);


		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println(new TreePanelNode().format(new TreePanelNode().toOneLine(editArea.getText())));
				TreePanelNode tree = new TreePanelNode().fromTextToTree(editArea.getText());
				if (tree != null) {
					// TreePanelNode.allocatePosition(tree);
					treeLists.clear();
					treeLists.add(tree);
					nodes = TreePanelNode.nodesOfAllTrees(treeLists);
					for (TreePanelNode node : nodes) {
						node.calculateAngle();
					}

					tree.print();
					t.setTreeLists(treeLists);
					t.setNodes(nodes);
					t.repaint();
				}
			}
		});
		textToTree.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				treeLists = t.getTreeLists();
				try {
					SimpleDateFormat date =new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
					System.out.println(date.format(new Date())+".txt");
					FileWriter treeToText = new FileWriter(date.format(new Date())+".txt");
					int count = 0;
					for (TreePanelNode root : treeLists) {
						count ++;
						if(root.examTheTree() && root != null) 
							for (String word : root.changeIntoText())
								treeToText.write(word);
						else
							treeToText.write("第"+count+"个括号表达式格式错误。");

						treeToText.write("\r\n");			
					}
					treeToText.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
					t.repaint();
				}
			}});
		
		combine.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t.setCombine_Clicked(!t.isCombine_Clicked());
				t.initCombineNodes();
				t.setSelectedNodes(-1);//不选中节点
				
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
				t.setSelectedNodes(-1);//不选中节点
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
//		modify.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				t.setModify_Clicked(!t.isModify_Clicked());
//				if (t.isModify_Clicked())
//					modify.setBackground(Color.green);
//				else
//					modify.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setCombine_Clicked(false);
//				combine.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setDelete_Clicked(false);
//				delete.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setAdd_Clicked(false);
//				add.setBackground((Color)new ColorUIResource(238,238,238));
//				t.setSelectRoot_Clicked(false);
//		        selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
//			}});
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				t.setSelectedNodes(-1);//不选中节点
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
				t.setSelectedNodes(-1);//不选中节点
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
	}
	
	
	
	public static void main (String args[]) {
		new Main().init();	
	}
	
}


















