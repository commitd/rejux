package software.committed.rejux;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import software.committed.rejux.impl.AbstractReducer;
import software.committed.rejux.impl.SimpleStore;
import software.committed.rejux.interfaces.Action;
import software.committed.rejux.interfaces.Dispatcher;
import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;

public class RejuxFullApiTest {

	public static class ArthimeticStore {

		private final SimpleStore<SumState> sum;

		public ArthimeticStore() {
			sum = Rejux.createStore(new SumState(0), new SumReducer());
		}

		public Subscription subscribe(Subscriber<SumState> subscriber) {
			return sum.subscribe(subscriber);
		}

		public SimpleStore<SumState> getSum() {
			return sum;
		}

		public int getSumValue() {
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
		assertEquals(0, store.getSumValue());
	}

	@Test
	public void canCreateDispatch() {
		assertNotNull(Rejux.createSuperStore(new ArthimeticStore()));
	}

	@Test
	public void canDispatchAction() {
		ArthimeticStore store = new ArthimeticStore();
		Dispatcher dispatcher = Rejux.createSuperStore(store);
		assertEquals(0, store.getSumValue());
		dispatcher.dispatch(new AddAction(10));
		assertEquals(10, store.getSumValue());
	}

	@Test
	public void canSubscribe() {
		ArthimeticStore store = new ArthimeticStore();
		Dispatcher dispatcher = Rejux.createSuperStore(store);
		assertEquals(0, store.getSumValue());

		CountSubscriber subscriber = new CountSubscriber();

		Subscription subscription = store.subscribe(subscriber);
		assertTrue(subscription.isSubscribed());

		dispatcher.dispatch(new AddAction(10));

		assertEquals(10, store.getSumValue());

		assertEquals(1, subscriber.getCount());

		subscription.remove();
		assertFalse(subscription.isSubscribed());

		assertEquals(1, subscriber.getCount());
	}

}
