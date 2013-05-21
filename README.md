Aramis
========

Aramis is a template engine that extends the Mustache templating language. It is written in Java, and has few dependencies.

The template language has three different flavors (or mustaches).

{{mustache expression}}
------
Plain mustache templating.


[[render content]]
-------
Renders content from any content provider. The content provider is pluggable, and you need to implement your own.

```java
public class MyContentProvider extends ContentProvider{
	public Object getContent(Object contentref){
		// find and return your content
	}
}
```

The template to render the content must be in the same package as the content object, and be named the same as the class - in lowercase with .art suffix.
In the calling template, just provide the id to the content to be rendered:

```
[[ id:contentID ]]
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
=========

For now, you'll have to clone this repo, and mvn install the artifact..

