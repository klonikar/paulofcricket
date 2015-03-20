package com.roland.ui.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class NewsLetterClient implements Serializable {
	private Long id;
	private Date createDate;
	private String creator;
	private String url;
	private String state;
	private String fileName;
	
	public NewsLetterClient()
	{
		createDate = new Date();
	}
	
	public NewsLetterClient(String url, String user)
	{
		this();
		this.url = url;
		this.creator = user;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String user) {
		this.creator = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		Object[] values = {id, creator, createDate, url, state, fileName};
		return Util.toXMLString("UploadRecord", fieldNames, values);
	}
}