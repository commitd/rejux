package software.committed.rejux.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Middleware;

public class MiddlewareUtilsTest {

	@Test
	public void testCreateChainCheckPassesThroughMiddleware() {
		Dispatcher firstDispatcher = Mockito.mock(Dispatcher.class);
		Dispatcher lastDispatcher = Mockito.mock(Dispatcher.class);

		List<Middleware<? super String>> mw = new ArrayList<>();
		mw.add((first, store, action, next) -> next.dispatch(action));

		Dispatcher chain = MiddlewareUtils.createChain(firstDispatcher, new String(), mw, lastDispatcher);

		Object action = new Object();

		chain.dispatch(action);

		Mockito.verify(lastDispatcher).dispatch(action);
	}

	@Test
	public void testCreateChainCheckCallsMiddleware() {
		Dispatcher firstDispatcher = Mockito.mock(Dispatcher.class);
		Dispatcher lastDispatcher = Mockito.mock(Dispatcher.class);

		List<Middleware<? super String>> mw = new ArrayList<>();
		mw.add(Mockito.mock(Middleware.class));

		Dispatcher chain = MiddlewareUtils.createChain(firstDispatcher, new String(), mw, lastDispatcher);

		Object action = new Object();
		chain.dispatch(action);

		Mockito.verify(mw.get(0)).apply(Matchers.any(), Matchers.anyString(), Matchers.eq(action), Matchers.any());
	}

	@Test
	public void testCreateEmptyChain() {
		Dispatcher firstDispatcher = Mockito.mock(Dispatcher.class);
		Dispatcher lastDispatcher = Mockito.mock(Dispatcher.class);

		Dispatcher chain = MiddlewareUtils.createChain(firstDispatcher, new String(), null, lastDispatcher);

		Object action = new Object();

		chain.dispatch(action);

		Mockito.verify(lastDispatcher).dispatch(action);
	}
}
