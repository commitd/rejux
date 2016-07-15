package software.committed.rejux.impl;

import java.lang.reflect.Method;

import com.github.andrewoma.dexx.collection.HashMap;
import com.github.andrewoma.dexx.collection.Map;
import com.github.andrewoma.dexx.collection.Pair;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.utils.AnnotationUtils;

public class CombinedReducer<S> {

	private Map<String, Reducer<?>> reducers = new HashMap<>();
	private final CombinedState<S> combinedState;

	public CombinedReducer(S initialState, CombinedState<S> combinedState) {

		this.combinedState = combinedState;

		// Build the initial state map and reducers
		for (String methodName : combinedState.getNames()) {

			try {
				Method m = initialState.getClass().getMethod(methodName);

				if (isReduceMethod(m)) {
					Reduce reduce = AnnotationUtils.getAnnotationFromHierarchy(m, Reduce.class);
					Class<?> stateClass = combinedState.getStateClass(methodName);

					// Create the reducer

					if (stateClass != null && reduce != null && reduce.value() != null) {

						try {

							Reducer<?> reducer = reduce.value().newInstance();

							Class<?> reducerStateClazz = reducer.getType();

							if (reducerStateClazz.isAssignableFrom(stateClass)) {
								// Passed our check, so we move create a reducer
								reducers = reducers.put(methodName, reducer);
							} else {
								System.err.println(
										"Reduce method '" + methodName + "' has a reducer '" + reduce.value()
												+ "' which has state '" + reducerStateClazz
												+ "' not match the initial state type '"
												+ stateClass + "', ignoring...");
							}
						} catch (Exception e) {
							System.err.println("Reducer for " + methodName + "appears invalid, ignoring...");
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.err.println("Cant setup a reduce for an unknown return type");
					}

				}

			} catch (NoSuchMethodException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public boolean dispatch(Dispatcher dispatcher, Object action) {
		boolean changed = false;

		for (Pair<String, Reducer<?>> e : reducers) {

			String name = e.component1();
			Reducer reducer = e.component2();
			Object oldState = combinedState.getStateByName(name);

			// TODO: Store middleware

			// Note we check when we create the reducer map that the newState, oldState, etc are
			// "correctly" typed.
			Object newState = reducer.reduce(oldState, action);

			// Update our "global" state
			if (combinedState.set(name, newState)) {
				changed = true;
			}
		}

		return changed;
	}

	public static boolean isReduceMethod(Method method) {
		return method.getParameterCount() == 0 && AnnotationUtils.isAnnotationPresentInHierarchy(method, Reduce.class)
				&& !method.isDefault()
				&& !method.getReturnType().equals(Void.class);
	}
}
