package software.committed.rejux;

import java.util.Arrays;
import java.util.Collections;

import software.committed.rejux.impl.SimpleStore;
import software.committed.rejux.impl.SuperStore;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.interfaces.StatefulMiddleware;

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

	public static <S> SimpleStore<S> createStore(S initialState, Reducer<S> reducer) {
		return new SimpleStore<>(initialState, reducer, Collections.emptyList());
	}

	public static <S> SimpleStore<S> createStore(S initialState, Reducer<S> reducer, StatefulMiddleware<S>... middlewares) {
		return new SimpleStore<>(initialState, reducer, Arrays.asList(middlewares));
	}
}
