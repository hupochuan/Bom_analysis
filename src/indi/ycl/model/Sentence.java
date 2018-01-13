package indi.ycl.model;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
	String content; //原文
	Boolean hasCom; //是否包含公司名
	Boolean hasPro; //是否包含产品名
	List<SegmentWord> words; //分词、词性、句法依赖

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<SegmentWord> getWords() {
		return words;
	}

	public void setWords(List<SegmentWord> words) {
		this.words = words;
	}

	public Boolean getHasCom() {
		return hasCom;
	}

	public void setHasCom(Boolean hasCom) {
		this.hasCom = hasCom;
	}

	public Boolean getHasPro() {
		return hasPro;
	}

	public void setHasPro(Boolean hasPro) {
		this.hasPro = hasPro;
	}

	public static void dealCoo(List<SegmentWord> words, String ne) {

		List<Integer> nes = new ArrayList<Integer>();
		Boolean isCom = false;
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) { // 处理句法依赖中的并列关系，句子中包含多个词并列的情况下，若有一个识别为公司名，则其他并列词也为公司名
			word = words.get(i);
			if (word.getDeprel().equals("COO")) {
				if (nes.isEmpty()) { // 找到一组COO关系的头
					nes.add(i);
					nes.add(word.getHead());
					if (word.getNe().equals(ne) || words.get(word.getHead()).getNe().equals(ne))
						isCom = true;
				} else {
					if (nes.contains(word.getHead())) { // 后续出现的COO关系是否跟之前的是同一组
						nes.add(i);
						if (word.getNe().equals(ne))
							isCom = true;
					} else {
						// System.out.println(nes);
						if (isCom) {
							for (Integer index : nes) {
								words.get(index).setNe(ne);
							}
						}
						isCom = false;
						nes.clear();
						i--;
					}
				}
			}
		}
		if (!nes.isEmpty() && isCom) {
			for (Integer index : nes) {
				words.get(index).setNe(ne);
			}
		}

	}

	public static void dealATT(List<SegmentWord> words, String ne) {
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				if (i + 1 < words.size() && word.getDeprel().equals("ATT") && word.getHead() == i + 1)
					words.get(i + 1).setNe(ne);
				if (i - 1 >= 0 && words.get(i - 1).getDeprel().equals("ATT") && words.get(i - 1).getHead() == i)
					words.get(i - 1).setNe(ne);
			}

		}
	}
	
	public static void dealVOB(List<SegmentWord> words, String ne) {
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				if (i + 1 < words.size() && words.get(i+1).getDeprel().equals("VOB") && words.get(i+1).getHead() == i)
					words.get(i + 1).setNe(ne);
		
			}

		}
	}
	
	public static void dealRAD(List<SegmentWord> words, String ne) { //右附加关系
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				if (i + 1 < words.size() && words.get(i+1).getDeprel().equals("RAD") && words.get(i+1).getHead() == i)
					words.get(i + 1).setNe(ne);
		
			}

		}
	}


}
