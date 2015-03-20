package com.roland.ui.client;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.roland.ui.shared.NewsLetterClient;
import com.roland.ui.shared.RolandUserInfo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	public void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException, NotLoggedInException;
	
	public void uploadURL(String url, String fileName, AsyncCallback<Void> callback) throws IllegalArgumentException, NotLoggedInException;
	public void getAllUploadedURLs(AsyncCallback<NewsLetterClient[]> callback);
	public void getUploadedURLsAfter(String createDate, AsyncCallback<NewsLetterClient[]> callback);
	public void getUploadedURL(Long id, AsyncCallback<NewsLetterClient> callback);

	void getAllUsers(AsyncCallback<RolandUserInfo[]> callback);

	void createUser(String user, Integer quota, String quotaUnits,
			AsyncCallback<Void> callback) throws IllegalArgumentException, NotLoggedInException;
}
