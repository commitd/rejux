package software.committed.rejux.impl;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Store;

public class SimpleStore<S> extends AbstractSubscribableState<S> implements Store<S> {

	private final Dispatcher dispatchToStates;

	public SimpleStore(Class<S> clazz, S state, Dispatcher dispatchToStates, Middleware<? super S>[] middleware) {
		super(clazz, state);
		this.dispatchToStates = dispatchToStates;
	}

	@Override
	public void dispatch(Object action) {
		// Discard nulls
		if (action == null) {
			return;
		}

		// TODO: Apply middleware

		dispatchToStates.dispatch(action);

		// TODO Only on update
		fireStateChanged();
	}

}
