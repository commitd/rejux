package sofware.committed.rejux;

@FunctionalInterface
public interface StatefulMiddleware<S> {

	void apply(Dispatcher first, S state, Action action, Dispatcher next);
}
