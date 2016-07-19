package software.committed.rejux.interfaces;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ExplicitDispatcher extends BiConsumer<Dispatcher, Object> {

  /**
   * Dispatch the action, with an explicit "first dispatcher"
   *
   * The firstDispatcher is provided to middleware to allow them to dispatch back to the front of
   * the dispatch pipeline.
   *
   * @param dispatcher the first dispatcher
   * @param action the action
   */
  void dispatch(Dispatcher dispatcher, Object action);


  @Override
  default void accept(Dispatcher dispatcher, Object action) {
    dispatch(dispatcher, action);
  }

}
