package edu.hust.structuretree;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TxtFileFilter extends FileFilter {

	@Override	
	public boolean accept(File f) {
		String fileName = f.getName();		
		return f.isDirectory()||fileName.toLowerCase().endsWith(".txt");
	}

	@Override

	public String getDescription() {
		return "*.txt";
	}

}
