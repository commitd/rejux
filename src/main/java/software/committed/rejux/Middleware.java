package software.committed.rejux;

@FunctionalInterface
public interface Middleware<G> {

	void apply(Dispatcher first, G store, Action action, Dispatcher next);

}
