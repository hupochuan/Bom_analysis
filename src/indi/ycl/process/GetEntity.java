package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import indi.ycl.nlp.StanfordNer;
import indi.ycl.nlp.StanfordSegmenter;

public class GetEntity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test="在汽车物流自动化装备系统领域，本公司以雄厚的技术实力和良好的产品质量赢得了众多整车厂的信赖，客户覆盖了大多数主流汽车厂商，特别是与福特汽车公司、东风汽车公司、一汽大众汽车有限公司、广州本田汽车有限公司、一汽轿车股份有限公司、广汽菲亚特汽车有限公司等知名汽车整车厂商建立了良好稳定的业务合作关系；在商业物流系统领域，本公司服务过的客户有白云机场、耐克（中国）等知名企业；在散料输送机领域，本公司的客户包括了宝钢、韶钢等大型冶金矿山企业；在风电产业领域，本公司与西门子、阿尔斯通、GE等结成了战略合作关系。";
		StanfordNer ner=new StanfordNer();
		StanfordSegmenter segmenter=new StanfordSegmenter();
		System.out.println(GetCompanies(test,ner,segmenter));

	}
	//由于分词和命名实体识别初始化需要加载词典花费大量时间，所以只实例化一次
	public static List<String> GetCompanies(String sentence,StanfordNer ner,StanfordSegmenter segmenter){
		
		List<String> result=new ArrayList<String>();
		
		List<String> segments = segmenter.doSegment(sentence);
		List<String> tags=ner.getNerList(listToString(segments,' '));
		Boolean isEnd=true;
		String company="";
		for (int i = 0; i < tags.size(); i++) {
			String tag=tags.get(i);
			String word=segments.get(i);
			if(tag.equals("ORGANIZATION")){
				if(company.equals("")){
					company=segments.get(i);
				}else{
					company+=segments.get(i);
					
				}
				
			}else{
				if(company.equals("")){
					continue;
				}else{
					result.add(company);
					company="";
				}
				
			}
		}
		return result;
		
	}
	public static List<String> GetProducts(){
		List<String> result=new ArrayList<String>();
		
		return result;
		
	}
	public static String listToString(List list, char separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i)).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}

}
