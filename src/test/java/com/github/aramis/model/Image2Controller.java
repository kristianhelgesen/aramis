package com.github.aramis.model;




public class Image2Controller {
	
	public Image2 content;

	public Image2Controller( Image2 content){
		this.content = content;
	}
	
	
	public String getSrc() {
		return "http://images/"+content.getImageID(); 
	}


	
	
}
