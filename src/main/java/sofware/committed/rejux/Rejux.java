package sofware.committed.rejux;

import sofware.committed.rejux.impl.Store;
import sofware.committed.rejux.impl.SuperStore;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <G> SuperStore<G> createSuperStore(G store, Middleware<? super G>... middlewares) {
		return new SuperStore<>(store, middlewares);
	}

	public static <S> Store<S> createStore(S initialState, Reducer<S> reducer, StatefulMiddleware<S>... middlewares) {
		return new Store<>(initialState, reducer, middlewares);
	}
}
