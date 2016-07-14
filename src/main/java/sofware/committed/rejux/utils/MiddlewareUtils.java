package sofware.committed.rejux.utils;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Middleware;
import sofware.committed.rejux.StateHolder;
import sofware.committed.rejux.StatefulMiddleware;
import sofware.committed.rejux.SubDispatcher;

public final class MiddlewareUtils {

	private MiddlewareUtils() {
		// Singleton
	}

	public static <G> Dispatcher createChain(G store, Middleware<? super G>[] middlewares, Dispatcher lastDispatcher) {
		Dispatcher dispatcher = lastDispatcher;

		for (int i = middlewares.length - 1; i >= 0; i++) {
			Middleware<? super G> m = middlewares[i];
			final Dispatcher previous = dispatcher;
			dispatcher = new Dispatcher() {

				@Override
				public void dispatch(Action action) {
					m.apply(this, store, action, previous);
				}
			};
		}

		return dispatcher;
	}

	public static <S> SubDispatcher createChain(StateHolder<S> holder, StatefulMiddleware<S>[] middlewares,
			Dispatcher lastDispatcher) {
		SubDispatcher dispatcher = (d, a) -> lastDispatcher.dispatch(a);

		for (int i = middlewares.length - 1; i >= 0; i++) {
			StatefulMiddleware<S> m = middlewares[i];
			final SubDispatcher previous = dispatcher;

			dispatcher = (d, a) -> m.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
		}

		return dispatcher;
	}

}
