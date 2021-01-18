package com.yc.web.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yc.tomcat.core.ConstantInfo;
import com.yc.tomcat.util.StringUtil;

public class HttpServletRequest implements ServletRequest {
	private String method;
	private String url;
	private InputStream is;
	private BufferedReader reader;
	private Map<String, String> parameter = new HashMap<String, String>();
    private String protocalVersion;
	
    public HttpServletRequest (InputStream is) {
		this.is = is;
		parse();
	}
	
	@Override
	public void parse() {
		try {
			reader = new BufferedReader(new InputStreamReader(is));
		    List<String> headers = new ArrayList<String>();
		    String line = null;
			while((line = reader.readLine()) != null && !"".equals(line)) {
				headers.add(line);
			}
		    
			// headers.forEach(System.out::println);
			parseFirseLine(headers.get(0)); // 解析起始行
			
			parseParameter(headers);
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

	/**
	 * 解析参数
	 * @param headers
	 */
	private void parseParameter(List<String> headers) {
		// 如果是GET请求，那么参数只会在起始行中有
		String str = headers.get(0).split("")[1];
		
		if (str.contains("?")) {
			str = str.substring(str.indexOf("?") + 1);
			String[] params = str.split("&"); // ?account=yc&pwd=123
			String[] temp = null;
			for (String param : params) {
				temp = param.split("=");
				this.parameter.put(temp[0], temp[1]);
			}
		}
		
		// 如果是post请求，那么要获取头部字段中的Content-Length  Content-Type
		if (ConstantInfo.REQUEST_METHOD_POST.equals(this.method)) {
			int len = 0;
			for (String head: headers) {
				if (head.contains("Content-Length")) {
					len = Integer.parseInt(head.substring(head.indexOf(":") +1).trim());
					break;
				}
			}
			
			if (len <= 0) {
				return;
			}
			
			try {
				char[] ch = new char[10240];
				int count = 0, total = 0;
				StringBuffer sbf = new StringBuffer(10240);
				while((count = reader.read(ch)) > 0) {
					sbf.append(ch, 0, count);
					total += count;
					
					if (total >= len) {
						break;
					}
				}
				
				str = URLDecoder.decode(sbf.toString(), "utf-8");
				String[] params = str.split("&");
				String[] temp = null;
				for (String param : params) {
					temp = param.split("=");
					this.parameter.put(temp[0], temp[1]);
				}
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	
	}

	/**
	 * 解析起始行
	 * @param str
	 * @param StringUtil 
	 */
	private void parseFirseLine(String str) {
		if (StringUtil.checkNull(str)) {
			return;
		}
		String[] arrs = str.split(" ");
		this.method = arrs[0]; // 请求方式
		if (arrs[1].contains("?")) { // 说明有请求参数
			this.url = arrs[1].substring(0, arrs[1].indexOf("?"));
		
		} else {
			this.url = arrs[1];
		}
		this.protocalVersion = arrs[2];
	}

	
	@Override
	public String getParameter(String key) {
		return this.parameter.getOrDefault(key, null);
	
	}

	
	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public String getUrl() {
		return this.url;
	}
	
	public String getProtocalVersion() {
		return protocalVersion;
	}

}
