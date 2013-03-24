package moly;

public interface ParserCallback {
	
	public void handleUnescapedVariable(String var);

	public void handleVariable( String var);
	
	public void handleText( String text);
	
	public void handleRender( String render);

	public void handleSectionStart(String string);

	public void handleInvertedSectionStart(String string);

	public void handleSectionEnd(String string);
	
//	public void handleDecoratorStart( String decoratorName);
	

}
