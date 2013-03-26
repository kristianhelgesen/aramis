package moly;

import java.util.Stack;

import moly.renderinstruction.DecoratorRenderInstruction;
import moly.renderinstruction.MvelRenderInstruction;
import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.RenderInstructionContainer;
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
		addRenderInstruction( new SectionRenderInstruction( section));
	}

	@Override
	public void handleInvertedSectionStart(String string) {
	}

	@Override
	public void handleSectionEnd(String string) {
	}

	public Template getTemplate() {
		return template;
	}

	
	@Override
	public void handleUseDecorator(String decoratorTemplateName) {
		if( template.getRenderInsturctions().size()>0) {
			throw new ParserException("tag <<<"+decoratorTemplateName+">>> must be first in template");
		}
		decorationReceivingTemplate = new DecorationReceivingTemplate( template.name);
		template = templateFactory.getTemplate( decoratorTemplateName+".moly");
		for( RenderInstruction ri:template.getRenderInsturctions()) {
			if( ri instanceof DecoratorRenderInstruction) {
				((DecoratorRenderInstruction)ri).setDecorationReceivingTemplate( decorationReceivingTemplate);
			}
		}
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
		if( decorationReceivingTemplate == null) {
			template.addRenderInstruction( ri);
			return;
		}
		
		if( decoratorSectionName != null) {
			decorationReceivingTemplate.addRenderInstruction( ri, decoratorSectionName);
			return;
		}
	}


	@Override
	public void handleDecoratorApplySection(String decorateSectionName) {
		template.addRenderInstruction( new DecoratorRenderInstruction( decorateSectionName));
	}
	
	

}
