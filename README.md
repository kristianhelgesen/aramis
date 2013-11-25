Aramis
========

Aramis is a template engine that extends the Mustache templating language. It is written in Java, and has few dependencies.

The template language has three different flavors (or mustaches).

{{mustache expression}}
------
Plain mustache templating. An excelent description here: http://mustache.github.io/mustache.5.html

In brief:

| Expression | Description |
|-------|--------|
| {{expression}} | evaluates and prints the result of the expression. HTML escaped. |
| {{{expression}}} | evaluates and prints the result of the expression. Not HTML escaped. |
| {{#section}} | Starts a section if variable doesn`t evaluate to false or an empty list. A section also loops over the elements if the expression is a list |
| {{/section}} | Ends a section |
| {{^section}} | Inverted section. Renders if expression is false or an empty list |
| {{!comment}} | Comment. Not rendered |
| {{>partial}} | Partial. Includes another template |


[[render content]]
-------

Usage: 

To render content, provide the ID to the desired content. 
```
[[ content-ID ]]
```

To render content in a different perspective, add the name of the perspective, separated with a pipe |
```
[[ content-ID | list ]]
```

You can also include parameters when rendering another content:
```
[[ content-ID | list | index:1, caption:'lorem ipsum...' ]]
```



&lt;&lt;decorator>>
-------
Support for the decorator layout pattern. 

Create a decorator with all your markup, and define one or more "holes" where your dynamic content will be inserted.

```
<html>
<body>
<<contentsection>>
</body>
</html>

```

In your template, define the decorator at the top, and the content of the sections
```
<<<decorator name>>>

<<#contentsection>>
<div>content of section</div>
<</contentsection>>
```




Installation
==============

Add the aramis dependency to you pom

```xml
		<dependency>
		    <groupId>com.github.kristianhelgesen.aramis</groupId>
		    <artifactId>aramis</artifactId>
		    <version>1.0</version>
		</dependency>			
```

Setup:

The content provider is pluggable, and you need to implement your own.

```java
public class MyContentProvider extends ContentProvider{
	public Object getContent(Object contentref){
		// find and return your content
	}
}
```

Run:

```java
ContentProvider contentProvider = new MyContentProvider();
RenderEngine renderEngine = new RenderEngine( contentProvider);

String articleReference = "article123";
renderEngine.render( System.out, articleReference);
```


The template to render the content must be in the same package as the content object, and be named the same as the class - in lowercase with .art suffix.

To add a template for rendering the content in a different perspective, add a template that ends with the perspective name.
 
Controllers are also supported. Add "Controller" to the model class name, and it is directly available in the template. 

```
Article.java
ArticleController.java
article.art
article-list.art
```



To get the model object injected into the controller, simply add the model to the controllers constructor.
```java
public ArticleController( Article a) {
  ...
}
```

To inject both the model and the reference to the controller, add both the model and the reference to the constructor.
```java
public ArticleController( Article a, ArticleReference ref) {
  ...
}
```




