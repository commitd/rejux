package software.committed.rejux.impl;

import java.util.List;

import software.committed.rejux.interfaces.ApplyMiddleware;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.SettableState;
import software.committed.rejux.utils.MiddlewareUtils;

public class SimpleState<S> extends AbstractSubscribableState<S> implements SettableState<S>, ApplyMiddleware<S> {

	private final List<Middleware<? super S>> middleware;

	public SimpleState(Class<S> clazz, S initial, List<Middleware<? super S>> middleware) {
		super(clazz, initial);
		this.middleware = middleware;
	}

	@Override
	public synchronized boolean setState(S newState) {
		return super.setState(newState);
	}

	@Override
	public Dispatcher applyMiddleware(Dispatcher firstDispatcher, Dispatcher lastDispatcher) {
		return MiddlewareUtils.createChain(firstDispatcher, this, middleware, lastDispatcher);
	}

}
