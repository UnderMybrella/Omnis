package org.abimon.omnis.util;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.LinkedList;

public class ExtraArrays{
	
	@SuppressWarnings("unchecked")
	public static <T> T[][] deepCopyOf(T[][] array, Class<? extends T> clazz){
		int secondLength = 0;
		for(T[] layer : array)
			if(layer.length > secondLength)
				secondLength = layer.length;
		T[][] copy = (T[][]) Array.newInstance(clazz, array.length, secondLength);
		for(int i = 0; i < array.length; i++)
		{
			T[] copyArray = (T[]) Array.newInstance(clazz, secondLength);
			for(int j = 0; j < copyArray.length; j++)
			{
				T obj = null;
				if(array[i][j] instanceof Cloneable)
					obj = General.unsafeClone(array[i][j]);
				else if(array[i][j] instanceof String)
					obj = array[i][j];
				else if(clazz.isPrimitive())
					obj = array[i][j];
				copyArray[j] = obj;
			}
			copy[i] = copyArray;
		}
		return copy;
	}
	
	public static <T> T[] randomise(T[] array){
		LinkedList<T> list = new LinkedList<T>();
		
		while(list.size() < array.length){
			int index = new SecureRandom().nextInt(array.length);
			
			if(array[index] != null){
				list.add(array[index]);
				array[index] = null;
			}
		}
		
		return list.toArray(array);
	}
	
	public static <T> boolean contains(T[] array, T object){
		for(T t : array)
			if(t != null && t.equals(object))
				return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <X, Y> Y[] cast(X[] array, Class<Y> clazz){
		Y[] casted = (Y[]) Array.newInstance(clazz, array.length);
		
		for(int i = 0; i < array.length; i++)
			casted[i] = (Y) array[i];
		
		return casted;
	}
	
	public static boolean[] cast(Boolean[] array){
		boolean[] casted = new boolean[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
	
	public static short[] cast(Short[] array){
		short[] casted = new short[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
	
	public static Short[] cast(short[] array){
		Short[] casted = new Short[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
	
	public static char[] cast(Character[] array){
		char[] casted = new char[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
	
	public static byte[] cast(Byte[] array){
		byte[] casted = new byte[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
	
	public static Byte[] cast(byte[] array){
		Byte[] casted = new Byte[array.length];
		
		for(int i = 0; i < array.length; i++)
			casted[i] = array[i];
		
		return casted;
	}
}
