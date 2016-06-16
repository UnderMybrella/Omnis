package org.abimon.omnis.reflect;

public class AmbiguousClass {
	
	String regex = ".*";
	Class<?>[] classes = null;
	
	public AmbiguousClass(){}
	
	public AmbiguousClass(String regex){
		this.regex = regex;
	}
	
	public AmbiguousClass(Class<?>... classes){
		this.classes = classes;
		this.regex = null;
	}
	
	public AmbiguousClass(String regex, Class<?>... classes){
		this.regex = regex;
		this.classes = classes;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(o instanceof Class){
			if(regex != null)
				return ((Class<?>) o).getName().matches(regex);
			else
				for(Class<?> clazz : classes)
					if(clazz == o)
						return true;
			return false;
		}
		if(o instanceof String){
			if(regex != null)
				return ((String) o).matches(regex);
			else
				return false;
		}
		if(o instanceof AmbiguousClass){
			AmbiguousClass ac = (AmbiguousClass) o;
			
			if(regex != null){
				if(ac.regex != null)
					return ac.regex.equals(regex);
				else{
					for(Class<?> clazz : ac.classes)
						if(clazz.getName().matches(regex))
							return true;
					return false;
				}
			}
			else{
				if(ac.regex != null){
					for(Class<?> clazz : classes)
						if(clazz.getName().matches(ac.regex))
							return true;
					return false;
				}
				else{
					for(Class<?> ourClass : classes)
						for(Class<?> theirClass : ac.classes)
							if(theirClass == ourClass)
								return true;
					return false;
				}
			}
		}
		return false;
	}
}
