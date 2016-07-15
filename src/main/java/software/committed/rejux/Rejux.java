package software.committed.rejux;

import java.lang.reflect.Proxy;

import software.committed.rejux.impl.SimpleState;
import software.committed.rejux.impl.SimpleStore;
import software.committed.rejux.impl.StateProxyHandler;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.SubscribableState;
import software.committed.rejux.interfaces.Store;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <S> Store<S> createStore(Class<S> clazz, S initialState, Middleware<? super S>... middleware) {
		StateProxyHandler<S> handler = new StateProxyHandler<>(initialState);
		S state = (S) Proxy.newProxyInstance(Rejux.class.getClassLoader(), new Class[] { clazz }, handler);
		return new SimpleStore<>(clazz, state, handler, middleware);
	}

	public static <S> SubscribableState<S> createState(Class<S> clazz, S initialState, Middleware<? super S>... middleware) {
		return new SimpleState<>(clazz, initialState);
	}

}
