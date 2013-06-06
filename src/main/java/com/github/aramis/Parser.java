package com.github.aramis;

import java.io.InputStream;

public class Parser {
	
	static final int EOF = -1;
	
	enum ParserState {
		TEXT,
		EXPRESSION_START_1,
		EXPRESSION_START_2,
		EXPRESSION_COMMENT, 
		EXPRESSION_SECTION_START, 
		EXPRESSION_SECTION_END, 
		EXPRESSION_INV_SECTION,
		EXPRESSION_PARTIAL,
		EXPRESSION_START_UNESCAPED,
		EXPRESSION_STOP_2,
		EXPRESSION_STOP_1,
		RENDER_START_1,
		RENDER_START_2,
		RENDER_STOP_1, 
		DECORATOR_START_1,
		DECORATOR_START_2,
		DECORATOR_SECTIONSTART,
		DECORATOR_SECTIONEND,
		DECORATOR_STOP_1,
		DECORATOR_USE,
		DECORATOR_STOP_2,
		
	}

	private ParserState ps = ParserState.TEXT;
	private StringBuffer buffer = new StringBuffer();
	private ParserCallback callback;
	
	public Parser( ParserCallback callback) {
		this.callback = callback;
	}
	
	
	public void parse( String name, InputStream is) throws Exception{
		
		if( is==null) {
			throw new ParserException("InputStream to Parser cannot be null");
		}
		
		int b;
		while( (b=is.read())!=-1) {
			parse( name, b, 1);
		}
		parse( name, EOF, 1);
		
	}
	
	private void parse( String name, int ch, int line) {

		switch( ps) {
			case TEXT: 
				switch( ch) {
					case '{': ps = ParserState.EXPRESSION_START_1;break;
					case '[': ps = ParserState.RENDER_START_1;break;
					case '<': ps = ParserState.DECORATOR_START_1;break;
					case EOF: callback.handleText( buffer.toString()); break; // EOF
					default: buffer.append( (char)ch);
				}
				break;
				
			case RENDER_START_1:
				switch( ch) {
					case '[': 
						ps = ParserState.RENDER_START_2;
						callback.handleText( buffer.toString());
						buffer = new StringBuffer();
						break;
					default: 
						buffer.append( '[').append( (char)ch);
						ps = ParserState.TEXT;
				}
				break;
				
			case EXPRESSION_START_1:
				switch( ch) {
					case '{': 
						ps = ParserState.EXPRESSION_START_2;
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
				
			case RENDER_START_2:
				switch( ch) {
					case ']': ps = ParserState.RENDER_STOP_1;break;
					default: buffer.append( (char)ch);
				}
				break;	
				
			case DECORATOR_START_2:
				switch( ch) {
					case '<': if(buffer.length()==0) ps = ParserState.DECORATOR_USE; break;
					case '#': if(buffer.length()==0) ps = ParserState.DECORATOR_SECTIONSTART; break;
					case '/': if(buffer.length()==0) ps = ParserState.DECORATOR_SECTIONEND; break;
					case '>': 
						ps = ParserState.DECORATOR_STOP_1; 
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
						ps = ParserState.DECORATOR_STOP_1;
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case DECORATOR_SECTIONEND:
				switch( ch) {
					case '>':
						callback.handleDecoratorSectionEnd( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.DECORATOR_STOP_1;
						break;
					default: buffer.append( (char)ch);
				}
				break;				
				
			case DECORATOR_USE:
				switch( ch) {
					case '>': 
						callback.handleUseDecorator( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.DECORATOR_STOP_2; 
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case DECORATOR_STOP_2:
				switch( ch) {
					case '>': 
						ps = ParserState.DECORATOR_STOP_1;
						break;
					default: throw new ParserException("Expected > at line "+line+" in template "+name);				
				}
				break;
				
			case DECORATOR_STOP_1:
				switch( ch) {
					case '>': 
						ps = ParserState.TEXT;
						break;
					default: throw new ParserException("Expected > at line "+line+" in template "+name);				
				}
				break;
				
			case EXPRESSION_START_2:
				switch( ch) {
					case '#': if(buffer.length()==0) ps = ParserState.EXPRESSION_SECTION_START; break;
					case '/': if(buffer.length()==0) ps = ParserState.EXPRESSION_SECTION_END; break;
					case '^': if(buffer.length()==0) ps = ParserState.EXPRESSION_INV_SECTION; break;
					case '>': if(buffer.length()==0) ps = ParserState.EXPRESSION_PARTIAL; break;
					case '{': if(buffer.length()==0) ps = ParserState.EXPRESSION_START_UNESCAPED; break;
					case '!': if(buffer.length()==0) ps = ParserState.EXPRESSION_COMMENT; break;
					case '}': 
						callback.handleVariable( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_1; 
					break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case EXPRESSION_SECTION_START:
				switch( ch) {
					case '}': 
						callback.handleSectionStart( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_1; 
						break;
					default: buffer.append( (char)ch);
				}
				break;		
				
			case EXPRESSION_PARTIAL:
				switch( ch) {
					case '}': 
						callback.handlePartial( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_1; 
						break;
					default: buffer.append( (char)ch);
				}
				break;			

			case EXPRESSION_INV_SECTION:
				switch( ch) {
					case '}': 
						callback.handleInvertedSectionStart( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_1; 
						break;
					default: buffer.append( (char)ch);
				}
				break;			
				
			case EXPRESSION_SECTION_END:
				switch( ch) {
					case '}': 
						callback.handleSectionEnd( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_1; 
						break;
					default: buffer.append( (char)ch);
				}
				break;			
				
			case EXPRESSION_START_UNESCAPED:
				switch( ch) {
					case '}': 
						callback.handleUnescapedVariable( buffer.toString());
						buffer = new StringBuffer();
						ps = ParserState.EXPRESSION_STOP_2; 
						break;
					default: buffer.append( (char)ch);
				}
				break;
				
			case EXPRESSION_COMMENT:
				switch( ch) {
					case '}': ps = ParserState.EXPRESSION_STOP_1; break;
				}
				break;			
				
			case EXPRESSION_STOP_2: 
				switch( ch) {
					case '}': 
						ps = ParserState.EXPRESSION_STOP_1;
					break;
					default: throw new ParserException("Expected } at line "+line+" in template "+name);
				}
				break;
				
			case EXPRESSION_STOP_1:
				switch( ch) {
					case '}': ps = ParserState.TEXT;
					break;
					default: throw new ParserException("Expected } at line "+line+" in template "+name);
				}
				break;

			case RENDER_STOP_1:
				switch( ch) {
					case ']': 
						ps = ParserState.TEXT;
						callback.handleRender( buffer.toString());
						buffer = new StringBuffer();
					break;
					default: throw new ParserException("Expected ] at line "+line+" in template "+name);
				}
				break;
				

		}
			
			
		
		
	}
	
	
	
}
