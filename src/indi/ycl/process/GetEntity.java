package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import indi.ycl.nlp.CRF;
import indi.ycl.nlp.Ltp;
import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;

public class GetEntity {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String test = "客户 涵盖 了 包括 百 得 、 博 世 、 牧田 、 创科 等 在内 的 主要 电动 工具 厂商 。";
		StanfordNer ner = new StanfordNer();
		StanfordSegmenter segmenter = new StanfordSegmenter();
		System.out.println(GetCompanies(test,ner,segmenter));
//		GetProducts(test);

	}



	// 由于分词和命名实体识别初始化需要加载词典花费大量时间，所以只实例化一次
	public  static List<String> GetCompanies(String sentence,StanfordNer ner,StanfordSegmenter segmenter) {

		List<String> result = new ArrayList<String>();
		List<String> segments=segmenter.doSegment(sentence);
		List<String> tags = ner.getNerList(listToString(segments, ' ').trim());
		Boolean isEnd = true;
		String company = "";
//		System.out.println(segments.size()+" "+segments);
//		System.out.println(tags.size()+" "+tags);
		for (int i = 0; i < tags.size(); i++) {
			String tag = tags.get(i);
			String word = segments.get(i);
			if (tag.equals("ORGANIZATION")) {
				if (company.equals("")) {
					company = segments.get(i);
				} else {
					company += segments.get(i);

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
				// System.out.println(words.get(i)+" "+"S"+tags.get(i));
				words1.add("" + words.get(i));
				tags1.add("B" + tags.get(i));

			} else {
				for (int j = 0; j < chrs.length; j++) {
					if (j == 0) {
						// System.out.println(chrs[j]+" "+"B"+tags.get(i));
						words1.add("" + chrs[j]);
						tags1.add("B" + tags.get(i));
					} else if (j == chrs.length - 1) {
						// System.out.println(chrs[j]+" "+"E"+tags.get(i));
						words1.add("" + chrs[j]);
						tags1.add("E" + tags.get(i));
					} else {
						// System.out.println(chrs[j]+" "+"M"+tags.get(i));
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

	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
		
				sb.append(list.get(i).toString().trim()).append(separator);
		
			
		}
		System.out.println("xx:"+sb.toString().substring(0, sb.toString().length() - 1));
		return sb.toString().substring(0, sb.toString().length() - 1);
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
