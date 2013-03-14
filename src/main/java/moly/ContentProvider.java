package moly;

public abstract class ContentProvider {
	

	public abstract Object getContent(String contentref);

	
	@SuppressWarnings("unchecked")
	public <T> T getContent( String contentref, Class<T> type) {

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