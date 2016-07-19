package software.committed.rejux.interfaces;

@FunctionalInterface
public interface Middleware<S> {

  void apply(Dispatcher front, S storeOrState, Object action, Dispatcher next);

}
