package software.committed.rejux;

import org.junit.Assert;
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

    public Sum(final int sum) {
      this.sum = sum;
    }

    public int getValue() {
      return sum;
    }
  }

  public static class Count {

    private final int count;

    public Count(final int count) {
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

    public Sum add(final Sum state, final AddAction action) {
      return new Sum(state.getValue() + action.getAmount());
    }

  }

  public static class CountSubscriber implements Subscriber<ArthimeticState> {

    private int count;

    @Override
    public void onStateChanged(final ArthimeticState state) {
      count++;
    }

    public int getCount() {
      return count;
    }
  }

  public static class AddAction {

    private final int amount;

    public AddAction(final int amount) {
      this.amount = amount;
    }

    public int getAmount() {
      return amount;
    }

  }

  @Test
  public void canDispatchAction() {
    final ArthimeticState initial = () -> Rejux.createState(Sum.class, new Sum(0));
    final Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

    Assert.assertNotNull(store);
    Assert.assertEquals(0, store.get().getSum().get().getValue());
    store.dispatch(new AddAction(10));
    Assert.assertEquals(10, store.get().getSum().get().getValue());
  }

  @Test
  public void canSubscribe() {
    final ArthimeticState initial = () -> Rejux.createState(Sum.class, new Sum(0));
    final Store<ArthimeticState> store = Rejux.createStore(ArthimeticState.class, initial);

    Assert.assertNotNull(store);
    Assert.assertEquals(0, store.get().getSum().get().getValue());
    store.dispatch(new AddAction(10));

    final CountSubscriber subscriber = new CountSubscriber();
    Assert.assertEquals(0, subscriber.getCount());

    final Subscription subscription = store.subscribe(subscriber);
    Assert.assertTrue(subscription.isSubscribed());
    store.dispatch(new AddAction(20));

    Assert.assertEquals(30, store.get().getSum().get().getValue());

    Assert.assertEquals(1, subscriber.getCount());

    subscription.remove();
    Assert.assertFalse(subscription.isSubscribed());
    store.dispatch(new AddAction(40));

    Assert.assertEquals(1, subscriber.getCount());
    Assert.assertEquals(70, store.get().getSum().get().getValue());

  }

}
