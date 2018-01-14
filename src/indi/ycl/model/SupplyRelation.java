package indi.ycl.model;

public class SupplyRelation {

	private String company;
	private String client;
	private String product;
	private int type; //1是完整的供应关系；2是只包含客户公司名的供应关系
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	public SupplyRelation(String client,String company,String product,int type){
		this.client=client;
	    this.company=company;
	    this.product=product;
	    this.type=type;
		
	}
	public SupplyRelation(){
		
	}

}
