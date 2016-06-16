package org.abimon.omnis.io;

import java.util.LinkedList;

public class VirtualDirectory extends VirtualFile{
	public LinkedList<VirtualFile> subfiles = new LinkedList<VirtualFile>();
	
	public VirtualDirectory(String name) {
		super(name);
	}

	public void addSubFile(VirtualFile file){
		file.parent = this;
		subfiles.add(file);
	}
	
	public VirtualFile removeSubFile(String name){
		for(int i = 0; i < subfiles.size(); i++)
			if(subfiles.get(i) != null && subfiles.get(i).getName().equalsIgnoreCase(name)){
				return subfiles.remove(i);
			}
		
		return null;
	}
	
	/** Returns null if the file cannot be found */
	public VirtualFile search(String name){
		for(VirtualFile file : subfiles){
			if(file.name.equalsIgnoreCase(name) || file.toString().equalsIgnoreCase(name)){
				return file;
			}
			if(file instanceof VirtualDirectory){
				VirtualFile results = ((VirtualDirectory) file).search(name);
				if(results != null)
					return results;
			}
		}
		return null;
	}
	
	public LinkedList<VirtualFile> getAllSubFiles(){
		LinkedList<VirtualFile> files = new LinkedList<VirtualFile>();
		
		for(VirtualFile file : subfiles){
			if(file instanceof VirtualDirectory)
				files.addAll(((VirtualDirectory) file).getAllSubFiles());
			else
				files.add(file);
		}
		
		return files;
	}
	
	public LinkedList<VirtualDirectory> getAllSubDirs(){
		LinkedList<VirtualDirectory> files = new LinkedList<VirtualDirectory>();

		files.add(this);
		
		for(VirtualFile file : subfiles){
			if(file instanceof VirtualDirectory)
				files.addAll(((VirtualDirectory) file).getAllSubDirs());
		}
		
		return files;
	}

	public void addSubFileWithPath(VirtualFile file, String path) {
		VirtualDirectory workingDirectory = this;
		for(String s : path.split("/")){
			boolean found = s.trim().equals("");
			for(VirtualFile vFile : workingDirectory.subfiles){
				if(vFile instanceof VirtualDirectory && vFile.name.equalsIgnoreCase(s.trim())){
					workingDirectory = (VirtualDirectory) vFile;
					found = true;
					break;
				}
			}
			if(!found && !path.endsWith(s)){
				VirtualDirectory dir = new VirtualDirectory(s);
				workingDirectory.addSubFile(dir);
				workingDirectory = dir;
			}
		}
		
		workingDirectory.addSubFile(file);
	}

	public VirtualDirectory getParent() {
		return parent;
	}
}
