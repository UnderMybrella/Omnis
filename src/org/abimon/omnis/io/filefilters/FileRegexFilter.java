package org.abimon.omnis.io.filefilters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileRegexFilter extends FileFilter{
	String regex = "";
	String desc = "";
	
	public FileRegexFilter(String regex, String desc){
		this.regex = regex;
		this.desc = desc;
	}
	
	@Override
	public boolean accept(File f) {
		return f.getName().matches(regex) || f.isDirectory();
	}

	@Override
	public String getDescription() {
		return desc;
	}
}
