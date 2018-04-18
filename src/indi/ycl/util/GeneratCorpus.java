package indi.ycl.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;



import indi.ycl.dao.AnnualReportDao;
import indi.ycl.dao.CompanyDao;

public class GeneratCorpus {

	//用来生成测试语料 从一百份年报中抽
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Integer> coms=CompanyDao.GetCompanyByIndustry();
		HashSet<Integer> rep_num=new HashSet<Integer>();
		ArrayList<String> sents=new ArrayList<String>(); 
		
		int index=1;
		int num=0;
	    while(index<100){
	    	num=new Random().nextInt(21543-8517+1)+8517;
	    	if(rep_num.add(num)&&AnnualReportDao.ExistReport(num, coms)){
	    		StringBuffer sb=new StringBuffer();
	    		sents=AnnualReportDao.getReportById(num);
	    		if(sents==null||sents.size()==0){
	    			continue;
	    		}
	    		for (int i = 0; i < sents.size(); i++) {
	    			sb.append(sents.get(i).trim()+"\r\n");
				}
	    		FileOperation.writeTxtFile(sb.toString(),new File("D:/workspace/Bom_analysis/ERECorpus/" + index + ".txt"));
	    		index++;
	    	}
	    	
	    	
	    	
	    }

	}
	

}
