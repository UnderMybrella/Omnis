package org.abimon.omnis.io.filefilters;

public class FileExtensionFilter extends FileRegexFilter {

	public FileExtensionFilter(String extension){
		super(".*\\." + extension, "." + extension);
	}

}
