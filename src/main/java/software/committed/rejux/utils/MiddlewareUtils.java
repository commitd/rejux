package software.committed.rejux.utils;

import java.util.List;

import software.committed.rejux.Action;
import software.committed.rejux.Dispatcher;
import software.committed.rejux.Middleware;
import software.committed.rejux.StateHolder;
import software.committed.rejux.StatefulMiddleware;
import software.committed.rejux.SubDispatcher;

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

	public static <S> SubDispatcher createChain(StateHolder<S> holder, List<StatefulMiddleware<S>> middlewares,
			Dispatcher lastDispatcher) {
		SubDispatcher dispatcher = (d, a) -> lastDispatcher.dispatch(a);

		if (middlewares != null) {
			for (StatefulMiddleware<S> middleware : middlewares) {
				final SubDispatcher previous = dispatcher;
				dispatcher = (d, a) -> middleware.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
				dispatcher = (d, a) -> middleware.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
			}
		}

		return dispatcher;
	}

}
