package org.abimon.omnis.reflect;

import java.lang.reflect.Field;

public class ReflectionHelper {
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromVariable(Object mainClass, String fieldName, T returnClass){
		try{
			Field field = mainClass.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) (field.get(mainClass));
		}
		catch(Throwable th){
		}
		
		return null;
	}
	
	public static void setObjectFromVariable(Object mainClass, String fieldName, Object value){
		try{
			Field field = mainClass.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(mainClass, value);
		}
		catch(Throwable th){
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromVariable(Class<?> clazz, Object main, String fieldName, T returnClass){
		try{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) (field.get(main));
		}
		catch(Throwable th){}
		
		return null;
	}
	
	public static void setObjectFromVariable(Class<?> clazz, Object main, String fieldName, Object value){
		try{
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(clazz.cast(main), value);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}
	
	public static <T> T runMethod(Object mainClass, String methodName, T returnClass, Object... params){
		Class<? extends Object>[] classes = new Class<?>[params.length];
		for(int i = 0; i < classes.length; i++)
			if(params[i] != null)
				classes[i] = params[i].getClass();
		
		try{
			
		}
		catch(Throwable th){}
		
		return null;
	}
}
