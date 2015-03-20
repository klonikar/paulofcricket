package com.roland.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.roland.ui.shared.LoginInfo;

public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
