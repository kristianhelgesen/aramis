package moly;

import java.util.Stack;

import moly.renderinstruction.DecoratorRenderInstruction;
import moly.renderinstruction.MvelRenderInstruction;
import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.RenderInstructionContainer;
import moly.renderinstruction.SectionInvRenderInstruction;
import moly.renderinstruction.SectionRenderInstruction;
import moly.renderinstruction.TemplateRenderInstruction;
import moly.renderinstruction.TextRenderInstruction;

public class TemplateBuilder implements ParserCallback{
	
	Template template;
	DecorationReceivingTemplate decorationReceivingTemplate;
	String decoratorSectionName;
	ContentProvider contentProvider;
	RenderEngine renderEngine;
	TemplateFactory templateFactory;
	Stack<RenderInstructionContainer> stack = new Stack<RenderInstructionContainer>(); 
	
	
	public TemplateBuilder( String templateName, RenderEngine renderEngine, ContentProvider contenProvider, TemplateFactory templateFactory) {
		this.template = new Template( templateName);
		this.renderEngine = renderEngine;
		this.contentProvider = contenProvider;
		this.templateFactory = templateFactory;
		stack.push( template);
	}

	
	@Override
	public void handleUnescapedVariable(String var) {
		addRenderInstruction( new MvelRenderInstruction( var, false));
	}

	@Override
	public void handleVariable(String var) {
		addRenderInstruction( new MvelRenderInstruction( var));
	}

	@Override
	public void handleText(String text) {
		if( text.length()>0){
			addRenderInstruction( new TextRenderInstruction( text));
		}
	}

	@Override
	public void handleRender(String render) {
		addRenderInstruction( new TemplateRenderInstruction(renderEngine,contentProvider,render));
	}

	@Override
	public void handleSectionStart(String section) {
		SectionRenderInstruction sri = new SectionRenderInstruction( section);
		addRenderInstruction( sri);
		stack.push( sri);
	}

	@Override
	public void handleInvertedSectionStart(String section) {
		SectionInvRenderInstruction siri = new SectionInvRenderInstruction( section);
		addRenderInstruction( siri);
		stack.push( siri);
	}

	@Override
	public void handleSectionEnd(String string) {
		stack.pop();
	}

	public Template getTemplate() {
		return template;
	}

	
	@Override
	public void handleUseDecorator(String decoratorTemplateName) {
		if( template.getRenderInsturctions().size()>0) {
			throw new ParserException("tag <<<"+decoratorTemplateName+">>> must be first in template");
		}
		decorationReceivingTemplate = new DecorationReceivingTemplate( template.getName());
		template = templateFactory.getTemplate( decoratorTemplateName+".moly");
		
		stack.clear();
		stack.push( decorationReceivingTemplate);
		
		for( RenderInstruction ri:template.getRenderInsturctions()) {
			if( ri instanceof DecoratorRenderInstruction) {
				((DecoratorRenderInstruction)ri).setDecorationReceivingTemplate( decorationReceivingTemplate);
			}
		}
	}

	
	@Override
	public void handleDecoratorApplySection(String decorateSectionName) {
		addRenderInstruction( new DecoratorRenderInstruction( decorateSectionName));
	}

	@Override
	public void handleDecoratorSectionStart(String decoratorSectionName) {
		this.decoratorSectionName = decoratorSectionName;
	}

	@Override
	public void handleDecoratorSectionEnd(String decoratorSectionName) {
		this.decoratorSectionName = null;
	}
	
	
	private void addRenderInstruction( RenderInstruction ri){
		stack.peek().addRenderInstruction( ri, decoratorSectionName);
	}
	

}
