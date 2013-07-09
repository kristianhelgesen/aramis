package com.github.aramis.site;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.ContentProvider;
import com.github.aramis.RenderEngine;

public class RenderDispatcher implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger( RenderDispatcher.class);
	
	private RenderEngine renderEngine;
	private URLResolver urlResolver;
	private String controllerPackage;
	private String templatePackage;
	
	private String contentProviderClass;
	private String urlResolverClass;
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		System.out.println("init");
		contentProviderClass = filterConfig.getServletContext().getInitParameter("contentProvider");
		urlResolverClass = filterConfig.getServletContext().getInitParameter("urlResolver");
		System.out.println("urlResolverClass"+urlResolverClass);
		templatePackage = filterConfig.getServletContext().getInitParameter("templatePackage");
		controllerPackage = filterConfig.getServletContext().getInitParameter("controllerPackage");
		
		if( templatePackage==null){
			templatePackage = "/templates";
		}
		if( controllerPackage==null){
			controllerPackage = "controllers";
		}
		
		
		if( contentProviderClass==null || urlResolverClass==null) {
			return;
		}
		
		ContentProvider contentProvider = null;
		try {
			contentProvider = (ContentProvider)Class.forName( contentProviderClass).newInstance();
		} catch (ClassNotFoundException e) {
			logger.warn( "Class {} for contentProvider not found",contentProviderClass);
		} catch (InstantiationException e) {
			logger.error( "", e);
		} catch (IllegalAccessException e) {
			logger.error( "", e);
		}
		
		try {
			urlResolver = (URLResolver)Class.forName( urlResolverClass).newInstance();
		} catch (ClassNotFoundException e) {
			logger.warn( "Class {} for urlResolver not found",urlResolverClass);
		} catch (InstantiationException e) {
			logger.error( "", e);
		} catch (IllegalAccessException e) {
			logger.error( "", e);
		}
		
		renderEngine = new RenderEngine( contentProvider);
	}

	
	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		
		String uri = getPlainRequestURI( (HttpServletRequest)req);
		System.out.println("filter: "+uri);

		if( urlResolver==null) {
			filterChain.doFilter( req, res);
			return;
		}

		Object resolvedContent = urlResolver.urlToContent(uri);
		if( resolvedContent!=null) {
			OutputStream os = res.getOutputStream();
			renderEngine.renderContent( os, resolvedContent, null, "", new HashMap<String,Object>());
		}
		else {
			filterChain.doFilter( req, res);
		}
		
	}
	
	
	/**
	 * Returns the requestURI excluding the webapp contextpath
	 * @param request
	 * @return
	 */
	protected String getPlainRequestURI(HttpServletRequest request){
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		if( uri.startsWith(contextPath))
			uri = uri.substring(contextPath.length());
		return uri;
	}	


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}