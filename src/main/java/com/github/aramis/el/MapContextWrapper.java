package com.github.aramis.el;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.github.aramis.Context;

public class MapContextWrapper implements Map<String,Object> {

	Context aramisContext;
	public MapContextWrapper( Context aramisContext) {
		this.aramisContext = aramisContext;
	}
	
	public void clear() {
	}

	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean containsKey(Object key) {
		return get(key)!=null;
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}


	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

}
