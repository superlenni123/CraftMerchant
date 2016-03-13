package de.jens.Merchants;

import java.lang.reflect.Field;

public class IReflection {
	
	public static void set(Object instance, String name, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(name);
		
		field.setAccessible(true);
		
		field.set(instance, value);
	}

}
