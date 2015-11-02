package org.abimon.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.abimon.omnis.util.EnumObjects;

public class Function {
	
	Method func;
	Object obj;
	Object[] args = new Object[0];
	
	public Function(Object object, String methodName, Class<?>... params) throws NoSuchMethodException, SecurityException{
		func = object.getClass().getMethod(methodName, params);
		this.obj = object;
	}
	
	public Function(Class<?> clazz, String methodName, Class<?>... params) throws NoSuchMethodException, SecurityException{
		func = clazz.getMethod(methodName, params);
	}
	
	public Function(Object object, String methodName, Object... params) throws NoSuchMethodException, SecurityException{
		Class<?>[] classes = new Class<?>[params.length];
		for(int i = 0; i < params.length; i++)
			classes[i] = params[i].getClass();
		func = object.getClass().getMethod(methodName, classes);
		this.obj = object;
		this.args = params;
	}
	
	public void setParams(Object[] params){
		args = params;
	}
	
	public void setArguments(Object[] args){
		this.args = args;
	}
	
	public void setParamsPartially(Object[] params){
		if(args.length != params.length)
			args = Arrays.copyOf(args, params.length);
		for(int i = 0; i < args.length; i++)
			if(params[i] == null)
				continue;
			else if(params[i] instanceof EnumObjects && params[i] == EnumObjects.NULL)
				args[i] = null;
			else
				args[i] = params[i];
	}
	
	public void setArgumentsPartially(Object[] args){
		if(this.args.length != args.length)
			this.args = Arrays.copyOf(this.args, args.length);
		for(int i = 0; i < this.args.length; i++)
			if(args[i] == null)
				continue;
			else if(args[i] instanceof EnumObjects && args[i] == EnumObjects.NULL)
				this.args[i] = null;
			else
				this.args[i] = args[i];
	}
	
	public void invoke(Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		func.invoke(obj, params);
	}
	
	public void invokeUnsafe(Object... params){
		try{
			func.invoke(obj, params);
		}
		catch(Throwable th){}
	}
	
	public void invokePartially(Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object[] tmpArgs = new Object[Math.min(args.length, params.length)];
		for(int i = 0; i < tmpArgs.length; i++)
			if(params[i] == null)
				tmpArgs[i] = args[i];
			else if(params[i] instanceof EnumObjects && params[i] == EnumObjects.NULL)
				tmpArgs[i] = null;
			else
				tmpArgs[i] = params[i];
		func.invoke(obj, tmpArgs);
	}
	
	public void invokeUnsafePartially(Object... params){
		try{
			if(args == null || args.length == 0)
				args = new Object[params.length];
			Object[] tmpArgs = new Object[Math.min(args.length, params.length)];
			for(int i = 0; i < tmpArgs.length; i++)
				if(params[i] == null)
					tmpArgs[i] = args[i];
				else if(params[i] instanceof EnumObjects && params[i] == EnumObjects.NULL)
					tmpArgs[i] = null;
				else
					tmpArgs[i] = params[i];
			func.invoke(obj, tmpArgs);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}

	public static Function getUnsafe(Class<?> staticClass, String name, Class<?>... params) {
		try{
			return new Function(staticClass, name, params);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}
}
