package com.github.aramis.model;
public class ArticleB {
	
	public static ImageReference imageRef = new ImageReference();
	
	public String getTitle() { return "Mock article title";}
	public String getBody() { return "Mock article body";}
	public ImageReference getImage() { return imageRef; }
	
	public static class ImageReference {
		public String getId() { return "imageBBB";}
		public String getCaption() { return "Image caption";}
	}
}
