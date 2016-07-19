package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;

public class ThunkMiddleware<G> implements Middleware<G> {

  @Override
  public void apply(final Dispatcher first, final G store, final Object action,
      final Dispatcher next) {
    if (action instanceof ThunkAction) {
      final ThunkAction<G> thunk = (ThunkAction<G>) action;
      thunk.execute(first, store);
    } else {
      next.dispatch(action);
    }
  }

}
