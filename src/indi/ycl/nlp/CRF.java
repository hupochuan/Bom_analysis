package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;
import org.chasen.crfpp.Tagger;

import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;

public class CRF {
	
	public static void LabelSent(Sentence sentence,String path){
		
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
		Tagger tagger_B = new Tagger("-m "+path);
		
		for (int i = 0; i < segments.size(); i++) {
			tagger_B.add(segments.get(i) + " " + tags.get(i));
		}
		tagger_B.parse();
		
//		CRF.toString(tagger_B);

		// 遍历tagger，如果某词中的某个字被标记为PRODUCT，那么这个词的NE为PRODUCT
		int cnt = 0;
		int l = 0;
		SegmentWord word;
		sentence.setHasPro(false);

		for (int i = 0; i < words.size(); i++) {
			word = words.get(i);
			l = word.getWord().length();
			for (int j = cnt; j < cnt + l; j++) {
		
				if (!tagger_B.y2(j).equals("O")) { //如果之前识别出为公司名或产品名，不能对其
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
		
//		CRF.toString(tagger_B);

		// 遍历tagger，如果某词中的某个字被标记为PRODUCT，那么这个词的NE为PRODUCT
		int cnt = 0;
		int l = 0;
		SegmentWord word;
		sentence.setHasPro(false);

		for (int i = 0; i < words.size(); i++) {
			word = words.get(i);
			l = word.getWord().length();
			for (int j = cnt; j < cnt + l; j++) {
		
				if (!tagger_B.y2(j).equals("O")) { //如果之前识别出为公司名或产品名，不能对其
					word.setNe("PRODUCT");
					sentence.setHasPro(true);
					break;
				}
			}
			cnt += l;
		}
        tagger_B.delete();

		// 如果B模板找不到产品，用U模板进行预测 
		if (!sentence.getHasPro()) {
			
			Tagger tagger_U = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model1");
			for (int i = 0; i < segments.size(); i++) {
				tagger_U.add(segments.get(i) + " " + tags.get(i));
			}
			tagger_U.parse();
//			CRF.toString(tagger_U);
            
			cnt=0; //初始化统计量
			l=0; 
			for (int i = 0; i < words.size(); i++) {
				word = words.get(i);
				l = word.getWord().length();
				for (int j = cnt; j < cnt + l; j++) {
					if (!tagger_U.y2(j).equals("O")&&word.getNe().equals("O")) {
						word.setNe("PRODUCT");
						sentence.setHasPro(true);
						break;
					}
				}
				cnt += l;
			}
		}

		// 最后利用
		// 1. 利用ATT(定中结构)补全零部件修饰语，如 “重型发动机”，识别出“发动机”，找到其修饰语；
		// 2. COO并列关系的词具备相同的NE;
		// 对结果进行修正

		if (sentence.getHasPro()) {
			Sentence.dealCoo(words, "PRODUCT");
			Sentence.dealATT(words, "PRODUCT");
			Sentence.dealVOB(words, "PRODUCT");
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
		String test = "公司半导体功率器件产品主要包括高压超结MOSFET、IGBT、IGTO等先进半导体功率器件以及相关的电源管理集成电路等产品，可以广泛应用于节能、绿色照明、风力发电、智能电网、混合动力/电动汽车、仪器仪表、消费电子等领域。";

	}

}
