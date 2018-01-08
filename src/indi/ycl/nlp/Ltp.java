package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;

import edu.hit.ir.ltp4j.NER;
import edu.hit.ir.ltp4j.Parser;
import edu.hit.ir.ltp4j.Postagger;
import edu.hit.ir.ltp4j.Segmentor;
import indi.ycl.model.SegmentWord;

public class Ltp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sent = "客户涵盖了包括百得、博世、 牧田、创科等在内的主要电动工具厂商。";
		Ltp.getWordsList(sent);
		// List<String> words =
		// Ltp.ltp_segmentor(sent);System.out.println(words);
		// List<String> tags=Ltp.postagger(words);System.out.println(tags);
		// Ltp.parser(words, tags);
	}

	public static List<String> ltp_segmentor(String sentence) {

		System.loadLibrary("segmentor");
		if (Segmentor.create("ltp_data_v3.4.0/cws.model") < 0) {
			System.err.println("load failed");
			return null;
		}
		List<String> words = new ArrayList<String>();
		int size = Segmentor.segment(sentence, words);

		Segmentor.release();
		return words;

	}

	public static List<String> postagger(List<String> words) {
		System.loadLibrary("postagger");// 词性标注，需要分词

		if (Postagger.create("ltp_data_v3.4.0/pos.model") < 0) {
			System.err.println("load failed");
			return null;
		}

		List<String> postags = new ArrayList<String>();
		int size = Postagger.postag(words, postags);

		Postagger.release();
		return postags;

	}

	public static void parser(List<String> words, List<String> tags) {
		System.loadLibrary("parser");
		if (Parser.create("ltp_data_v3.4.0/parser.model") < 0) {
			System.err.println("load failed");
			return;
		}

		List<Integer> heads = new ArrayList<Integer>();
		List<String> deprels = new ArrayList<String>();

		int size = Parser.parse(words, tags, heads, deprels);

		for (int i = 0; i < size; i++) {
			System.out.print(heads.get(i) + ":" + deprels.get(i));
			if (i == size - 1) {
				System.out.println();
			} else {
				System.out.print(" ");
			}
		}

		Parser.release();

	}

	public static List<String> ner(List<String> words, List<String> postags) {
		System.loadLibrary("ner"); // 命名实体识别，需要分词、词性标注

		if (NER.create("ltp_data_v3.4.0/ner.model") < 0) {
			System.err.println("load failed");
			return null;
		}
		List<String> ners = new ArrayList<String>();

		NER.recognize(words, postags, ners);

		for (int i = 0; i < words.size(); i++) {
			System.out.println(ners.get(i));
		}

		NER.release();
		return ners;
	}

	// 包括分词、词性标注、
	public static List<SegmentWord> getWordsList(String sentence) {
		List<SegmentWord> result = new ArrayList<SegmentWord>();
		List<String> words = Ltp.ltp_segmentor(sentence);
		List<String> tags = Ltp.postagger(words);

		System.loadLibrary("parser");
		if (Parser.create("ltp_data_v3.4.0/parser.model") < 0) {
			System.err.println("load failed");
			return null;
		}

		List<Integer> heads = new ArrayList<Integer>();
		List<String> deprels = new ArrayList<String>();

		int size = Parser.parse(words, tags, heads, deprels);

		for (int i = 0; i < size; i++) {
			SegmentWord word = new SegmentWord();
			word.setWord(words.get(i));
			word.setType(tags.get(i));
			word.setHead(heads.get(i) - 1);// 保存的是数组下标
			word.setDeprel(deprels.get(i));
			word.setNe("O");
//			System.out.print(heads.get(i)-1 + ":" + deprels.get(i));
			result.add(word);
		}
//		System.out.println(words);
//		System.out.println(heads);
//		System.out.println(deprels);

		Parser.release();
		return result;
	}

	// Stanford Ner命名实体识别需要输入带分隔符的字符串
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {

			sb.append(list.get(i).toString().trim()).append(separator);

		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
	
	
	
	

}
