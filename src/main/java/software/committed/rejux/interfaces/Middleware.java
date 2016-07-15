package software.committed.rejux.interfaces;

@FunctionalInterface
public interface Middleware<G> {

	void apply(Dispatcher first, G store, Action action, Dispatcher next);

}
