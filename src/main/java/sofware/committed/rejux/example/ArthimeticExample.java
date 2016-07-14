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

		public ArthimeticStore() {
			sum = new Store<>(new SumState(0), new SumReducer());
		}

		public Store<SumState> getSum() {
			return sum;
		}
	}

	public static class SumState {
		private final int sum;

		public SumState(int sum) {
			this.sum = sum;
		}

		public int getSum() {
			return sum;
		}
	}

	public static class SumReducer extends AbstractReducer<SumState> {

		public SumReducer() {
			super(SumState.class);
		}

		public SumState add(SumState state, AddAction action) {
			return new SumState(state.getSum() + action.getAmount());
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

		System.out.println(store.getSum().getState().getSum());

		Subscription subscription = store.getSum()
				.subscribe((s) -> System.out.println("Subscriber says: " + s.getSum()));

		dispatcher.dispatch(new AddAction(10));

		dispatcher.dispatch(new AddAction(40));

		System.out.println(store.getSum().getState().getSum());

		subscription.remove();

		dispatcher.dispatch(new AddAction(50));

		System.out.println("(Subscriber should be quiet)");

		System.out.println(store.getSum().getState().getSum());

	}

}
