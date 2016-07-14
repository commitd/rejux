package sofware.committed.rejux.middleware.filter;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;

public class ActionFilterMiddlewareTest {

	@Test
	public void testNever() {

		ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(a -> false);

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		Action action = new Action() {
		};
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
		Mockito.verify(next, Mockito.never()).dispatch(Matchers.any());
	}

	@Test
	public void testAlways() {

		ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(a -> true);

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
	public void testAccepts() {

		ActionFilter filter = Mockito.mock(ActionFilter.class);
		ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(filter);

		Dispatcher first = Mockito.mock(Dispatcher.class);
		Dispatcher next = Mockito.mock(Dispatcher.class);
		Action action = new Action() {
		};
		Object state = new Object();

		m.apply(first, state, action, next);

		Mockito.verify(filter).accepts(action);
	}

}
