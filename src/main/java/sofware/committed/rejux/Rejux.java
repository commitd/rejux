package sofware.committed.rejux;

import java.util.Arrays;
import java.util.Collections;

import sofware.committed.rejux.impl.Store;
import sofware.committed.rejux.impl.SuperStore;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <G> SuperStore<G> createSuperStore(G store) {
		return new SuperStore<>(store, Collections.emptyList());
	}

	public static <G> SuperStore<G> createSuperStore(G store, Middleware<? super G>... middlewares) {
		return new SuperStore<>(store, Arrays.asList(middlewares));
	}

	public static <S> Store<S> createStore(S initialState, Reducer<S> reducer) {
		return new Store<>(initialState, reducer, Collections.emptyList());
	}

	public static <S> Store<S> createStore(S initialState, Reducer<S> reducer, StatefulMiddleware<S>... middlewares) {
		return new Store<>(initialState, reducer, Arrays.asList(middlewares));
	}
}
