package moly;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;


@BuildParseTree 
class MoParser extends BaseParser<Object> {
	
	public static void main(String[] args) {
		try{
			
			String input = "asdfasdf{{var}}jkljkljkl";
			
			MoParser parser = Parboiled.createParser( MoParser.class);
			
			ParsingResult<?> result = new ReportingParseRunner<MoParser>( parser.Template()).run( input);
			
			String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
			
			System.out.println(parseTreePrintOut);		        
		}
		catch( Exception e) {
			e.printStackTrace();
		}
	}
	
	

    Rule Template() {
        return Sequence(
        		OneOrMore( TemplatePart()),
        		EOI
        );
    }
    
    Rule TemplatePart() {
    	return FirstOf(	
    				Expression(),
    				Render(),
    				Markup()
    	);
    }

    Rule Expression() {
        return Sequence(
        		"{{",Variable(),"}}"
        );
    }
    
    Rule Render() {
    	return Sequence(
        		"[[",Variable(),"]]"
        );
    }

    Rule Number() {
        return OneOrMore(CharRange('0', '9'));
    }
    
    Rule Variable() {
    	return OneOrMore( CharRange('a','z'));
    }
    
    Rule Markup() {
    	return OneOrMore( CharRange('a','z'));
    }
}
