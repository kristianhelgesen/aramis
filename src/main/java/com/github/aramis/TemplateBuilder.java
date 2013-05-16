package com.github.aramis;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.renderinstruction.DecoratorRenderInstruction;
import com.github.aramis.renderinstruction.MvelRenderInstruction;
import com.github.aramis.renderinstruction.RenderInstruction;
import com.github.aramis.renderinstruction.RenderInstructionContainer;
import com.github.aramis.renderinstruction.SectionInvRenderInstruction;
import com.github.aramis.renderinstruction.SectionRenderInstruction;
import com.github.aramis.renderinstruction.TemplateRenderInstruction;
import com.github.aramis.renderinstruction.TextRenderInstruction;


public class TemplateBuilder implements ParserCallback{
	private static final Logger logger = LoggerFactory.getLogger( TemplateBuilder.class);

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
		decoratorTemplateName = decoratorTemplateName.trim();
		decorationReceivingTemplate = new DecorationReceivingTemplate( template.getName());
		template = templateFactory.getTemplate( decoratorTemplateName);
		if( template==null){
			logger.error("Template not found " +decoratorTemplateName+ " ");
			return;
		}
		
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
