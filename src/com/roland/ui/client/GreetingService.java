package com.roland.ui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.roland.ui.shared.NewsLetterClient;
import com.roland.ui.shared.RolandUserInfo;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	public String greetServer(String name) throws IllegalArgumentException, NotLoggedInException;
	public void uploadURL(String url, String fileName) throws IllegalArgumentException, NotLoggedInException;
	public NewsLetterClient[] getAllUploadedURLs();
	public NewsLetterClient[] getUploadedURLsAfter(String createDate);
	public RolandUserInfo[] getAllUsers();
	public void createUser(String user, Integer quota, String quotaUnits) throws IllegalArgumentException, NotLoggedInException;
	public NewsLetterClient getUploadedURL(Long id);
}
