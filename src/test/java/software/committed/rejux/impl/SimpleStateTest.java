package software.committed.rejux.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;

public class SimpleStateTest {

  @Test
  public void testSimpleState() {
    SimpleState<String> state = new SimpleState(String.class, "initial", null);

    assertThat(state.state()).isEqualTo("initial");

  }

  @Test
  public void testSetState() {
    SimpleState<String> state = new SimpleState(String.class, "initial", null);

    state.setState("set");
    assertThat(state.state()).isEqualTo("set");
  }



  @Test
  public void testSubscribe() {
    SimpleState<String> state = new SimpleState(String.class, "initial", null);

    Subscriber<String> subscriber = mock(Subscriber.class);


    Subscription subscription = state.subscribe(subscriber);
    assertThat(subscription).isNotNull();
    assertTrue(subscription.isSubscribed());


    state.setState("new");
    assertThat(state.state()).isEqualTo("new");
    verify(subscriber).onStateChanged("new");
  }

  @Test
  public void testUnsubscribe() {
    SimpleState<String> state = new SimpleState(String.class, "initial", null);

    Subscriber<String> subscriber = mock(Subscriber.class);

    Subscription subscription = state.subscribe(subscriber);
    assertThat(subscription).isNotNull();
    assertTrue(subscription.isSubscribed());

    subscription.remove();

    assertFalse(subscription.isSubscribed());

    state.setState("new");
    assertThat(state.state()).isEqualTo("new");
    verify(subscriber, never()).onStateChanged(any());
  }


}
