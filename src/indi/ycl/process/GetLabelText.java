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
		//LabelText();
		SentenceFilter();

	}

	public static void LabelText() {
		CompanyDao comdao=new CompanyDao();
		ArrayList<Integer> companys=comdao.GetCompanyByIndustry();
		AnnualReportDao dao = new AnnualReportDao();
		StanfordNer ner=new StanfordNer();
		StanfordSegmenter segmenter=new StanfordSegmenter();
		int i = 8517;
		while (i <= 18172) {
			if (dao.ExistReport(i,companys)) {
				ArrayList<String> sentence = dao.getReportById(i);
				if (sentence != null) {
					for (int j = 0; j < sentence.size(); j++) {
//						List<String> segments = Ltp.ltp_segmentor(sentence.get(j));
						List<String> segments = segmenter.doSegment(sentence.get(j));
						String result=ner.doNer(listToString(segments,' '));
					
						if(result!=null){
							FileOperation.writeToFile("D:/workspace/Bom_analysis/specific1.txt",result);
							
						}
						

					}
				}

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
		ArrayList<Integer> companys=comdao.GetCompanyByIndustry();
		AnnualReportDao dao = new AnnualReportDao();
		int i = 8517;
		while (i <= 18172) {
			if (dao.ExistReport(i,companys)) {
				ArrayList<String> sentence = dao.getReportById(i);
				if (sentence != null) {
					for (int j = 0; j < sentence.size(); j++) {
						if(sentence.get(j).contains("产品")||sentence.get(j).contains("领域")||sentence.get(j).contains("服务")){
							FileOperation.writeToFile("D:/workspace/Bom_analysis/filter.txt",sentence.get(j));
						}
					
						
							
							
						
						

					}
				}

			}
			i++;

		}
		
	}

}
