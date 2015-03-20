package com.roland.ui.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RolandUserInfo implements Serializable  {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date createDate;
	@Persistent
	private Integer quota;
	@Persistent
	private String quotaUnits; // per day, per week, per month etc.
	@Persistent
	private String userEmail;
	
	
	public RolandUserInfo()
	{
		createDate = new Date();
	}
	
	public RolandUserInfo(String user, Integer quota, String quotaUnits)
	{
		this();
		this.quota = quota;
		this.quotaUnits = quotaUnits;
		this.userEmail = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}

	public String getQuotaUnits() {
		return quotaUnits;
	}

	public void setQuotaUnits(String quotaUnits) {
		this.quotaUnits = quotaUnits;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String user) {
		this.userEmail = user;
	}


	public String toXMLString()
	{
		String[] fieldNames = {"id", "createDate", "quota", "quotaUnits", "userEmail"};
		Object[] values = {id, createDate, quota, quotaUnits, userEmail};
		return Util.toXMLString("RolandUserInfo", fieldNames, values);
	}

}
