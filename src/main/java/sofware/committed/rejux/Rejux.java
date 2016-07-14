package sofware.committed.rejux;

import sofware.committed.rejux.impl.StoreDispatcher;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <G> Dispatcher create(G store, Middleware<? super G>... middlewares) {
		return new StoreDispatcher<>(store, middlewares);
	}

}
