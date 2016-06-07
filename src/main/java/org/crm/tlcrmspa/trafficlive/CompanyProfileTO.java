package org.crm.tlcrmspa.trafficlive;

import java.util.Calendar;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CompanyProfileTO extends BaseTO  {

	private Long sourceOfBusinessListItemId;
	private Calendar relationshipSince;
	private Long turnover;
	private Integer employees;
	private String taxNumber;
	private String companyNumber;
	private String nominalCode;
	private String accountPackageId;
	private Boolean optOutMarketing;
	private Boolean optOutEmail;
	private Boolean optOutTelephone;
	
	@Size(max=8192)
	private String notes;

	public Long getSourceOfBusinessListItemId() {
		return sourceOfBusinessListItemId;
	}
	public void setSourceOfBusinessListItemId(Long sourceOfBusinessListItemId) {
		this.sourceOfBusinessListItemId = sourceOfBusinessListItemId;
	}
	public Calendar getRelationshipSince() {
		return relationshipSince;
	}
	public void setRelationshipSince(Calendar relationshipSince) {
		this.relationshipSince = relationshipSince;
	}
	public Long getTurnover() {
		return turnover;
	}
	public void setTurnover(Long turnover) {
		this.turnover = turnover;
	}
	public Integer getEmployees() {
		return employees;
	}
	public void setEmployees(Integer employees) {
		this.employees = employees;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	public String getNominalCode() {
		return nominalCode;
	}
	public void setNominalCode(String nominalCode) {
		this.nominalCode = nominalCode;
	}
	public String getAccountPackageId() {
		return accountPackageId;
	}
	public void setAccountPackageId(String accountPackageId) {
		this.accountPackageId = accountPackageId;
	}
	public Boolean getOptOutMarketing() {
		return optOutMarketing;
	}
	public void setOptOutMarketing(Boolean optOutMarketing) {
		this.optOutMarketing = optOutMarketing;
	}
	public Boolean getOptOutEmail() {
		return optOutEmail;
	}
	public void setOptOutEmail(Boolean optOutEmail) {
		this.optOutEmail = optOutEmail;
	}
	public Boolean getOptOutTelephone() {
		return optOutTelephone;
	}
	public void setOptOutTelephone(Boolean optOutTelephone) {
		this.optOutTelephone = optOutTelephone;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
