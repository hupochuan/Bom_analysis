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
		if(result.contains("ORGANIZATION")&&result.contains("客户")){
			//System.out.println(result);
			return result;
		}else{
			return null;
		}
		
	}
	public ArrayList<String> getNerList(String sent){
	  List<List<CoreLabel>> out = ner.classify(sent);
	  System.out.println(ner.classifyWithInlineXML(sent));
	  ArrayList<String> wordtype=new ArrayList<>();
      for (List<CoreLabel> sentence : out) {
        for (CoreLabel word : sentence) {
        	
          System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
          wordtype.add(word.get(CoreAnnotations.AnswerAnnotation.class));
        }
        System.out.println();
      }
      
      return wordtype;
	}

	public static void main(String args[]) {
		String str = "目前  公司  已  拥有  LED  封装  、  LED  背光  、  触控  面板  、  模组  贴合  、  结构件  的  全  产业链  完整  产能  ，  拥有  联想  、  JDI  、  小米  、  华为  等  行业  知名  客户  以及  具备  一定  技术  基础  的  研发  团队  。";
		StanfordNer extractDemo = new StanfordNer();
		System.out.println(extractDemo.doNer(str));
		System.out.println("Complete!");
	}

}
