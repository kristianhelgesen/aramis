package com.github.aramis.model;

import com.github.aramis.model.Article.ImageReference;



public class Image3Controller {
	
	public Image3 content;
	public ImageReference ref;
	
	public Image3Controller( Image3 content, ImageReference ref){
		this.content = content;
		this.ref = ref;
	}
	
	
	public String getSrc() {
		return "http://images/"+content.getImageID(); 
	}


	
	
}
