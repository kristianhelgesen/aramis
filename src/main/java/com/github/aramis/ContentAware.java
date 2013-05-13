package com.github.aramis;

public interface ContentAware<T> {
	
	public T getContent();
	public void setContent( T content);

}
