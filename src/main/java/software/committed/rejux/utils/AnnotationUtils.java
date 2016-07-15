package software.committed.rejux.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class AnnotationUtils {

	private AnnotationUtils() {
		// Singleton
	}

	public static boolean isAnnotationPresentInHierarchy(Method method, Class<? extends Annotation> annotation) {
		return getAnnotationFromHierarchy(method, annotation) != null;
	}

	public static <T extends Annotation> T getAnnotationFromHierarchy(Method method, Class<T> annotation) {

		if (method.isAnnotationPresent(annotation)) {
			return method.getAnnotation(annotation);
		}

		// Look to super class and other interfaces
		Class<?> superClass = method.getDeclaringClass().getSuperclass();
		Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();

		if (superClass != null) {
			T a = getAnnotation(superClass, method.getName(), method.getParameterTypes(), annotation);
			if (a != null) {
				return a;
			}
		}

		for (Class<?> clazz : interfaces) {
			T a = getAnnotation(clazz, method.getName(), method.getParameterTypes(), annotation);
			if (a != null) {
				return a;
			}
		}

		return null;
	}

	public static <T extends Annotation> T getAnnotation(Class<?> clazz, String methodName, Class<?>[] parameterTypes,
			Class<T> annotation) {
		if (clazz == null) {
			return null;
		}
		try {
			Method method = clazz.getMethod(methodName, parameterTypes);
			return getAnnotationFromHierarchy(method, annotation);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return null;
		}
	}

}
