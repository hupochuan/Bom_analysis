package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;
import org.chasen.crfpp.Tagger;

public class CRF {

	public static void ProductNER(String read) {
		List<String> words = Ltp.ltp_segmentor(read);

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
		// System.out.println(words1);
		Tagger tagger = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model");
		for (int i = 0; i < words.size(); i++) {
			tagger.add(words1.get(i) + " " + tags1.get(i));
		}
		tagger.parse();

		for (int i = 0; i < tagger.size(); ++i) {
			for (int j = 0; j < tagger.xsize(); ++j) {
				System.out.print(tagger.x(i, j) + "\t");
			}
			System.out.print(tagger.y2(i) + "\t");

			System.out.print("\n");

		}
		System.out.println("Done");

	}

	public static void main(String[] argv) {

		

	}

}
