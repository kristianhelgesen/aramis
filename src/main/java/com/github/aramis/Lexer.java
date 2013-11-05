package com.github.aramis;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.Tag.TagType;

public class Lexer {
	private static final Logger logger = LoggerFactory.getLogger(Lexer.class);


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
	}
	
	private void clear() {
		buf = new StringBuffer();
		matchStr = "";
		currentTagType = null;
		searchTokens = new HashSet<Token>(fromTextTokens);
	}

	private void process(String s) {
		clear();
		buf = new StringBuffer();
		
		for( char ch : s.toCharArray()) {
			try {
				process(ch);
			} catch (Exception e) {
				logState(ch);
				logger.error( "Error parsing template", e);
				break;
			}
		}
		
		wrapItUp();
	}
	
	private void process(char ch) {

		Set<Token> tokenMatches = findMatchingTokens(ch);

		// no matching tokens
		if( tokenMatches.size()==0 && match==null && matchStr.length()==0) {
			buf.append( ch);
			return;
		}

		
		// no matching tokens, but matchStr contains some data that must be processed
		if( tokenMatches.size()==0 && match==null && matchStr.length()>0) {
			reprocessMatchStr(ch);
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

	
	private void wrapItUp() {
		process(' '); // processing one last whitespace to flush tokens at the very end
		buf.deleteCharAt( buf.length()-1); // and removes the same space from buffer.
		processText( buf.toString()); // and finally sends the remaining text to processing
	}


	private void reprocessMatchStr(Character ch) {
		String unmatched = matchStr + ch;
		
		buf.append( unmatched.charAt(0));
		matchStr = "";
		
		for( char ch2 : unmatched.substring(1).toCharArray()) {
			process( ch2);
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
	
	
	private void logState(char ch) {
		logger.debug("---------------------------------------------------------------");
		logger.debug("ch: "+ch);
		logger.debug("buf: "+buf);
		logger.debug("match: "+match);
		logger.debug("matchStr: "+matchStr);
		logger.debug("searchTokens: "+searchTokens);
		logger.debug("currentTagType: "+currentTagType);
	}
	
	
	public static void main(String[] args) {
		
		Lexer l = new Lexer();
		l.process( "Before [[[ in ]] after [{{asdf}}<<oooo>>");
	}
	
}
