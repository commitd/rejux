package software.committed.rejux.middleware.filter;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;

public class ActionFilterMiddleware<S> implements Middleware<S> {

  private final ActionFilter filter;

  public ActionFilterMiddleware(final ActionFilter filter) {
    this.filter = filter;
  }

  @Override
  public void apply(final Dispatcher first, final S state, final Object action,
      final Dispatcher next) {
    if (filter.accepts(action)) {
      next.dispatch(action);
    }
  }

}
