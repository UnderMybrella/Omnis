package org.abimon.omnis.reflect;

public class ClassWrapper<T> {

	T obj;
	
	public ClassWrapper(T obj){
		this.obj = obj;
	}
	
	public boolean equals(Object o){
		if(o instanceof ClassWrapper){
			ClassWrapper<?> other = (ClassWrapper<?>) o;
			if(obj == null)
				return other.obj == null;
			return obj.equals(other);
		}
		else if(o == null)
			return obj == null;
		else if(obj == null)
			return o == null;
		else if(obj.equals(o))
			return true;
		else if(obj instanceof Class && o instanceof Class)
			return obj == o || ((Class<?>) obj).isAssignableFrom((Class<?>) o) || ((Class<?>) o).isAssignableFrom((Class<?>) obj);
		else if(obj instanceof Class)
			return ((Class<?>) obj).isInstance(o);
		else if(o instanceof Class)
			return ((Class<?>) o).isInstance(obj);
		return false;
	}
}
