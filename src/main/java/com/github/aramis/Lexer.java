package com.github.aramis;

import java.util.HashSet;
import java.util.Set;

import com.github.aramis.Tag.TagType;

public class Lexer {

	StringBuffer buf;
	Token match;
	String matchStr;
	Set<Token> fromTextTokens;
	Set<Token> searchTokens;
	TagType currentTagType;

	
	public Lexer() {
		fromTextTokens = new HashSet<Token>();
		for( TagType tagType : TagType.values()){
			fromTextTokens.add( tagType.startToken);
		}
		clear();
	}
	
	private void clear() {
		buf = new StringBuffer();
		matchStr = "";
		currentTagType = null;
		searchTokens = new HashSet<Token>(fromTextTokens);
	}

	private void process(String s) {
		buf = new StringBuffer();
		
		for( char ch : s.toCharArray()) {
			try {
				process(ch);
			} catch (Exception e) {
				System.out.println("ch: "+ch);
				System.out.println("buf: "+buf);
				System.out.println("match: "+match);
				System.out.println("matchStr: "+matchStr);
				System.out.println("searchTokens: "+searchTokens);
				System.out.println("currentTagType: "+currentTagType);
				e.printStackTrace();
				break;
			}
		}
		
		processText( buf.toString()+matchStr);
	}
	
	
	private void process(char ch) {

		Set<Token> tokenMatches = findMatchingTokens(ch);

		// no matching tokens
		if( tokenMatches.size()==0 && matchStr.length()==0) {
			buf.append(ch);
			return;
		}

		// one or more matching tokens
		if( tokenMatches.size()>0) {
			matchStr += ch;
			return;
		}

		// token found
		if( tokenMatches.size()==0 && matchStr.length()>0) {
			
			String text = buf.toString();
			if( currentTagType==null) {
				currentTagType = match.parent;
				processText( text);
				searchTokens = new HashSet<Token>();
				searchTokens.add(currentTagType.endToken);
			}
			else {
				Tag tag = new Tag( currentTagType, text);		
				processTag( tag);
				currentTagType = null;
				searchTokens = new HashSet<Token>();
				searchTokens = new HashSet<Token>(fromTextTokens);
			}

			String unprocessed = matchStr.substring( match.charseq.length()) + ch;
			match = null;
			matchStr = "";
			buf = new StringBuffer();

			// re-process text after token match
			for( Character ch2 : unprocessed.toCharArray()) {
				process(ch2);
			}
		}
		
	}

	
	private Set<Token> findMatchingTokens(char ch) {
		Set<Token> tokenMatch = new HashSet<Token>();
		for( Token nt : searchTokens) {
			if( nt.charseq.equals(matchStr+ch)){
				match = nt;
			}
			if( nt.charseq.startsWith( matchStr+ch)) {
				tokenMatch.add( nt);
			}
		}
		return tokenMatch;
	}
	
	private void processText(String text) {
		System.out.println("TEXT: " + text);
	}
	
	private void processTag(Tag t) {
		switch( t.tagdef){
		case RENDER:
			break;
		case DECORATOR:
			break;
		}
		System.out.println("TAG:  " + t);
	}
	
	public static void main(String[] args) {
		
		String s = "Before [[[ in ]] after [{{asdf}}";
		
		Lexer l = new Lexer();
		
		l.process( s);
	}
	
}
