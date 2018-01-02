package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import indi.ycl.dao.AnnualReportDao;
import indi.ycl.dao.CompanyDao;
import indi.ycl.model.Annual_Report;
import indi.ycl.nlp.Ltp;
import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;
import indi.ycl.util.FileOperation;

public class GetLabelText {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		LabelText();
		SentenceFilter();

	}

	public static void LabelText() {
		CompanyDao comdao=new CompanyDao();
//		ArrayList<Integer> companys=comdao.GetCompanyByIndustry();
		ArrayList<Integer> companys=new ArrayList<Integer>();
		companys.add(42);
		
		AnnualReportDao dao = new AnnualReportDao();		
//		StanfordSegmenter segmenter=new StanfordSegmenter();
		StanfordNer ner =new StanfordNer();
		GetEntity getentity=new GetEntity();
		int i = 8517;
		while (i <= 18172) {
			
			if (dao.ExistReport(i,companys)) {
//				String title=dao.getReportTitleById(i);
//				System.out.println(title);
				ArrayList<String> coms=new ArrayList<String>();
				ArrayList<String> pros=new ArrayList<String>();
				ArrayList<String> sentence = dao.getReportById(i);
				if (sentence != null) {
					for (int j = 0; j < sentence.size(); j++) {
						System.out.println(sentence.get(j));
						List<String> tmp1=getentity.GetCompanies(sentence.get(j),ner);
						System.out.println(tmp1);
						for (int k = 0; k < tmp1.size(); k++) {
							coms.add(tmp1.get(k));
						}
						
//						List<String> tmp2=GetEntity.GetProducts(sentence.get(j));
//						
//						for (int k = 0; k < tmp2.size(); k++) {
//							pros.add(tmp2.get(k));
//						}
						
						
						

					
					}
				}
				System.out.println(coms);
				System.out.println(pros);

			}
			i++;

		}

	}

	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
	public static void SentenceFilter() {
		CompanyDao comdao=new CompanyDao();
//		ArrayList<Integer> companys=comdao.GetCompanyByIndustry();
		ArrayList<Integer> companys=new ArrayList<Integer>();
		companys.add(42);companys.add(2334);companys.add(2961);
		AnnualReportDao dao = new AnnualReportDao();
		int i = 8517;
		while (i <= 18172) {
			if (dao.ExistReport(i,companys)) {
				ArrayList<String> sentence = dao.getReportById(i);
				if (sentence != null) {
					for (int j = 0; j < sentence.size(); j++) {
							FileOperation.writeToFile("C:/Users/ycl/Desktop/年报句子.txt", sentence.get(j));

					}
				}

			}
			i++;
			

		}
		
	}

}
