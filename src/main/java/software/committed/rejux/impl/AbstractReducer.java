package software.committed.rejux.impl;

import software.committed.rejux.interfaces.Reducer;

public abstract class AbstractReducer<S> implements Reducer<S> {

	private final Class<S> stateClass;

	protected AbstractReducer(Class<S> stateClazz) {
		this.stateClass = stateClazz;
	}

	@Override
	public Class<S> getType() {
		return stateClass;
	}

}
