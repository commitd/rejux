package sofware.committed.rejux.example;

import sofware.committed.rejux.Action;
import sofware.committed.rejux.Dispatcher;
import sofware.committed.rejux.Rejux;
import sofware.committed.rejux.Subscription;
import sofware.committed.rejux.impl.AbstractReducer;
import sofware.committed.rejux.impl.Store;

public class ArthimeticExample {

	public static class ArthimeticStore {

		private final Store<SumState> sum;

		private final Store<CountState> count;

		public ArthimeticStore() {
			sum = new Store<>(new SumState(0), new SumReducer());
			count = new Store<>(new CountState(0), new CountReducer());
		}

		public Store<SumState> getSum() {
			return sum;
		}

		public Store<CountState> getCount() {
			return count;
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

	public static class CountState {
		private final int count;

		public CountState(int count) {
			this.count = count;
		}

		public int getValue() {
			return count;
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

	public static class CountReducer extends AbstractReducer<CountState> {

		public CountReducer() {
			super(CountState.class);
		}

		public CountState add(CountState state, Action action) {
			return new CountState(state.getValue() + 1);
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

	public static void main(String[] args) {
		ArthimeticStore store = new ArthimeticStore();
		Dispatcher dispatcher = Rejux.create(store);

		System.out.println(store.getSum().getState().getValue());
		System.out.println(store.getCount().getState().getValue());

		Subscription subscription = store.getSum()
				.subscribe((s) -> System.out.println("Subscriber says: " + s.getValue()));

		System.out.println();
		dispatcher.dispatch(new AddAction(10));
		System.out.println(store.getSum().getState().getValue());
		System.out.println(store.getCount().getState().getValue());

		System.out.println();
		dispatcher.dispatch(new AddAction(40));
		System.out.println(store.getSum().getState().getValue());
		System.out.println(store.getCount().getState().getValue());

		subscription.remove();

		System.out.println();
		dispatcher.dispatch(new AddAction(50));
		System.out.println("(Subscriber should be quiet)");
		System.out.println(store.getSum().getState().getValue());
		System.out.println(store.getCount().getState().getValue());

	}

}
