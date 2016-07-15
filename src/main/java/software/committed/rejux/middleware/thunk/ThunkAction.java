package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Dispatcher;

@FunctionalInterface
public interface ThunkAction<G> {

	void execute(Dispatcher dispatcher, G store);
}
