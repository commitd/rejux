package software.committed.rejux.interfaces;

public interface SubscribableState<S> extends State<S> {

  Subscription subscribe(Subscriber<S> subscriber);

  // TODO: In future support FluxState from reactor-core:
  // Flux<S> flux();
}
