package org.abimon.omnis.util;

import java.lang.reflect.Array;

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
}
