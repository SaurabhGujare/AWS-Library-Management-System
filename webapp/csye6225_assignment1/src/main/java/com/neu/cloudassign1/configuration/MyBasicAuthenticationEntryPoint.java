package com.neu.cloudassign1.configuration;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	 @Override
	    public void commence
	      (HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) 
	      throws IOException, ServletException {
	        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        PrintWriter writer = response.getWriter();
		 Map<String, String> messageMap = new HashMap<>();
		 messageMap.put("HTTP Status 401 ",authEx.getMessage());
		 writer.println(new JSONObject(messageMap));
	    }
	 
	    @Override
	    public void afterPropertiesSet() throws Exception {
	        setRealmName("Baeldung");
	        super.afterPropertiesSet();
	    }

}
