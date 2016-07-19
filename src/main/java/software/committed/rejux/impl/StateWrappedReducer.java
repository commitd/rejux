package software.committed.rejux.impl;

import software.committed.rejux.interfaces.Reducer;
import software.committed.rejux.interfaces.SettableState;
import software.committed.rejux.interfaces.State;

public class StateWrappedReducer<S, T extends State<S>> extends AbstractReducer<T> {

  private final Reducer<S> reducer;

  public StateWrappedReducer(final Class<T> clazz, final Reducer<S> reducer) {
    super(clazz);
    this.reducer = reducer;
  }

  @Override
  public T reduce(final T wrapped, final Object action) {

    final S oldState = wrapped.get();
    final S newState = reducer.reduce(oldState, action);

    if (wrapped instanceof SettableState) {
      ((SettableState<S>) wrapped).setState(newState);
    } else {
      // TODO: Or do what?
    }

    return wrapped;
  }

}
