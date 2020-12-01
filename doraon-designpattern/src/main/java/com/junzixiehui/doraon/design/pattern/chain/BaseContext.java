package com.junzixiehui.doraon.design.pattern.chain;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/1  16:27
 * @version: 1.0
 */
public class BaseContext extends HashMap implements Context {
	private transient Map descriptors = null;
	private transient PropertyDescriptor[] pd = null;
	private static Object singleton = new Serializable() {
		public boolean equals(Object object) {
			return false;
		}
	};
	private static Object[] zeroParams = new Object[0];

	public BaseContext() {
		this.initialize();
	}

	public BaseContext(Map map) {
		super(map);
		this.initialize();
		this.putAll(map);
	}

	@Override
	public void clear() {
		if (this.descriptors == null) {
			super.clear();
		} else {
			Iterator keys = this.keySet().iterator();

			while(keys.hasNext()) {
				Object key = keys.next();
				if (!this.descriptors.containsKey(key)) {
					keys.remove();
				}
			}
		}

	}

	@Override
	public boolean containsValue(Object value) {
		if (this.descriptors == null) {
			return super.containsValue(value);
		} else if (super.containsValue(value)) {
			return true;
		} else {
			for(int i = 0; i < this.pd.length; ++i) {
				if (this.pd[i].getReadMethod() != null) {
					Object prop = this.readProperty(this.pd[i]);
					if (value == null) {
						if (prop == null) {
							return true;
						}
					} else if (value.equals(prop)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public Set entrySet() {
		return new BaseContext.EntrySetImpl();
	}

	public Object get(Object key) {
		if (this.descriptors == null) {
			return super.get(key);
		} else {
			if (key != null) {
				PropertyDescriptor descriptor = (PropertyDescriptor)this.descriptors.get(key);
				if (descriptor != null) {
					if (descriptor.getReadMethod() != null) {
						return this.readProperty(descriptor);
					}

					return null;
				}
			}

			return super.get(key);
		}
	}

	public boolean isEmpty() {
		if (this.descriptors == null) {
			return super.isEmpty();
		} else {
			return super.size() <= this.descriptors.size();
		}
	}

	public Set keySet() {
		return super.keySet();
	}

	public Object put(Object key, Object value) {
		if (this.descriptors == null) {
			return super.put(key, value);
		} else {
			if (key != null) {
				PropertyDescriptor descriptor = (PropertyDescriptor)this.descriptors.get(key);
				if (descriptor != null) {
					Object previous = null;
					if (descriptor.getReadMethod() != null) {
						previous = this.readProperty(descriptor);
					}

					this.writeProperty(descriptor, value);
					return previous;
				}
			}

			return super.put(key, value);
		}
	}

	public void putAll(Map map) {
		Iterator pairs = map.entrySet().iterator();

		while(pairs.hasNext()) {
			Map.Entry pair = (Map.Entry)pairs.next();
			this.put(pair.getKey(), pair.getValue());
		}

	}

	public Object remove(Object key) {
		if (this.descriptors == null) {
			return super.remove(key);
		} else {
			if (key != null) {
				PropertyDescriptor descriptor = (PropertyDescriptor)this.descriptors.get(key);
				if (descriptor != null) {
					throw new UnsupportedOperationException("Local property '" + key + "' cannot be removed");
				}
			}

			return super.remove(key);
		}
	}

	public Collection values() {
		return new BaseContext.ValuesImpl();
	}

	private Iterator entriesIterator() {
		return new BaseContext.EntrySetIterator();
	}

	private Map.Entry entry(Object key) {
		return this.containsKey(key) ? new BaseContext.MapEntryImpl(key, this.get(key)) : null;
	}

	private void initialize() {
		try {
			this.pd = Introspector.getBeanInfo(this.getClass()).getPropertyDescriptors();
		} catch (IntrospectionException var3) {
			this.pd = new PropertyDescriptor[0];
		}

		for(int i = 0; i < this.pd.length; ++i) {
			String name = this.pd[i].getName();
			if (!"class".equals(name) && !"empty".equals(name)) {
				if (this.descriptors == null) {
					this.descriptors = new HashMap(this.pd.length - 2);
				}

				this.descriptors.put(name, this.pd[i]);
				super.put(name, singleton);
			}
		}

	}

	private Object readProperty(PropertyDescriptor descriptor) {
		try {
			Method method = descriptor.getReadMethod();
			if (method == null) {
				throw new UnsupportedOperationException("Property '" + descriptor.getName() + "' is not readable");
			} else {
				return method.invoke(this, zeroParams);
			}
		} catch (Exception var3) {
			throw new UnsupportedOperationException("Exception reading property '" + descriptor.getName() + "': " + var3.getMessage());
		}
	}

	private boolean remove(Map.Entry entry) {
		Map.Entry actual = this.entry(entry.getKey());
		if (actual == null) {
			return false;
		} else if (!entry.equals(actual)) {
			return false;
		} else {
			this.remove(entry.getKey());
			return true;
		}
	}

	private Iterator valuesIterator() {
		return new BaseContext.ValuesIterator();
	}

	private void writeProperty(PropertyDescriptor descriptor, Object value) {
		try {
			Method method = descriptor.getWriteMethod();
			if (method == null) {
				throw new UnsupportedOperationException("Property '" + descriptor.getName() + "' is not writeable");
			} else {
				method.invoke(this, value);
			}
		} catch (Exception var4) {
			throw new UnsupportedOperationException("Exception writing property '" + descriptor.getName() + "': " + var4.getMessage());
		}
	}

	private class ValuesIterator implements Iterator {
		private Map.Entry entry;
		private Iterator keys;

		private ValuesIterator() {
			this.entry = null;
			this.keys = BaseContext.this.keySet().iterator();
		}

		public boolean hasNext() {
			return this.keys.hasNext();
		}

		public Object next() {
			this.entry = BaseContext.this.entry(this.keys.next());
			return this.entry.getValue();
		}

		public void remove() {
			BaseContext.this.remove(this.entry);
		}
	}

	private class ValuesImpl extends AbstractCollection {
		private ValuesImpl() {
		}

		public void clear() {
			BaseContext.this.clear();
		}

		public boolean contains(Object obj) {
			if (!(obj instanceof Map.Entry)) {
				return false;
			} else {
				Map.Entry entry = (Map.Entry)obj;
				return BaseContext.this.containsValue(entry.getValue());
			}
		}

		public boolean isEmpty() {
			return BaseContext.this.isEmpty();
		}

		public Iterator iterator() {
			return BaseContext.this.valuesIterator();
		}

		public boolean remove(Object obj) {
			return obj instanceof Map.Entry ? BaseContext.this.remove((Map.Entry)obj) : false;
		}

		public int size() {
			return BaseContext.this.size();
		}
	}

	private class MapEntryImpl implements Map.Entry {
		private Object key;
		private Object value;

		MapEntryImpl(Object key, Object value) {
			this.key = key;
			this.value = value;
		}

		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			} else if (!(obj instanceof Map.Entry)) {
				return false;
			} else {
				Map.Entry entry = (Map.Entry)obj;
				if (this.key == null) {
					return entry.getKey() == null;
				} else if (this.key.equals(entry.getKey())) {
					if (this.value == null) {
						return entry.getValue() == null;
					} else {
						return this.value.equals(entry.getValue());
					}
				} else {
					return false;
				}
			}
		}

		public Object getKey() {
			return this.key;
		}

		public Object getValue() {
			return this.value;
		}

		public int hashCode() {
			return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
		}

		public Object setValue(Object value) {
			Object previous = this.value;
			BaseContext.this.put(this.key, value);
			this.value = value;
			return previous;
		}

		public String toString() {
			return this.getKey() + "=" + this.getValue();
		}
	}

	private class EntrySetIterator implements Iterator {
		private Map.Entry entry;
		private Iterator keys;

		private EntrySetIterator() {
			this.entry = null;
			this.keys = BaseContext.this.keySet().iterator();
		}

		public boolean hasNext() {
			return this.keys.hasNext();
		}

		public Object next() {
			this.entry = BaseContext.this.entry(this.keys.next());
			return this.entry;
		}

		public void remove() {
			BaseContext.this.remove(this.entry);
		}
	}

	private class EntrySetImpl extends AbstractSet {
		private EntrySetImpl() {
		}

		public void clear() {
			BaseContext.this.clear();
		}

		public boolean contains(Object obj) {
			if (!(obj instanceof Map.Entry)) {
				return false;
			} else {
				Map.Entry entry = (Map.Entry)obj;
				Object actual = BaseContext.this.entry(entry.getKey());
				return actual != null ? actual.equals(entry) : false;
			}
		}

		public boolean isEmpty() {
			return BaseContext.this.isEmpty();
		}

		public Iterator iterator() {
			return BaseContext.this.entriesIterator();
		}

		public boolean remove(Object obj) {
			return obj instanceof Map.Entry ? BaseContext.this.remove((Map.Entry)obj) : false;
		}

		public int size() {
			return BaseContext.this.size();
		}
	}
}
