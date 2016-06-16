package org.abimon.omnis.reflect.schematics;

import java.lang.reflect.Constructor;

public class JsonSchematic implements ObjectSchematic {

	@Override
	public boolean doesSchematicApply(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesConstructorApply(Constructor<?> constructor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesParameterApply(String paramName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getParameterIndex(String parameter) {
		// TODO Auto-generated method stub
		return 0;
	}

}
