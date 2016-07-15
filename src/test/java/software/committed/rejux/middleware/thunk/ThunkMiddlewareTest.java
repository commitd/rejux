package software.committed.rejux.middleware.thunk;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import software.committed.rejux.interfaces.Dispatcher;

public class ThunkMiddlewareTest {

	@Test
	public void testNonThunk() {
		ThunkMiddleware<Object> m = new ThunkMiddleware<>();

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		Object action = new Object();
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
		Mockito.verify(next).dispatch(Matchers.any());
	}

	@Test
	public void testThunk() {
		ThunkMiddleware<Object> m = new ThunkMiddleware<>();

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		ThunkAction<Object> action = Mockito.mock(ThunkAction.class);
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
		Mockito.verify(next, Mockito.never()).dispatch(Matchers.any());

		Mockito.verify(action).execute(first, state);
	}

}
