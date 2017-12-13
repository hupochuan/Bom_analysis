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
      System.out.println(wordtype.size());
      
      return wordtype;
	}
	

	public static void main(String args[]) {
		String str = "客户 涵盖 了 包括 百 得 、 博 世 、 牧田 、 创科 等 在内 的 主要 电动 工具 厂商 。";
		StanfordNer extractDemo = new StanfordNer();
		System.out.println(extractDemo.doNer(str));
		System.out.println(extractDemo.getNerList(str));
		System.out.println("Complete!");
	}

}
