package software.committed.rejux.impl;

import java.lang.reflect.Method;

import com.github.andrewoma.dexx.collection.HashMap;
import com.github.andrewoma.dexx.collection.Map;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.utils.AnnotationUtils;

public class CombinedState<S> {
  private Map<String, Object> state = new HashMap<>();
  private Map<String, Class<?>> stateClasses = new HashMap<>();

  public CombinedState(final S initialState) {
    for (final Method m : initialState.getClass().getMethods()) {
      if (CombinedState.isReduceMethod(m)) {
        final String methodName = m.getName();

        // We make the method accessible, as otherwise it doesn't seem to be able to access
        // lambda/functional anonymous classes.
        m.setAccessible(true);

        // Get the initial state
        try {
          final Class<?> stateClass = m.getReturnType();
          final Object returnedValue = m.invoke(initialState);

          state = state.put(methodName, returnedValue);
          stateClasses = stateClasses.put(methodName, stateClass);
        } catch (final Exception ex) {
          // TODO Auto-generated catch block
          ex.printStackTrace();
        }
      }
    }
  }

  public boolean isStateMethod(final Method method) {
    return method.getParameterCount() == 0 && state.containsKey(method.getName())
        && !method.isDefault();
  }

  public static boolean isReduceMethod(final Method method) {
    return method.getParameterCount() == 0
        && AnnotationUtils.isAnnotationPresentInHierarchy(method, Reduce.class)
        && !method.isDefault() && !method.getReturnType().equals(Void.class);
  }

  public Object getStateByName(final String name) {
    // TODO: Check the types match? Should be unnecessary - as these have been checked already
    // (as part of constructor)
    return state.get(name);
  }

  public Class<?> getStateClass(final String name) {
    return stateClasses.get(name);
  }

  public Iterable<String> getNames() {
    return state.keys();
  }

  public void set(final String name, final Object newState) {
    this.state = this.state.put(name, newState);

  }

}
