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

	protected ReflectingReducer(Class<S> stateClazz) {
		super(stateClazz);

		List<Method> reducerMethods = ReflectionUtils.findMethods(this, stateClazz, stateClazz, Object.class);

		reducers = new HashMap<>(reducerMethods.size());
		for (Method m : reducerMethods) {
			if (m.getName().equals("reduce")) {
				// Skip the actual reduce function!
				continue;
			}

			Class<?>[] parameters = m.getParameterTypes();
			reducers.put(parameters[1], (s, a) -> ReflectionUtils.callWithDefaultReturn(this, stateClazz, s, m, s, a));
		}
	}

	@Override
	public final S reduce(S originalState, Object action) {
		S state = originalState;

		Class<?> clazz = action.getClass();
		for (Map.Entry<Class<?>, ReduceFunction<S>> e : reducers.entrySet()) {
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
