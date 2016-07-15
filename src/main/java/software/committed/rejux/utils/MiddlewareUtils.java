package software.committed.rejux.utils;

import java.util.List;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.SubscribableState;
import software.committed.rejux.interfaces.ExplicitDispatcher;

public final class MiddlewareUtils {

	private MiddlewareUtils() {
		// Singleton
	}

	public static <G> Dispatcher createChain(G store, List<Middleware<? super G>> middlewares,
			Dispatcher lastDispatcher) {
		Dispatcher dispatcher = lastDispatcher;

		if (middlewares != null) {
			for (Middleware<? super G> middleware : middlewares) {
				final Dispatcher previous = dispatcher;
				dispatcher = new Dispatcher() {

					@Override
					public void dispatch(Object action) {
						middleware.apply(this, store, action, previous);
					}
				};
			}
		}

		return dispatcher;
	}

	public static <S> ExplicitDispatcher createChain(SubscribableState<S> holder, List<Middleware<S>> middlewares,
			Dispatcher lastDispatcher) {
		ExplicitDispatcher dispatcher = (d, a) -> lastDispatcher.dispatch(a);

		if (middlewares != null) {
			for (Middleware<S> middleware : middlewares) {
				final ExplicitDispatcher previous = dispatcher;
				dispatcher = (d, a) -> middleware.apply(d, holder.get(), a, x -> previous.dispatch(d, x));
				dispatcher = (d, a) -> middleware.apply(d, holder.get(), a, x -> previous.dispatch(d, x));
			}
		}

		return dispatcher;
	}

}
