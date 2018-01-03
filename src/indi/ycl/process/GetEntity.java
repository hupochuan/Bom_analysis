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
		StanfordNer ner = new StanfordNer();
	
		for (int i = 0; i < sentences.size(); i++) {
			System.out.println(GetCompanies(sentences.get(i),ner));
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
		List<String> result = new ArrayList<String>();
		List<String> words = Ltp.ltp_segmentor(sentence);
		List<String> tags = Ltp.postagger(words);
		List<String> words1 = new ArrayList<String>();
		List<String> tags1 = new ArrayList<String>();
		for (int i = 0; i < words.size(); i++) {
			char[] chrs = words.get(i).toCharArray();
			if (chrs.length == 1) {
				words1.add("" + words.get(i));
				tags1.add("B" + tags.get(i));

			} else {
				for (int j = 0; j < chrs.length; j++) {
					if (j == 0) {
						words1.add("" + chrs[j]);
						tags1.add("B" + tags.get(i));
					} else if (j == chrs.length - 1) {
						words1.add("" + chrs[j]);
						tags1.add("E" + tags.get(i));
					} else {
						words1.add("" + chrs[j]);
						tags1.add("M" + tags.get(i));
					}
				}
			}

		}

		List<String> labelchrs = new CRF().ProductNER(words1, tags1);
		if (labelchrs.size() == 0) {
			return null;
		}
		// 根据前后文的标注对O重新进行标注
		int i = 0;
		Boolean findpro = false, findtail = false;
		int head = -1, tail = -1, pretail = -1;
		String[] filter = new String[] { "wp", "u", "p", "nl", "q" };
		while (i < labelchrs.size()) {	
			
			String label = labelchrs.get(i);
			String tag = tags1.get(i);
			//System.out.println(words1.get(i)+" "+label);
			
			if (!findpro) {
				if (label.equals("B_PRODUCT")) {
					findpro = true;
					head = i;
				} else if (label.equals("M_PRODUCT")) {
				  
					int j = i - 1;
					while (j >= (pretail > 0 ? pretail : 0)) {

						// 获取ltp标注的词性
						String l = tags1.get(j);
						String cixing = l.substring(1, l.length());
						// 判断词性，若不可能出现在产品名中，break
						if (cixing.equals("wp") || cixing.equals("u") || cixing.equals("p") || cixing.equals("nl")
								|| cixing.equals("q") || cixing.equals("d") || cixing.equals("nd")
								|| cixing.equals("ws") || cixing.equals("r")) {
							findpro = true;
							head = j + 1;
							break;

						}else if(l.equals("Ea")){
							findpro = true;
							head=j+1;
							break;
						}
						j--;
					}
					
					

				} else {

				}
				

			} else {
				String cixing1 = tag.substring(1, tag.length());

				if (label.equals("E_PRODUCT")) {
					findpro=false;
					tail = i + 1;
					result.add(sentence.substring(head, tail));
					System.out.println(sentence.substring(head, tail));
				} else if (cixing1.equals("wp")||cixing1.equals("u")||cixing1.equals("p")||cixing1.equals("m")||cixing1.equals("a")) {
					findpro=false;
					tail = i;
					result.add(sentence.substring(head, tail));
					System.out.println(sentence.substring(head, tail));
					

				}else if(cixing1.equals("u")){
					
				}
				
				
			}

			i++;
			

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
