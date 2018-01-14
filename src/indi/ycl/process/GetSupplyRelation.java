package indi.ycl.process;

import java.util.ArrayList;
import java.util.List;

import indi.ycl.model.SegmentWord;
import indi.ycl.model.Sentence;
import indi.ycl.model.SupplyRelation;

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
						result.add(re);

						company = "";
					}

				}
			}

		}
		return result;

	}
	public void extractSupplyRelation(Sentence sent, String supplier){
		
		
		
		
	}

}
