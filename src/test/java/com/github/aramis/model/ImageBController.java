package com.github.aramis.model;




public class ImageBController {
	
	public ImageB content;
	public int width;

	public ImageBController( ImageB content){
		this.content = content;
	}
	
	public String getSrc() {
		return "http://images/"+content.getImageID()+"?w="+width; 
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
