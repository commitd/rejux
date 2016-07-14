package software.committed.rejux;

@FunctionalInterface
public interface Dispatcher {

	void dispatch(Action action);

}
