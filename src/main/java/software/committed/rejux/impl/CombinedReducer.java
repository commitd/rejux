package software.committed.rejux.impl;

import java.lang.reflect.Method;

import com.github.andrewoma.dexx.collection.HashMap;
import com.github.andrewoma.dexx.collection.Map;
import com.github.andrewoma.dexx.collection.Pair;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.interfaces.SettableState;
import software.committed.rejux.interfaces.State;
import software.committed.rejux.utils.AnnotationUtils;

public class CombinedReducer<S> {

  private Map<String, Reducer<?>> reducers = new HashMap<>();
  private final CombinedState<S> combinedState;

  public CombinedReducer(final S initialState, final CombinedState<S> combinedState) {

    this.combinedState = combinedState;

    // Build the initial state map and reducers
    for (final String methodName : combinedState.getNames()) {

      try {
        final Method m = initialState.getClass().getMethod(methodName);

        if (CombinedReducer.isReduceMethod(m)) {
          final Reduce reduce = AnnotationUtils.getAnnotationFromHierarchy(m, Reduce.class);
          Class<?> stateClass = combinedState.getStateClass(methodName);

          // Create the reducer

          if (stateClass != null && reduce != null && reduce.value() != null) {

            try {
              final Reducer<?> reducer = reduce.value().newInstance();
              final Class<?> reducerStateClazz = reducer.getType();

              if (State.class.isAssignableFrom(stateClass)) {
                // If we have are State<> wrapped then we need to unpack and repack
                // in the reducer

                final State state = (State) combinedState.getStateByName(methodName);
                if (state == null) {
                  throw new RuntimeException("Initial state for State<> types  may not be null");
                }

                stateClass = state.getType();
                //
                // reducer = new StateWrappedReducer(stateClass, reducer);
              }

              if (reducerStateClazz.isAssignableFrom(stateClass)) {
                // Passed our check, so we move create a reducer
                reducers = reducers.put(methodName, reducer);
              } else {
                System.err.println("Reduce method '" + methodName + "' has a reducer '"
                    + reduce.value() + "' which has state '" + reducerStateClazz
                    + "' not match the initial state type '" + stateClass + "', ignoring...");
              }

            } catch (final Exception ex) {
              System.err.println("Reducer for " + methodName + "appears invalid, ignoring...");
              // TODO Auto-generated catch block
              ex.printStackTrace();
            }
          } else {
            System.err.println("Cant setup a reduce for an unknown return type");
          }

        }

      } catch (NoSuchMethodException | SecurityException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    }

  }

  public boolean dispatch(final Dispatcher dispatcher, final Object action) {
    boolean changed = false;

    for (final Pair<String, Reducer<?>> e : reducers) {

      final String name = e.component1();
      final Reducer reducer = e.component2();
      Object oldState = combinedState.getStateByName(name);
      State oldWrappedState = null;

      if (oldState instanceof State) {
        oldWrappedState = (State) oldState;
        oldState = oldWrappedState.get();
      }

      // Note we check when we create the reducer map that the newState, oldState, etc are
      // "correctly" typed.
      final Object newState = reducer.reduce(oldState, action);

      if (oldState == null && newState != null || oldState != null && !oldState.equals(newState)) {

        if (oldWrappedState != null) {
          if (oldWrappedState != null || oldWrappedState instanceof SettableState) {
            final SettableState newWrappedState = (SettableState) oldWrappedState;
            newWrappedState.setState(newState);
            combinedState.set(name, newWrappedState);
          } else {
            // TODO: If oldWrappedState != null but can't be set? we effectively throw
            // away the reduced result at the moment!
          }

        } else {
          combinedState.set(name, newState);
        }

        changed = true;
      }

    }

    return changed;
  }

  public static boolean isReduceMethod(final Method method) {
    return method.getParameterCount() == 0
        && AnnotationUtils.isAnnotationPresentInHierarchy(method, Reduce.class)
        && !method.isDefault() && !method.getReturnType().equals(Void.class);
  }
}
