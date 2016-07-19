package software.committed.rejux.interfaces;

import java.util.function.Supplier;

public interface State<S> extends Supplier<S> {

  /**
   * Get the Java class of the state (to compensate for type erasure in Java).
   *
   * @return class of the state
   */
  Class<S> getType();

  /**
   * Alternative alias for get() which can look cleaner in code.
   *
   * @return the state
   */
  S state();


  @Override
  default S get() {
    return state();
  }
}
