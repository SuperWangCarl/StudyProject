package com.util.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
/**
 * 
 * @author SuperWang
 *
 */
public abstract class BaseController {

	private final static ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal<HttpServletRequest>();
	private final static ThreadLocal<HttpServletResponse> RESPONSE = new ThreadLocal<HttpServletResponse>();
	private final static ThreadLocal<ModelMap> MODEL = new ThreadLocal<ModelMap>();
	
	@ModelAttribute
	public final void init(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		REQUEST.set(request);
		RESPONSE.set(response);
		MODEL.set(model);
	}
	
	public final HttpServletRequest getRequest(){
		return REQUEST.get();
	}
	
	public final HttpServletResponse getResponse(){
		return RESPONSE.get();
	}
	
	public final ModelMap getModel(){
		return MODEL.get();
	}
}
