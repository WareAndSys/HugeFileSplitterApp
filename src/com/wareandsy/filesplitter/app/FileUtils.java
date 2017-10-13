/**
 * 
 */
package com.wareandsy.filesplitter.app;

import java.io.File;

/**
 * @author fangbe
 *
 */
public class FileUtils {
	
	private String filePath;
	private String root;
	private String ext;

	/**
	 * 
	 */
	public FileUtils(String filePath) {
		this.filePath = filePath;
		
		File f = new File(filePath);
		String name = f.getName();
		String parent = f.getParent();
		
		int pos = name.lastIndexOf('.');
		if(pos < 0){
			root = filePath;
			ext = "";
		} else {
			root = new File(parent, name.substring(0,  pos)).getPath();
			ext = name.substring(pos + 1);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFileRootPath(){
		return root;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFileExtension(){
		return ext;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	
	

}
