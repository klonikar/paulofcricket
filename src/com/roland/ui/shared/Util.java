package com.roland.ui.shared;

public class Util {
	public static final String adminEmailAddress = "recommender.roland@gmail.com";

	public static String toXMLString(String objectName, String[] fieldNames, Object[] values) throws IllegalArgumentException
	{
		if(fieldNames.length != values.length)
			throw new IllegalArgumentException("Field names array not same sized as values array size");

		String ret = "<" + objectName + ">";
		
		for(int i = 0;i < fieldNames.length;i++)
		{
			ret += "<" + fieldNames[i] + " value=\"" + values[i] + "\"/>";
		}
		ret += "</" + objectName + ">";
		return ret;
	}
}
