package edu.hust.structuretree;

import java.util.ArrayList;

public class TreeAtTxt {//һ�����ű��ʽ��Ӧһ��TreeAtTxt����
	private ArrayList<TreePanelNode>  treeListWithOneTree ;//ֻ��һ������ɭ�֣�������ʾĳ��txt�ļ��е�һ�������ű��ʽ��ʾ����
	private String txtPath;
	public TreeAtTxt() {
	}

	public TreeAtTxt(ArrayList<TreePanelNode> treeListWithOneTree) {
		this.treeListWithOneTree = treeListWithOneTree;
	}
	public TreeAtTxt(TreePanelNode treeRoot) {
		treeListWithOneTree.add(treeRoot);
	}
	public TreeAtTxt(TreePanelNode treeRoot,String txtPath) {
		this.treeListWithOneTree = new ArrayList<TreePanelNode>();
		treeListWithOneTree.add(treeRoot);
		this.txtPath = txtPath;
	}
	/*
	 * 
	 *   �÷�������txt�ļ������ű��ʽ��ɵ�һ��Stringת��Ϊһ����TreeAtTxt���󣬲���װ��ArrayList�С�
	 *   TreeAtTxt��txtPath����Ϊ���ļ�·����treeListWithOneTree����Ϊ����һ�����ű��ʽ��Ӧ��ɭ�֡�
	 *   
	 * */

	public ArrayList<TreeAtTxt> getAllTreeListsOfOneTxt(String strOfExpressions,String txtPath){
		ArrayList<TreeAtTxt> treeLists = new ArrayList<TreeAtTxt>();
		ArrayList<TreePanelNode> trees = new TreePanelNode().fromTextToTree(strOfExpressions);
		if (trees == null)
			return null;
		for (TreePanelNode tree : trees) {
			treeLists.add(new TreeAtTxt(tree, txtPath));
		}
		return treeLists;
	}
	
	public  String treePositionAtTxt(ArrayList<TreeAtTxt> allTreesAtTxt) {
		String txtPath = this.txtPath;
		int count = 0;
		int position = 0;
		for(int i = 0 ; i < allTreesAtTxt.size();i++) {
			if(allTreesAtTxt.get(i).getTxtPath().equals(txtPath)) {
				count ++;
				if(allTreesAtTxt.get(i).equals(this))
					position = count;
			}else if(count != 0) {
				break;
			}
			
		}
		return position + " / "+count;
	}
	
	
	public ArrayList<TreePanelNode> getTreeListWithOneTree() {
		return treeListWithOneTree;
	}
	public void setTreeListWithOneTree(ArrayList<TreePanelNode> treeListWithOneTree) {
		this.treeListWithOneTree = treeListWithOneTree;
	}
	public String getTxtPath() {
		return txtPath;
	}
	public void setTxtPath(String txtPath) {
		this.txtPath = txtPath;
	}
	
}
