package software.committed.rejux.impl;

import java.util.List;

import software.committed.rejux.Action;
import software.committed.rejux.Dispatcher;
import software.committed.rejux.Reducer;
import software.committed.rejux.StateHolder;
import software.committed.rejux.StatefulMiddleware;
import software.committed.rejux.SubDispatcher;
import software.committed.rejux.Subscriber;
import software.committed.rejux.Subscription;
import software.committed.rejux.utils.MiddlewareUtils;

public class Store<S> implements StateHolder<S> {

	private final SimpleStateHolder<S> holder;
	private final Reducer<S> reducer;
	private final SubDispatcher chain;

	public Store(S initialState, Reducer<S> reducer, List<StatefulMiddleware<S>> middlewares) {
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
