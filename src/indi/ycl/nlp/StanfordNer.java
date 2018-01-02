package indi.ycl.nlp;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;
import indi.ycl.model.SegmentWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class StanfordNer {

	private static AbstractSequenceClassifier<CoreLabel> ner;

	public StanfordNer() {
		InitNer();
	}

	public void InitNer() {
		String serializedClassifier = "classifiers/chinese.misc.distsim.crf.ser.gz"; // chinese.misc.distsim.crf.ser.gz
		if (ner == null) {
			ner = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		}
	}

	public String doNer(String sent) {
		String result = ner.classifyWithInlineXML(sent);
		if (result != null) {

			return result;
		} else {
			return null;
		}

	}

	public void getNerList1(List<SegmentWord> words) {
		String sent = listToString(words, ' ');
		List<List<CoreLabel>> out = ner.classify(sent);
		ArrayList<String> wordtype = new ArrayList<>();
		for (List<CoreLabel> sentence : out) {
			for (CoreLabel word : sentence) {
				wordtype.add(word.get(CoreAnnotations.AnswerAnnotation.class));
				System.out.println(word + " " + word.get(CoreAnnotations.AnswerAnnotation.class));
			}

		}

		// return wordtype;
	}

	public void getNerList(List<SegmentWord> words) {
		String sent = listToString(words, ' ');
		String result = ner.classifyToString(sent);
		String[] segs = result.split(" ");// 应该是在传入字符串的基础上进行的分割，传入字符串分隔符是什么，输出分隔符就是什么

		for (int i = 0; i < segs.length; i++) {
			String string = segs[i];
			int fenge = string.lastIndexOf("/");
			words.get(i).setNe(string.substring(fenge + 1, string.length()));

		}
		SegmentWord word;
		List<Integer> nes = new ArrayList<Integer>();
		Boolean isCom = false;
		// 处理句法依赖中的并列关系，句子中包含多个词并列的情况下，若有一个识别为公司名，则其他并列词也为公司名
		for (int i = 0; i < words.size(); i++) {
			word = words.get(i);
			if (word.getDeprel().equals("COO")) {
				if (nes.isEmpty()) {
					nes.add(i);
					nes.add(word.getHead());
					if (word.getNe().equals("ORGANIZATION") || words.get(word.getHead()).getNe().equals("ORGANIZATION"))
						isCom = true;
				} else {
					if (nes.contains(word.getHead())){
						nes.add(i);
						if (word.getNe().equals("ORGANIZATION"))
							isCom=true;
						
					}
						
				}
			}

		}
		System.out.println(isCom);
		if(isCom){
			for (Integer i : nes) {
				words.get(i).setNe("ORGANIZATION");
			}
		}
			

	}

	public static String listToString(List<SegmentWord> list, char separator) {
		System.out.println(list.size());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {

			sb.append(list.get(i).getWord().toString().trim()).append(separator);

		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public static void main(String args[]) {
		String str = " 《第五名客户》    涵盖  了  包括  百得  、  博世  、  牧田  、  创科  等  在内  的  主要  电动  工具  厂商  。";
		StanfordNer extractDemo = new StanfordNer();
		System.out.println(extractDemo.doNer(str));
		// System.out.println(extractDemo.getNerList1(str));
		// System.out.println(extractDemo.getNerList(str));
		System.out.println("Complete!");
	}

}
