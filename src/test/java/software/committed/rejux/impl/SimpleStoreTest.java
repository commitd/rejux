package software.committed.rejux.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import software.committed.rejux.interfaces.Subscriber;

public class SimpleStoreTest {

  @Test
  public void testDispatchWithoutChange() {

    CombinedReducer<String> reducer = mock(CombinedReducer.class);
    when(reducer.dispatch(any(), any())).thenReturn(false);

    Subscriber<String> subscriber = mock(Subscriber.class);


    SimpleStore<String> store = new SimpleStore<>(String.class, "initial", reducer, null);

    store.subscribe(subscriber);

    Object action = new Object();
    store.dispatch(action);

    verify(reducer).dispatch(any(), eq(action));
    verify(subscriber, never()).onStateChanged(any());

  }

  @Test
  public void testDispatchWithChange() {

    CombinedReducer<String> reducer = mock(CombinedReducer.class);
    when(reducer.dispatch(any(), any())).thenReturn(true);

    Subscriber<String> subscriber = mock(Subscriber.class);


    SimpleStore<String> store = new SimpleStore<>(String.class, "initial", reducer, null);

    store.subscribe(subscriber);

    Object action = new Object();
    store.dispatch(action);

    verify(reducer).dispatch(any(), eq(action));
    verify(subscriber).onStateChanged(any());
  }

}
