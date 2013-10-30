package com.github.aramis.model;

import com.github.aramis.model.Article.ImageReference;



public class Image2Controller {
	
	private Image2 content;

	public Image2Controller( Image2 content, ImageReference ref){
		this.content = content;
		System.out.println(ref);
	}
	
	
	public String getSrc() {
		return "http://images/"+content.getImageID(); 
	}


	
	
}
