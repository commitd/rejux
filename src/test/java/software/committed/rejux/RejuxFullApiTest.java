package software.committed.rejux;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.impl.ReflectingReducer;
import software.committed.rejux.interfaces.Store;
import software.committed.rejux.interfaces.SubscribableState;
import software.committed.rejux.interfaces.Subscriber;
import software.committed.rejux.interfaces.Subscription;

public class RejuxFullApiTest {
	public interface ArthimeticState {
		@Reduce(SumReducer.class)
		SubscribableState<Sum> getSum();
	}

	// NOTE: This normally be an immutable (dexx or immutables)
	public static class Sum {

		private final int sum;

		public Sum(int sum) {
			this.sum = sum;
		}

		public int getValue() {
			return sum;
		}
	}

	public static class Count {

		private final int count;

		public Count(int count) {
			this.count = count;
		}

		public int getValue() {
			return count;
		}
	}

	public static class SumReducer extends ReflectingReducer<Sum> {

		public SumReducer() {
			super(Sum.class);
		}

		public Sum add(Sum state, AddAction action) {
			return new Sum(state.getValue() + action.getAmount());
		}

	}

	public static class CountSubscriber implements Subscriber<ArthimeticState> {

		private int count;

		@Override
		public void onStateChanged(ArthimeticState state) {
			count++;
		}

		public int getCount() {
			return count;
		}
	}

	public static class AddAction {

		private final int amount;

		public AddAction(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

	}

	@Test
	public void canDispatchAction() {
		ArthimeticState initial = () -> Rejux.createState(Sum.class, new Sum(0));
		Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

		assertNotNull(store);
		assertEquals(0, store.get().getSum().get().getValue());
		store.dispatch(new AddAction(10));
		assertEquals(10, store.get().getSum().get().getValue());
	}

	@Test
	public void canSubscribe() {
		ArthimeticState initial = () -> Rejux.createState(Sum.class, new Sum(0));
		Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

		assertNotNull(store);
		assertEquals(0, store.get().getSum().get().getValue());
		store.dispatch(new AddAction(10));

		CountSubscriber subscriber = new CountSubscriber();

		Subscription subscription = store.subscribe(subscriber);
		assertTrue(subscription.isSubscribed());
		store.dispatch(new AddAction(10));

		assertEquals(10, store.get().getSum().get().getValue());

		assertEquals(1, subscriber.getCount());

		subscription.remove();
		assertFalse(subscription.isSubscribed());
		store.dispatch(new AddAction(10));

		assertEquals(1, subscriber.getCount());
		assertEquals(20, store.get().getSum().get().getValue());

	}

}
