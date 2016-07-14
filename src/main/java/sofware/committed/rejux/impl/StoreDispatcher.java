package sofware.committed.rejux.impl;

import java.util.List;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Middleware;
import sofware.committed.rejux.utils.MiddlewareUtils;
import sofware.committed.rejux.utils.ReflectionUtils;

public class StoreDispatcher<G> implements Dispatcher {

	private final List<Store> subStores;
	private final G store;
	private final Dispatcher chain;

	public StoreDispatcher(G store, Middleware<? super G>[] middlewares) {
		this.store = store;

		// Get the return type
		subStores = ReflectionUtils.getAllReturnedOfType(store, Store.class);
		this.chain = MiddlewareUtils.createChain(store, middlewares, this::dispatchToSubDispatchers);
	}

	private void dispatchToSubDispatchers(Action action) {
		for (Store<?> subStore : subStores) {
			subStore.dispatch(this, action);
		}
	}

	@Override
	public void dispatch(Action action) {
		this.chain.dispatch(action);
	}

	public G getStore() {
		return store;
	}

}
