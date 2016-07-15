package software.committed.rejux.interfaces;

@FunctionalInterface
public interface StatefulMiddleware<S> {

	// QUESTION: Should this get the "G store" as well? We think not, sure it pushes a lot of
	// generics into the code and makes it unclear which middleware to use.
	void apply(Dispatcher first, S state, Action action, Dispatcher next);
}
