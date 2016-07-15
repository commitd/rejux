package software.committed.rejux.example;

import software.committed.rejux.Rejux;
import software.committed.rejux.annotations.ActionReducer;
import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.impl.ReflectingReducer;
import software.committed.rejux.interfaces.Store;

public class ArthimeticExample {

	public interface ArthimeticState {
		@Reduce(SumReducer.class)
		Sum getSum();
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

	public static class SumReducer extends ReflectingReducer<Sum> {

		public SumReducer() {
			super(Sum.class);
		}

		@ActionReducer
		public Sum add(Sum state, AddAction action) {
			return new Sum(state.getValue() + action.getAmount());
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

	public static void main(String[] args) {

		ArthimeticState initial = () -> new Sum(0);

		Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

		System.out.println(store.get().getSum().getValue());

		// System.out.println(store.get().getSum().get().getValue());
		//
		// Subscription subscription = store.get().getSum()
		// .subscribe((s) -> System.out.println("Subscriber says: " + s.getValue()));

		System.out.println();
		store.dispatch(new AddAction(10));
		System.out.println(store.get().getSum().getValue());

		// System.out.println(store.get().getSum().get().getValue());

		System.out.println();
		store.dispatch(new AddAction(40));
		System.out.println(store.get().getSum().getValue());

		// System.out.println(store.get().getSum().get().getValue());

		// subscription.remove();

		System.out.println();
		store.dispatch(new AddAction(50));
		System.out.println(store.get().getSum().getValue());

		System.out.println("(Subscriber should be quiet)");
		// System.out.println(store.get().getSum().get().getValue());

	}

}
