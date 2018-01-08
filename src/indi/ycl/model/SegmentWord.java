package indi.ycl.model;

public class SegmentWord {
	private String word;
	private String type;
	private String ne;//ORGANIZATION公司名 PRODUCT产品名 O其他
	
	private int head;
	private String deprel;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNe() {
		return ne;
	}
	public void setNe(String ne) {
		this.ne = ne;
	}
	public int getHead() {
		return head;
	}
	public void setHead(int head) {
		this.head = head;
	}
	public String getDeprel() {
		return deprel;
	}
	public void setDeprel(String deprel) {
		this.deprel = deprel;
	}
	@Override
	public String toString() {
		return "SegmentWord [word=" + word + ", type=" + type + ", ne=" + ne + ", head=" + head + ", deprel=" + deprel
				+ "]";
	}
	
	
}
