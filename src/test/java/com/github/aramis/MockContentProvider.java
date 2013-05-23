package com.github.aramis;

import java.util.HashMap;
import java.util.Map;

import com.github.aramis.ContentProvider;
import com.github.aramis.model.Article;
import com.github.aramis.model.Image;


public class MockContentProvider extends ContentProvider{
	
	
	Map<String,Object> content = new HashMap<String,Object>();

	public MockContentProvider() {
		content.put("123", new MockContent());
		content.put("article123", new Article());
		content.put("image234", new Image());
	}
	

	public Object getContent(Object contentref) {
		return content.get(contentref);
	}
	
	
	public class MockContent {
		public String getName() { return "MockContent 1";}
		public String getTitle() { return "MockContent title";}
		public String getBody() { return "MockContent body";}
	}
	
	


}