package edu.hust.structuretree;

import java.math.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

public class TreePanelNode  {
	private Vector<TreePanelNode> children;
	private TreePanelNode parent;
	private TreePanelNode root;
	private int x, y, width, height;
	private Object value;
	private double angle;// �ýڵ��븸�ڵ�֮��ġ��нǡ������ڵ㲻�ü���
	
	
    //�����ù��캯��
	public TreePanelNode(TreePanelNode parent, int x, int y, int width, int height, Object value) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = width;
		this.height = height;
		this.value = value;
		this.children = new Vector<TreePanelNode>();
		if (parent == null)
			this.root = this;
		else {			
			this.root = parent.getRoot();
		}
	}
	
//	public TreePanelNode(TreePanelNode parent,Object value) {
//		this.parent = parent;
//		this.value = value;
//		if (parent == null)
//			root = this;
//		else {
//			parent.addChild(this);
//		}
//	}
	public TreePanelNode() {	
	}

	/*
	 *
	 * 
	 * �ж�����Ҷ�ӽڵ��Ƿ��û���ֵܽڵ�(�ڵ������ű��ʽʱ������Ҷ�ӽڵ����ֵܽڵ�)
	 * */
	public boolean examTheTree() {
		TreePanelNode root = this.getRoot();
		for(TreePanelNode node :allNodes(root)) {
			if(node.getChildren().isEmpty())
				if(node.hasSibling()) {
					return false;
				}
		}
		return true;
        	
	}
	
	public boolean hasSibling() {
		if(this.equals(this.getRoot()))
			return false;
		if(this.getParent().getChildren().size() != 1) 
			return true;
		else return false;
	}
	
	
	public static Vector<TreePanelNode> nodesOfAllTrees(ArrayList<TreePanelNode> treeLists){
		Vector<TreePanelNode> nodes = new Vector<TreePanelNode>();
		int i = 0;
		for(TreePanelNode aRoot:treeLists) {//ÿѭ��һ�ξͽ�һ���������еĽڵ�ŵ�nodes�У�i��ʾnodes�Ľڵ����
			if(aRoot == null)
				continue;
			
			nodes.add(aRoot);
			for (; i < nodes.size(); i++) {
				if (nodes.get(i).children.size() !=0) {
					for (TreePanelNode child : nodes.get(i).children) {
						nodes.add(child);
					}
				}
			}
		}
		return nodes;
	}
	
	//������root�ڵ�Ϊ���ڵ�����нڵ��Vector���У�����ͨ����������õ�
	public static Vector<TreePanelNode> allNodes(TreePanelNode root) {
		Vector<TreePanelNode> nodes = new Vector<TreePanelNode>();
		if (root == null)
			return nodes;
		else {
			nodes.add(root);
			int i = 0;
			for (; i < nodes.size(); i++) {
				if (nodes.get(i).children.size() !=0) {
					for (TreePanelNode child : nodes.get(i).children) {
						nodes.add(child);
					}
				}
			}
			return nodes;
		}
	}
	
	//����һ���ڵ㣬������ڵ���ӽڵ㰴����angle�Ĵ�С�Ӹߵ�������
	
	public  Vector<TreePanelNode> sortByAngle(){
		int length = children.size();
//		System.out.println(length);
		if( length != 0 ) {
			int i = 1;
			for(;i < length;i ++) {
				for(int j = 0;j < length - i;j++) {
					if(children.elementAt(j).getAngle() < children.elementAt(j+1).getAngle()) {
						TreePanelNode temp = children.elementAt(j);
						children.set(j, children.elementAt(j+1));
						children.set(j+1, temp);					
					}
				}
			}
		} 
		return children;
	}
	
	
	// ����ýڵ��븸�ڵ�֮��ġ��нǡ�
	//���ڵ�ĺ��ӽڵ�����Vector�У�Vector�����С�Ľڵ����ֳ��ڵ�,
	//�ֳ��ڵ�����ֵܽڵ��븸�ڵ��"�н�"���нǴ�pi/2��-3pi/2.����y�����£��ʸüн��������ļнǶ���Щ��һ����
	//�ӽڵ����Ը��ڵ�Ϊԭ�������������ĵڶ�����ʱ���н�Ϊpi/2->0;�ڵ������ޣ��н�Ϊ0->-pi/2;�ڵ������ޣ��н�Ϊ-pi/2->-pi;�ڵ�һ���ޣ��н�Ϊ-pi->-3pi/2.
	//�нǴ�С��ʾһ���Ⱥ��ϵ
	public void calculateAngle() {
		if (this.parent != null) {
			if ( (this.x+ this.width/2) != (this.parent.x+ this.parent.width/2) ){
				double xChild = this.x + this.width/2;
				double yChild = this.y + this.height/2;
				double xParent = this.parent.x + this.parent.width/2;
				double yParent = this.parent.y + this.parent.height/2;
				if (xChild < xParent && yChild <= yParent)
					this.angle = Math.atan((yParent - yChild) / (xParent - xChild)) ;
				if (xChild < xParent && yChild > yParent)
					this.angle = Math.atan((yParent - yChild) / (xParent - xChild));
				if (xChild > xParent && yChild >= yParent)
					this.angle = Math.atan((yParent - yChild) / (xParent - xChild)) - Math.PI;
				if (xChild > xParent && yChild < yParent)
					this.angle = Math.atan((yParent - yChild) / (xParent - xChild)) - Math.PI;
			}
			else 
				if(y + this.height/2 > this.parent.y + this.parent.height/2)
					this.angle = -Math.PI/2;
				else
					this.angle = -3*Math.PI/2;
		}else {
			this.angle = 0;//���ڵ��angle��ʱ��λ0
		}
	}
	
	/*
	 *����ýڵ�������ӽڵ���ýڵ�ĽǶ�
	 * */
	public void calculateAngleOfChildren() {
		int length = this.children.size();
		if (length !=0) {
			for(int i = 0;i< length; i++) {
				children.elementAt(i).calculateAngle();
			}
		}
	}
	
	
	/*
	 * 
	 *ɾ���ýڵ����丸�ڵ�Ĺ�ϵ��ʹ�øýڵ��Ϊ�����ĸ��ڵ� 
	 * 
	 * */
	public void relieveRelationship() {
		if(this != this.root) {
			this.parent.getChildren().remove(this);
			this.angle = 0;
			this.parent = null;
			for(TreePanelNode aNode:allNodes(this)) {
				aNode.setRoot(this);
			}
		}
	}
	
	
	/*
	 * 
	 * 
	 * �жϸýڵ����丸�ڵ�������Ƿ�ѡ��
	 * 
	 * */
	public boolean isLineSelected(int X1,int Y1,int X2,int Y2) {
		if(this == this.root) return false;
		if(Math.min(X1, X2) <= Math.max(x+width/2,parent.x+parent.width/2) 
				&& Math.max(X1, X2) >= Math.min(x+width/2,parent.x+parent.width/2) 
				&& Math.min(Y1,Y2) <= Math.max(y,parent.y+parent.height)
				&& Math.max(Y1,Y2) >=Math.min(y,parent.y+parent.height)) {
			return true;
		}else
			return false;
	}
	
	/*
	 * ��ѡ�нڵ��޸�Ϊ�ýڵ��������ĸ��ڵ�
	 * */
	public TreePanelNode changeRoot() {
		TreePanelNode preRoot = this.root;
		if( this.equals(this.root) ) return preRoot;
		//���нڵ�����޸���rootֵ
		
		for(TreePanelNode node: allNodes(preRoot))
			node.setRoot(this);
		//1.�޸ı�ѡ�нڵ�	
		this.setAngle(0);
		this.children.add(this.parent);
	    
		
		//2.�޸ı�ѡ�ڵ���ԭroot·�������У���������ѡ�ڵ㼰ԭroot���Ľڵ�
		TreePanelNode cursor = this.getParent(),begin ,preParent;
		begin = this;

		for(;!cursor.equals(preRoot); begin = cursor,cursor = preParent) {
			preParent = cursor.getParent();
			cursor.setParent(begin);
			cursor.getChildren().remove(begin);
			cursor.getChildren().add(preParent);
			cursor.calculateAngle();
			cursor.sortByAngle();
		}
		this.setParent(null);
		this.sortByAngle();
		
		//3.�޸�ԭroot
		preRoot.setParent(begin);
		preRoot.getChildren().remove(begin);
		preRoot.calculateAngle();
		preRoot.sortByAngle();
		
		//�����ڵ��ֻ��Ҫ�޸���root
		
		return this;
	}
	/*
	 * ���������ȸ������õ�һ������
	 * */
	public  Vector<TreePanelNode> traverse_rootFisrt(TreePanelNode theNode,Vector<TreePanelNode> nodes_rootFirst){
		nodes_rootFirst.add(theNode);
		if (theNode.getChildren().size() != 0) {
			for (TreePanelNode child : theNode.getChildren()) {
				traverse_rootFisrt(child, nodes_rootFirst);
			}
		}
		
		return nodes_rootFirst;
	}
	/*
	 * �жϸ�Ҷ�ӽڵ������ϰ��ȸ������ĵڼ���Ҷ�ӽڵ�
	 * */
	public int getIndexOfLeaf(TreePanelNode leaf) {
		int count = 0;
		TreePanelNode rootOfLeaf = leaf.getRoot();
		if(leaf.children.size() != 0) 
			return 0;
		Vector<TreePanelNode> nodes_rootFirst = new Vector<TreePanelNode>();
		nodes_rootFirst = traverse_rootFisrt(rootOfLeaf,nodes_rootFirst);  
		for (TreePanelNode theLeaf : nodes_rootFirst) {
			if (theLeaf.getChildren().size() == 0) {
				count++;
				if (theLeaf.equals(leaf))
					return count;
			}
		}
		return count;
	} 
	
	/*
	 * ����ָ���ڵ������ϵĸ߶�
	 * */
	public int height() {//���ڵ�߶�Ϊ0
		TreePanelNode cursor = this;
		int count =0;
		while(!cursor.equals(this.root)) {
			count++;
			cursor = cursor.parent;
		}
		return count;
	}
	
	
	
	/*
	 * ��ָ���ĵ���������λ�ã��������Ƕȡ����ú��������
	 * */
	public static TreePanelNode allocatePosition(TreePanelNode root) {
//		if(!root.equals(root.getRoot()))
//			return null;
		
		if (root.getChildren().size() != 0) {
			int countOfChild = 0;
			int rootX = 0,rootY = 0,rootWidth = 60,rootHeight = 30;
			for (TreePanelNode child : root.getChildren()) {
				countOfChild ++;//�жϺ��ӵĸ���
				allocatePosition(child);
			}
			
			for(TreePanelNode child : root.getChildren()) { 
				rootX += child.getX();
			}
			root.setX(rootX / countOfChild);
			root.setY(80*root.height()); 
			root.setHeight(rootHeight);
			root.setWidth(rootWidth);	
		}else {//��root��Ҷ�ӽڵ�
			root.setX(80*root.getIndexOfLeaf(root));
			root.setY(80*root.height());
			root.setHeight(30);
			root.setWidth(60);	
		}
		return root;
	}
	
	
	/*
	 * ��ָ����ɭ�ַ���λ�ã��������Ƕȡ����ú��������
	 * */
public static ArrayList<TreePanelNode> repaintTreeLists(ArrayList<TreePanelNode> treeLists){
		if (treeLists.size() == 0 || treeLists == null)
			return new ArrayList<TreePanelNode>();
		int count =0;//��ʾɭ���еĵ�count����
		for (TreePanelNode tree : treeLists) {		
			if(count == 0)
				allocatePosition(tree);
			else {
				allocatePosition(tree);
				Vector<TreePanelNode> nodesOfTree = allNodes(treeLists.get(count - 1));
				int  modificationY = nodesOfTree.get(nodesOfTree.size()-1).getY() + 80;
				for(TreePanelNode node:allNodes(tree))
					node.setY(node.getY() + modificationY);
			}
			count++;
		}
		for(TreePanelNode node : nodesOfAllTrees(treeLists))
			node.calculateAngle();
		return treeLists;
}
	
	/*
	 * 
	 * 
	 * ��һ�����ű��ʽת��Ϊһ����
	 * 
	 * 
	 * 
	 * */
	//�����ű��ʽת��Ϊ����������λ�úͳ�ʼ���Ƕ�
	public TreePanelNode fromTextToTree(String  strWithFormat) {
		if(toOneLine(strWithFormat).trim().length() == 0) return null;
		TreePanelNode root =  allocatePosition(getTree(stringToList(format(toOneLine(strWithFormat)))));
		for(TreePanelNode node:allNodes(root)) {
			node.calculateAngle();
		}
		return root;
	}
	
	//���������ű��ʽת��Ϊ���е�
	public String toOneLine(String str) {
		return str.replaceAll("\n", "").replaceAll("\r", "");
	}
	
	//�������ű��ʽ����(ROOT  (IP    (NP (NT ȥ��      ))    (NP (NR �Ϳ� ))    (VP      (PP (P ��)        (NP          (NP            (DNP              (NP (NR ������))              (DEG ��))            (NP (NN ����) (NN �ۼ���)))          (NP (NR �պ���))))      (VP (VV ��) (AS ��)        (NP          (QP (CD һ)            (CLP (M ��)))          (NP (NN �µ�)))))    (PU ��)))
	//������(ROOT(IP(NP(NT(ȥ��)))(NP(NR(�Ϳ�)))(VP(PP(P(��))(NP(NP(DNP(NP(NR(������)))(DEG(��)))(NP(NN(����))(NN(�ۼ���))))(NP(NR(�պ���)))))(VP(VV(��))(AS(��))(NP(QP(CD(һ))(CLP(M(��))))(NP(NN(�µ�))))))(PU(��))))
	//��ʽ���������ű��ʽ���������пո�
	public String format(String str) { 
		str = str.trim();
		for(int i=0;i<str.length();i++) {
			if(str.charAt(i) == ' ') {
				if( (str.charAt(i-1) =='(') || (str.charAt(i-1) == ')') || str.charAt(i+1) == '(' || str.charAt(i+1) == ')'|| str.charAt(i+1) ==' ' ){
				str =str.substring(0, i) + str.substring(i+1, str.length());
				i--;
 				}else {
 					str = str.replaceFirst(" ", "(");
 					for(int j = i+1;j < str.length() ; j++) {
 						if(str.charAt(j) ==' ') {
 							str = str.replaceFirst(" ", ")");
 							i = j;
 							break;
 						}else if(str.charAt(j) == ')') {
 							str = str.substring(0, j) + ")" + str.substring(j, str.length());
 							i = j;
 							break;
 						}
 					}
 				}					
			}
		}
		return str;
	}
	
	//����ʽ��������ű��ʽ��װ��ArrayList��
	public ArrayList<String> stringToList(String str){ 
		ArrayList<String> list =new ArrayList<String>();
		char[] chars = str.toCharArray();
		for(int i = 0, j = 0 ;i < chars.length; i++) {
			if (chars[i] =='(' || chars[i] == ')') {
				if(i == j) {
					list.add(String.valueOf(chars,j,1));
				}else {
					if(!String.valueOf(chars, j+1, i-j-1).equals(""))
						list.add(String.valueOf(chars, j+1, i-j-1));
					list.add(String.valueOf(chars, i, 1));
					j = i;
					
				}
			}
		}
		return list;
	}
	
	public ArrayList<String> stringToList2(String treeStr){

		ArrayList<String> parts = new ArrayList<String>();
        for (int index = 0; index < treeStr.length(); ++index) {
            if (treeStr.charAt(index) == '(' || treeStr.charAt(index) == ')' || treeStr.charAt(index) == ' ') {
                parts.add(Character.toString(treeStr.charAt(index)));
            } else {
                for (int i = index + 1; i < treeStr.length(); ++i) {
                    if (treeStr.charAt(i) == '(' || treeStr.charAt(i) == ')' || treeStr.charAt(i) == ' ') {
                        parts.add(treeStr.substring(index, i));
                        index = i - 1;
                        break;
                    }
                }
            }
        }
        return parts;
	}
	
	//�����ű��ʽ��listת��Ϊһ����,���нڵ��angle��λ����Ϣû��ȷ��
	public TreePanelNode getTree(ArrayList<String> list) {
		Stack<TreePanelNode> words = new Stack<TreePanelNode>();
		TreePanelNode root = new TreePanelNode();
	    int count =0;//������������һ���ж��ٽڵ�
		
		int len = list.size();
		for(int i =0 ;i<len ;i++) {//ɨ��list��
			if(list.get(i).equals("("))//ɨ�赽(
				continue;
			else{
				if (!(list.get(i).equals(")"))) {//ɨ�赽word
					if (words.isEmpty()) {//ɨ�赽��һ��word�������ڵ��value
						root.setRoot(root);
						root.setParent(null);
						root.setChildren(new Vector<TreePanelNode>());
						root.setValue(list.get(i));
						words.push(root);
						count++;
					} else {//ɨ�赽�����word
						TreePanelNode child = new TreePanelNode();
						child.setRoot(root);
						child.setParent(words.peek());
						words.peek().getChildren().add(child);
						child.setChildren(new Vector<TreePanelNode>());
						child.setValue(list.get(i));
						words.push(child);
						count++;
					}
				} else {//ɨ�赽 )
					if( ! words.isEmpty() ) {
						words.pop();
					}
				}
				
			}
		}
		if(words.isEmpty() && count !=0)
			return root;
		else
			return null;
		
	}
	
	
	
	/*
	 * 
	 * 
	 * 
	 * ����ת��Ϊ���ű��ʽ
	 * */
	//����ת��Ϊһ���ַ������飬���а�����ӵ����ڻ��кͿո���ַ���
	public  LinkedList<String> changeIntoText(){

		LinkedList<String> tree = new LinkedList<String>();
		if(treeToText(this,tree) != -1 ) {//��ʾ���ǿ���
			tree.set(0, "(");
			return tree;
		}
		else
			return null;
	}
	
//	//����ת��Ϊ���е����ű��ʽ
//	public  LinkedList<String> changeIntoList(){
//		
//		LinkedList<String> tree = new LinkedList<String>();
//		if(treeToList(this,tree) != -1)
//			return tree;
//		else
//			return null;
//	}
	
	
	
	
	//�����Ľڵ���뵽list�У������ظ��ڵ���list�е��������������з��Ϳո񣬱��ڽ�list������ı��г�Ϊһ�������и�ʽ�����ű��ʽ��
	public int treeToText(TreePanelNode root, LinkedList<String> listOfTree) {// �ú�����ʾ��rootΪ���������к���list�У�������root�ڵ���list�е�������ͬʱ�жϸø��ڵ��Ƿ��ж��ӽڵ㣬
		// ����ýڵ��ж��ӽڵ��Ҷ��ӽڵ㲻��Ҷ�ӽڵ����list��root��'('ǰ���뻻�з���һ���Ŀո���ɵ��ַ�����
		if (root == null) {			
			return -1;
		}
			
		int indexOfFirstChild = -1;
		int indexOfRoot = -1;
		if (root.getChildren().size() != 0) {
			int count = 0;
			for (; count < root.getChildren().size();) {// ��������root�Ķ���Ϊ���ڵ���������뵽listOfTree��ȥ�������ص�һ��������listOfTree�е�����
				count++;
				if (count == 1) {
					indexOfFirstChild = treeToText(root.getChildren().get(count - 1), listOfTree);
				} else {
					treeToText(root.getChildren().get(count - 1), listOfTree);
				}
			}
			// ������ڵ�
			System.out.println("indexOfFirst====" + indexOfFirstChild);
	
			if (!isInsertString(root.getChildren().get(0))) {// �ж�root�ĵ�һ�����ӽڵ�ǰ���Ƿ���ӵ�����ʽ���ַ�����if������ʾû�����			
				if(!isInsertString(root)) {//�ж�root�ڵ�ǰ���Ƿ�����Ӹ�ʽ�ַ�����δ���
					listOfTree.add(indexOfFirstChild - 1,root.getValue().toString());                                                                 
			        listOfTree.add(indexOfFirstChild - 1," (");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
			        listOfTree.add(")");
			        indexOfRoot = indexOfFirstChild;
				}else {//root�ڵ�ǰ��Ҫ��Ӹ�ʽ�ַ���
					listOfTree.add(indexOfFirstChild - 1,root.getValue().toString());                                                                 
			        listOfTree.add(indexOfFirstChild - 1," (");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
			        String format = "";
			        for(int i=0;i < root.height();i++) {
			        	format = format + " ";
			        	format = format + " ";
				        } 
			        if(root.height() != 0)
			        	format = format.substring(0,format.length()-1);
			        listOfTree.add(indexOfFirstChild-1, "\r\n"+format);
			        listOfTree.add(")");
			        indexOfRoot = indexOfFirstChild+1;
				}
			}else {
				//���ж�root�ڵ�ǰ���Ƿ�����Ӹ�ʽ�ַ���
				if(!isInsertString(root)) {//root�ڵ�ǰ�治����Ӹ�ʽ�ַ���
					listOfTree.add(indexOfFirstChild - 2,root.getValue().toString());                                                                 
			        listOfTree.add(indexOfFirstChild - 2," (");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
			        listOfTree.add(")");
			        indexOfRoot = indexOfFirstChild -1;
				}else {//root�ڵ�ǰ��Ҫ��Ӹ�ʽ�ַ���
					listOfTree.add(indexOfFirstChild - 2,root.getValue().toString());                                                                 
			        listOfTree.add(indexOfFirstChild - 2," (");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
			        String format = "\r\n";
			        for(int i=0;i < root.height();i++) {
			        	format = format + "  ";
				    } 
			        if(root.height() != 0)
			        	format = format.substring(0,format.length()-1);
			        listOfTree.add(indexOfFirstChild-2, format);
			        listOfTree.add(")");
			        indexOfRoot = indexOfFirstChild;
				}
			}

		} else {// ����root��Ҷ�ӽڵ㣬�����ݹ������
//			listOfTree.add("(");//
			listOfTree.add(" ");//��Ҷ�ӽڵ���ߵ�����ɾ��
			listOfTree.add(root.getValue().toString());
			indexOfFirstChild = listOfTree.size() - 1;
//			listOfTree.add(")");
			indexOfRoot = indexOfFirstChild;
		}

		return indexOfRoot;
	}
	
	//�ж�list�иýڵ�֮ǰ�Ƿ�Ҫ�����ʽ�ַ���
	private boolean isInsertString(TreePanelNode node) {
		if (node.equals(node.getRoot()))
			return false;
		if (node == null)
			return false;
		if (node.getChildren().isEmpty())
			return false;
		else if (!node.getChildren().get(0).getChildren().isEmpty())
			return true;
		else {		
				for (TreePanelNode siblingBeforeNode : node.getParent().getChildren()) {
					if (siblingBeforeNode.equals(node))
						break;
					if (isInsertString(siblingBeforeNode))
						return true;
				}
				return false;		
		}
	}
	
	//�����Ľڵ���뵽list�У������ظ��ڵ���list�е��������������з��Ϳո񣬱��ڽ�list������ı��г�Ϊһ�������и�ʽ�����ű��ʽ��
		public  int treeToText2(TreePanelNode root,LinkedList<String> listOfTree){//�ú�����ʾ��rootΪ���������к���list�У�������root�ڵ���list�е�������ͬʱ�жϸø��ڵ��Ƿ��ж��ӽڵ㣬
			                                                                     // ����ýڵ��ж��ӽڵ��Ҷ��ӽڵ㲻��Ҷ�ӽڵ����list��root��'('ǰ���뻻�з���һ���Ŀո���ɵ��ַ�����
			if(root == null) return -1;
			int indexOfFirstChild = -1;
			int indexOfRoot = -1;
			if(root.getChildren().size() != 0) {
				int count =0;
				for(;count < root.getChildren().size();) {//��������root�Ķ���Ϊ���ڵ���������뵽listOfTree��ȥ�������ص�һ��������listOfTree�е�����
					count++;
					if(count == 1) {
						indexOfFirstChild=treeToText(root.getChildren().get(count - 1),listOfTree);
					}else {
						treeToText(root.getChildren().get(count - 1),listOfTree);
					}
				}
				//������ڵ�
				System.out.println("indexOfFirst===="+indexOfFirstChild);
				//�ж�root�ĵ�һ�����ӽڵ��Ƿ��ж��ӣ����ж�list��root�ĵ�һ�����ӽڵ�ǰ�Ƿ������һ���ɻ��з��Ϳո���ɵ��ַ���
				if(root.getChildren().get(0).getChildren().size() == 0 || root.getChildren().get(0).getChildren().get(0).getChildren().size() == 0 ) {//if������ʾroot�ĵ�һ�����ӽڵ�û�ж��ӣ�����root�ĵ�һ�����ӽڵ�ĺ�����Ҷ�ӽڵ㣬root�ĵ�һ�����ӽڵ�ǰ��û����ӵ�����ʽ���ַ���
					//�жϸ�root�ڵ�Ķ��ӽڵ��Ƿ���Ҷ�ӽڵ㣬(�����if�Ѿ��жϳ���root�ڵ��ж���)�����ж�root�ڵ�ǰ���Ƿ�����Ӹ�ʽ�ַ���
					if(root.getChildren().get(0).getChildren().size() == 0) {//��Ҷ�ӽڵ㣬root�ڵ�ǰ�治����Ӹ�ʽ�ַ���
						listOfTree.add(indexOfFirstChild - 1,root.getValue().toString());                                                                 
				        listOfTree.add(indexOfFirstChild - 1,"(");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
				        listOfTree.add(")");
				        indexOfRoot = indexOfFirstChild;
					}else {//����Ҷ�ӽڵ㣬root�ڵ�ǰ��Ҫ��Ӹ�ʽ�ַ���
						listOfTree.add(indexOfFirstChild - 1,root.getValue().toString());                                                                 
				        listOfTree.add(indexOfFirstChild - 1,"(");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
				        String format = "";
				        for(int i=0;i < root.height();i++) {
				        	format = format + " ";
				        	format = format + " ";
 				        } 
				        listOfTree.add(indexOfFirstChild-1, "\r\n"+format);
				        listOfTree.add(")");
				        indexOfRoot = indexOfFirstChild+1;
					}					
				}else {//root�ĵ�һ�����ӽڵ�ǰ������˵�����ʽ���ַ���
					//�жϸ�root�ڵ�Ķ��ӽڵ��Ƿ���Ҷ�ӽڵ㣬(�����if�Ѿ��жϳ���root�ڵ��ж���)�����ж�root�ڵ�ǰ���Ƿ�����Ӹ�ʽ�ַ���
					if(root.getChildren().get(0).getChildren().size() == 0) {//��Ҷ�ӽڵ㣬root�ڵ�ǰ�治����Ӹ�ʽ�ַ���
						listOfTree.add(indexOfFirstChild - 2,root.getValue().toString());                                                                 
				        listOfTree.add(indexOfFirstChild - 2,"(");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
				        listOfTree.add(")");
				        indexOfRoot = indexOfFirstChild -1;
					}else {//����Ҷ�ӽڵ�,root�ڵ�ǰ��Ҫ��Ӹ�ʽ�ַ���
						listOfTree.add(indexOfFirstChild - 2,root.getValue().toString());                                                                 
				        listOfTree.add(indexOfFirstChild - 2,"(");//root��index�͵�һ�����ӷ��ص�index��С��ͬ
				        String format = "";
				        for(int i=0;i < root.height();i++) {
				        	format = format + " ";
				        	format = format + " ";
 				        } 
				        listOfTree.add(indexOfFirstChild-2, "\r\n"+format);
				        listOfTree.add(")");
				        indexOfRoot = indexOfFirstChild;
					}
				}
				
			}else {//����root��Ҷ�ӽڵ㣬�����ݹ������
				listOfTree.add("(");
				listOfTree.add(root.getValue().toString());
				indexOfFirstChild = listOfTree.size() -1;
				listOfTree.add(")");
				indexOfRoot = indexOfFirstChild;
			}
		    
			return indexOfRoot;
		}
		
		//�����Ľڵ���뵽list�У������ظ��ڵ���list�е�������û�л��з��Ͷ���ո�
		public  int treeToList(TreePanelNode root,LinkedList<String> listOfTree){//��rootΪ���������к���list�У�������root�ڵ���list�е�����
			if(root == null) return -1;
			int indexOfFirst = -1;
			if(root.getChildren().size() != 0) {
				int count =0;
				for(;count < root.getChildren().size();) {//��������root�Ķ���Ϊ���ڵ���������뵽listOfTree��ȥ�������ص�һ��������listOfTree�е�����
					count++;
					if(count == 1) {
						indexOfFirst=treeToList(root.getChildren().get(count - 1),listOfTree);
					}else {
						treeToList(root.getChildren().get(count - 1),listOfTree);
					}
				} 
				//������ڵ�
//				System.out.println("indexOfFirst===="+indexOfFirst);
				listOfTree.add(indexOfFirst - 1,root.getValue().toString());
				listOfTree.add(indexOfFirst - 1,"(");//root��index�͵�һ�����ӷ��ڵ�index��С��ͬ
				listOfTree.add(")");
			}else {//����Ҷ�ӽڵ㣬�����ݹ������
				listOfTree.add("(");
				listOfTree.add(root.getValue().toString());
				indexOfFirst = listOfTree.size() -1;
				listOfTree.add(")");
			}
		
			return indexOfFirst;
		}
		
		
	public void addChild(TreePanelNode n) {
		addChild(children.size(), n);
	}

	public void addChild(int index, TreePanelNode n) {
		children.add(index, n);
		n.parent = this;
	}

	public void removeChild(TreePanelNode n) {
		children.remove(n);
	}

	public void removeChild(int index) {
		children.remove(index);
	}
	
	
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point getLocation() {
		return new Point(x, y);
	}	
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public Vector<TreePanelNode> getChildren() {
		return children;
	}

	public void setChildren(Vector<TreePanelNode> children) {
		if(children !=null) 
			this.children = children;
		else 
			this.children = new Vector<TreePanelNode>();
	}

	public TreePanelNode getParent() {
		return parent;
	}

	public void setParent(TreePanelNode parent) {
		this.parent = parent;
	}

	public TreePanelNode getRoot() {
		return root;
	}

	public void setRoot(TreePanelNode root) {
		this.root = root;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
	this.angle = angle;
}
	
	//����������Ļ���ֵ
	public void print() {
		//����
		int count=-1;
		for(TreePanelNode theNode:allNodes(this.getRoot())) {
			count++;
			System.out.println("count:"+count+theNode.getValue()+"��x,yΪ��"+theNode.getX()+","+theNode.getY()+"�Ƕ���"+theNode.getAngle());
			if(!theNode.getChildren().isEmpty())
				System.out.println("firstChlild:"+theNode.getChildren().get(0).getValue().toString());
			if(theNode.getParent() != null)System.out.println("parent"+theNode.getParent().getValue().toString());
		}
	}
	
}