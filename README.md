Aramis
========

Aramis is a template engine that extends the Mustache templating language. It is written in Java, and has few dependencies.

The template language has three different flavors (or mustaches).

{{mustache expression}}
------
Plain mustache templating.


[[render content]]
-------

Usage: 

To render content, provide the ID to the desired content. 
```
[[ id:contentId ]]
```

To render content in a different perspective, add the parameter 'perspective' 
```
[[ id:contentId, perspective:'list' ]]
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

For now, you'll have to clone this repo, and mvn install the artifact..

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

```
ContentProvider contentProvider = new MyContentProvider();
RenderEngine renderEngine = new RenderEngine( contentProvider);

renderEngine.render( System.out, "article123");
```


The template to render the content must be in the same package as the content object, and be named the same as the class - in lowercase with .art suffix.
To enable rendering in different perspectives, add the perspective name to the template file. 
Controllers is also supported. Add "Controller" to the model class name, and it is directly available in the template. 
```
Article.java
ArticleController.java
article.art
article-list.art
```





