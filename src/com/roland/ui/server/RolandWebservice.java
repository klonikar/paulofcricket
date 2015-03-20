package com.roland.ui.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.roland.ui.client.NotLoggedInException;
import com.roland.ui.shared.NewsLetterClient;

public class RolandWebservice extends HttpServlet {
	private static final Logger log = Logger.getLogger("RolandWebservice");

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		    throws IOException
    {
		String url = req.getParameter("url");
		String fileName = req.getParameter("fileName");
		log.info("Creating URL: " + url);
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/xml");
		
		try {
			NewsLetterInfo o = Util.uploadURL(url, fileName);
			String xml = o.toXMLString();
			out.println(xml);
			//System.out.println(xml);
		} catch (IllegalArgumentException e) {
			out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (NotLoggedInException e) {
			out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		out.close();
    }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		    throws IOException
    {
		String fromDate = req.getParameter("from");
		String id = req.getParameter("id");
		String user = req.getParameter("user");
		//System.out.println("Retrieving data");
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/xml");
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<Results>");
		if(id != null && !id.isEmpty())
		{
			NewsLetterClient o = Util.getUploadedURL(Long.parseLong(id));
			String xml = o.toXMLString();
			out.println(xml);
			//System.out.println(xml);
		}
		else if(fromDate != null && !fromDate.isEmpty())
		{
			NewsLetterClient[] objs = Util.getUploadedURLsAfter(fromDate);
			for(NewsLetterClient o : objs)
			{
				String xml = o.toXMLString();
				out.println(xml);
				//System.out.println(xml);
			}
		}
		else
		{
			NewsLetterClient[] objs = Util.getAllUploadedURLs();
			for(NewsLetterClient o : objs)
			{
				String xml = o.toXMLString();
				out.println(xml);
				//System.out.println(xml);
			}
		}
		out.println("</Results>");
		//System.out.println("Retrieved data");
		out.flush();
		out.close();
    }
    
}
