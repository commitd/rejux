package software.committed.rejux.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import software.committed.rejux.interfaces.SettableStateHolder;
import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;

public class SimpleStateHolder<S> implements SettableStateHolder<S> {

	private final Set<Subscriber<S>> subscribers = Collections.synchronizedSet(new HashSet<>());

	private S state;

	protected SimpleStateHolder(S initial) {
		this.state = initial;
	}

	@Override
	public synchronized void setState(S newState) {
		if (newState != null && !newState.equals(state)) {
			state = newState;
			fireStateChanged();
		}
	}

	private void fireStateChanged() {
		if (state != null) {
			subscribers.forEach(s -> s.onStateChanged(state));
		}
	}

	@Override
	public S getState() {
		return state;
	}

	@Override
	public Subscription subscribe(Subscriber<S> subscriber) {
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
