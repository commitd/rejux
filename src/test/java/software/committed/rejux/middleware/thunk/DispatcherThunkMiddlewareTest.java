package software.committed.rejux.middleware.thunk;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import software.committed.rejux.Action;
import software.committed.rejux.Dispatcher;
import software.committed.rejux.middleware.thunk.DispatcherThunkAction;
import software.committed.rejux.middleware.thunk.DispatcherThunkMiddleware;

public class DispatcherThunkMiddlewareTest {

	@Test
	public void testNonThunk() {
		DispatcherThunkMiddleware<Object> m = new DispatcherThunkMiddleware<>();

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		Action action = new Action() {
		};
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
		Mockito.verify(next).dispatch(Matchers.any());
	}

	@Test
	public void testThunk() {
		DispatcherThunkMiddleware<Object> m = new DispatcherThunkMiddleware<>();

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		DispatcherThunkAction<Object> action = Mockito.mock(DispatcherThunkAction.class);
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
		Mockito.verify(next, Mockito.never()).dispatch(Matchers.any());

		Mockito.verify(action).execute(first, state);
	}

}
