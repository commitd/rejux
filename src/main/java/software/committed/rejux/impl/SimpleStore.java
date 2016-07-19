package software.committed.rejux.impl;

import java.util.List;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;
import software.committed.rejux.interfaces.Store;
import software.committed.rejux.utils.MiddlewareUtils;

public class SimpleStore<S> extends AbstractSubscribableState<S> implements Store<S> {

  private final CombinedReducer<S> reducer;
  private final Dispatcher chain;

  public SimpleStore(final Class<S> clazz, final S state, final CombinedReducer<S> reducer,
      final List<Middleware<? super S>> middleware) {
    super(clazz, state);
    this.reducer = reducer;

    this.chain = MiddlewareUtils.createChain(this, state, middleware, this::dispatchToReducers);
  }

  @Override
  public void dispatch(final Object action) {
    // Discard nulls
    if (action == null) {
      return;
    }

    this.chain.dispatch(action);
  }

  private void dispatchToReducers(final Object action) {
    final boolean changed = reducer.dispatch(this, action);

    if (changed) {
      fireStateChanged();
    }
  }

}
