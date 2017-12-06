package indi.ycl.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import indi.ycl.nlp.Ltp;

public class CrfTransfor {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SegmentTextTrans();
//		LabeledTextTrans();
		
	
	}
	public static void FinalTextTrans() throws IOException{
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		FileReader fileReader1 = null;
		BufferedReader bufferedReader1 = null;
		try {
			fileReader = new FileReader("C:/Users/ycl/Desktop/yuanwen.txt");
			bufferedReader = new BufferedReader(fileReader);
			
			fileReader1 = new FileReader("D:/workspace/Bom_analysis/CRF/CRF++/train.txt");
			bufferedReader1 = new BufferedReader(fileReader1);
			try {
				String read = null;
				String read1=null;
				while ((read = bufferedReader.readLine()) != null&&(read1 = bufferedReader1.readLine()) != null) {
					List<String> words=Ltp.ltp_segmentor(read);
					List<String> tags=Ltp.postagger(words);
					for (int i = 0; i < words.size(); i++) {
						char[] chrs=words.get(i).toCharArray();
						if(chrs.length==1){
							System.out.println(words.get(i)+"	"+"S"+tags.get(i));
						}else{
							for (int j = 0; j < chrs.length; j++) {
								if(j==0){
									System.out.println(chrs[j]+"	"+"B"+tags.get(i));
								}else if (j==chrs.length-1) {
									System.out.println(chrs[j]+"	"+"E"+tags.get(i));
								}else{
									System.out.println(chrs[j]+"	"+"M"+tags.get(i));
								}
							}
						}
						
					}
					   
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
	}
	
	public static void SegmentTextTrans() throws IOException{
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader("C:/Users/ycl/Desktop/test.txt");
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
					List<String> words=Ltp.ltp_segmentor(read);
					List<String> tags=Ltp.postagger(words);
					for (int i = 0; i < words.size(); i++) {
						char[] chrs=words.get(i).toCharArray();
						if(chrs.length==1){
							System.out.println(words.get(i)+"	"+"S"+tags.get(i));
						}else{
							for (int j = 0; j < chrs.length; j++) {
								if(j==0){
									System.out.println(chrs[j]+"	"+"B"+tags.get(i));
								}else if (j==chrs.length-1) {
									System.out.println(chrs[j]+"	"+"E"+tags.get(i));
								}else{
									System.out.println(chrs[j]+"	"+"M"+tags.get(i));
								}
							}
						}
						
					}
					System.out.println();
					
						
					   
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
		
	}
	
	public static void LabeledTextTrans() throws IOException{
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader("C:/Users/ycl/Desktop/labeled.txt");
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
				
					char[] chr=read.toCharArray();	
					String yuanwen=read.replace("<B_PRODUCT>", "");
					
					List<String> words=Ltp.ltp_segmentor(yuanwen);
					List<String> tags=Ltp.postagger(words);
					
					List<String> wordtags=new ArrayList<String>();
					for (int i = 0; i < words.size(); i++) {
						char[] chrs=words.get(i).toCharArray();
						if(chrs.length==1){
							//System.out.println(words.get(i)+"	"+"S"+tags.get(i));
							wordtags.add("S"+tags.get(i));
						}else{
							for (int j = 0; j < chrs.length; j++) {
								if(j==0){
									//System.out.println(chrs[j]+"	"+"B"+tags.get(i));
									wordtags.add("B"+tags.get(i));
								}else if (j==chrs.length-1) {
									//System.out.println(chrs[j]+"	"+"E"+tags.get(i));
									wordtags.add("E"+tags.get(i));
								}else{
									//System.out.println(chrs[j]+"	"+"M"+tags.get(i));
									wordtags.add("M"+tags.get(i));
								}
							}
						}
						
					}
					int k=0;
					
					
					boolean tag1=false;
					boolean tag2=false;
					char type='0';
					for (int i = 0; i < chr.length; i++) {
						if(chr[i]=='<'){
							tag2=true;
							tag1=!tag1;
							type=chr[i+3];
							continue;
						}else if(chr[i]=='>'){
							tag2=false;
							continue;
						}
						if(!tag2){
							if(tag1){
								if(type=='C'){
									if(chr[i-1]=='>'){
										System.out.println(chr[i]+"	"+wordtags.get(k)+" B_COMPANY");
										k++;
									}else if((i+1)<chr.length&&chr[i+1]=='<'){
										System.out.println(chr[i]+"	"+wordtags.get(k)+" E_COMPANY");
										k++;
									}else{
										System.out.println(chr[i]+"	"+wordtags.get(k)+" M_COMPANY");
										k++;
									}
									
								}else{
									if(chr[i-1]=='>'){
										System.out.println(chr[i]+"	"+wordtags.get(k)+" B_PRODUCT");
										k++;
									}else if((i+1)<chr.length&&chr[i+1]=='<'){
										System.out.println(chr[i]+"	"+wordtags.get(k)+" E_PRODUCT");
										k++;
									}else{
										System.out.println(chr[i]+"	"+wordtags.get(k)+" M_PRODUCT");
										k++;
									}
									
								}
								
							}else{
								System.out.println(chr[i]+"	"+wordtags.get(k)+" O");
								k++;
							}
							
							
						}
						
					}
					System.out.println();
					   
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}
	}

}
