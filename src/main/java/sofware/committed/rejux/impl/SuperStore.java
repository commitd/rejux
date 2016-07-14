package sofware.committed.rejux.impl;

import java.util.List;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Middleware;
import sofware.committed.rejux.utils.MiddlewareUtils;
import sofware.committed.rejux.utils.ReflectionUtils;

public class SuperStore<G> implements Dispatcher {

	private final List<Store> subStores;
	private final G stores;
	private final Dispatcher chain;

	public SuperStore(G stores, List<Middleware<? super G>> middlewares) {
		this.stores = stores;

		// Get the return type
		subStores = ReflectionUtils.getAllReturnedOfType(stores, Store.class);
		this.chain = MiddlewareUtils.createChain(stores, middlewares, this::dispatchToSubStores);
	}

	private void dispatchToSubStores(Action action) {
		for (Store<?> subStore : subStores) {
			subStore.dispatch(this, action);
		}
	}

	@Override
	public void dispatch(Action action) {
		this.chain.dispatch(action);
	}

	public G getStores() {
		return stores;
	}

	public G stores() {
		return stores;
	}
}
