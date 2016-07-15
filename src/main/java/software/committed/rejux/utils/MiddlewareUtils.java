package software.committed.rejux.utils;

import java.util.List;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.State;
import software.committed.rejux.interfaces.SubDispatcher;

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
					public void dispatch(Action action) {
						middleware.apply(this, store, action, previous);
					}
				};
			}
		}

		return dispatcher;
	}

	public static <S> SubDispatcher createChain(State<S> holder, List<Middleware<S>> middlewares,
			Dispatcher lastDispatcher) {
		SubDispatcher dispatcher = (d, a) -> lastDispatcher.dispatch(a);

		if (middlewares != null) {
			for (Middleware<S> middleware : middlewares) {
				final SubDispatcher previous = dispatcher;
				dispatcher = (d, a) -> middleware.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
				dispatcher = (d, a) -> middleware.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
			}
		}

		return dispatcher;
	}

}
