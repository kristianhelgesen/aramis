package com.github.aramis.el;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aramis.Context;

public class Evaluator {

	public static Object lookup(String expression, Context aramisContext){
        JexlEngine jexl = new JexlEngine();
		Expression e = jexl.createExpression(expression); 
		JexlContext jc = new DelegatingJexlContext(aramisContext, jexl);
        return e.evaluate(jc);
	}

	public static Object lookup(Expression expression, Context aramisContext){
        JexlEngine jexl = new JexlEngine();
		JexlContext jc = new DelegatingJexlContext(aramisContext, jexl);
        return expression.evaluate(jc);
	}
}
