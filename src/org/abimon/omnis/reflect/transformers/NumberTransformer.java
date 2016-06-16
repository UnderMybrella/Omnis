package org.abimon.omnis.reflect.transformers;

import org.abimon.omnis.reflect.ReflectionHelper;

public class NumberTransformer implements ObjectTransformer {

	@Override
	public boolean isObjectSupported(Class<?> from, Class<?> to) {
		return ReflectionHelper.isRelative(from, Number.class) && ReflectionHelper.isRelative(to, Number.class);
	}

	@Override
	public <T> T transform(Class<T> transformTo, Object transforming) {
		if(transforming instanceof Number){
			Number num = (Number) transforming;
			
			Object obj = null;
			
			if(transformTo == Long.class)
				obj = num.longValue();
			if(transformTo == Integer.class)
				obj = num.intValue();
			if(transformTo == Short.class)
				obj = num.shortValue();
			if(transformTo == Byte.class)
				obj = num.byteValue();
			
			if(obj == null)
				return null;
			return transformTo.cast(obj);
		}
		return null;
	}

}
