package software.committed.rejux.interfaces;

import java.util.function.Consumer;

@FunctionalInterface
public interface Subscriber<S> extends Consumer<S> {

  @Override
  default void accept(S state) {
    onStateChanged(state);
  }

  void onStateChanged(S state);

}
