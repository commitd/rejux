package software.committed.rejux.impl;

import software.committed.rejux.interfaces.State;

public class AbstractState<S> implements State<S> {

  private final Class<S> stateClass;
  protected S state;

  public AbstractState(final Class<S> clazz, final S initial) {
    stateClass = clazz;
    state = initial;
  }

  @Override
  public Class<S> getType() {
    return stateClass;
  }

  protected boolean setState(final S newState) {
    if (newState != null && !newState.equals(state)) {
      state = newState;
      return true;
    }
    return false;
  }

  @Override
  public S state() {
    return state;
  }

}
