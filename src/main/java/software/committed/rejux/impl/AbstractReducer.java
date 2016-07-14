package software.committed.rejux.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.committed.rejux.Action;
import software.committed.rejux.Reducer;
import software.committed.rejux.utils.ReflectionUtils;

public abstract class AbstractReducer<S> implements Reducer<S> {

	private final Map<Class<?>, Reducer<S>> reducers;

	protected AbstractReducer(Class<S> stateClazz) {
		List<Method> reducerMethods = ReflectionUtils.findMethods(this, stateClazz, stateClazz, Action.class);

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
	public final S reduce(S originalState, Action action) {
		S state = originalState;

		Class<? extends Action> clazz = action.getClass();
		for (Map.Entry<Class<?>, Reducer<S>> e : reducers.entrySet()) {
			if (e.getKey().isAssignableFrom(clazz)) {
				state = e.getValue().reduce(state, action);
			}
		}

		return state;
	}
}
