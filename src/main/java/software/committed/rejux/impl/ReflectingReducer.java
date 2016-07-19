package software.committed.rejux.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.committed.rejux.utils.ReflectionUtils;

public abstract class ReflectingReducer<S> extends AbstractReducer<S> {

  // TODO: Look for ActionReducer annotation?
  // TODO: ActionReducer annotation should have a order, and the map should be a list?
  // TODO: ActionReducer should have a fallthrough=false option (if it matched)

  private final Map<Class<?>, ReduceFunction<S>> reducers;

  protected ReflectingReducer(final Class<S> stateClazz) {
    super(stateClazz);

    final List<Method> reducerMethods =
        ReflectionUtils.findMethods(this, stateClazz, stateClazz, Object.class);

    reducers = new HashMap<>(reducerMethods.size());
    for (final Method m : reducerMethods) {
      if (m.getName().equals("reduce")) {
        // Skip the actual reduce function!
        continue;
      }

      final Class<?>[] parameters = m.getParameterTypes();
      reducers.put(parameters[1], (state, action) -> ReflectionUtils.callWithDefaultReturn(this,
          stateClazz, state, m, state, action));
    }
  }

  @Override
  public final S reduce(final S originalState, final Object action) {
    S state = originalState;

    final Class<?> clazz = action.getClass();
    for (final Map.Entry<Class<?>, ReduceFunction<S>> e : reducers.entrySet()) {
      if (e.getKey().isAssignableFrom(clazz)) {
        state = e.getValue().reduce(state, action);
      }
    }

    return state;
  }

  private interface ReduceFunction<S> {
    S reduce(S state, Object action);
  }
}
