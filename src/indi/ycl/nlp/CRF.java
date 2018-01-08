package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;
import org.chasen.crfpp.Tagger;

import indi.ycl.model.SegmentWord;

public class CRF {
	// CRF的结果处理上，目前发现可利用的规律有：
	// 1. 利用ATT(定中结构)补全零部件修饰语，如 “重型发动机”，识别出“发动机”，找到其修饰语；

	public static void ProductNER(List<SegmentWord> segments) {

		List<String> words = new ArrayList<String>();
		List<String> tags = new ArrayList<String>();

		for (int i = 0; i < segments.size(); i++) {
			String word = segments.get(i).getWord();
			String tag = segments.get(i).getType();
			char[] chrs = word.toCharArray();
			if (chrs.length == 1) {
				words.add("" + word);
				tags.add("B" + tag);

			} else {
				for (int j = 0; j < chrs.length; j++) {
					if (j == 0) {
						words.add("" + chrs[j]);
						tags.add("B" + tag);
					} else if (j == chrs.length - 1) {

						words.add("" + chrs[j]);
						tags.add("E" + tag);
					} else {

						words.add("" + chrs[j]);
						tags.add("M" + tag);
					}
				}
			}

		}
		Tagger tagger = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model");
		for (int i = 0; i < words.size(); i++) {
			tagger.add(words.get(i) + " " + tags.get(i));
		}
		tagger.parse();

		// for (int i = 0; i < tagger.size(); ++i) {
		//
		// for (int j = 0; j < tagger.xsize(); ++j) {//输出输入的标签
		// System.out.print(tagger.x(i, j) + "\t");
		// }
		// System.out.print(tagger.y2(i) + "\t");
		//
		// System.out.print("\n");
		//
		// }
		int cnt = 0;
		int l = 0;
		SegmentWord word;

		for (int i = 0; i < segments.size(); i++) {// 遍历tagger，如果某词中的某个字被标记为PRODUCT，那么这个词的NE为PRODUCT
			word = segments.get(i);
			l = word.getWord().length();
		    
			for (int j = cnt; j < cnt + l; j++) {
				if (!tagger.y2(j).equals("O")) {
					word.setNe("PRODUCT");
					break;
				}

			}
			cnt += l;		
		}
		//System.out.println("Done");

	}

	public static void main(String[] argv) {
		String test = "公司半导体功率器件产品主要包括高压超结MOSFET、IGBT、IGTO等先进半导体功率器件以及相关的电源管理集成电路等产品，可以广泛应用于节能、绿色照明、风力发电、智能电网、混合动力/电动汽车、仪器仪表、消费电子等领域。";

		CRF.ProductNER(Ltp.getWordsList(test));

	}

}
