package software.committed.rejux.example;

import software.committed.rejux.Rejux;
import software.committed.rejux.annotations.ActionReducer;
import software.committed.rejux.annotations.Reduce;
import software.committed.rejux.impl.ReflectingReducer;
import software.committed.rejux.interfaces.Store;
import software.committed.rejux.interfaces.SubscribableState;
import software.committed.rejux.interfaces.Subscription;

public class ArthimeticExample {

	public interface ArthimeticState {
		@Reduce(SumReducer.class)
		SubscribableState<Sum> getSum();

		@Reduce(CountReducer.class)
		Count getCount();
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

		@ActionReducer
		public Sum add(Sum state, AddAction action) {
			return new Sum(state.getValue() + action.getAmount());
		}

	}

	public static class CountReducer extends ReflectingReducer<Count> {

		public CountReducer() {
			super(Count.class);
		}

		@ActionReducer
		public Count add(Count state, AddAction action) {
			return new Count(state.getValue() + 1);
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

		ArthimeticState initial = new ArthimeticState() {

			@Override
			public SubscribableState<Sum> getSum() {
				return Rejux.createState(Sum.class, new Sum(0));
			}

			@Override
			public Count getCount() {
				return new Count(0);
			}
		};

		Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

		System.out.println(store.get().getSum().get().getValue());

		Subscription subscription = store.get().getSum()
				.subscribe((s) -> System.out.println("State Subscriber says: " + s.getValue()));

		System.out.println();
		store.dispatch(new AddAction(10));
		System.out.println(store.get().getSum().get().getValue());
		System.out.println(store.get().getCount().getValue());

		System.out.println();
		store.dispatch(new AddAction(40));
		System.out.println(store.get().getSum().get().getValue());
		System.out.println(store.get().getCount().getValue());

		subscription.remove();

		System.out.println();
		store.dispatch(new AddAction(50));
		System.out.println(store.get().getSum().get().getValue());
		System.out.println(store.get().getCount().getValue());

		System.out.println("(Subscriber should be quiet now)");
		System.out.println(store.get().getSum().get().getValue());

		Subscription storeSubscription = store
				.subscribe(
						(s) -> System.out.println("Store Subscriber says: " + store.get().getSum().get().getValue()));

		System.out.println();
		store.dispatch(new AddAction(100));
		System.out.println("(Store Subscriber should fire)");

		storeSubscription.remove();

	}

}
