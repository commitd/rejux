package software.committed.rejux.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.assertj.core.util.Lists;
import org.junit.Ignore;
import org.junit.Test;

public class StateProxyHandlerTest {

  @Test
  public void testProxy() throws NoSuchMethodException, SecurityException {
    CombinedState combinedState = mock(CombinedState.class);

    when(combinedState.getNames()).thenReturn(Lists.newArrayList("hello", "world"));
    when(combinedState.getStateClass("world")).thenReturn(String.class);
    when(combinedState.getStateByName("world")).thenReturn("from store");
    when(combinedState.isStateMethod(FakeStore.class.getMethod("world"))).thenReturn(true);


    StateProxyHandler<String> handler = new StateProxyHandler<>(combinedState);

    FakeStore proxy = (FakeStore) Proxy.newProxyInstance(getClass().getClassLoader(),
        new Class<?>[] {FakeStore.class}, handler);

    assertThat(proxy.world()).isEqualTo("from store");

  }


  // TODO: This functionality does not work as we get an exception through from the unreflectSpecial
  // code with the handler.invoke.
  // See
  // http://stackoverflow.com/questions/37812393/how-to-explicitly-invoke-default-method-from-a-dynamic-proxy
  // appears theres no fix
  @Ignore
  @Test
  public void testDefault() throws NoSuchMethodException, SecurityException {
    CombinedState combinedState = mock(CombinedState.class);

    when(combinedState.getNames()).thenReturn(Lists.newArrayList("hello", "world"));
    when(combinedState.getStateClass("world")).thenReturn(String.class);
    when(combinedState.getStateByName("world")).thenReturn("from store");
    when(combinedState.isStateMethod(FakeStore.class.getMethod("world"))).thenReturn(true);


    StateProxyHandler<String> handler = new StateProxyHandler<>(combinedState);

    FakeStore proxy = (FakeStore) Proxy.newProxyInstance(getClass().getClassLoader(),
        new Class<?>[] {FakeStore.class}, handler);

    assertThat(proxy.hello()).isEqualTo("hello from store");

  }


}
