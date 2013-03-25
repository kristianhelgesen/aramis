package moly;

import java.io.InputStream;

public class Parser {
	
	static final int EOF = -1;
	
	enum ParserState {
		TEXT,
		EXPSTARTING,
		EXPSTART,
		EXPSTOPPING,
		RENDERSTARTING,
		RENDERSTART,
		RENDERSTOPPING, 
		EXPCOMMENT, 
		EXPSECTIONSTART, 
		EXPSECTIONEND, 
		EXPINVSECTION,
		EXPSTARTUNESCAPED,
		EXPSTOPUNESCAPED,
		DECORATOR_START_1,
		DECORATOR_START_2,
		DECORATOR_SECTIONSTART,
		DECORATOR_SECTIONEND,
		DECORATOR_STOPPING,
		DECORATOR_USE,
		DECORATOR_USE_STOPPING,
		
	}

	private ParserState ps = ParserState.TEXT;
	private StringBuffer buffer = new StringBuffer();
	private ParserCallback callback;
	
	public Parser( ParserCallback callback) {
		this.callback = callback;
	}
	
	
	public void parse( InputStream is) throws Exception{
		
		if( is==null) {
			throw new ParserException("InputStream to Parser cannot be null");
		}
		
		int b;
		while( (b=is.read())!=-1) {
			parse( b, 1);
		}
		parse( EOF,1);
		
	}
	
	private void parse( int ch, int line) {

		switch( ps) {
			case TEXT: 
				switch( ch) {
					case '{': ps = ParserState.EXPSTARTING;break;
					case '[': ps = ParserState.RENDERSTARTING;break;
					case '<': ps = ParserState.DECORATOR_START_1;break;
					case EOF: callback.handleText( buffer.toString()); break; // EOF
					default: buffer.append( (char)ch);
				}
				break;
				
			case RENDERSTARTING:
				switch( ch) {
					case '[': 
						ps = ParserState.RENDERSTART;
						callback.handleText( buffer.toString());
						buffer = new StringBuffer();
						break;
					default: 
						buffer.append( '[').append( (char)ch);
						ps = ParserState.TEXT;
				}
				break;
				
			case EXPSTARTING:
				switch( ch) {
					case '{': 
						ps = ParserState.EXPSTART;
						callback.handleText( buffer.toString());
						buffer = new StringBuffer();
						break;
					default:
						buffer.append( '{').append( (char)ch);
						ps = ParserState.TEXT;
				}
				break;
				
			case DECORATOR_START_1:
				switch( ch) {
					case '<':
						ps = ParserState.DECORATOR_START_2;
						callback.handleText( buffer.toString());
						buffer = new StringBuffer();
						break;
					default:
						buffer.append( '<').append( (char)ch);
						ps = ParserState.TEXT;
				}
				break;
				
			case RENDERSTART:
				switch( ch) {
					case ']': ps = ParserState.RENDERSTOPPING;break;
					default: buffer.append( (char)ch);
				}
				break;	
				
			case DECORATOR_START_2:
				switch( ch) {
					case '<': if(buffer.length()==0) ps = ParserState.DECORATOR_USE; break;
					case '#': if(buffer.length()==0) ps = ParserState.DECORATOR_SECTIONSTART; break;
					case '/': if(buffer.length()==0) ps = ParserState.DECORATOR_SECTIONEND; break;
					case '>': 
						ps = ParserState.DECORATOR_STOPPING; 
						callback.handleDecoratorApplySection( buffer.toString());
						buffer = new StringBuffer();
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case DECORATOR_SECTIONSTART:
				switch( ch) {
					case '>':
						callback.handleDecoratorSectionStart( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.DECORATOR_STOPPING;
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case DECORATOR_SECTIONEND:
				switch( ch) {
					case '>':
						callback.handleDecoratorSectionEnd( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.DECORATOR_STOPPING;
						break;
					default: buffer.append( (char)ch);
				}
				break;				
				
			case DECORATOR_USE:
				switch( ch) {
					case '>': 
						callback.handleUseDecorator( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.DECORATOR_USE_STOPPING; 
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case DECORATOR_USE_STOPPING:
				switch( ch) {
					case '>': 
						ps = ParserState.DECORATOR_STOPPING;
						break;
					default: throw new ParserException("Expected > at line "+line);				
				}
				break;
				
			case DECORATOR_STOPPING:
				switch( ch) {
					case '>': 
						ps = ParserState.TEXT;
						break;
					default: throw new ParserException("Expected > at line "+line+".");				
				}
				break;
				
			case EXPSTART:
				switch( ch) {
					case '#': if(buffer.length()==0) ps = ParserState.EXPSECTIONSTART; break;
					case '/': if(buffer.length()==0) ps = ParserState.EXPSECTIONEND; break;
					case '^': if(buffer.length()==0) ps = ParserState.EXPINVSECTION; break;
					case '{': if(buffer.length()==0) ps = ParserState.EXPSTARTUNESCAPED; break;
					case '!': if(buffer.length()==0) ps = ParserState.EXPCOMMENT; break;
					case '}': ps = ParserState.EXPSTOPPING; break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case EXPSECTIONSTART:
				switch( ch) {
					case '}': 
						callback.handleSectionStart( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPSTOPPING; 
						break;
					default: buffer.append( (char)ch);
				}
				break;		
				
			case EXPINVSECTION:
				switch( ch) {
					case '}': 
						callback.handleInvertedSectionStart( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPSTOPPING; 
						break;
					default: buffer.append( (char)ch);
				}
				break;			
				
			case EXPSECTIONEND:
				switch( ch) {
					case '}': 
						callback.handleSectionEnd( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPSTOPPING; 
						break;
					default: buffer.append( (char)ch);
				}
				break;			
				
			case EXPSTARTUNESCAPED:
				switch( ch) {
					case '}': 
						callback.handleUnescapedVariable( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPSTOPUNESCAPED; 
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case EXPCOMMENT:
				switch( ch) {
					case '}': ps = ParserState.EXPSTOPPING; break;
				}
				break;			
				
			case EXPSTOPUNESCAPED: 
				switch( ch) {
					case '}': 
						ps = ParserState.EXPSTOPPING;
					break;
					default: throw new ParserException("Expected } at line "+line);
				}
				break;
				
			case EXPSTOPPING:
				switch( ch) {
					case '}': 
						ps = ParserState.TEXT;
						callback.handleVariable( buffer.toString());
						buffer = new StringBuffer();
					break;
					default: throw new ParserException("Expected } at line "+line);
				}
				break;

			case RENDERSTOPPING:
				switch( ch) {
					case ']': 
						ps = ParserState.TEXT;
						callback.handleRender( buffer.toString());
						buffer = new StringBuffer();
					break;
					default: throw new ParserException("Expected ] at line "+line);
				}
				break;
				

		}
			
			
		
		
	}
	
	
	
}
