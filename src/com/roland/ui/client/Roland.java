package com.roland.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.roland.ui.shared.FieldVerifier;
import com.roland.ui.shared.LoginInfo;
import com.roland.ui.shared.NewsLetterClient;
import com.roland.ui.shared.Util;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Roland implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private FlexTable newsLettersFlexTable;

	private boolean devMode = false;

	private void loadLogin() {
		    // Assemble login panel.
		    signInLink.setHref(loginInfo.getLoginUrl());
		    loginPanel.add(signInLink);
		    RootPanel.get("headerContainer").add(loginPanel);
	}

	  private void handleError(Throwable error) {
		    Window.alert(error.getMessage());
		    if (error instanceof NotLoggedInException) {
		      Window.Location.replace(loginInfo.getLogoutUrl());
		    }
		  }


	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    // Check login status using login service.
	    LoginServiceAsync loginService = GWT.create(LoginService.class);
	    String requestURI = GWT.getHostPageBaseURL();
	    String hostName = requestURI.substring(requestURI.indexOf(':')+3, requestURI.lastIndexOf(':'));
	    if(devMode)
	    {
	    	requestURI += "Roland.html?gwt.codesvr=" + hostName + ":9997";
	    }
	    loginService.login(requestURI, new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  handleError(error);
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	// Normal processing
	        	processLoginSuccess();
	        } else {
	          // Load the page with login link...
	          loadLogin();
	        }
	      }
	    });
	    createNewslettersFlexTable();
	}

	private void createNewslettersFlexTable()
	{
		newsLettersFlexTable = new FlexTable();
	    newsLettersFlexTable.addStyleName("cw-FlexTable");
	    newsLettersFlexTable.setWidth("32em");
	    newsLettersFlexTable.setCellSpacing(5);
	    newsLettersFlexTable.setCellPadding(3);
	    RootPanel.get("existingUrlsContainer").add(newsLettersFlexTable);
	}
	
	private void displayExistingNewsLetters() {
		if(loginInfo == null || loginInfo.getEmailAddress() == null || ! loginInfo.getEmailAddress().equals(Util.adminEmailAddress))
			return;

		//greetingService.getUploadedURLsAfter("2011/11/26 10:15", new AsyncCallback<NewsLetterClient[]>()
		greetingService.getAllUploadedURLs(new AsyncCallback<NewsLetterClient[]>()
				{
					@Override
					public void onFailure(Throwable caught) {
						
					}

					public void onSuccess(NewsLetterClient[] results) {
						newsLettersFlexTable.clear();
					    for(final NewsLetterClient result : results)
					    {
					    	int numRows = newsLettersFlexTable.getRowCount();
					    	Anchor link = new Anchor();
					    	link.setText(result.getCreator() + ": " + result.getCreateDate());
					    	link.addClickHandler(new ClickHandler()
					    	{
								@Override
								public void onClick(ClickEvent event) {
									DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
									simplePopup.setWidget(new HTML(result.getUrl()));
						            // Reposition the popup relative to the button
						            Widget source = (Widget) event.getSource();
						            int left = source.getAbsoluteLeft() + 10;
						            int top = source.getAbsoluteTop() + 10;
						            simplePopup.setPopupPosition(left, top);
						            // Show the popup
						            simplePopup.show();

								}
					    		
					    	});
					    	newsLettersFlexTable.setWidget(numRows, 0, link);
					    }
					}
			
				});
	}

	private void processLoginSuccess()
	{
		displayExistingNewsLetters();
		
		final Button sendButton = new Button("Send");
		final Label titleLabel = new Label(loginInfo.getEmailAddress() + ": Please enter the URL of the data you want to upload:");
		final Label fileNameLabel = new Label("File Name:");
		final TextBox fileNameField = new TextBox();
		final TextBox nameField = new TextBox();
		nameField.setText("");
		final Label errorLabel = new Label();
	    // Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
	    RootPanel.get("headerContainer").add(signOutLink);

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		Grid uploadUiPanel = new Grid(2, 2);
		uploadUiPanel.setWidget(0,  0, titleLabel);
		uploadUiPanel.setWidget(1, 0, fileNameLabel);
		uploadUiPanel.setWidget(0, 1, nameField);
		uploadUiPanel.setWidget(1, 1, fileNameField);
		RootPanel.get("nameFieldContainer").add(uploadUiPanel);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Sending URL to server for further processing...");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending URL to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server response:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				final String url = nameField.getText();
				final String fileName = fileNameField.getText();
				if (!FieldVerifier.isValidName(url)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(fileName);
				serverResponseLabel.setText("");
				try {
					greetingService.uploadURL(url, fileName,
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
									handleError(caught);
									// Show the RPC error message to the user
									dialogBox
											.setText("Error in saving URL to database. Please contact the system administrator.");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(SERVER_ERROR);
									dialogBox.center();
									closeButton.setFocus(true);
								}

								public void onSuccess(Void obj) {
									dialogBox.setText("You will be notified upon completion of processing of the data.");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML("Saved " + fileName + " for further processing.");
									dialogBox.center();
									closeButton.setFocus(true);
									displayExistingNewsLetters();
								}
							});
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotLoggedInException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
