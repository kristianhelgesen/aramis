package com.github.aramis;

public abstract class ContentProvider {
	

	public abstract Object getContent(Object contentref);

	
	@SuppressWarnings("unchecked")
	public <T> T getContent( Object contentref, Class<T> type) {

		Object value = getContent(contentref);
		if (value == null) {
			return null;
		}
		if (!type.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException(
					"Incorrect type specified for conetentref'" + contentref
							+ "'. Expected [" + type + "] but actual type is ["
							+ value.getClass() + "]");

		}
		return (T) value;
	}

}
