package indi.ycl.nlp;

import java.util.ArrayList;
import java.util.List;

import com.ibugs.crfpp.Tagger;

public class CRF {
	private static Tagger tagger;

	static {
		try {
			System.loadLibrary("libcrfpp");
			System.loadLibrary("CRFPP");

		} catch (UnsatisfiedLinkError e) {
			System.err.println(
					"Cannot load the example native code.\nMake sure your LD_LIBRARY_PATH contains \'.\'\n" + e);
			System.exit(1);
		}
	}
	
	public CRF() {
		Init();
	}

	public void Init() {
		tagger = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model -v 3 -n2");
	}

	public static List<String> ProductNER(List<String> words, List<String> tags) {
		List<String> result=new ArrayList<String>();
		tagger.clear();
		if (words.size() != tags.size()) {
			return null;
		}

		for (int i = 0; i < words.size(); i++) {
			tagger.add(words.get(i) + " " + tags.get(i));
		}
		if (!tagger.parse())
			return null;

		//System.out.println("conditional prob=" + tagger.prob() + " log(Z)=" + tagger.Z());

		for (int i = 0; i < tagger.size(); ++i) {
			for (int j = 0; j < tagger.xsize(); ++j) {
				System.out.print(tagger.x(i, j) + "\t");
			}
			System.out.print(tagger.y2(i) + "\t");
			result.add(tagger.y2(i));
			System.out.print("\n");
			
		}
		
		return result;

	}
	
	public static void main(String[] argv){
		String read="如果公司不能及时把握改性塑料行业的发展趋势，加大对新产品的研究开发力度，以保持较高的技术研发水平，公司将在市场竞争中逐渐失去优势地位。";
		List<String> words=Ltp.ltp_segmentor(read);
		List<String> tags=Ltp.postagger(words);
		List<String> words1=new ArrayList<String>();
		List<String> tags1=new ArrayList<String>(); 
		for (int i = 0; i < words.size(); i++) {
			char[] chrs=words.get(i).toCharArray();
			if(chrs.length==1){
				System.out.println(words.get(i)+"	"+"S"+tags.get(i));
				words1.add(""+words.get(i));
				tags1.add("B"+tags.get(i));
				
			}else{
				for (int j = 0; j < chrs.length; j++) {
					if(j==0){
						//System.out.println(chrs[j]+"	"+"B"+tags.get(i));
						words1.add(""+chrs[j]);
						tags1.add("B"+tags.get(i));
					}else if (j==chrs.length-1) {
						//System.out.println(chrs[j]+"	"+"E"+tags.get(i));
						words1.add(""+chrs[j]);
						tags1.add("E"+tags.get(i));
					}else{
						//System.out.println(chrs[j]+"	"+"M"+tags.get(i));
						words1.add(""+chrs[j]);
						tags1.add("M"+tags.get(i));
					}
				}
			}
			
		}

		new CRF().ProductNER(words1, tags1);
		
	}

//	public static void main(String[] argv) {
//		Tagger tagger = new Tagger("-m D:/workspace/Bom_analysis/CRF/CRF++/model -v 3 -n2");
//		// clear internal context
//		tagger.clear();
//
//		// add context
//		tagger.add("Confidence NN");
//		tagger.add("in IN");
//		tagger.add("the DT");
//		tagger.add("pound NN");
//		tagger.add("is VBZ");
//		tagger.add("widely RB");
//		tagger.add("expected VBN");
//		tagger.add("to TO");
//		tagger.add("take VB");
//		tagger.add("another DT");
//		tagger.add("sharp JJ");
//		tagger.add("dive NN");
//		tagger.add("if IN");
//		tagger.add("trade NN");
//		tagger.add("figures NNS");
//		tagger.add("for IN");
//		tagger.add("September NNP");
//
//		// System.out.println("column size: " + tagger.xsize());
//		// System.out.println("token size: " + tagger.size());
//		// System.out.println("tag size: " + tagger.ysize());
//		//
//		// System.out.println("tagset information:");
//		// for (int i = 0; i < tagger.ysize(); ++i) {
//		// System.out.println("tag " + i + " " + tagger.yname(i));
//		// }
//
//		// parse and change internal stated as 'parsed'
//		if (!tagger.parse())
//			return;
//
//		System.out.println("conditional prob=" + tagger.prob() + " log(Z)=" + tagger.Z());
//
//		for (int i = 0; i < tagger.size(); ++i) {
//			for (int j = 0; j < tagger.xsize(); ++j) {
//				System.out.print(tagger.x(i, j) + "\t");
//			}
//			System.out.print(tagger.y2(i) + "\t");
//			System.out.print("\n");
//
//			// System.out.print("Details");
//			// for (int j = 0; j < tagger.ysize(); ++j) {
//			// System.out.print("\t" + tagger.yname(j) + "/prob=" +
//			// tagger.prob(i,j)
//			// + "/alpha=" + tagger.alpha(i, j)
//			// + "/beta=" + tagger.beta(i, j));
//			// }
//			// System.out.print("\n");
//		}
//
//		// when -n20 is specified, you can access nbest outputs
//		// System.out.println("nbest outputs:");
//		// for (int n = 0; n < 10; ++n) {
//		// if (! tagger.next()) break;
//		// System.out.println("nbest n=" + n + "\tconditional prob=" +
//		// tagger.prob());
//		// // you can access any information using tagger.y()...
//		// }
//		// System.out.println("Done");
//	}

	

}
