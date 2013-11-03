package com.github.aramis;

import com.github.aramis.Tag.TagType;

public class Token {
	public String charseq;
	public TagType parent;
	
	public Token( String charseq, TagType parent) {
		this.charseq = charseq;
		this.parent = parent;
	}
}