package sofware.committed.rejux.impl;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Reducer;
import sofware.committed.rejux.StateHolder;
import sofware.committed.rejux.StatefulMiddleware;
import sofware.committed.rejux.SubDispatcher;
import sofware.committed.rejux.Subscriber;
import sofware.committed.rejux.Subscription;
import sofware.committed.rejux.utils.MiddlewareUtils;

public class Store<S> implements StateHolder<S> {

	private final SimpleStateHolder<S> holder;
	private final Reducer<S> reducer;
	private final SubDispatcher chain;

	public Store(S initialState, Reducer<S> reducer, StatefulMiddleware<S>... middlewares) {
		this.reducer = reducer;
		holder = new SimpleStateHolder<>(initialState);

		this.chain = MiddlewareUtils.createChain(holder, middlewares, this::reduce);
	}

	// NOTE: This is deliberately package level as it is accessed only StoreDispatcher
	void dispatch(Dispatcher dispatcher, Action action) {
		this.chain.dispatch(dispatcher, action);
	}

	private void reduce(Action action) {
		S originalState = holder.getState();
		S newState = reducer.reduce(originalState, action);
		holder.setState(newState);
	}

	@Override
	public S getState() {
		return holder.getState();
	}

	@Override
	public Subscription subscribe(Subscriber<S> subscriber) {
		return holder.subscribe(subscriber);
	}

}
