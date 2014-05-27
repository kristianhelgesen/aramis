package com.github.aramis.model;




public class ImageBController {
	
	public Image2 content;
	public int width;

	public ImageBController( Image2 content){
		this.content = content;
	}
	
	
	public String getSrc() {
		System.out.println("ÆÆÆØØØØ");
		return "http://images/"+content.getImageID()+"?w="+width; 
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}

	

	
	
}
