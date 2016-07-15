package software.committed.rejux.middleware.thunk;

import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;

@FunctionalInterface
public interface StatefulThunkAction<S> extends Action {

	void execute(Dispatcher dispatcher, S state);
}
