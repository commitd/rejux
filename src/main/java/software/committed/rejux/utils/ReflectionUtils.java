package software.committed.rejux.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

	private ReflectionUtils() {
		// Singleton
	}

	public static <T> List<Method> findParameterlessMethodsWithReturnType(Object object, Class<T> clazz) {

		Method[] methods = object.getClass().getMethods();
		List<Method> matching = new ArrayList<>(methods.length);

		for (Method m : methods) {
			if (clazz.isAssignableFrom(m.getReturnType()) && m.getParameterCount() == 0) {
				matching.add(m);
			}
		}

		return matching;
	}

	// Suppressed as this is checked
	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllReturnedOfType(Object object, Class<T> clazz) {

		List<Method> methods = findParameterlessMethodsWithReturnType(object, clazz);

		List<T> list = new ArrayList<>(methods.size());

		for (Method m : methods) {
			if (m.getParameterCount() == 0) {
				try {
					Object returned = m.invoke(object);
					if (returned != null && clazz.isInstance(returned)) {
						list.add((T) returned);
					}
				} catch (Exception e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public static List<Method> findMethods(Object object, Class<?> returnClass,
			Class<?>... params) {

		Method[] methods = object.getClass().getMethods();
		List<Method> matching = new ArrayList<>(methods.length);

		for (Method m : methods) {
			if (returnClass.isAssignableFrom(m.getReturnType()) && m.getParameterCount() == params.length) {
				Parameter[] parameters = m.getParameters();
				boolean matched = true;
				for (int i = 0; i < params.length; i++) {
					if (!params[i].isAssignableFrom(parameters[i].getType())) {
						matched = false;
						break;
					}
				}

				if (matched) {
					matching.add(m);
				}
			}
		}

		return matching;
	}

	// This is checked
	@SuppressWarnings("unchecked")
	public static <T> T callWithDefaultReturn(Object object, Class<T> returnClazz, T defaultReturnOnNull, Method method,
			Object... params) {
		try {
			Object r = method.invoke(object, params);
			if (r != null && returnClazz.isInstance(r)) {
				return (T) r;
			} else {
				return defaultReturnOnNull;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return defaultReturnOnNull;
		}
	}

}
