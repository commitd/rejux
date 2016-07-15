package software.committed.rejux.interfaces;

@FunctionalInterface
public interface Dispatcher {

	void dispatch(Action action);

}
