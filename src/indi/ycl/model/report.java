package indi.ycl.model;

public class report {
	

	String title;
	String content;
	String company_name;
	
	
	
	//文字中带有供应信息
    String primary_service;//主要业务
    String core_competitiveness;//核心竞争力
    String business_circumstance;//经营情况 
    
    //表格
    String debt_company;

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

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
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
	
    public report() {
		
	}

	public report(String title, String content, String company_name, String primary_service,
			String core_competitiveness, String business_circumstance, String debt_company) {
		super();
		this.title = title;
		this.content = content;
		this.company_name = company_name;
		this.primary_service = primary_service;
		this.core_competitiveness = core_competitiveness;
		this.business_circumstance = business_circumstance;
		this.debt_company = debt_company;
	}

}
