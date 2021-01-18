package com.yc.web.core;

public interface Servlet {
	public void init();
	
	public void Service(ServletRequest request, ServletResponse response);

	public void doGet(ServletRequest request, ServletResponse response);

	public void doPost(ServletRequest request, ServletResponse response);

}
