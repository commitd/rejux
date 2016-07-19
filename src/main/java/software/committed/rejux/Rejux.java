package software.committed.rejux;

import java.lang.reflect.Proxy;
import java.util.Arrays;

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

  public static <S> Store<S> createStore(final Class<S> clazz, final S initialState,
      final Middleware<? super S>... middleware) {
    final CombinedState<S> combinedState = new CombinedState<>(initialState);
    final CombinedReducer<S> combinedReducer = new CombinedReducer<>(initialState, combinedState);

    final StateProxyHandler<S> handler = new StateProxyHandler<>(combinedState);
    final S state =
        (S) Proxy.newProxyInstance(Rejux.class.getClassLoader(), new Class[] {clazz}, handler);

    return new SimpleStore<>(clazz, state, combinedReducer, Arrays.asList(middleware));
  }

  public static <S> SubscribableState<S> createState(final Class<S> clazz, final S initialState,
      final Middleware<? super S>... middleware) {
    return new SimpleState<>(clazz, initialState, Arrays.asList(middleware));
  }

}
