package software.committed.rejux.middleware.thunk;

import software.committed.rejux.Action;
import software.committed.rejux.Dispatcher;

@FunctionalInterface
public interface DispatcherThunkAction<G> extends Action {

	void execute(Dispatcher dispatcher, G store);
}
