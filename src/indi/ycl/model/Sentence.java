package indi.ycl.model;

import java.util.List;

public class Sentence {
	String content;
	Boolean com;
	Boolean pro;
	List<SegmentWord> words;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Boolean getCom() {
		return com;
	}
	public void setCom(Boolean com) {
		this.com = com;
	}
	public Boolean getPro() {
		return pro;
	}
	public void setPro(Boolean pro) {
		this.pro = pro;
	}
	public List<SegmentWord> getWords() {
		return words;
	}
	public void setWords(List<SegmentWord> words) {
		this.words = words;
	}

}
