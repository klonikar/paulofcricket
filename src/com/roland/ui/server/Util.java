package com.roland.ui.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.roland.ui.client.NotLoggedInException;
import com.roland.ui.shared.NewsLetterClient;
import com.roland.ui.shared.RolandUserInfo;

public class Util {

	private static final PersistenceManagerFactory PMF =
		      JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static User getUser() {
		    UserService userService = UserServiceFactory.getUserService();
		    return userService.getCurrentUser();
	}

	public static void checkLoggedIn() throws NotLoggedInException {
	    if (Util.getUser() == null) {
	      throw new NotLoggedInException("Not logged in.");
	    }
}

	public static PersistenceManager getPersistenceManager() {
		    return PMF.getPersistenceManager();
	}

	public static NewsLetterClient toClientObject(NewsLetterInfo obj)
    {
			NewsLetterClient ret = new NewsLetterClient();
			ret.setCreateDate(obj.getCreateDate());
			ret.setId(obj.getId());
			ret.setUrl(obj.getUrl().getValue());
			ret.setCreator(obj.getCreator());
			ret.setFileName(obj.getFileName());
			
			return ret;
	}
	  
	public static NewsLetterClient getUploadedURL(Long id) {
		NewsLetterClient result = null;
		PersistenceManager pm = Util.getPersistenceManager();
		try {
	    	Query q = pm.newQuery(NewsLetterInfo.class, "id == givenId");
	    	q.declareParameters("java.lang.Long givenId");
	    	List<NewsLetterInfo> qResults = (List<NewsLetterInfo>) q.execute(id);
	    	if(qResults != null && qResults.size() == 1)
	    	{
	    		result = Util.toClientObject(qResults.get(0));
	    	}
	    }
	    finally {
	    	pm.close();
	    }

		return result;
	}

	public static NewsLetterClient[] getAllUploadedURLs() {
		PersistenceManager pm = Util.getPersistenceManager();
	    Stack<NewsLetterClient> results = new Stack<NewsLetterClient>();
	    try {
	    	Query q = pm.newQuery(NewsLetterInfo.class);
	    	/// TBD: How to retrieve in reverse cronological order?
	    	q.setOrdering("createDate");
	    	List<NewsLetterInfo> qResults = (List<NewsLetterInfo>) q.execute();
	    	for(NewsLetterInfo r : qResults)
	    	{
	    		results.push(Util.toClientObject(r));
	    	}
	    }
	    finally {
	    	pm.close();
	    }

	    NewsLetterClient[] ret = new NewsLetterClient[results.size()];
	    for(int i = 0;;i++)
	    {
	    	try {
	    		ret[i] = results.pop();
	    	} catch(Exception ex) {
	    		break;
	    	}
	    }
	    
		return ret;
	}

	public static NewsLetterClient[] getUploadedURLsAfter(String createDate) {
		PersistenceManager pm = Util.getPersistenceManager();
	    Stack<NewsLetterClient> results = new Stack<NewsLetterClient>();
	    try {
	    	Query q = pm.newQuery(NewsLetterInfo.class, "createDate >= givenDate");
	    	q.declareImports("import java.util.Date");
	    	q.declareParameters("Date givenDate");
	    	/// TBD: How to retrieve in reverse cronological order?
	    	q.setOrdering("createDate");
	    	SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	    	List<NewsLetterInfo> qResults = null;
			try {
				Date givenDate = sf.parse(createDate);
				//System.out.println("Locating data after: " + givenDate);
				qResults = (List<NewsLetterInfo>) q.execute(givenDate);
				//System.out.println("Obtained " + qResults.size() + " results");
		    	for(NewsLetterInfo r : qResults)
		    	{
		    		results.push(Util.toClientObject(r));
		    	}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    finally {
	    	pm.close();
	    }

	    NewsLetterClient[] ret = new NewsLetterClient[results.size()];
	    for(int i = 0;;i++)
	    {
	    	try {
	    		ret[i] = results.pop();
	    	} catch(Exception ex) {
	    		break;
	    	}
	    }
	    
		return ret;
	}

	public static NewsLetterInfo uploadURL(String url, String fileName)
			throws IllegalArgumentException, NotLoggedInException {
		checkLoggedIn();
		NewsLetterInfo o = null;
		PersistenceManager pm = Util.getPersistenceManager();
		try {
			// TBD: Validate that its a valid URL, for example from dropbox or google docs
			// Throw IllegalArgumentExceptionif invalid URL
			o = pm.makePersistent(new NewsLetterInfo(url, Util.getUser().getEmail(), fileName));
		}
		finally {
			pm.close();
		}
		return o;
	}

	public static RolandUserInfo[] getAllUsers()
	{
		PersistenceManager pm = Util.getPersistenceManager();
	    Stack<RolandUserInfo> results = new Stack<RolandUserInfo>();
	    try {
	    	Query q = pm.newQuery(RolandUserInfo.class);
	    	/// TBD: How to retrieve in reverse cronological order?
	    	q.setOrdering("createDate");
	    	List<RolandUserInfo> qResults = (List<RolandUserInfo>) q.execute();
	    	for(RolandUserInfo r : qResults)
	    	{
	    		results.push(r);
	    	}
	    }
	    finally {
	    	pm.close();
	    }

	    RolandUserInfo[] ret = new RolandUserInfo[results.size()];
	    for(int i = 0;;i++)
	    {
	    	try {
	    		ret[i] = results.pop();
	    	} catch(Exception ex) {
	    		break;
	    	}
	    }
	    
		return ret;
	}
	
	public static RolandUserInfo createUser(String user, Integer quota, String quotaUnits) throws IllegalArgumentException, NotLoggedInException {
		checkLoggedIn();
		// TBD: Check that logged in user is same as our admin user com.roland.ui.shared.Util.adminEmailAddress.
		// If not, throw IllegalArgumentException.
		RolandUserInfo o = null;
		PersistenceManager pm = Util.getPersistenceManager();
		try {
			o = pm.makePersistent(new RolandUserInfo(user, quota, quotaUnits));
		}
		finally {
			pm.close();
		}
		return o;
	}
}
