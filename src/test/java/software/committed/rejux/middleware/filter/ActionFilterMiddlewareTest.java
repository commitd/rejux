package software.committed.rejux.middleware.filter;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import software.committed.rejux.interfaces.Dispatcher;

public class ActionFilterMiddlewareTest {

  @Test
  public void testNever() {

    final ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(a -> false);

    final Dispatcher first = Mockito.mock(Dispatcher.class);
    final Dispatcher next = Mockito.mock(Dispatcher.class);
    final Object action = new Object();
    final Object state = new Object();

    m.apply(first, state, action, next);

    Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
    Mockito.verify(next, Mockito.never()).dispatch(Matchers.any());
  }

  @Test
  public void testAlways() {

    final ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(a -> true);

    final Dispatcher first = Mockito.mock(Dispatcher.class);
    final Dispatcher next = Mockito.mock(Dispatcher.class);
    final Object action = new Object();

    final Object state = new Object();

    m.apply(first, state, action, next);

    Mockito.verify(first, Mockito.never()).dispatch(Matchers.any());
    Mockito.verify(next).dispatch(Matchers.any());
  }

  @Test
  public void testAccepts() {

    final ActionFilter filter = Mockito.mock(ActionFilter.class);
    final ActionFilterMiddleware<Object> m = new ActionFilterMiddleware<>(filter);

    final Dispatcher first = Mockito.mock(Dispatcher.class);
    final Dispatcher next = Mockito.mock(Dispatcher.class);
    final Object action = new Object();

    final Object state = new Object();

    m.apply(first, state, action, next);

    Mockito.verify(filter).accepts(action);
  }

}
