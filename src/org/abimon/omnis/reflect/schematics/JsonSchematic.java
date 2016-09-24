package org.abimon.omnis.reflect.schematics;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.abimon.omnis.reflect.ClassWrapper;
import org.abimon.omnis.reflect.ReflectionHelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonSchematic implements ObjectSchematic {

	Class<?> applicable;
	Constructor<?> constructor;
	HashMap<String, Integer> parameterIndexes = new HashMap<String, Integer>();
	
	public JsonSchematic(JsonObject json) throws ClassNotFoundException{
		applicable = Class.forName(json.get("class").getAsString());
		LinkedList<ClassWrapper<?>> classes = new LinkedList<ClassWrapper<?>>();
		for(JsonElement elem : json.getAsJsonArray("params")){
			Class<?> clazz = Class.forName(elem.getAsString());
			classes.add(new ClassWrapper<Class<?>>(clazz));
		}
		constructor = ReflectionHelper.getConstructor(applicable, classes.toArray(new ClassWrapper<?>[0]));
		for(Entry<String, JsonElement> mapping : json.getAsJsonObject("mapping").entrySet())
			parameterIndexes.put(mapping.getKey().toLowerCase(), mapping.getValue().getAsInt());
	}
	
	@Override
	public boolean doesSchematicApply(Class<?> clazz) {
		return applicable == clazz;
	}

	@Override
	public boolean doesConstructorApply(Constructor<?> constructor) {
		return this.constructor.equals(constructor);
	}

	@Override
	public boolean doesParameterApply(String paramName) {
		return parameterIndexes.containsKey(paramName.toLowerCase());
	}

	@Override
	public int getParameterIndex(String parameter) {
		return parameterIndexes.get(parameter.toLowerCase());
	}

}
