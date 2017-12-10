package indi.ycl.nlp;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

public class StanfordNer {

	private static AbstractSequenceClassifier<CoreLabel> ner;

	public StanfordNer() {
		InitNer();
	}

	public void InitNer() {
		String serializedClassifier = "classifiers/chinese.misc.distsim.crf.ser.gz"; // chinese.misc.distsim.crf.ser.gz
		if (ner == null) {
			ner = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		}
	}

	public String doNer(String sent) {
		String result=ner.classifyWithInlineXML(sent);
		if(result != null){
			//System.out.println(result);
			return result;
		}else{
			return null;
		}
		
	}
	public ArrayList<String> getNerList(String sent){
	  List<List<CoreLabel>> out = ner.classify(sent);
	 
	
	  ArrayList<String> wordtype=new ArrayList<>();
      for (List<CoreLabel> sentence : out) {
        for (CoreLabel word : sentence) {
          wordtype.add(word.get(CoreAnnotations.AnswerAnnotation.class));
        }

      }
      
      String[] segs=sent.split(" ");
    
      //规则1：用“、”分隔的词，
      for (int i = 1; i < segs.length-1; i++) {
		if(segs[i+1].equals("、")||segs[i-1].equals("、")){
			if(wordtype.get(i+2).equals("ORGANIZATION")||wordtype.get(i-2).equals("ORGANIZATION")){
				wordtype.set(i, "ORGANIZATION");
			}
		
			
		}
	  }
      
      return wordtype;
	}
	

	public static void main(String args[]) {
		String str = "在 汽车 物流 自动化 装备 系统 领域 ， 本 公司 以 雄厚 的 技术 实力 和 良好 的 产品 质量 赢得 了 众多 整车厂 的 信赖 ， 客户 覆盖 了 大多数 主流 汽车 厂商 ， 特别 是 与 福特 汽车 公司 、 东风 汽车 公司 、 一汽 大众 汽车 有限公司 、 广州 本田 汽车 有限公司 、 一汽 轿车 股份 有限公司 、 广汽菲亚特 汽车 有限公司 等 知名 汽车 整车 厂商 建立 了 良好 稳定 的 业务 合作 关系 ； 在 商业 物流 系统 领域 ， 本 公司 服务 过 的 客户 有 白云 机场 、 耐克 （ 中国 ） 等 知名 企业 ； 在 散料 输送机 领域 ， 本 公司 的 客户 包括 了 宝钢 、 韶钢 等 大型 冶金 矿山 企业 ； 在 风电 产业 领域 ， 本 公司 与 西门子 、 阿尔斯通 、 GE 等 结成 了 战略 合作 关系 。";
		StanfordNer extractDemo = new StanfordNer();
		System.out.println(extractDemo.doNer(str));
		System.out.println(extractDemo.getNerList(str));
		System.out.println("Complete!");
	}

}
