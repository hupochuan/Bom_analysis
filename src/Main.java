import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import indi.ycl.nlp.Ltp;
import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;
import indi.ycl.process.GetEntity;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String result = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		StanfordNer ner = new StanfordNer();
		StanfordSegmenter segmenter = new StanfordSegmenter();
		File file = new File("C:/Users/ycl/Desktop/result1.txt");
		FileWriter fw = new FileWriter(file,true); 
        BufferedWriter bw = new BufferedWriter(fw);
		try {
			fileReader = new FileReader("C:/Users/ycl/Desktop/东山精密：2017年半年度报告.txt");
			bufferedReader = new BufferedReader(fileReader);
			try {
				String read = null;
				while ((read = bufferedReader.readLine()) != null) {
				    	
	
					List<String> segments=segmenter.doSegment(read);
//					List<String> tags = ner.getNerList(listToString(segments, ' ').trim());
					String tmp=ner.doNer(listToString(segments, ' ').trim());
					System.out.println(tmp);
					bw.write(tmp);
					bw.write("\r\n");
								   
				}
				bw.close(); fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (fileReader != null) {
				fileReader.close();
			}
		}

	}
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
		
				sb.append(list.get(i).toString().trim()).append(separator);
		
			
		}
		System.out.println("xx:"+sb.toString().substring(0, sb.toString().length() - 1));
		return sb.toString().substring(0, sb.toString().length() - 1);
	}


}
