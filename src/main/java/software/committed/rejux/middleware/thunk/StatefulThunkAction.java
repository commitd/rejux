package software.committed.rejux.middleware.thunk;

import software.committed.rejux.Action;
import software.committed.rejux.Dispatcher;

@FunctionalInterface
public interface StatefulThunkAction<S> extends Action {

	void execute(Dispatcher dispatcher, S state);
}
