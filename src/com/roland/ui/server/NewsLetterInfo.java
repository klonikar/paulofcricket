package com.roland.ui.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;
import com.roland.ui.shared.Util;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class NewsLetterInfo {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private Date createDate;
	@Persistent
	private String creator;
	@Persistent
	private Text url;
	@Persistent
	private String state;
	@Persistent
	private String fileName;
	
	public NewsLetterInfo()
	{
		createDate = new Date();
	}
	
	public NewsLetterInfo(String val, String user,  String fileName)
	{
		this();
		this.creator = user;
		this.url = new Text(val);
		this.fileName = fileName;
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

	public Text getUrl() {
		return url;
	}

	public void setUrl(Text val) {
		this.url = val;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String toXMLString()
	{
		String[] fieldNames = {"id", "creator", "createDate", "url", "state", "fileName"};
		Object[] values = {id, creator, createDate, url.getValue(), state, fileName};
		return Util.toXMLString("UploadRecord", fieldNames, values);
	}

}
