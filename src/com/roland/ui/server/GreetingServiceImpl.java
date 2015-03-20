package com.roland.ui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.roland.ui.client.GreetingService;
import com.roland.ui.client.NotLoggedInException;
import com.roland.ui.shared.FieldVerifier;
import com.roland.ui.shared.NewsLetterClient;
import com.roland.ui.shared.RolandUserInfo;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

//	  private static final Logger LOG = Logger.getLogger(GreetingServiceImpl.class.getName());


	public String greetServer(String input) throws IllegalArgumentException, NotLoggedInException {
		Util.checkLoggedIn();
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}
	
	public void addNewsLetter(String input) throws IllegalArgumentException, NotLoggedInException
	{
		Util.checkLoggedIn();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NewsLetterClient getUploadedURL(Long id) {
		NewsLetterClient result = Util.getUploadedURL(id);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public NewsLetterClient[] getAllUploadedURLs() {
	    NewsLetterClient[] ret = Util.getAllUploadedURLs();
	    
		return ret;
	}

	@Override
	public NewsLetterClient[] getUploadedURLsAfter(String createDate) {
	    NewsLetterClient[] ret = Util.getUploadedURLsAfter(createDate);
	    
		return ret;
	}

	@Override
	public void uploadURL(String url, String fileName)
			throws IllegalArgumentException, NotLoggedInException {
		Util.uploadURL(url, fileName);
	}

	@Override
	public RolandUserInfo[] getAllUsers() {
		RolandUserInfo[] ret = Util.getAllUsers();
		return ret;
	}

	@Override
	public void createUser(String user, Integer quota, String quotaUnits) throws IllegalArgumentException, NotLoggedInException {
		Util.createUser(user, quota, quotaUnits);
	}

}
