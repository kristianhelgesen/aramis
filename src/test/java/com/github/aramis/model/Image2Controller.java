package com.github.aramis.model;

import com.github.aramis.ContentAware;


public class Image2Controller implements ContentAware<Image2> {
	
	private Image2 content;
	
	public String getSrc() {
		return "http://images/"+content.getImageID(); 
	}

	@Override
	public void setContent(Image2 content) {
		this.content = content;
	}

	@Override
	public Image2 getContent() {
		return content;
	}
	
	
	
}