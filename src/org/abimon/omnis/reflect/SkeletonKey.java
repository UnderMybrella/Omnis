package org.abimon.omnis.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SkeletonKey<L> {
	
	L lock;
	Class<?> clazz;

	public SkeletonKey(Class<L> clazz){
		this.clazz = clazz;
	}
	
	public SkeletonKey(L lock){
		
		clazz = lock.getClass();
		
		for(Field f : clazz.getDeclaredFields())
			f.setAccessible(true);
		
		for(Method m : clazz.getDeclaredMethods())
			m.setAccessible(true);
		
		this.lock = lock;
	}
	
	public Object get(String fieldName){
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(lock);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
