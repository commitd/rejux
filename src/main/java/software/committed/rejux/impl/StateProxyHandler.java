package software.committed.rejux.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StateProxyHandler<S> implements InvocationHandler {

  private final CombinedState<S> combinedState;

  public StateProxyHandler(final CombinedState<S> combinedState) {
    this.combinedState = combinedState;
  }

  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable {

    final String methodName = method.getName();

    // If we have a default we can directly use that...
    // TODO: This does not work (see notes in the unit test)
    // if (method.isDefault()) {
    // try {
    // return MethodHandles.lookup().in(method.getDeclaringClass())
    // .unreflectSpecial(method, method.getDeclaringClass()).bindTo(proxy)
    // .invokeWithArguments(args);
    // } catch (Exception ex) {
    // return null;
    // }
    // }

    // Could it be a state method...
    if (combinedState.isStateMethod(method)) {
      return combinedState.getStateByName(methodName);
    }

    // Not sure what it is!
    return null;
  }

}
