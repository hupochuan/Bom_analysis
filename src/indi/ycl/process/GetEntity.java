package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.CoreMapSequenceMatchAction.AnnotateAction;
import indi.ycl.dao.AnnualReportDao;
import indi.ycl.model.SegmentWord;
import indi.ycl.nlp.CRF;
import indi.ycl.nlp.Ltp;
import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;

public class GetEntity {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	    List<String> sentences=new AnnualReportDao().getReportById(9601);
//		List<String> sentences=new ArrayList<String>();
//	    sentences.add("汽车电子领域经过多年潜心耕耘，与东风日产、广汽本田、东风本田、众泰汽车、东风英菲尼迪、广汽乘用车、广汽三菱、东风启辰等前装车厂建立了稳固的合作关系，拥有一批稳定的核心客户群。");
//		sentences.add("客户涵盖了包括百得、博世、 牧田、创科等在内的主要电动工具厂商。");
	    StanfordNer ner = new StanfordNer();
	
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println(GetCompanies(sentences.get(i),ner));
			System.out.println(GetProducts(sentences.get(i)));
		}
	}



	// 由于分词和命名实体识别初始化需要加载词典花费大量时间，所以只实例化一次
	public  static List<String> GetCompanies(String sentence,StanfordNer ner) {

		List<String> result = new ArrayList<String>();
		List<SegmentWord> words=Ltp.getWordsList(sentence);
		ner.getNerList(words);
		Boolean isEnd = true;
		String company = "";

		//把前后两个同标注为组织名的词合并
		for (int i = 0; i < words.size(); i++) {
			
			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("ORGANIZATION")) {
				if (company.equals("")) {
					company = word;
				} else {
					company += word;

				}

			} else {
				if (company.equals("")) {
					continue;
				} else {
					result.add(company);
					company = "";
				}

			}
		}
		
		return result;

	}

	public  static List<String> GetProducts(String sentence) {
		List<String> products=new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		List<SegmentWord> words=Ltp.getWordsList(sentence);
	    CRF.ProductNER(words);
		Boolean isEnd = true;
		String company = "";

		//把前后两个同标注为组织名的词合并
		for (int i = 0; i < words.size(); i++) {
			
			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("PRODUCT")) {
				if (company.equals("")) {
					company = word;
				} else {
					company += word;

				}

			} else {
				if (company.equals("")) {
					continue;
				} else {
					result.add(company);
					company = "";
				}

			}
		}
		
		return result;
		
		

	}


	public static Boolean isWord(String tag1, String tag2) {

		String shunxu1 = tag1.substring(0, 1);
		String shunxu2 = tag2.substring(0, 1);
		if (shunxu1.equals("B") && shunxu2.equals("M")) {
			return true;
		} else if (shunxu1.equals("B") && shunxu2.equals("E")) {
			return true;
		} else if (shunxu1.equals("M") && shunxu2.equals("M")) {
			return true;
		} else if (shunxu1.equals("M") && shunxu2.equals("E")) {
			return true;
		} else {
			return false;
		}

	}

}
