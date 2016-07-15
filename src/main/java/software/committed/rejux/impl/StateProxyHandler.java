package software.committed.rejux.impl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.utils.AnnotationUtils;

public class StateProxyHandler<S> implements InvocationHandler, Dispatcher {

	private final Map<String, Object> state = new HashMap<>();
	private final Map<String, Reducer<?>> reducers = new HashMap<>();

	public StateProxyHandler(S initialState) {

		// Build the initial state map and reducers
		for (Method m : initialState.getClass().getMethods()) {
			if (isReduceMethod(m)) {
				final String methodName = m.getName();

				// We make the method accessible, as otherwise it doesn't seem to be able to access
				// lambda/functional anonymous classes.
				m.setAccessible(true);

				// Get the initial state
				Type returnedType = null;
				Class<?> stateClass = null;
				try {
					stateClass = m.getReturnType();
					returnedType = m.getGenericReturnType();
					Object returnedValue = m.invoke(initialState);

					state.put(methodName, returnedValue);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Create the reducer

				Reduce reduce = AnnotationUtils.getAnnotationFromHierarchy(m, Reduce.class);
				if (returnedType != null && stateClass != null && reduce != null && reduce.value() != null) {

					try {

						Reducer<?> reducer = reduce.value().newInstance();

						Class<?> reducerStateClazz = reducer.getType();

						if (reducerStateClazz.isAssignableFrom(stateClass)) {
							// Passed our check, so we move create a reducer
							reducers.put(methodName, reducer);
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
		}

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		String methodName = method.getName();

		// If we have a default we can directly use that...
		if (method.isDefault()) {
			return MethodHandles.lookup()
					.in(method.getDeclaringClass())
					.unreflectSpecial(method, method.getDeclaringClass())
					.bindTo(proxy)
					.invokeWithArguments(args);
		}

		// Could it be a state method...
		if (isStateMethod(method)) {
			return getStateByName(methodName, method.getGenericReturnType());
		}

		// Not sure what it is!
		return null;
	}

	private boolean isStateMethod(Method method) {
		return method.getParameterCount() == 0 && state.containsKey(method.getName()) && !method.isDefault();
	}

	private boolean isReduceMethod(Method method) {
		return method.getParameterCount() == 0 && AnnotationUtils.isAnnotationPresentInHierarchy(method, Reduce.class)
				&& !method.isDefault()
				&& !method.getReturnType().equals(Void.class);
	}

	private Object getStateByName(String methodName, Type returnType) {
		// TODO: Check the types match? Should be unnecessary - as these have been checked already
		// (as part of constructor)
		return state.get(methodName);
	}

	@Override
	public void dispatch(Object action) {
		for (Map.Entry<String, Reducer<?>> e : reducers.entrySet()) {

			String name = e.getKey();
			Reducer reducer = e.getValue();
			Object oldState = this.state.get(name);

			// TODO: Store middleware

			// Note we check when we create the reducer map that the newState, oldState, etc are
			// "correctly" typed.
			Object newState = reducer.reduce(oldState, action);

			// Update our "global" state
			this.state.put(name, newState);
		}
	}

}
