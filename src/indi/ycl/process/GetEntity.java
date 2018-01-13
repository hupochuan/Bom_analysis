package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.CoreMapSequenceMatchAction.AnnotateAction;
import indi.ycl.dao.AnnualReportDao;
import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;
import indi.ycl.nlp.CRF;
import indi.ycl.nlp.Ltp;
import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;

public class GetEntity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		 List<String> sentences=new AnnualReportDao().getReportById(9601);
		List<String> sentences = new ArrayList<String>();
		sentences.add("LCM、触控业务加大对研发的投入力度，重点客户推进有力，集约化管理进步明显，年内拓展了小米、OPPO等新客户。");
		sentences.add("客户涵盖了包括百得、博世、 牧田、创科等在内的主要电动工具厂商。");
		sentences.add("公司通信业务主营产品天线和滤波器是基站建设的核心组件，主要客户包括华为、爱立信等全球领先的移动通信网络设备制造商。");
		sentences.add("目前公司已拥有LED封装、LED背光、触控面板、模组贴合、结构件的全产业链完整产能，拥有联想、JDI、小米、华为等行业知名客户以及具备一定技术基础的研发团队。");
		sentences.add("得益于公司的市场布局及大客户战略，伴随着OPPO、VIVO、华为等客户市场份额的增长，2016年公司在金属外观件等产品领域取得了高速的增长。");
		sentences.add("京东方已成为半导体显示领域世界顶级供货商，与包括三星、LG、海信、康佳、联想、戴尔、惠普等在内的国内外知名客户保持了长期、可持续的合作，是众多国际一线品牌的第一供应商。");
		sentences.add(
				"车辆减震产品获得了华晨汽车等工厂认可并获得整车悬置件的订单，并有十多个车型的减震产品正在开发研制，汽车密封件整车业务全面推进，并逐步进入上汽荣威、上汽大通、一汽轿车、一汽大众、广汽三菱、北汽及新能源车的整车密封供系统， 为公司的持续发展打下了坚实基础。");
		StanfordNer ner = new StanfordNer();

		for (int i = 0; i < sentences.size(); i++) {
			Sentence sent = new Sentence();
			sent.setContent(sentences.get(i));
			sent.setWords(Ltp.getWordsList(sentences.get(i)));

			System.out.println(GetCompanies(sent, ner));
			System.out.println(GetProducts(sent));
		}

	}

	// 由于分词和命名实体识别初始化需要加载词典花费大量时间，所以只实例化一次
	public static List<String> GetCompanies(Sentence sentence, StanfordNer ner) {
		List<SegmentWord> words = sentence.getWords();
		List<String> result = new ArrayList<String>();

		ner.getNerList(sentence);
		Boolean isEnd = true;
		String company = "";

		// 把前后两个同标注为组织名的词合并
		for (int i = 0; i < words.size(); i++) {

			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("ORGANIZATION")) {
				if (company.equals("")) {
					company = word;
				} else {
					company += word;

				}
			} else {
				if (company.equals("")) {
					continue;
				} else {
					result.add(company);
					company = "";
				}

			}
		}

		return result;

	}

	public static List<String> GetProducts(Sentence sentence) {

		List<String> result = new ArrayList<String>();
		CRF.ProductNER(sentence);
		Boolean isEnd = true;
		String company = "";
		List<SegmentWord> words = sentence.getWords();

		// 把前后两个同标注为组织名的词合并
		for (int i = 0; i < words.size(); i++) {

			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("PRODUCT")) {
				if (company.equals("")) {
					company = word;
				} else {
					company += word;

				}

			} else {
				if (company.equals("")) {
					continue;
				} else {
					result.add(company);
					company = "";
				}

			}
		}

		return result;

	}

	public static Boolean isWord(String tag1, String tag2) {

		String shunxu1 = tag1.substring(0, 1);
		String shunxu2 = tag2.substring(0, 1);
		if (shunxu1.equals("B") && shunxu2.equals("M")) {
			return true;
		} else if (shunxu1.equals("B") && shunxu2.equals("E")) {
			return true;
		} else if (shunxu1.equals("M") && shunxu2.equals("M")) {
			return true;
		} else if (shunxu1.equals("M") && shunxu2.equals("E")) {
			return true;
		} else {
			return false;
		}

	}

}
