package org.abimon.omnis.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SkeletonKey<L> {
	
	L lock;

	public SkeletonKey(L lock){
		
		Class<?> clazz = lock.getClass();
		
		for(Field f : clazz.getDeclaredFields())
			f.setAccessible(true);
		

		for(Method m : clazz.getDeclaredMethods())
			m.setAccessible(true);
		
		this.lock = lock;
	}
	
	public Object get(String fieldName){
		try {
			return lock.getClass().getField(fieldName);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		}
		
		return null;
	}
}
