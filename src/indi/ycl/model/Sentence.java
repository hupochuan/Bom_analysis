package indi.ycl.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sentence {
	String content; // 原文
	Boolean hasCom; // 是否包含公司名
	Boolean hasPro; // 是否包含产品名
	List<SegmentWord> words; // 分词、词性、句法依赖
	List<List<Integer>> com_groups; // 用于保存公司的并列组，以COO关系为核心生成的依存组
	List<List<Integer>> pro_groups; // 用于保存产品名的并列组，以COO关系为核心生成的依存组

	public Sentence() {
		this.com_groups = new ArrayList<List<Integer>>();
		this.pro_groups = new ArrayList<List<Integer>>();
	}

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

	public List<List<Integer>> getCom_groups() {
		return com_groups;
	}

	public void setCom_groups(List<List<Integer>> com_groups) {
		this.com_groups = com_groups;
	}

	public List<List<Integer>> getPro_groups() {
		return pro_groups;
	}

	public void setPro_groups(List<List<Integer>> pro_groups) {
		this.pro_groups = pro_groups;
	}

	public static void dealCoo(List<SegmentWord> words, List<List<Integer>> groups, String ne) {

		List<Integer> nes = new ArrayList<Integer>();
		Boolean isCom = false;
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) { // 处理句法依赖中的并列关系，句子中包含多个词并列的情况下，若有一个识别为公司名，则其他并列词也为公司名
			word = words.get(i);
//			System.out.println(word.getDeprel());
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

							List<Integer> coos = new ArrayList<Integer>(); // 发现一个并列组
																			// ，保存

							for (Integer index : nes) {

								words.get(index).setNe(ne);
								coos.add(index);

							}
							// 排序
							groups.add(coos);
						}
						isCom = false;
						nes.clear();
						i--;
					}
				}
			}
		}
		if (!nes.isEmpty() && isCom) {
			List<Integer> coos = new ArrayList<Integer>(); // 发现一个并列组 ，保存
			for (Integer index : nes) {

				words.get(index).setNe(ne);
				coos.add(index);

			}
			groups.add(coos);
		}

	}

	public static void dealATT(List<SegmentWord> words, List<List<Integer>> groups, String ne) {
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				int group = -1; // 先初始化成0，并不合适
				// 判断当前word属于哪一个并列组
				for (int j = 0; j < groups.size(); j++) {
					if (groups.get(j).contains(i)) {
						group = j;
						break;
					}
				}
				if (i + 1 < words.size() && word.getDeprel().equals("ATT") && word.getHead() == i + 1) {
					words.get(i + 1).setNe(ne);
					if (group != -1) {
						groups.get(group).add(i + 1);
					}

				}
				if (i - 1 >= 0 && words.get(i - 1).getDeprel().equals("ATT") && words.get(i - 1).getHead() == i) {
					words.get(i - 1).setNe(ne);
					if (group != -1) {
						groups.get(group).add(i - 1);
					}
				}

			}

		}
	}

	public static void dealVOB(List<SegmentWord> words, List<List<Integer>> groups, String ne) {
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				int group = -1; // 先初始化成0，并不合适
				// 判断当前word属于哪一个并列组
				for (int j = 0; j < groups.size(); j++) {
					if (groups.get(j).contains(i)) {
						group = j;
						break;
					}
				}
				if (i + 1 < words.size() && words.get(i + 1).getDeprel().equals("VOB")
						&& words.get(i + 1).getHead() == i) {
					words.get(i + 1).setNe(ne);
					if (group!= -1) {
						groups.get(group).add(i + 1);
					}
				}

			}

		}
	}

	public static void dealRAD(List<SegmentWord> words, List<List<Integer>> groups, String ne) { // 右附加关系
		SegmentWord word = new SegmentWord();
		for (int i = 0; i < words.size(); i++) {

			word = words.get(i);
			// 方法一 只考虑公司名上下相邻词。另外还可以进行关系的判断，例如 COO-ATT关系
			if (word.getNe().equals(ne)) {
				int group = -1; // 先初始化成0，并不合适
				// 判断当前word属于哪一个并列组
				for (int j = 0; j < groups.size(); j++) {
					if (groups.get(j).contains(i)) {
						group = j;
						break;
					}
				}
				if (i + 1 < words.size() && words.get(i + 1).getDeprel().equals("RAD")
						&& words.get(i + 1).getHead() == i) {
					words.get(i + 1).setNe(ne);
					if (group!= -1) {
						groups.get(group).add(i + 1);
					}
				}

			}

		}
	}

}
