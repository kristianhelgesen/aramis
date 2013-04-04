package site;

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

import moly.ContentProvider;
import moly.RenderEngine;

public class RenderDispatcher implements Filter {
	
	private RenderEngine renderEngine;
	private URLResolver urlResolver;
	private String controllerPackage;
	private String templatePackage;
	
	private String contentProviderClass;
	private String urlResolverClass;
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
		ContentProvider contentProvider;
		try {
			contentProvider = (ContentProvider)Class.forName(contentProviderClass).newInstance();
			urlResolver = (URLResolver)Class.forName(urlResolverClass).newInstance();
			renderEngine = new RenderEngine( contentProvider, controllerPackage, templatePackage);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		
		String uri = getPlainRequestURI( (HttpServletRequest)req);
		Object resolvedContent = urlResolver.urlToContent(uri);
		if( resolvedContent!=null) {
			OutputStream os = res.getOutputStream();
			renderEngine.render( os, resolvedContent, "", new HashMap<String,Object>());
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