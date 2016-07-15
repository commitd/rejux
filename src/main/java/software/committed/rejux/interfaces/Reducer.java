package software.committed.rejux.interfaces;

@FunctionalInterface
public interface Reducer<S> {

	S reduce(S state, Action action);

}
