package software.committed.rejux;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Rejux;
import sofware.committed.rejux.Subscriber;
import sofware.committed.rejux.Subscription;
import sofware.committed.rejux.impl.AbstractReducer;
import sofware.committed.rejux.impl.Store;

public class RejuxFullApiTest {

	public static class ArthimeticStore {

		private final Store<SumState> sum;

		public ArthimeticStore() {
			sum = Rejux.createStore(new SumState(0), new SumReducer());
		}

		public Subscription subscribe(Subscriber<SumState> subscriber) {
			return sum.subscribe(subscriber);
		}

		public SumState getState() {
			return sum.getState();
		}

		public int getSum() {
			return sum.getState().getValue();
		}
	}

	public static class SumState {
		private final int sum;

		public SumState(int sum) {
			this.sum = sum;
		}

		public int getValue() {
			return sum;
		}
	}

	public static class SumReducer extends AbstractReducer<SumState> {

		public SumReducer() {
			super(SumState.class);
		}

		public SumState add(SumState state, AddAction action) {
			return new SumState(state.getValue() + action.getAmount());
		}

	}

	public static class CountSubscriber implements Subscriber<SumState> {

		private int count;

		@Override
		public void onStateChanged(SumState state) {
			count++;
		}

		public int getCount() {
			return count;
		}
	}

	public static class AddAction implements Action {

		private final int amount;

		public AddAction(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

	}

	@Test
	public void canCreateStore() {
		ArthimeticStore store = new ArthimeticStore();
		assertNotNull(store);
		assertEquals(0, store.getSum());
	}

	@Test
	public void canCreateDispatch() {
		assertNotNull(Rejux.createSuperStore(new ArthimeticStore()));
	}

	@Test
	public void canDispatchAction() {
		ArthimeticStore store = new ArthimeticStore();
		Dispatcher dispatcher = Rejux.createSuperStore(store);
		assertEquals(0, store.getSum());
		dispatcher.dispatch(new AddAction(10));
		assertEquals(10, store.getSum());
	}

	@Test
	public void canSubscribe() {
		ArthimeticStore store = new ArthimeticStore();
		Dispatcher dispatcher = Rejux.createSuperStore(store);
		assertEquals(0, store.getSum());

		CountSubscriber subscriber = new CountSubscriber();

		Subscription subscription = store.subscribe(subscriber);
		assertTrue(subscription.isSubscribed());

		dispatcher.dispatch(new AddAction(10));

		assertEquals(10, store.getSum());

		assertEquals(1, subscriber.getCount());

		subscription.remove();
		assertFalse(subscription.isSubscribed());

		assertEquals(1, subscriber.getCount());
	}

}
