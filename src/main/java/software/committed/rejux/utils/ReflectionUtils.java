package software.committed.rejux.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

  private ReflectionUtils() {
    // Singleton
  }

  public static <T> List<Method> findParameterlessMethodsWithReturnType(final Object object,
      final Class<T> clazz) {

    final Method[] methods = object.getClass().getMethods();
    final List<Method> matching = new ArrayList<>(methods.length);

    for (final Method m : methods) {
      if (clazz.isAssignableFrom(m.getReturnType()) && m.getParameterCount() == 0) {
        matching.add(m);
      }
    }

    return matching;
  }

  // Suppressed as this is checked
  @SuppressWarnings("unchecked")
  public static <T> List<T> getAllReturnedOfType(final Object object, final Class<T> clazz) {

    final List<Method> methods =
        ReflectionUtils.findParameterlessMethodsWithReturnType(object, clazz);

    final List<T> list = new ArrayList<>(methods.size());

    for (final Method m : methods) {
      if (m.getParameterCount() == 0) {
        try {
          final Object returned = m.invoke(object);
          if (returned != null && clazz.isInstance(returned)) {
            list.add((T) returned);
          }
        } catch (final Exception ex) {
          // TODO
          ex.printStackTrace();
        }
      }
    }

    return list;
  }

  public static List<Method> findMethods(final Object object, final Class<?> returnClass,
      final Class<?>... params) {

    final Method[] methods = object.getClass().getMethods();
    final List<Method> matching = new ArrayList<>(methods.length);

    for (final Method m : methods) {
      if (returnClass.isAssignableFrom(m.getReturnType())
          && m.getParameterCount() == params.length) {
        final Parameter[] parameters = m.getParameters();
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
  public static <T> T callWithDefaultReturn(final Object object, final Class<T> returnClazz,
      final T defaultReturnOnNull, final Method method, final Object... params) {
    try {
      final Object r = method.invoke(object, params);
      if (r != null && returnClazz.isInstance(r)) {
        return (T) r;
      } else {
        return defaultReturnOnNull;
      }
    } catch (final Exception ex) {
      ex.printStackTrace();
      return defaultReturnOnNull;
    }
  }

}
