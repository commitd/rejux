package sofware.committed.rejux;

@FunctionalInterface
public interface Reducer<S> {

	S reduce(S state, Action action);

}
