package software.committed.rejux.impl;

import software.committed.rejux.interfaces.State;

public class AbstractState<S> implements State<S> {

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