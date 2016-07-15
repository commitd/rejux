package software.committed.rejux.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import software.committed.rejux.interfaces.State;
import software.committed.rejux.interfaces.Subscriber;

public class AbstractState<S> implements State<S> {

	private final Set<Subscriber<S>> subscribers = Collections.synchronizedSet(new HashSet<>());

	private final Class<S> stateClass;
	protected S state;

	public AbstractState(Class<S> clazz, S initial) {
		stateClass = clazz;
		state = initial;
	}

	@Override
	public Class<S> getType() {
		return stateClass;
	}

	protected boolean setState(S newState) {
		if (newState != null && !newState.equals(state)) {
			state = newState;
			return true;
		}
		return false;
	}

	@Override
	public S get() {
		return state;
	}

}