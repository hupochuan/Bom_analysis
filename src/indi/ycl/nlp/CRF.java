package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;
import org.chasen.crfpp.Tagger;

import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;

public class CRF {

	public static void LabelSent(Sentence sentence, String path) {

		List<SegmentWord> words = sentence.getWords();
		List<String> segments = new ArrayList<String>();
		List<String> tags = new ArrayList<String>();

		for (int i = 0; i < words.size(); i++) {

			String word = words.get(i).getWord();
			String tag = words.get(i).getType();
			char[] chrs = word.toCharArray();
			if (chrs.length == 1) {
				segments.add("" + word);
				tags.add("B" + tag);

			} else {
				for (int j = 0; j < chrs.length; j++) {
					if (j == 0) {
						segments.add("" + chrs[j]);
						tags.add("B" + tag);
					} else if (j == chrs.length - 1) {
						segments.add("" + chrs[j]);
						tags.add("E" + tag);
					} else {
						segments.add("" + chrs[j]);
						tags.add("M" + tag);
					}
				}
			}

		}
		Tagger tagger_B = new Tagger("-m " + path);

		for (int i = 0; i < segments.size(); i++) {
			tagger_B.add(segments.get(i) + " " + tags.get(i));
		}
		tagger_B.parse();

		// CRF.toString(tagger_B);

		// 遍历tagger，如果某词中的某个字被标记为PRODUCT，那么这个词的NE为PRODUCT
		int cnt = 0;
		int l = 0;
		SegmentWord word;
		sentence.setHasPro(false);

		for (int i = 0; i < words.size(); i++) {
			word = words.get(i);
			l = word.getWord().length();
			for (int j = cnt; j < cnt + l; j++) {

				if (!tagger_B.y2(j).equals("O")) { // 如果之前识别出为公司名或产品名，不能对其
					word.setNe("PRODUCT");
					sentence.setHasPro(true);
					break;
				}
			}
			cnt += l;
		}

	}

	public static void ProductNER(Sentence sentence) {

		List<SegmentWord> words = sentence.getWords();

		List<String> segments = new ArrayList<String>();
		List<String> tags = new ArrayList<String>();

		for (int i = 0; i < words.size(); i++) {

			String word = words.get(i).getWord();
			String tag = words.get(i).getType();
			char[] chrs = word.toCharArray();
			if (chrs.length == 1) {
				segments.add("" + word);
				tags.add("B" + tag);

			} else {
				for (int j = 0; j < chrs.length; j++) {
					if (j == 0) {
						segments.add("" + chrs[j]);
						tags.add("B" + tag);
					} else if (j == chrs.length - 1) {
						segments.add("" + chrs[j]);
						tags.add("E" + tag);
					} else {
						segments.add("" + chrs[j]);
						tags.add("M" + tag);
					}
				}
			}

		}
		Tagger tagger_B = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model");

		for (int i = 0; i < segments.size(); i++) {
			tagger_B.add(segments.get(i) + " " + tags.get(i));
		}
		tagger_B.parse();

		 CRF.toString(tagger_B);

		// 遍历tagger，如果某词中的某个字被标记为PRODUCT，那么这个词的NE为PRODUCT
		int cnt = 0;
		int l = 0;
		SegmentWord word;
		sentence.setHasPro(false);

		for (int i = 0; i < words.size(); i++) {
			word = words.get(i);
			l = word.getWord().length();
			for (int j = cnt; j < cnt + l; j++) {

				if (!tagger_B.y2(j).equals("O")&& word.getNe().equals("O")) { // 如果之前识别出为公司名或产品名，不能对其
					word.setNe("PRODUCT");
					sentence.setHasPro(true);
					break;
				}
			}
			cnt += l;
		}
		tagger_B.delete();

		// 如果B模板找不到产品，用U模板进行预测
//		if (!sentence.getHasPro()) {

			Tagger tagger_U = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model1");
			for (int i = 0; i < segments.size(); i++) {
				tagger_U.add(segments.get(i) + " " + tags.get(i));
			}
			tagger_U.parse();
			 CRF.toString(tagger_U);

			cnt = 0; // 初始化统计量
			l = 0;
			for (int i = 0; i < words.size(); i++) {
				word = words.get(i);
				l = word.getWord().length();
				for (int j = cnt; j < cnt + l; j++) {
					if (!tagger_U.y2(j).equals("O") && word.getNe().equals("O")) {
						word.setNe("PRODUCT");
						sentence.setHasPro(true);
						break;
					}
				}
				cnt += l;
			}
			CRF.toString(tagger_U);
//		}

		// 最后利用
		// 1. 利用ATT(定中结构)补全零部件修饰语，如 “重型发动机”，识别出“发动机”，找到其修饰语；
		// 2. COO并列关系的词具备相同的NE;
		// 对结果进行修正

		if (sentence.getHasPro()) {
			Sentence.dealCoo(words, sentence.getPro_groups(), "PRODUCT");
			Sentence.dealATT(words, sentence.getPro_groups(), "PRODUCT");
			Sentence.dealVOB(words, sentence.getPro_groups(), "PRODUCT");
		}

	}

	public static void toString(Tagger tagger) {
		for (int i = 0; i < tagger.size(); ++i) {

			for (int j = 0; j < tagger.xsize(); ++j) {// 输出输入的标签
				System.out.print(tagger.x(i, j) + "\t");
			}
			System.out.print(tagger.y2(i) + "\t");
			System.out.print("\n");
		}
	}

	public static void main(String[] argv) {
		String test = "上海大众车用空调是上海德尔福汽车空调系统"
+"有限公司,一汽大众的空调是长春一汽杰克赛尔空调有限公司，广汽的空调是广州电装空调有限公司，神龙的空调是法雷奥"
+"汽车空调湖北有限公司，东风的空调是德国贝尔公司。";
		Sentence sent = new Sentence();
		sent.setContent(test);
		sent.setWords(Ltp.getWordsList(test));
		CRF.ProductNER(sent);
	}

}
