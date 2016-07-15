package software.committed.rejux.impl;

import java.util.List;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.interfaces.State;
import software.committed.rejux.interfaces.SubDispatcher;
import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;
import software.committed.rejux.utils.MiddlewareUtils;

public class SimpleStore<S> implements State<S> {

	private final SimpleStateHolder<S> holder;
	private final Reducer<S> reducer;
	private final SubDispatcher chain;

	public SimpleStore(S initialState, Reducer<S> reducer, List<Middleware<S>> middlewares) {
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
