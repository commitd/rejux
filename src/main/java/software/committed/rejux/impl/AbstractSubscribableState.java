package software.committed.rejux.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import software.committed.rejux.interfaces.SubscribableState;
import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;

public class AbstractSubscribableState<S> extends AbstractState<S> implements SubscribableState<S> {

  private final Set<Subscriber<S>> subscribers = Collections.synchronizedSet(new HashSet<>());

  public AbstractSubscribableState(final Class<S> clazz, final S initial) {
    super(clazz, initial);
  }

  @Override
  protected boolean setState(final S newState) {
    final boolean changed = super.setState(newState);
    if (changed) {
      fireStateChanged();
    }
    return changed;
  }

  protected void fireStateChanged() {
    if (state != null) {
      subscribers.forEach(s -> s.onStateChanged(state));
    }
  }

  @Override
  public Subscription subscribe(final Subscriber<S> subscriber) {
    subscribers.add(subscriber);
    return new Subscription() {

      @Override
      public void remove() {
        subscribers.remove(subscriber);
      }

      @Override
      public boolean isSubscribed() {
        return subscribers.contains(subscriber);
      }
    };
  }

}
