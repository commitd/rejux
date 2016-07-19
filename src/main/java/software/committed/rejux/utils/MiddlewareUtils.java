package software.committed.rejux.utils;

import java.util.List;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.State;

public final class MiddlewareUtils {

  private MiddlewareUtils() {
    // Singleton
  }

  public static <S> Dispatcher createChain(final Dispatcher firstDispatcher, final S store,
      final List<Middleware<? super S>> middlewares, final Dispatcher lastDispatcher) {
    Dispatcher dispatcher = lastDispatcher;

    if (middlewares != null && !middlewares.isEmpty()) {
      for (int i = middlewares.size() - 1; i >= 0; i--) {
        final Middleware<? super S> m = middlewares.get(i);
        final Dispatcher previous = dispatcher;
        dispatcher = (action) -> m.apply(firstDispatcher, store, action, previous);
      }
    }

    return dispatcher;
  }

  public static <S> Dispatcher createChain(final Dispatcher firstDispatcher, final State<S> holder,
      final List<Middleware<? super S>> middlewares, final Dispatcher lastDispatcher) {
    Dispatcher dispatcher = lastDispatcher;

    if (middlewares != null && !middlewares.isEmpty()) {
      for (int i = middlewares.size() - 1; i >= 0; i--) {
        final Middleware<? super S> m = middlewares.get(i);
        final Dispatcher previous = dispatcher;
        dispatcher = (action) -> m.apply(firstDispatcher, holder.get(), action, previous);
      }
    }

    return dispatcher;
  }

}
