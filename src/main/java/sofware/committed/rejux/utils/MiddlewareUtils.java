package sofware.committed.rejux.utils;

import java.util.List;

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

	public static <G> Dispatcher createChain(G store, List<Middleware<? super G>> middlewares,
			Dispatcher lastDispatcher) {
		Dispatcher dispatcher = lastDispatcher;

		if (middlewares != null) {
			for (int i = middlewares.size() - 1; i >= 0; i--) {
				Middleware<? super G> m = middlewares.get(i);
				final Dispatcher previous = dispatcher;
				dispatcher = new Dispatcher() {

					@Override
					public void dispatch(Action action) {
						m.apply(this, store, action, previous);
					}
				};
			}
		}

		return dispatcher;
	}

	public static <S> SubDispatcher createChain(StateHolder<S> holder, StatefulMiddleware<S>[] middlewares,
			Dispatcher lastDispatcher) {
		SubDispatcher dispatcher = (d, a) -> lastDispatcher.dispatch(a);

		if (middlewares != null) {
			for (int i = middlewares.length - 1; i >= 0; i--) {
				StatefulMiddleware<S> m = middlewares[i];
				final SubDispatcher previous = dispatcher;

				dispatcher = (d, a) -> m.apply(d, holder.getState(), a, x -> previous.dispatch(d, x));
			}
		}

		return dispatcher;
	}

}
