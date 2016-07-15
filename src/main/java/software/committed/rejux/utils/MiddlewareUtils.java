package software.committed.rejux.utils;

import java.util.List;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.State;

public final class MiddlewareUtils {

	private MiddlewareUtils() {
		// Singleton
	}

	public static <S> Dispatcher createChain(Dispatcher firstDispatcher, S store,
			List<Middleware<? super S>> middlewares,
			Dispatcher lastDispatcher) {
		Dispatcher dispatcher = lastDispatcher;

		if (middlewares != null && !middlewares.isEmpty()) {
			for (int i = middlewares.size() - 1; i >= 0; i--) {
				Middleware<? super S> m = middlewares.get(i);
				final Dispatcher previous = dispatcher;
				dispatcher = (a) -> m.apply(firstDispatcher, store, a, previous);
			}
		}

		return dispatcher;
	}

	public static <S> Dispatcher createChain(Dispatcher firstDispatcher, State<S> holder,
			List<Middleware<? super S>> middlewares,
			Dispatcher lastDispatcher) {
		Dispatcher dispatcher = lastDispatcher;

		if (middlewares != null && !middlewares.isEmpty()) {
			for (int i = middlewares.size() - 1; i >= 0; i--) {
				Middleware<? super S> m = middlewares.get(i);
				final Dispatcher previous = dispatcher;
				dispatcher = (a) -> m.apply(firstDispatcher, holder.get(), a, previous);
			}
		}

		return dispatcher;
	}

}
