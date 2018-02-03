package indi.ycl.process;

import java.nio.file.LinkPermission;
import java.util.ArrayList;
import java.util.List;

import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;
import indi.ycl.model.SupplyRelation;
import indi.ycl.nlp.Ltp;
// 1.需要修正的
public class GetSupplyRelation {

	public static List<SupplyRelation> extractClient(Sentence sent, String supplier) {
		List<SupplyRelation> result = new ArrayList<SupplyRelation>();
		List<SegmentWord> words = sent.getWords();
		if (sent.getContent().contains("客户") && sent.getHasCom()) {

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

						SupplyRelation re = new SupplyRelation();
						re.setCompany(supplier);
						re.setClient(company);
						re.setType(2);
						re.setConfidence_level(1);
						result.add(re);

						company = "";
					}

				}
			}

		}
		return result;

	}

	public static List<SupplyRelation> extractSupplyRelation(Sentence sent, String supplier) {
		// 先获取公司名list和产品名list

		List<SupplyRelation> re = new ArrayList<SupplyRelation>();// 保存结果

		List<SegmentWord> words = sent.getWords();
		List<List<Integer>> coms = new ArrayList<List<Integer>>();

		Boolean isEnd = true;
		String company = "";

		// 把前后两个同标注为组织名的词合并
		List<Integer> com_no = new ArrayList<Integer>();// 临时，用于保存公司名编号
		List<String> com_name = new ArrayList<String>();// 保存对应公司名
		for (int i = 0; i < words.size(); i++) {

			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("ORGANIZATION")) {
				if (company.equals("")) {
					company = word;
					com_no.add(i);
				} else {
					company += word;
					com_no.add(i);

				}
			} else {
				if (company.equals("")) {
					continue;
				} else {
					com_name.add(company);
					company = "";
					coms.add(com_no);
					com_no = new ArrayList<Integer>();
				}

			}
		}

		// 获取公司名List
		List<List<Integer>> pros = new ArrayList<List<Integer>>();

		isEnd = true;
		String product = "";

		List<Integer> pro_no = new ArrayList<Integer>();// 临时，用于保存公司名编号
		List<String> pro_name = new ArrayList<String>();// 保存对应公司名
		for (int i = 0; i < words.size(); i++) {

			String tag = words.get(i).getNe();
			String word = words.get(i).getWord();
			if (tag.equals("PRODUCT")) {
				if (product.equals("")) {
					product = word;
					pro_no.add(i);
				} else {
					product += word;
					pro_no.add(i);

				}
			} else {
				if (product.equals("")) {
					continue;
				} else {
					pro_name.add(product);
					product = "";
					pros.add(pro_no);
					pro_no = new ArrayList<Integer>();// 加入完成以后清空
				}

			}
		}
		// System.out.println(coms);
		// System.out.println(com_name);
		// System.out.println(pros);
		// System.out.println(pro_name);
		String pro_string=Ltp.listToString(pro_name, ' ');

		// 以上得到了公司名产品名的List，包含每个实体的详细名称和对应的编号

		for (int i = 0; i < coms.size(); i++) {
			Boolean foundrelation=false;
			for (int j = 0; j < pros.size(); j++) {
				List<Integer> cname = coms.get(i);
				List<Integer> pname = pros.get(j);
			    boolean isfound=false; //用于标记是否找到
				for (int k = 0; k < cname.size(); k++) {
					for (int k2 = 0; k2 < pname.size(); k2++) {
						// 需要判断优先顺序，公司名、产品名根据顺序编号为v1，v2
						if(!isfound){
							int e1 = cname.get(k);
							int e2 = pname.get(k2);
							if (e1 > e2) {
								int tmp = e1;
								e1 = e2;
								e2 = tmp;
							}
							int ee1 = LinkPareMethod(words, e1);
							int ee2 = LinkPareMethod(words, e2);
	
							int e2v = SENFirstVerd(words, ee2);
							int e1v = FENFirstVerd(words, ee1);
	
							int dv = JudgeDV(words, e1v, e2v);
	
							if (dv != -1) {
								SupplyRelation tmp = new SupplyRelation();
								tmp.setClient(com_name.get(i));
								tmp.setCompany(supplier);
								tmp.setProduct(pro_name.get(j));
								tmp.setConfidence_level(1);
								tmp.setType(1);
								re.add(tmp);
								
								isfound=true;
								foundrelation=true;
							}
						}
					}
				}
			}
			 //如果没找到与其匹配的产品列表
			if(!foundrelation){
				SupplyRelation tmp = new SupplyRelation();
				tmp.setClient(com_name.get(i));
				tmp.setCompany(supplier);
				tmp.setProduct(pro_string);
				tmp.setType(1);
				tmp.setConfidence_level(2);
				re.add(tmp);
			}
		}
		//以上，根据句法分析判断产品与客户公司名之间的关系
		
		
		return re;

	}

	// 提取实体的依存关联节点
	public static int LinkPareMethod(List<SegmentWord> words, int i) {
		if (i == -1)
			return -1;
		SegmentWord word = words.get(i);
		int parent = word.getHead();
		Boolean isloop = false;
		if (parent != -1) {
			while (words.get(parent).getDeprel().equals("COO") || words.get(parent).getDeprel().equals("ATT")) {
				parent = words.get(parent).getHead();
				
			}
			isloop = true;
		}

		if (isloop) { 
			return parent;
		} else {
			return i;
		}

	}

	// 提取第二个实体发生依存关系的距离最近动词
	public static int SENFirstVerd(List<SegmentWord> words, int target) {
		if (target == -1)
			return -1;
		int V = -1;
		int p = words.get(target).getHead();
		while (p != -1) {
			if (words.get(p).getType().equals("v")) {
				V = p;
				break;
			} else {
				p = words.get(p).getHead();
			}

		}
		return V;
	}

	// 提取与第1个实体发生SBV或FOB关系的距离最近动词
	public static int FENFirstVerd(List<SegmentWord> words, int target) {
		if (target == -1)
			return -1;
		int V = -1;
		int p = words.get(target).getHead();

		while (p != -1) {
			if (words.get(p).getType().equals("v")
					&& (words.get(target).getDeprel().equals("SBV") || words.get(target).getDeprel().equals("FOB"))) {
				V = p;
				break;
			} else {
				p = words.get(p).getHead();
			}

		}
		return V;
	}

	// 返回最近依赖动词
	public static int JudgeDV(List<SegmentWord> words, int v1, int v2) {
		int dv = -1;
		if (v2 != -1) {
			if (v1 == v2) {
				dv = v2;
			} else {
				int p = words.get(v2).getHead();
				int vk = v2;
				while (p != -1) {
					if (words.get(vk).getDeprel().equals("COO") || words.get(vk).getDeprel().equals("CMP")) {
						if (v1 == words.get(vk).getHead()) {
							dv = v2;
							break;
						} else {
							vk = p;
							p = words.get(p).getHead();
						}
					}else{
						return -1;
					}
					
				}
			}
		}
		return dv;

	}

}
