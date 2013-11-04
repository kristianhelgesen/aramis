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
				debugPrint(ch);
				e.printStackTrace();
				break;
			}
		}
		process(' '); // processing one last whitespace to flush tokens at the very end
		
		debugPrint(' ');
		
//		processText( buf.toString()+matchStr);
	}

	private void debugPrint(char ch) {
		System.out.println("---------------------------------------------------------------");
		System.out.println("ch: "+ch);
		System.out.println("buf: "+buf);
		System.out.println("match: "+match);
		System.out.println("matchStr: "+matchStr);
		System.out.println("searchTokens: "+searchTokens);
		System.out.println("currentTagType: "+currentTagType);
	}
	
	
	private void process(char ch) {

		Set<Token> tokenMatches = findMatchingTokens(ch);
//		debugPrint(ch);
//		System.out.println(tokenMatches);

		// no matching tokens
		if( tokenMatches.size()==0 && match==null && matchStr.length()==0) {
			buf.append( ch);
			return;
		}

		
		// no matching tokens, but matchStr contains some data that must be processed
		if( tokenMatches.size()==0 && match==null && matchStr.length()>0) {
			String unmatched = matchStr + ch;
			buf.append( unmatched.charAt(0));
			matchStr = "";
			
			for( char ch2 : unmatched.substring(1).toCharArray()) {
				process( ch2);
			}
			return;
		}
		
		
		// one or more matching tokens
		if( tokenMatches.size()>0) {
			matchStr += ch;
			return;
		}

		// token found
		if( tokenMatches.size()==0 && match!=null) {
			
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
		if( text.length()==0) return;
		System.out.println("TEXT: " + text+"|");
	}
	
	private void processTag(Tag t) {
		switch( t.tagdef){
		case RENDER:
			break;
		case DECORATOR:
			break;
		}
		System.out.println("TAG:  " + t+"|");
	}
	
	public static void main(String[] args) {
		
		String s = "Before [[[ in ]] after [{{asdf}}<<oooo>>";
		
		Lexer l = new Lexer();
		
		l.process( s);
	}
	
}
