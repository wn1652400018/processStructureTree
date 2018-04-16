package edu.hust.structuretree;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;


public class TreePanel extends JPanel {
	private Vector<TreePanelNode> nodes;	
	private ArrayList<TreePanelNode> treeLists; 
	private int selectedNodes = -1;// Ĭ�ϱ�ʾû��ѡ�нڵ�
	private ArrayList<Integer> selectedALines =new ArrayList<Integer>();
	private int selectedALine = -1;//Ĭ��û��ѡ���ߡ�selectedALine��ʾ ��ѡ���ߵļ�ͷָ��Ľڵ� ���丸�ڵ�֮�������
	private int tempX;//��¼�����ʱ�����꣬���߼�¼�϶�ʱ������һ������
	private int tempY;//��¼�����ʱ�����꣬���߼�¼�϶�ʱ������һ������
	private int mouseX; //��ʾ����ʱ������λ��
	private int mouseY;//��ʾ����ʱ������λ��
	private int editingNode = -1;//��ʾ���༭�Ľڵ����ţ�Ĭ����û��Ҫ�༭�Ľڵ�
	private int parentOfAddingNode = -1;//��ʾҪ��ӽڵ�ĸ��׽ڵ㣬Ĭ��Ϊ-1������ӽڵ�
	private int[] combinedNodes = {-1,-1};//��ʾ��Ҫ���ӵ������ڵ�
	private int count = -1;//������ʾ��ӵ�����combinedNodes�е�λ�ã���ĳ��û�е�����ڵ�ʱ���µ���Ϊ-1
	private int XBeforeDrag ;//������ʾ��һ���������ק�¼�֮ǰ����λ�ã�������ָ��ק������û���ͷ����
	private int YBeforeDrag ;//������ʾ��һ���������ק�¼�֮ǰ����λ��
	private boolean add_Clicked = false,delete_Clicked = false,combine_Clicked = false,selectRoot_Clicked = false;//add,delete,modify,combine��ť��״̬
	private JButton add,delete,combine,selectRoot;
	
	
	public TreePanel() {
		
//		selectedNodes = new Vector<Integer>();
		setBackground(Color.WHITE);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				mouseX = me.getX();
				mouseY = me.getY();
				tempX = me.getX();//��ק����һֱ���ģ�����ק�¼��ֽ��С����ק�¼���ÿ��ֻ�ƶ�һ������
				tempY = me.getY();
				
				XBeforeDrag = me.getX();//��ק�����в����ģ�����ק�¼�һ��ʼ������
				YBeforeDrag = me.getY();
				
				int i=0;
				
				for (; i < nodes.size(); i++) {
//					System.out.println("--------"+mouseX+" "+mouseY);
					if (nodes.get(i).getBounds().contains(mouseX, mouseY)) {// ���ͣ���ڽڵ�i�ľ�������
    					selectedNodes = i;
						if (me.getClickCount() == 2&&!me.isAltDown()&&!me.isControlDown()&&!me.isShiftDown()) {//˫���༭�ڵ�
							editingNode = i;
							nodes.get(i).setValue(nodes.get(editingNode).getValue());
							
						}
						if(editingNode != i) {
							editingNode = -1;
						}
						
						if( ( me.isAltDown()&&!me.isControlDown() ) || isAdd_Clicked()) {//alt�ӵ��i�ڵ�,��ѡ�нڵ������һ���ӽڵ�
							parentOfAddingNode = selectedNodes;
							add_Clicked = false;
							add.setBackground((Color)new ColorUIResource(238,238,238));
//							add.setBackground(Color.YELLOW);
						}
						if(parentOfAddingNode != i) {
							parentOfAddingNode = -1;
						}
						
						//��ѡ��Ľڵ�����Ϊ�����ĸ��ڵ�
						if(isSelectRoot_Clicked()) {
							int indexOfPreRoot = treeLists.indexOf(nodes.get(selectedNodes).getRoot());
							treeLists.set(indexOfPreRoot, nodes.get(selectedNodes).changeRoot());
							nodes =TreePanelNode.nodesOfAllTrees(treeLists);
							selectRoot_Clicked = false;
							selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
							selectedNodes = -1;
							repaint();
						}
						// �������combine��ť���ð�ť��ѡ��״̬,���������ڵ�
						if (isCombine_Clicked()) {
							count++;
							count = count % 2;

							combinedNodes[count] = selectedNodes;
							if (combinedNodes[0] != -1 && combinedNodes[1] != -1
									&& combinedNodes[0] != combinedNodes[1]) {// ��ʱ��ʾ���������ڵ�

								if (!nodes.get(combinedNodes[count % 2]).getRoot()
										.equals(nodes.get(combinedNodes[(count + 1) % 2]).getRoot())) {// �����ڵ㲻��ͬһ������
									TreePanelNode firstNode = nodes.elementAt(combinedNodes[(count + 1) % 2]);
									TreePanelNode secondNode = nodes.get(combinedNodes[count % 2]);

									treeLists.remove(secondNode.getRoot());

									if (secondNode.equals(secondNode.getRoot()) == false) {
										secondNode.changeRoot();
									}
									for (TreePanelNode aNode : TreePanelNode.allNodes(secondNode)) {
										aNode.setRoot(firstNode.getRoot());
									}
									secondNode.setParent(firstNode);
									firstNode.getChildren().add(secondNode);
									secondNode.calculateAngle();
									firstNode.sortByAngle();
									nodes = TreePanelNode.nodesOfAllTrees(treeLists);
									combinedNodes[0] = -1;
									combinedNodes[1] = -1;
									count = -1;
									selectedNodes = -1;
									
								} else {
									combinedNodes[0] = selectedNodes;
									combinedNodes[1] = -1;
									count = 0;
								}
							}
						} else {
							combinedNodes[0] = -1;
							combinedNodes[1] = -1;
							count = -1;
						}
//						 ��סshift��������ڵ㣬�������ڵ�����һ��\
//						if (!me.isAltDown() && !me.isControlDown() && me.isShiftDown()) {
//
//							count++;
//							count = count % 2;
//							
//							combinedNodes[count] = selectedNodes;
//							if (combinedNodes[0] != -1 && combinedNodes[1] != -1 && combinedNodes[0] != combinedNodes[1] ) {// ��ʱ��ʾ���������ڵ�
//								
//								if(!nodes.get(combinedNodes[count % 2]).getRoot().equals(nodes.get(combinedNodes[(count+1)%2]).getRoot())) {//�����ڵ㲻��ͬһ������
//									TreePanelNode firstNode = nodes.elementAt(combinedNodes[(count + 1) % 2]);
//									TreePanelNode secondNode = nodes.get(combinedNodes[count % 2]);
//
//									treeLists.remove(firstNode.getRoot());
//
//									if (firstNode.equals(firstNode.getRoot()) == false) {
//										firstNode.changeRoot();
//									}
//									for (TreePanelNode aNode : TreePanelNode.allNodes(firstNode)) {
//										aNode.setRoot(secondNode.getRoot());
//									}
//									firstNode.setParent(secondNode);
//									secondNode.getChildren().add(firstNode);
//									firstNode.calculateAngle();
//									secondNode.sortByAngle();
//									nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//									combinedNodes[0] = -1;
//									combinedNodes[1] = -1;
//									count = -1;
//									selectedNodes = -1;
//								}else {
//									combinedNodes[0] = -1;
//									combinedNodes[1] = -1;
//									count = -1;
//									selectedNodes = -1;
//								}					
//							}
//
//						} else {
//							System.out.println("����˻�ȡ������");
//							combinedNodes[0] = -1;
//							combinedNodes[1] = -1;
//							count = -1;
//						}
						
						if(isDelete_Clicked()) {//�����ɾ����ť��ɾ������Ľڵ�
							for(TreePanelNode child : nodes.get(selectedNodes).getChildren()) {
								Vector<TreePanelNode> nodesOfTree_child =  TreePanelNode.allNodes(child);
								child.setParent(null);
								child.setRoot(child);
								child.calculateAngle();//���ڵ��angle�ݶ�Ϊ0
								for(int k = 1;k < nodesOfTree_child.size();k++) {
									nodesOfTree_child.get(k).setRoot(child);
								}
								treeLists.add(child);//���ɸýڵ�������������뵽treeLists��
								
							}
//							nodes.get(selectedNodes).setChildren(new Vector<TreePanelNode>() );//���ýڵ���ӽڵ�ȥ��
							if(nodes.get(selectedNodes) == nodes.get(selectedNodes).getRoot()) {
								treeLists.remove(nodes.get(selectedNodes));
							}else{
								nodes.get(selectedNodes).getParent().getChildren().remove(nodes.get(selectedNodes));//ɾ���ýڵ�
							}
							
							nodes = TreePanelNode.nodesOfAllTrees(treeLists);
			
							selectedNodes = -1;
							editingNode = -1;//�༭�����п���ֱ��ɾ���ýڵ�
							System.out.println("ɾ���˸ýڵ�");
							repaint();
						}
						
//						if(me.isControlDown()&&me.isAltDown()) {//ɾ���ڵ�,��ͬʱɾ���ýڵ��Լ�����������ڵ�
//							if(!nodes.get(selectedNodes).equals(nodes.get(selectedNodes).getRoot()) ) {//ɾ�����ڵ�����ڵ�
//								nodes.get(selectedNodes).getParent().getChildren().remove(nodes.get(selectedNodes));
//								nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//								selectedNodes =-1;
//							}else {
//								System.out.println("ɾ�����ڵ�");
//								treeLists.remove(nodes.get(selectedNodes).getRoot());
//								nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//								selectedNodes =-1;
//							}
//						}
						
//						if(me.isControlDown() && !me.isAltDown()) {//ɾ���ýڵ������ӽڵ�����ߣ�ʹ���ӽڵ���ɭ��
//							for(TreePanelNode child : nodes.get(selectedNodes).getChildren()) {
//								Vector<TreePanelNode> nodesOfTree_child =  TreePanelNode.allNodes(child);
//								child.setParent(null);
//								child.setRoot(child);
//								child.calculateAngle();//���ڵ��angle�ݶ�Ϊ0
//								for(int k = 1;k < nodesOfTree_child.size();k++) {
//									nodesOfTree_child.get(k).setRoot(child);
//								}
//								treeLists.add(child);//���ɸýڵ�������������뵽treeLists��
//								
//							}
//							nodes.get(selectedNodes).setChildren(new Vector<TreePanelNode>() );//���ýڵ���ӽڵ�ȥ��
//							nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//						}
						
						break;
					}
					else {//�ڵ�i����ѡ�нڵ�
						selectedNodes = -1;
						editingNode = -1;
						parentOfAddingNode = -1;
					}
				}
				
				
//				if (selectedNodes == -1 && !me.isAltDown() && !me.isControlDown() && !me.isShiftDown()) {// û��ѡ�нڵ㣬Ҳû�е��shift��alt��ctrl��
//					for (int j = 0; j < nodes.size(); j++) {// �ж��Ƿ�ѡ����
//						System.out.println("�Ƚ��˵�"+j+"���ڵ�");
//						if (nodes.get(j).getParent() != null) {
//							
//							double angle = 0;
//							if ((nodes.get(j).getX() + nodes.get(j).getWidth() / 2) != (mouseX)) {
//								double xChild = nodes.get(j).getX() + nodes.get(j).getWidth() / 2;
//								double yChild = nodes.get(j).getY() + nodes.get(j).getHeight();
//								double xParent = mouseX;
//								double yParent = mouseY;
//								if (xChild < xParent && yChild <= yParent)
//									angle = Math.atan((yParent - yChild) / (xParent - xChild));
//								if (xChild < xParent && yChild > yParent)
//									angle = Math.atan((yParent - yChild) / (xParent - xChild));
//								if (xChild > xParent && yChild >= yParent)
//									angle = Math.atan((yParent - yChild) / (xParent - xChild)) - Math.PI;
//								if (xChild > xParent && yChild < yParent)
//									angle = Math.atan((yParent - yChild) / (xParent - xChild)) - Math.PI;
//							} else if (nodes.get(j).getY() + nodes.get(j).getHeight() > mouseY)
//								angle = -Math.PI / 2;
//							else
//								angle = -3 * Math.PI / 2;
//							if ( (nodes.get(j).getX() >= mouseX && mouseX >= nodes.get(j).getParent().getX() )
//									|| (nodes.get(j).getX() <= mouseX && mouseX <= nodes.get(j).getParent().getX()) ) {
//								if ((int) (angle * 10) == (int) (nodes.get(j).getAngle() * 10)) {
//									selectedALine = j;
//									break;
//								} else
//									selectedALine = -1;
//							}
//							
//						} 
//					}
//				}else {
//					selectedALine = -1;
//				}
				
				
				if(selectedNodes == -1 && !(!me.isAltDown() && !me.isControlDown() && me.isShiftDown())) {
					combinedNodes[0] = -1;
					combinedNodes[1] = -1;
					count = -1;
				}
				
				if(selectedNodes == -1 && parentOfAddingNode == -1) {//û��ѡ�нڵ㣬�������һ���µ���
					if( ( me.isAltDown() && !me.isControlDown() ) || isAdd_Clicked() ) {
						parentOfAddingNode = -2;
						add_Clicked = false;
						add.setBackground((Color)new ColorUIResource(238,238,238));
					}
				}

				repaint();
				
			}

			public void mouseReleased(MouseEvent me) {// ���¼��ڵ���ջ����ʱ������ק�����ͷ����ʱ����
				parentOfAddingNode = -1;
				System.out.println("selectedNodes=" + selectedNodes);

				if ((selectedNodes != -1) && (((me.getX() - XBeforeDrag) != 0) || ((me.getY() - YBeforeDrag) != 0))) {// ��ʾ��������ק�¼�������nodes,����repaint
					nodes.get(selectedNodes).sortByAngle();// �����ƶ��ڵ�������ӽڵ�����
					if (nodes.get(selectedNodes).getParent() != null
							&& nodes.get(selectedNodes).getParent().getChildren().size() != 1) {// ������ƶ��Ľڵ����ֵܽڵ�
						TreePanelNode theMovingNode = nodes.get(selectedNodes);
						Vector<TreePanelNode> siblingOfMovedNode = theMovingNode.getParent().getChildren();// ��ð����ƶ��ڵ�����ֵܽڵ��Vector
						siblingOfMovedNode.remove(theMovingNode);// �����ƶ��ڵ�����ֵܽڵ��������Ƴ��������°�����˳����ӽ�ȥ���Ҹ���nodes����
						int i = 0;
						for (; i < siblingOfMovedNode.size(); i++) {// �����ƶ��ڵ����ڵ�Vector���°���kֵ��С����
							if (theMovingNode.getAngle() > siblingOfMovedNode.get(i).getAngle()) {
								siblingOfMovedNode.insertElementAt(theMovingNode, i);
								nodes = TreePanelNode.nodesOfAllTrees(treeLists);
								selectedNodes = nodes.indexOf(siblingOfMovedNode.elementAt(i));
								break;
							}
						}
						if (i == siblingOfMovedNode.size())
							siblingOfMovedNode.add(theMovingNode);
						nodes = TreePanelNode.nodesOfAllTrees(treeLists);
						selectedNodes = nodes.indexOf(siblingOfMovedNode.elementAt(i));
					}
					nodes = TreePanelNode.nodesOfAllTrees(treeLists);

					// ����
//					int count = -1;
//					for (TreePanelNode theNode : nodes) {
//						count++;
//						System.out.print("count:" + count + theNode.getValue() + "��x,yΪ��" + theNode.getX() + ","
//								+ theNode.getY() + "�Ƕ���" + theNode.getAngle() + "firstChild:");
//						if (!theNode.getChildren().isEmpty())
//							System.out.println("firstChlild:" + theNode.getChildren().get(0).getValue().toString());
//					}

					repaint();
				}
				
//���޸�				
//				if (selectedNodes == -1 && !selectedALines.isEmpty()) {
//					for(int selectedALine:selectedALines) {
//						nodes.get(selectedALine).relieveRelationship();
//						treeLists.add(nodes.get(selectedALine));
//						
//					}
//					nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//					for(TreePanelNode aNode:nodes)
//						aNode.print();
//					selectedALines =new ArrayList<Integer>();
//					repaint();
//				}
			}
		});
		
		//�������������
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {//�÷�������keyType
				if(ke.isControlDown()||ke.isAltDown()) {
					add_Clicked = false;
					delete_Clicked = false;
					combine_Clicked = false;
					selectRoot_Clicked = false; 
					initCombineNodes();
//					System.out.println("308"+1313465465);
					add.setBackground((Color)new ColorUIResource(238,238,238));
					delete.setBackground((Color)new ColorUIResource(238,238,238));
					combine.setBackground((Color)new ColorUIResource(238,238,238));
					selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
					
				}
				if(ke.getKeyCode() == KeyEvent.VK_DELETE) {
					add_Clicked = false;
					delete_Clicked = false;
					combine_Clicked = false;
					selectRoot_Clicked = false;
					initCombineNodes();
//					System.outprintln("308"+1313465465);
					add.setBackground((Color)new ColorUIResource(238,238,238));
					delete.setBackground((Color)new ColorUIResource(238,238,238));
					combine.setBackground((Color)new ColorUIResource(238,238,238));
					selectRoot.setBackground((Color)new ColorUIResource(238,238,238));
				}
//				System.out.println("+++++");
				if(selectedNodes !=-1 && ke.getKeyCode() == KeyEvent.VK_DELETE) {
					for(TreePanelNode child : nodes.get(selectedNodes).getChildren()) {
						Vector<TreePanelNode> nodesOfTree_child =  TreePanelNode.allNodes(child);
						child.setParent(null);
						child.setRoot(child);
						child.calculateAngle();//���ڵ��angle�ݶ�Ϊ0
						for(int k = 1;k < nodesOfTree_child.size();k++) {
							nodesOfTree_child.get(k).setRoot(child);
						}
						treeLists.add(child);//���ɸýڵ�������������뵽treeLists��
						
					}
//					nodes.get(selectedNodes).setChildren(new Vector<TreePanelNode>() );//���ýڵ���ӽڵ�ȥ��
					if(nodes.get(selectedNodes) == nodes.get(selectedNodes).getRoot()) {
						treeLists.remove(nodes.get(selectedNodes));
					}else{
						nodes.get(selectedNodes).getParent().getChildren().remove(nodes.get(selectedNodes));//ɾ���ýڵ�
					}
					
					nodes = TreePanelNode.nodesOfAllTrees(treeLists);
					
					
					
					selectedNodes = -1;
					editingNode = -1;//�༭�����п���ֱ��ɾ���ýڵ�
					System.out.println("ɾ���˸ýڵ�");
					repaint();
				}
					
				if (editingNode != -1 && ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {//������backspaceʱ ����ɾ���ַ�
					String str = nodes.get(editingNode).getValue().toString() + String.valueOf(ke.getKeyChar()).trim();
					if(str.length() != 0) {
						str = str.substring(0, str.length()-1);
						nodes.get(editingNode).setValue(str);

					}		
				}
				if(editingNode !=-1 && ke.getKeyCode() == KeyEvent.VK_ENTER) {//����enter��ʾ�������
					editingNode = -1;
				}
				
			}
			public void keyTyped(KeyEvent ke) {//����Shift��Ctrl��alt֮��������һ���ַ�������ø÷���
//				System.out.println("====");
				if (editingNode != -1  ) { 
					nodes.get(editingNode).setValue(nodes.get(editingNode).getValue().toString() + String.valueOf(ke.getKeyChar()).trim());
					repaint();		
				}
			}
			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent me) {//���¼����ᴥ��mouseMoved
				mouseX = me.getX();
				mouseY = me.getY();
//				System.out.println("��ק��");
				// ȷ�����ͣ������ѡ��ڵ������
				if (selectedNodes != -1 ) {

					if (me.isAltDown() && !me.isControlDown()&&parentOfAddingNode!=-1) {
						parentOfAddingNode = selectedNodes;
//						System.out.println("�����"+parentOfAddingNode+"���ӽڵ�");
					} else {
						parentOfAddingNode = -1;

						// ������ѡ�еĽڵ��ƶ�,�ƶ��ڵ�						
						nodes.get(selectedNodes).setLocation(
									nodes.get(selectedNodes).getLocation().x + mouseX - tempX,
									nodes.get(selectedNodes).getLocation().y + mouseY - tempY);
						nodes.get(selectedNodes).calculateAngle();//���㱻�ƶ��ڵ��븸�ڵ�ĽǶ�
		                nodes.get(selectedNodes).calculateAngleOfChildren();//���㱻�ƶ��ڵ���ӽڵ��뱻�ƶ��ڵ�֮��ĽǶ�
						
						tempX = mouseX;
						tempY = mouseY;
						System.out.println("�ƶ��˵�"+selectedNodes+"���ڵ�");
					}
					repaint();
				}
				//���޸�
//				else if(isDelete_Clicked()) {//ɾ���ڵ�֮�������
//					selectedALines = new ArrayList<Integer>();
//					for(int i=0;i<nodes.size();i++) {
//						if( nodes.get(i).isLineSelected(XBeforeDrag,YBeforeDrag,me.getX(),me.getY()) ) {
//							selectedALines.add(i);
//						}
//					}
//					repaint();
//				}
			}

			public void mouseMoved(MouseEvent me) {// ���û�е��ʱ���ƶ�
				mouseX = me.getX();
				mouseY = me.getY();
//				System.out.println(mouseX+" "+mouseY);
				// System.out.println("mouseMoved");
			}
		});
	}
	
	
	public Vector<TreePanelNode> getNodes() {
		return nodes;
	}
	
	public void setNodes(Vector<TreePanelNode> nodes) {
		this.nodes = nodes;
	}
	
	public ArrayList<TreePanelNode> getTreeLists() {
		return treeLists;
	}


	public void setTreeLists(ArrayList<TreePanelNode> treeLists) {
		this.treeLists = treeLists;
	}
	
	public boolean isAdd_Clicked() {
		return add_Clicked;
	}


	public void setAdd_Clicked(boolean add_Clicked) {
		this.add_Clicked = add_Clicked;
	}


	public boolean isDelete_Clicked() {
		return delete_Clicked;
	}


	public void setDelete_Clicked(boolean delete_Clicked) {
		this.delete_Clicked = delete_Clicked;
	}


//	public boolean isModify_Clicked() {
//		return modify_Clicked;
//	}
//
//
//	public void setModify_Clicked(boolean modify_Clicked) {
//		this.modify_Clicked = modify_Clicked;
//	}


	public boolean isCombine_Clicked() {
		return combine_Clicked;
	}


	public void setCombine_Clicked(boolean combine_Clicked) {
		this.combine_Clicked = combine_Clicked;
	}

	public JButton getAdd() {
		return add;
	}


	public void setAdd(JButton add) {
		this.add = add;
	}


	public JButton getDelete() {
		return delete;
	}


	public void setDelete(JButton delete) {
		this.delete = delete;
	}


//	public JButton getModify() {
//		return modify;
//	}
//
//
//	public void setModify(JButton modify) {
//		this.modify = modify;
//	}


	public JButton getCombine() {
		return combine;
	}


	public void setCombine(JButton combine) {
		this.combine = combine;
	}
	

	public boolean isSelectRoot_Clicked() {
		return selectRoot_Clicked;
	}


	public void setSelectRoot_Clicked(boolean selectRoot_Clicked) {
		this.selectRoot_Clicked = selectRoot_Clicked;
	}


	public JButton getSelectRoot() {
		return selectRoot;
	}


	public void setSelectRoot(JButton selectRoot) {
		this.selectRoot = selectRoot;
	}
	
	
	
	public int getSelectedNodes() {
		return selectedNodes;
	}


	public void setSelectedNodes(int selectedNodes) {
		this.selectedNodes = selectedNodes;
	}


	public int getSelectedALine() {
		return selectedALine;
	}


	public void setSelectedALine(int selectedALine) {
		this.selectedALine = selectedALine;
	}
	
	public void initCombineNodes() {
		combinedNodes[0] = -1;
		combinedNodes[1] = -1;
		count = -1;
	}
	
	
	
    public static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2)  
    {  
  
        double H = 10; // ��ͷ�߶�  
        double L = 4; // �ױߵ�һ��  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // ��ͷ�Ƕ�  
        double arraow_len = Math.sqrt(L * L + H * H); // ��ͷ�ĳ���  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)�ǵ�һ�˵�  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)�ǵڶ��˵�  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // ����  
        g2.drawLine(sx, sy, ex, ey);  
        //  
        GeneralPath triangle = new GeneralPath();  
        triangle.moveTo(ex, ey);  
        triangle.lineTo(x3, y3);  
        triangle.lineTo(x4, y4);  
        triangle.closePath();  
        //ʵ�ļ�ͷ  
//        g2.fill(triangle);  
        //��ʵ�ļ�ͷ  
        g2.draw(triangle);  
  
    }  
  
    // ����  
    public static double[] rotateVec(int px, int py, double ang,  
            boolean isChLen, double newLen) {  
  
        double mathstr[] = new double[2];  
        // ʸ����ת��������������ֱ���x������y��������ת�ǡ��Ƿ�ı䳤�ȡ��³���  
        double vx = px * Math.cos(ang) - py * Math.sin(ang);  
        double vy = px * Math.sin(ang) + py * Math.cos(ang);  
        if (isChLen) {  
            double d = Math.sqrt(vx * vx + vy * vy);  
            vx = vx / d * newLen;  
            vy = vy / d * newLen;  
            mathstr[0] = vx;  
            mathstr[1] = vy;  
        }  
        return mathstr;  
    }
	
	
	public void paint(Graphics g) {
		super.paint(g);
		FontMetrics fm = g.getFontMetrics();
		String str = "newNode";
		int width = fm.stringWidth(str)+20;
		int height = fm.getHeight()+20;	
		
		//����½ڵ�
		if(parentOfAddingNode != -1 && parentOfAddingNode != -2 ) {//���һ����Ҷ
			TreePanelNode newNode = new TreePanelNode(nodes.get(parentOfAddingNode),
					mouseX, mouseY, width, height, "newNode") ;
			newNode.setRoot(nodes.get(parentOfAddingNode).getRoot());//Ϊ�����ڵ����root�ڵ�
			newNode.calculateAngle();//Ϊ�����ڵ����kֵ
			double kOfNewNode =  newNode.getAngle();
			Vector<TreePanelNode> siblingOfNewNode = nodes.get(parentOfAddingNode).getChildren();
			int j;
			
			for ( j=0 ; j<siblingOfNewNode.size() ; j++) {//�������ڵ���뵽�丸�ڵ��vector<>children�����е��ʵ�λ��
				if( kOfNewNode > siblingOfNewNode.get(j).getAngle() ) {
					siblingOfNewNode. insertElementAt(newNode,j);
					nodes = TreePanelNode.nodesOfAllTrees(treeLists);
					selectedNodes = nodes.indexOf(newNode);
					
					//����
					for(int i=0; i<nodes.size();i++) {
						System.out.println(nodes.get(i).getValue()+"�ڵ��б�ʣ�"+nodes.get(i).getAngle());
					}
					
					break;
				}
			}
			if(j == siblingOfNewNode.size()) {
				siblingOfNewNode. insertElementAt(newNode,j);
				nodes = TreePanelNode.nodesOfAllTrees(treeLists);
				selectedNodes = nodes.indexOf(newNode);
				
				//����
				for(int i=0; i<nodes.size();i++) {
					System.out.println(nodes.get(i).getValue()+"�ڵ��б�ʣ�"+nodes.get(i).getAngle());
				}
				
			}
			parentOfAddingNode = -1;
		}

		if(parentOfAddingNode == -2) {//����һ����
			TreePanelNode newNode = new TreePanelNode(null,
					mouseX, mouseY, width, height, "newNode") ;
			newNode.setRoot(newNode);//Ϊ�����ڵ����root�ڵ�
			newNode.calculateAngle();//Ϊ�����ڵ����kֵ
			treeLists.add(newNode);
//			System.out.println("treeListsΪ�գ�"+treeLists.isEmpty());
			nodes = TreePanelNode.nodesOfAllTrees(treeLists);
//			System.out.println("nodesΪ�գ�"+nodes.isEmpty());
			selectedNodes = nodes.indexOf(newNode);
			parentOfAddingNode = -1;
		}
		
		
		for (int i = 0; i < nodes.size(); i++) {//repaint�����ø�forѭ����Ȼ������
		   {	
			if (selectedNodes == i)
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);
		   }	
		
		   {//�������еĽڵ���Vector����nodes�����ڵ�
			String value = nodes.get(i).getValue().toString();
			int wide = fm.stringWidth(value)+20;
			int high = fm.getHeight()+20;		
			nodes.get(i).setSize(wide , high );//���ݽڵ��������趨�ڵ�Ŀ��
			
//			g.drawLine(nodes.get(i).getLocation().x + nodes.get(i).getWidth() / 2,
//					nodes.get(i).getLocation().y + nodes.get(i).getHeight() / 2,
//					nodes.get(i).getParent().getLocation().x + nodes.get(i).getParent().getWidth() / 2,
//					nodes.get(i).getParent().getLocation().y );
			g.drawString(value, nodes.get(i).getLocation().x + 10, nodes.get(i).getLocation().y + 20);
			g.drawRect(nodes.get(i).getLocation().x, nodes.get(i).getLocation().y, nodes.get(i).getWidth(),
					nodes.get(i).getHeight());
		
		   }
		}
		
		for(int i = 0; i < nodes.size(); i++) {
			if (selectedALines.contains(i) )
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);
			if (nodes.get(i).getParent() != null)
				drawAL(
						nodes.get(i).getParent().getLocation().x + nodes.get(i).getParent().getWidth() / 2, 
						nodes.get(i).getParent().getLocation().y  +nodes.get(i).getParent().getHeight() , 
						nodes.get(i).getLocation().x + nodes.get(i).getWidth() / 2, 
						nodes.get(i).getLocation().y,
						(Graphics2D)g);
		}
		
		grabFocus();//������ȡ���㣬����keylistener����Ӧ
	}









}