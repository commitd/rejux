package software.committed.rejux.interfaces;

import java.util.function.Consumer;

@FunctionalInterface
public interface Dispatcher extends Consumer<Object> {

  /**
   * Dispatch the action.
   *
   * An alias for accept(action).
   *
   * @param action the action
   */
  void dispatch(Object action);

  @Override
  default void accept(Object action) {
    dispatch(action);
  }

}
