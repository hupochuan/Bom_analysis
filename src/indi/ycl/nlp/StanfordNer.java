package indi.ycl.nlp;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;
import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;

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

	public void getNerList(Sentence sentence) {
		List<SegmentWord> words = sentence.getWords();
		String sent = listToString(words, ' ');
		String result = ner.classifyToString(sent);
		String[] segs = result.split(" ");// 应该是在传入字符串的基础上进行的分割，传入字符串分隔符是什么，输出分隔符就是什么
		sentence.setHasCom(false); // 默认情况下不包含公司名
		for (int i = 0; i < segs.length; i++) {
			String string = segs[i];
			int fenge = string.lastIndexOf("/");
			if (words.get(i).getNe().equals("O")) {
				String type = string.substring(fenge + 1, string.length());
				if (type.equals("ORGANIZATION"))
					sentence.setHasCom(true);// 标记这句话中包含公司名
				words.get(i).setNe(type);
			}
		}

		if (sentence.getHasCom()) {
			// 增加 规则1：具备并列关系的多个词具备相同的实体类别；
			Sentence.dealCoo(words, "ORGANIZATION");
			// 规则2：具备ATT（定中关系）的相邻词，如果其中一个词为公司名，那么另外一个也是公司名。
			Sentence.dealATT(words, "ORGANIZATION");
			
			Sentence.dealVOB(words, "ORGANIZATION");
		}

		// 除了ATT，RAD(右附加)似乎也具备相同的性质，在经过测试后再进行改进

	}

	public static String listToString(List<SegmentWord> list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {

			sb.append(list.get(i).getWord().toString().trim()).append(separator);

		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	public static void main(String args[]) {
		String str = "汽车电子领域经过多年潜心耕耘，与东风日产、广汽本田、东风本田、众泰汽车、东风英菲尼迪、广汽乘用车、广汽三菱、东风启辰等前装车厂建立了稳固的合作关系，拥有一批稳定的核心客户群。";

		StanfordNer extractDemo = new StanfordNer();
		System.out.println(extractDemo.doNer(str));
		System.out.println("Complete!");
	}

}
