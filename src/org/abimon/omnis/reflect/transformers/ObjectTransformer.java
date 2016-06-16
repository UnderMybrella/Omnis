package org.abimon.omnis.reflect.transformers;

public interface ObjectTransformer {

	public boolean isObjectSupported(Class<?> from, Class<?> to);
	
	public <T> T transform(Class<T> transformTo, Object transforming);
}
