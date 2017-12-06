package indi.ycl.model;

public class Annual_Report{
	public Annual_Report(){
		
	}
	public Annual_Report(String title, String content, int company_id, String primary_service,
			String core_competitiveness, String business_circumstance, String debt_company,String prospects, String important_sales_contracts, String client_supplier) {
		this.title = title;
		this.content = content;
		this.company_id= company_id;
		this.primary_service = primary_service;
		this.core_competitiveness = core_competitiveness;
		this.business_circumstance = business_circumstance;
		this.debt_company = debt_company;
		this.prospects = prospects;
		this.important_sales_contracts = important_sales_contracts;
		this.client_supplier = client_supplier;
	}
	String title;
	String content;
	int company_id;
	
	
	
	//文字中带有供应信息
    String primary_service;//主要业务
    String core_competitiveness;//核心竞争力
    String business_circumstance;//经营情况 
    String prospects;//前景展望 
    
    //表格
 
	
	String debt_company;
    String important_sales_contracts;
    String client_supplier;
    
	public String getProspects() {
		return prospects;
	}
	public void setProspects(String prospects) {
		prospects = prospects;
	}
	public String getImportant_sales_contracts() {
		return important_sales_contracts;
	}
	public void setImportant_sales_contracts(String important_sales_contracts) {
		this.important_sales_contracts = important_sales_contracts;
	}
	public String getClient_supplier() {
		return client_supplier;
	}
	public void setClient_supplier(String client_supplier) {
		this.client_supplier = client_supplier;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getCompany_id() {
		return company_id;
	}
	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}
	public String getPrimary_service() {
		return primary_service;
	}
	public void setPrimary_service(String primary_service) {
		this.primary_service = primary_service;
	}
	public String getCore_competitiveness() {
		return core_competitiveness;
	}
	public void setCore_competitiveness(String core_competitiveness) {
		this.core_competitiveness = core_competitiveness;
	}
	public String getBusiness_circumstance() {
		return business_circumstance;
	}
	public void setBusiness_circumstance(String business_circumstance) {
		this.business_circumstance = business_circumstance;
	}
	public String getDebt_company() {
		return debt_company;
	}
	public void setDebt_company(String debt_company) {
		this.debt_company = debt_company;
	}    

}
