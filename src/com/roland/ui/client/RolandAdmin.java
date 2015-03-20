package com.roland.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.roland.ui.shared.FieldVerifier;
import com.roland.ui.shared.LoginInfo;
import com.roland.ui.shared.RolandUserInfo;
import com.roland.ui.shared.Util;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RolandAdmin implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private LoginInfo loginInfo = null;
	private FlexTable newsLettersFlexTable;

	private boolean devMode = false;

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
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	// Normal processing
	        	processLoginSuccess();
	        } else {
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
	    RootPanel.get("adminExistingUsersContainer").add(newsLettersFlexTable);
	}
	
	private void displayExistingUsers() {
		if(loginInfo == null || loginInfo.getEmailAddress() == null || ! loginInfo.getEmailAddress().equals(Util.adminEmailAddress))
			return;

		greetingService.getAllUsers(new AsyncCallback<RolandUserInfo[]>()
				{
					@Override
					public void onFailure(Throwable caught) {
						
					}

					public void onSuccess(RolandUserInfo[] results) {
						newsLettersFlexTable.clear();
					    for(final RolandUserInfo result : results)
					    {
					    	int numRows = newsLettersFlexTable.getRowCount();
					    	Label info = new Label(result.getUserEmail() + ": " + result.getQuota() + " " + result.getQuotaUnits() + ", created at " + result.getCreateDate());
					    	newsLettersFlexTable.setWidget(numRows, 0, info);
					    }
					}
			
				});
	}

	private void processLoginSuccess()
	{
		final Button sendButton = new Button("Add User");
		final Label titleLabel = new Label("User: ");
		final Label quotaLabel = new Label("Quota: ");
		final Label quotaUnitsLabel = new Label("Quota Units: ");
		final TextBox nameField = new TextBox();
		final TextBox quotaField = new TextBox();
		final TextBox quotaUnitsField = new TextBox();
		nameField.setText("");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		if(loginInfo != null && loginInfo.getEmailAddress() != null && loginInfo.getEmailAddress().equals(Util.adminEmailAddress))
		{
			displayExistingUsers();
			HTML adminTitle = new HTML("<h2><center>Roland Administration</center></h2><br/>Existing Users:");
			RootPanel.get("adminHeaderContainer").add(adminTitle);
			Grid userPanelGrid = new Grid(3, 2);
			userPanelGrid.setWidget(0, 0, titleLabel);
			userPanelGrid.setWidget(1, 0, quotaLabel);
			userPanelGrid.setWidget(2, 0, quotaUnitsLabel);
			userPanelGrid.setWidget(0, 1, nameField);
			userPanelGrid.setWidget(1, 1, quotaField);
			userPanelGrid.setWidget(2, 1, quotaUnitsField);
			RootPanel.get("adminSendButtonContainer").add(sendButton);
			RootPanel.get("adminErrorLabelContainer").add(errorLabel);
			RootPanel.get("adminUserDataPanel").add(userPanelGrid);
		}

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
				final String userEmail = nameField.getText();
				final String quota = quotaField.getText();
				final String quotaUnits = quotaUnitsField.getText();
				if (!FieldVerifier.isValidName(userEmail)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(userEmail);
				serverResponseLabel.setText("");
				try {
					greetingService.createUser(userEmail, Integer.parseInt(quota), quotaUnits,
							new AsyncCallback<Void>() {
								public void onFailure(Throwable caught) {
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
									dialogBox.setText("User created.");
									serverResponseLabel
											.removeStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML("Saved " + userEmail + ", quota " + quota + " " + quotaUnits);
									dialogBox.center();
									closeButton.setFocus(true);
									displayExistingUsers();
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
