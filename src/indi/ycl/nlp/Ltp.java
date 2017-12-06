package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;

import edu.hit.ir.ltp4j.NER;
import edu.hit.ir.ltp4j.Postagger;
import edu.hit.ir.ltp4j.Segmentor;

public class Ltp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary("segmentor");
		if (Segmentor.create("ltp_data_v3.4.0/cws.model") < 0) {
			System.err.println("load failed");
			return;
		}
		String sent = "目前公司已拥有LED封装、LED背光、触控面板、模组贴合、结构件的全产业链完整产能，拥有联想、JDI、小米、华为等行业知名客户以及具备一定技术基础的研发团队。";
		List<String> words = new ArrayList<String>();
		int size = Segmentor.segment(sent, words);

		for (int i = 0; i < size; i++) {
			System.out.print(words.get(i));

			if (i == size - 1) {
				System.out.println();
			} else {
				System.out.print("  ");
			}
		}
		Segmentor.release();
		
		System.loadLibrary("postagger");// 词性标注，需要分词

		if (Postagger.create("ltp_data_v3.4.0/pos.model") < 0) {
			System.err.println("load failed");
			return;
		}

		List<String> postags = new ArrayList<String>();
		size = Postagger.postag(words, postags);

		for (int i = 0; i < size; i++) {
			System.out.print(words.get(i)+":"+postags.get(i));

			if (i == size - 1) {
				System.out.println();
			} else {
				System.out.print(" ");
			}
		}
		Postagger.release();
		
		System.loadLibrary("ner"); // 命名实体识别，需要分词、词性标注

		if (NER.create("ltp_data_v3.4.0/ner.model") < 0) {
			System.err.println("load failed");
			return;
		}
		List<String> ners = new ArrayList<String>();

		NER.recognize(words, postags, ners);

		for (int i = 0; i < words.size(); i++) {
			System.out.println(ners.get(i));
		}

		NER.release();

	}
	public static List<String> ltp_segmentor(String sentence){
		
		System.loadLibrary("segmentor");
		if (Segmentor.create("ltp_data_v3.4.0/cws.model") < 0) {
			System.err.println("load failed");
			return null;
		}
		List<String> words = new ArrayList<String>();
		int size = Segmentor.segment(sentence, words);

//		for (int i = 0; i < size; i++) {
//			System.out.print(words.get(i));
//
//			if (i == size - 1) {
//				System.out.println();
//			} else {
//				System.out.print("  ");
//			}
//		}
		
		Segmentor.release();
		return words;
		
	}
	public static List<String> postagger(List<String> words){
		System.loadLibrary("postagger");// 词性标注，需要分词

		if (Postagger.create("ltp_data_v3.4.0/pos.model") < 0) {
			System.err.println("load failed");
			return null;
		}

		List<String> postags = new ArrayList<String>();
		int size = Postagger.postag(words, postags);

//		for (int i = 0; i < size; i++) {
//			System.out.print(postags.get(i));
//
//			if (i == size - 1) {
//				System.out.println();
//			} else {
//				System.out.print(" ");
//			}
//		}
		Postagger.release();
		return postags;
		
	}
	public static List<String> ner(List<String> words,List<String> postags){
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
	
	

}
