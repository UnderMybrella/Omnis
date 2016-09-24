package org.abimon.omnis.reflect;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.abimon.omnis.reflect.schematics.ObjectSchematic;
import org.abimon.omnis.reflect.transformers.NumberTransformer;
import org.abimon.omnis.reflect.transformers.ObjectTransformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class ObjectFactory<F> {

	Class<F> factorySchematic;

	public static LinkedList<ObjectTransformer> transformers = new LinkedList<ObjectTransformer>();
	public static LinkedList<ObjectSchematic> schematics = new LinkedList<ObjectSchematic>();

	static{
		transformers.add(new NumberTransformer());
	}

	public static void registerTransformer(ObjectTransformer transformer){
		transformers.add(transformer);
	}

	public static void registerSchematic(ObjectSchematic schematic) {
		schematics.add(schematic);
	}

	public ObjectFactory(Class<F> factorySchematic){
		this.factorySchematic = factorySchematic;
	}

	public F createObject(JsonObject structure){
		return createObject(factorySchematic, structure);
	}

	public static Object createObjectBlind(JsonObject structure){
		return createObject(Object.class, structure);
	}

	@SuppressWarnings("unchecked")
	public static <T> T createObject(Class<T> returnType, JsonObject structure){
		try{
			Class<? extends T> objectClass = (Class<? extends T>) Class.forName(structure.get("class").getAsString());
			LinkedList<Object> params = new LinkedList<Object>();
			LinkedList<ClassWrapper<?>> classes = new LinkedList<ClassWrapper<?>>();

			if(structure.get("params").isJsonArray()){
				for(JsonElement param : structure.get("params").getAsJsonArray()){
					if(param.isJsonArray()){

					}
					else if(param.isJsonObject()){
						Object obj = createObjectBlind(param.getAsJsonObject());
						params.add(obj);
						classes.add(new ClassWrapper<Class<?>>(obj.getClass()));
					}
					else if(param.isJsonPrimitive()){
						JsonPrimitive prim = param.getAsJsonPrimitive();

						if(prim.isBoolean()){
							params.add(prim.getAsBoolean());
							classes.add(new ClassWrapper<Class<Boolean>>(Boolean.class));
						}
						else if(prim.isString()){
							params.add(prim.getAsString());
							classes.add(prim.getAsString().length() == 1 ? new ClassWrapper<AmbiguousClass>(new AmbiguousClass(String.class, Character.class)) : new ClassWrapper<Class<String>>(String.class));
						}
						else if(prim.isNumber()){
							params.add(prim.getAsNumber());
							classes.add(new ClassWrapper<AmbiguousClass>(new AmbiguousClass(Number.class, Integer.class, Long.class, Short.class, Byte.class, Float.class, Double.class, int.class)));
						}
					}
					else{
						params.add(null);
						classes.add(new ClassWrapper<AmbiguousClass>(new AmbiguousClass()));
					}
				}
			}
			else if(structure.get("params").isJsonObject()){
				ClassWrapper<?>[] classWrappers = new ClassWrapper[structure.get("params").getAsJsonObject().entrySet().size()];
				Object[] parameters = new Object[classWrappers.length];
				for(Entry<String, JsonElement> entry : structure.get("params").getAsJsonObject().entrySet()){
					JsonElement param = entry.getValue();
					if(param.isJsonObject()){
						if(param.getAsJsonObject().has("array")){
							
						}
						else{
							Object obj = createObjectBlind(param.getAsJsonObject());
							params.add(obj);
							classes.add(new ClassWrapper<Class<?>>(obj.getClass()));
						}
					}
					else if(param.isJsonPrimitive()){
						JsonPrimitive prim = param.getAsJsonPrimitive();

						if(prim.isBoolean()){
							params.add(prim.getAsBoolean());
							classes.add(new ClassWrapper<Class<Boolean>>(Boolean.class));
						}
						else if(prim.isString()){
							params.add(prim.getAsString());
							classes.add(prim.getAsString().length() == 1 ? new ClassWrapper<AmbiguousClass>(new AmbiguousClass(String.class, Character.class)) : new ClassWrapper<Class<String>>(String.class));
						}
						else if(prim.isNumber()){
							params.add(prim.getAsNumber());
							classes.add(new ClassWrapper<AmbiguousClass>(new AmbiguousClass(Number.class, Integer.class, Long.class, Short.class, Byte.class, Float.class, Double.class, int.class)));
						}
					}
					else{
						params.add(null);
						classes.add(new ClassWrapper<AmbiguousClass>(new AmbiguousClass()));
					}
				}
			}

			Constructor<? extends T> constructor = ReflectionHelper.getConstructor(objectClass, classes.toArray(new ClassWrapper[0]));
			Class<?>[] constParams = constructor.getParameterTypes();

			for(int i = 0; i < constructor.getParameterCount(); i++){
				Object obj = params.get(i);
				Class<?> conformTo = ReflectionHelper.box(constParams[i]);

				System.out.println(conformTo);

				if(!conformTo.isInstance(obj)){
					for(ObjectTransformer trans : transformers){
						if(trans.isObjectSupported(obj.getClass(), conformTo)){
							obj = trans.transform(conformTo, obj);
							System.out.println("Attempting transformation from " + obj.getClass() + " to " + conformTo);
						}
					}
				}

				params.set(i, obj);
			}

			return constructor.newInstance(params.toArray(new Object[0]));
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}
}
