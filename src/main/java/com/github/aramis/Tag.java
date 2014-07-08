package com.github.aramis;

public class Tag {
	enum TagType {
		
		RENDER( 				"[[", 	"]]"),
		RENDERPARTIAL( 			"[[>", 	"]]"),
		DECORATOR( 				"<<", 	">>"),
		DECORATOR_DEFINITION( 	"<<<", 	">>>"),
		DECORATOR_START( 		"<<#", 	">>"),
		DECORATOR_END( 			"<</", 	">>"),
		EXPRESSION( 			"{{", 	"}}"),
		EXPRESSION_UNESCAPED( 	"{{{", 	"}}}"),
		SECTION_INVERSE( 		"{{^", 	"}}"),
		SECTION_START( 			"{{#", 	"}}"),
		SECTION_END( 			"{{/", 	"}}"),
		PARTIAL( 				"{{>", 	"}}"),
		COMMENT( 				"{{!", 	"}}"),
		;
		
		Token startToken;
		Token endToken;
		TagType( String start, String end) {
			startToken = new Token( start, this);
			endToken = new Token( end, this);
		}
	}
	
	public Tag( TagType tagdef, String value) {
		this.tagdef = tagdef;
		this.value = value;
	}
	
	public TagType tagdef;
	public String value;

	@Override
	public String toString() {
		return tagdef.startToken.charseq +"("+ value + ")"+ tagdef.endToken.charseq;
	}
 
}
