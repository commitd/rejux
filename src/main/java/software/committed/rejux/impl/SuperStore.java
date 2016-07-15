package software.committed.rejux.impl;

import java.util.List;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.utils.MiddlewareUtils;
import software.committed.rejux.utils.ReflectionUtils;

public class SuperStore<G> implements Dispatcher {

	private final List<SimpleStore> subStores;
	private final G stores;
	private final Dispatcher chain;

	public SuperStore(G stores, List<Middleware<? super G>> middlewares) {
		this.stores = stores;

		// Get the return type
		subStores = ReflectionUtils.getAllReturnedOfType(stores, SimpleStore.class);
		this.chain = MiddlewareUtils.createChain(stores, middlewares, this::dispatchToSubStores);
	}

	private void dispatchToSubStores(Action action) {
		for (SimpleStore<?> subStore : subStores) {
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
