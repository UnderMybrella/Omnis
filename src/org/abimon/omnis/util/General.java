package org.abimon.omnis.util;

import java.lang.reflect.Method;

public class General {

	@SuppressWarnings("unchecked")
	public static <T extends Cloneable> T clone(T cloning){
		try{
			Method cloneMethod = cloning.getClass().getMethod("clone");
			return (T) cloneMethod.invoke(cloning);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unsafeClone(T cloning){
		if(cloning instanceof Cloneable){
			try{
				Method cloneMethod = cloning.getClass().getMethod("clone");
				return (T) cloneMethod.invoke(cloning);
			}
			catch(Throwable th){
				th.printStackTrace();
			}
		}
		return null;
	}
}
