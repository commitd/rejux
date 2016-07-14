package sofware.committed.rejux;

import sofware.committed.rejux.impl.Store;
import sofware.committed.rejux.impl.StoreDispatcher;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <G> Dispatcher create(G store, Middleware<? super G>... middlewares) {
		return new StoreDispatcher<>(store, middlewares);
	}

	public static <S> Store<S> create(S initialState, Reducer<S> reducer, StatefulMiddleware<S>... middlewares) {
		return new Store<>(initialState, reducer, middlewares);
	}
}
