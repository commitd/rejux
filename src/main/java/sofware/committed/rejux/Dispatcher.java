package sofware.committed.rejux;

@FunctionalInterface
public interface Dispatcher {

	void dispatch(Action action);

}
