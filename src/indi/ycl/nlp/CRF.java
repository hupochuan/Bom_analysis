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
		System.out.println("faafafasd");
		String read="此外，随着智能手机采用指纹识别技术渗透率的提高，公司利用现有手机客户资源，积极拓展移动终端指纹识别芯片封测方面的客户，进一步推动公司产业链向高附加值的中上游存储芯片封装测试产业链延伸，向封装测试等核心技术领域产业转型升级。2017年1月，公司新导入无人机和汽车电子业务，5月成立智能运通事业部，生产无人机和汽车电子相关产品，目前部分产品已进入批量生产阶段，客户认可度高，该领域有望导入更多行业领先的价值客户，为公司提供新的增长。公司的主要客户希捷缩减全球生产规模，关停其苏州工厂，公司的硬盘磁头及相关产品业务销售额也受到一定程度影响。公司主要业务包括计算机与存储、固态存储、通讯与消费电子、医疗产品、计量系统、商业与工业产品、自动化设备、触模屏以及半导体封测业务，在九大业务领域，为客户提供产品与服务。公司还投资了其它业务领域，主要涉猎的业务有，参股了开发晶照明（厦门）有限公司，布局LED业务，参股昂纳科技（集团）有限公司，涉足光通信业务领域，参股东莞捷荣技术股份有限公司，涉足专业精密塑胶、五金模具业务领域。无论在计算机存储产品、通讯产品，还是医疗产品等业务领域都能获得高端客户的一致认可；公司拥有完善的质量控制与持续改进系统，三十年来不断引入领先的管理理念和工具并积极实践，获得全面的产品和行业系统认证，并";
		List<String> words=Ltp.ltp_segmentor(read);
		System.out.println("faafafasd");
		List<String> tags=Ltp.postagger(words);
		System.out.println("faafafasd");
		List<String> words1=new ArrayList<String>();
		List<String> tags1=new ArrayList<String>(); 
		System.out.println("faafafasd");
		for (int i = 0; i < words.size(); i++) {
			char[] chrs=words.get(i).toCharArray();
			if(chrs.length==1){
				//System.out.println(words.get(i)+"	"+"S"+tags.get(i));
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
		System.out.println("faafafasd");

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
