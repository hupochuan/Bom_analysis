package indi.ycl.nlp;


import java.io.*;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;


/** This is a very simple demo of calling the Chinese Word Segmenter
 *  programmatically.  It assumes an input file in UTF8.
 *  <p/>
 *  <code>
 *  Usage: java -mx1g -cp seg.jar SegDemo fileName
 *  </code>
 *  This will run correctly in the distribution home directory.  To
 *  run in general, the properties for where to find dictionaries or
 *  normalizations have to be set.
 *
 *  @author Christopher Manning
 */

public class StanfordSegmenter {

//  private static final String basedir = System.getProperty("StanfordSegmenter", "data");
  private static final String basedir = "data";
  private static CRFClassifier<CoreLabel> segmenter;
  public StanfordSegmenter(){
	  try {
		  String[] args ={};
		Init(args);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  public void Init(String[] args) throws Exception{
	  System.setOut(new PrintStream(System.out, true, "utf-8"));

	    Properties props = new Properties();
	    props.setProperty("sighanCorporaDict", basedir);
	    // props.setProperty("NormalizationTable", "data/norm.simp.utf8");
	    // props.setProperty("normTableEncoding", "UTF-8");
	    // below is needed because CTBSegDocumentIteratorFactory accesses it
	    props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz");
	    if (args.length > 0) {
	      props.setProperty("testFile", args[0]);
	    }
	    props.setProperty("inputEncoding", "UTF-8");
	    props.setProperty("sighanPostProcessing", "true");
	
	    segmenter = new CRFClassifier<>(props);
	    segmenter.loadClassifierNoExceptions(basedir + "/ctb.gz", props);
	    
	    for (String filename : args) {
	      segmenter.classifyAndWriteAnswers(filename);
	    }
  }
  public List<String> doSegment(String sent){
	  List<String> segmented = segmenter.segmentString(sent);
	  
	  return segmented;
	  
  }
  public static void main(String[] args) throws Exception {
    StanfordSegmenter seg=new StanfordSegmenter();
    String sample = "我住在美国。";
    //seg.doSegment(sample);
    System.out.println(seg.doSegment(sample));
  }

}

