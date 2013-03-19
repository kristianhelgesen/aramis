package moly;

import moly.renderinstruction.MvelRenderInstruction;
import moly.renderinstruction.TemplateRenderInstruction;
import moly.renderinstruction.TextRenderInstruction;

public class TemplateBuilder implements ParserCallback{
	
	Template template = new Template();
	ContentProvider contentProvider;
	RenderEngine renderEngine;
	
	public TemplateBuilder( RenderEngine renderEngine, ContentProvider contenProvider) {
		this.renderEngine = renderEngine;
		this.contentProvider = contenProvider;
	}
	

	@Override
	public void handleUnescapedVariable(String var) {
		template.addRenderInstruction( new MvelRenderInstruction(var, false));
	}

	@Override
	public void handleVariable(String var) {
		template.addRenderInstruction( new MvelRenderInstruction(var));
	}

	@Override
	public void handleText(String text) {
		if( text.length()>0){
			template.addRenderInstruction( new TextRenderInstruction( text));
		}
	}

	@Override
	public void handleRender(String render) {
		template.addRenderInstruction( new TemplateRenderInstruction(renderEngine,contentProvider,render));
	}

	@Override
	public void handleSectionStart(String string) {
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
	
	

}
