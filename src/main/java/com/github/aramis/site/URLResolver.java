package com.github.aramis.site;

public interface URLResolver {

	public Object urlToContent(String uri);
	
	public String contentToURL( Object content);

}
