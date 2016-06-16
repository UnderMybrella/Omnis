package org.abimon.omnis.reflect.schematics;

import java.lang.reflect.Constructor;

public interface ObjectSchematic {
	public boolean doesSchematicApply(Class<?> clazz);
	public boolean doesConstructorApply(Constructor<?> constructor);
	public boolean doesParameterApply(String paramName);
	
	public int getParameterIndex(String parameter);
}
