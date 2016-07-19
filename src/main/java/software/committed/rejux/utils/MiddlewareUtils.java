package software.committed.rejux.utils;

import java.util.List;
import java.util.function.Supplier;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;

public final class MiddlewareUtils {

  private MiddlewareUtils() {
    // Singleton
  }

  public static <S> Dispatcher createChain(final Dispatcher firstDispatcher, final S store,
      final List<Middleware<? super S>> middlewares, final Dispatcher lastDispatcher) {
    Supplier<S> supplier = () -> store;
    return createChain(firstDispatcher, supplier, middlewares, lastDispatcher);
  }

  public static <S> Dispatcher createChain(final Dispatcher firstDispatcher,
      final Supplier<S> store, final List<Middleware<? super S>> middlewares,
      final Dispatcher lastDispatcher) {
    Dispatcher dispatcher = lastDispatcher;

    if (middlewares != null && !middlewares.isEmpty()) {
      for (int i = middlewares.size() - 1; i >= 0; i--) {
        final Middleware<? super S> m = middlewares.get(i);
        final Dispatcher previous = dispatcher;
        dispatcher = (action) -> m.apply(firstDispatcher, store.get(), action, previous);
      }
    }

    return dispatcher;
  }



}
