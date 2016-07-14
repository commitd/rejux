package sofware.committed.rejux.middleware.thunk;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;

@FunctionalInterface
public interface StatefulThunkAction<S> extends Action {

	void execute(Dispatcher dispatcher, S state);
}
