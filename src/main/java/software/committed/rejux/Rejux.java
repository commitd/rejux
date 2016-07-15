package software.committed.rejux;

import java.lang.reflect.Proxy;

import software.committed.rejux.impl.CombinedReducer;
import software.committed.rejux.impl.CombinedState;
import software.committed.rejux.impl.SimpleState;
import software.committed.rejux.impl.SimpleStore;
import software.committed.rejux.impl.StateProxyHandler;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Store;
import software.committed.rejux.interfaces.SubscribableState;

public final class Rejux {

	private Rejux() {
		// Singleton
	}

	public static <S> Store<S> createStore(Class<S> clazz, S initialState, Middleware<? super S>... middleware) {
		CombinedState<S> combinedState = new CombinedState<>(initialState);
		CombinedReducer<S> combinedReducer = new CombinedReducer<>(initialState, combinedState);

		StateProxyHandler<S> handler = new StateProxyHandler<>(combinedState);
		S state = (S) Proxy.newProxyInstance(Rejux.class.getClassLoader(), new Class[] { clazz }, handler);

		return new SimpleStore<>(clazz, state, combinedReducer, middleware);
	}

	public static <S> SubscribableState<S> createState(Class<S> clazz, S initialState,
			Middleware<? super S>... middleware) {
		return new SimpleState<>(clazz, initialState);
	}

}
