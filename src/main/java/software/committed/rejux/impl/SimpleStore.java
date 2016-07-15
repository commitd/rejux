package software.committed.rejux.impl;

import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Store;

public class SimpleStore<S> extends AbstractSubscribableState<S> implements Store<S> {

	private final CombinedReducer<S> reducer;

	public SimpleStore(Class<S> clazz, S state, CombinedReducer<S> reducer,
			Middleware<? super S>[] middleware) {
		super(clazz, state);
		this.reducer = reducer;
	}

	@Override
	public void dispatch(Object action) {
		// Discard nulls
		if (action == null) {
			return;
		}

		// TODO: Apply middleware

		boolean changed = reducer.dispatch(this, action);

		// TODO Only on update
		if (changed) {
			fireStateChanged();
		}
	}

}
