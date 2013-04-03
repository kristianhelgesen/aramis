package site;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RenderDispatcher implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void doFilter( ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		
		String uri = getPlainRequestURI( (HttpServletRequest)req);
		Object resolvedContent = urlResolver.urlToContent(uri);
		if( resolvedContent!=null) {
			
			renderEngine.render( resolvedContent);
			
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