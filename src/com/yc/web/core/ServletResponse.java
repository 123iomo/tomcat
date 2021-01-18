package com.yc.web.core;

import java.io.IOException;
import java.io.PrintWriter;

public interface ServletResponse {
	
	public void sendRedirect(String url);
	
	public PrintWriter getWriter() throws IOException;

}
