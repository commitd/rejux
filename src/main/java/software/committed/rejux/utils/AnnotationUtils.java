package software.committed.rejux.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public final class AnnotationUtils {

  private AnnotationUtils() {
    // Singleton
  }

  public static boolean isAnnotationPresentInHierarchy(final Method method,
      final Class<? extends Annotation> annotation) {
    return AnnotationUtils.getAnnotationFromHierarchy(method, annotation) != null;
  }

  public static <T extends Annotation> T getAnnotationFromHierarchy(final Method method,
      final Class<T> annotation) {

    if (method.isAnnotationPresent(annotation)) {
      return method.getAnnotation(annotation);
    }

    // Look to super class and other interfaces
    final Class<?> superClass = method.getDeclaringClass().getSuperclass();
    final Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();

    if (superClass != null) {
      final T a = AnnotationUtils.getAnnotation(superClass, method.getName(),
          method.getParameterTypes(), annotation);
      if (a != null) {
        return a;
      }
    }

    for (final Class<?> clazz : interfaces) {
      final T a = AnnotationUtils.getAnnotation(clazz, method.getName(), method.getParameterTypes(),
          annotation);
      if (a != null) {
        return a;
      }
    }

    return null;
  }

  public static <T extends Annotation> T getAnnotation(final Class<?> clazz,
      final String methodName, final Class<?>[] parameterTypes, final Class<T> annotation) {
    if (clazz == null) {
      return null;
    }
    try {
      final Method method = clazz.getMethod(methodName, parameterTypes);
      return AnnotationUtils.getAnnotationFromHierarchy(method, annotation);
    } catch (NoSuchMethodException | SecurityException ex) {
      // TODO Auto-generated catch block
      // ex.printStackTrace();
      return null;
    }
  }

}
