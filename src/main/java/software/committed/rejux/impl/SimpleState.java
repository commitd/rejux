package software.committed.rejux.impl;

import software.committed.rejux.interfaces.SettableState;

public class SimpleState<S> extends AbstractSubscribableState<S> implements SettableState<S> {

	public SimpleState(Class<S> clazz, S initial) {
		super(clazz, initial);
	}

	@Override
	public synchronized boolean setState(S newState) {
		return super.setState(newState);
	}

}
