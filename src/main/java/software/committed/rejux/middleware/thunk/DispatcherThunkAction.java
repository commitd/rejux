package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;

@FunctionalInterface
public interface DispatcherThunkAction<G> extends Action {

	void execute(Dispatcher dispatcher, G store);
}
