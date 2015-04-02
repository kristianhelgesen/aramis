Aramis
========

Aramis is a content centric templating engine, made for generating markup from any Content Management System. It's evolved from using and writing several less useful presentation frameworks over more than a decade.

The CMS typically provides content with different types, based on an ID. You only need to write a glue class to link your CMS to this framework.

```java
public class MyContentProvider extends com.github.aramis.ContentProvider{
	public Object getContent(Object contentref){
		// find and return your content
	}
}
```

Aramis follows the MVC pattern. The model is provided from your CMS, controllers are pojos augmenting each content type, and the view uses an extension to the Mustache template language, which also has the concept of presenting content in different perspectives. Content rendering uses the tags [[ with a reference to the content, and an optional perspective name. Each content type has a template for each perspective. The main perspective is called the same as the model, and is named the same as the class - in lowercase with .art suffix.


myContentType.art
```
<h1>{{title}}</h1>

<ul class="related">
	{{#related}}
		[[ ID | listPerspective ]]
	{{/related}}
</ul>
```


myContentType-listPerspective.art
```
<li><a href="{{URL}}">{{name}}</a></li>
```

Given that the related items has the same content type, this will render as a title, and a list of links. Other content types can also be listed, and will be presented with their listPerspective template. If the template for a given perspective doesn't exist, the content simply isn't rendered.


The Aramis template language has three different flavors (or mustaches).


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

You can also include parameters to the sub context:
```
[[ content-ID | list | index:1, caption:'lorem ipsum...' ]]
```



{{mustache formatting}}
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



&lt;&lt;decorator>>
-------
Decorator layout pattern. 

Create a decorator with your layout markup, and define one or more "holes" where your dynamic content will be inserted.

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




Project setup
==============

Add the aramis dependency to you pom

```xml
		<dependency>
		    <groupId>com.github.kristianhelgesen.aramis</groupId>
		    <artifactId>aramis</artifactId>
		    <version>1.1</version>
		</dependency>			
```



Run:

```java
ContentProvider contentProvider = new MyContentProvider();
RenderEngine renderEngine = new RenderEngine( contentProvider);

String articleReference = "article123";
renderEngine.render( System.out, articleReference);
```


The template to render the content must be in the same package as the content object, and be named the same as the class - in lowercase with .art suffix.

To add a template for rendering the content in a different perspective, add a dash, and the perspective name to the template name (before the suffix).
 
Add "Controller" to the model class name, and it is directly available in the template. 

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


To augment a property in the controller, simply define the same property in the controller. The controller getters have presedence over the model property getters.
```java
public ArticleController( Article a) {
	public String getMyModelProperty(){
		return a.getMyModelProperty().toLowerCase();
	}
  ...
}
```






