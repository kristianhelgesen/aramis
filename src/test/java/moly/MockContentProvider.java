package moly;

import moly.model.Article;
import moly.model.Image;

public class MockContentProvider extends ContentProvider{

	public Object getContent(String contentref) {
		if( "123".equals( contentref)) {
			return new MockContent();
		}
		if( "article123".equals( contentref)) {
			return new Article();
		}
		if( "image234".equals( contentref)) {
			return new Image();
		}
		
		
		return null;
	}
	
	
	public class MockContent {
		public String getName() { return "MockContent 1";}
		public String getTitle() { return "MockContent title";}
		public String getBody() { return "MockContent body";}
	}
	
	


}
