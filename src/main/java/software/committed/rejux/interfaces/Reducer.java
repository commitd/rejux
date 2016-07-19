package software.committed.rejux.interfaces;

public interface Reducer<S> {

  Class<S> getType();

  S reduce(S state, Object action);

}
