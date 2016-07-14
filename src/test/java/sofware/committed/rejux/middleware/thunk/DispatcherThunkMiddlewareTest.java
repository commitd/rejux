package sofware.committed.rejux.middleware.thunk;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;

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
