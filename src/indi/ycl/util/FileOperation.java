package indi.ycl.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileOperation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void writeToFile(String path,String content){
	     
	    try {
	       
	        File file = new File(path);
	        if(file.exists()){
	            FileWriter fw = new FileWriter(file,true); 
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(content+"\r\n");
	            bw.close(); fw.close();
	            System.out.println("done!");
	        }
	         
	    } catch (Exception e) {
	        // TODO: handle exception
	    }
	}

}
