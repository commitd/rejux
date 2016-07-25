# rejux

[![Build Status](https://travis-ci.org/commitd/rejux.svg?branch=master)](https://travis-ci.org/commitd/rejux)

A remix of [Redux](http://redux.js.org/) for Java.

We really like Redux, and [Flux](https://facebook.github.io/flux/) in general, as it makes your application state very easy to reasonable. All your meaningful application state exists in one shared place and that helps decouple components from the data they require.  We aren't going to introduce Redux and Flux here again because they are very well documented on their own sites.

Typically you use the Flux/Redux pattern for UI - specially redux itself is a Javascript library - but we've found it useful for any project where you have a series of components which need to react to global state. As an example we have a web server application with an API to controlling services. We keep the state of the controlled services in a Redux store. How does that help? It means that each API call can reason about what they can and can't do based on the global state. What's the alternative? Each API call would need to ask other services what state they are in and try and piece the puzzle together themselves - that's a lot of duplicated code and spread out logic.

Since we don't tend to use Javascript outside the browser, we recreated our beloved Redux in Java.

But we've added a few Java twists, since Java isn't Javascript! 

## Install 

Install though Maven with:

```
<dependency>
  <groupId>software.committed</groupId>
  <artifactId>rejux</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Rejux's interpretation of Redux

Rejux has the same primitives as Redux (paraphrasing a lot here, please read the original docs):

* **Actions** - an intent to do something which may change the state
* **Store** - holds state and allows actions to be dispatched
* **Reducers** - pure functions which manipulate state based on actions.
* **Middleware** - preprocess actions prior to reducers (allowing actions to be discarded, changed, or processed independently)

We have Java interfaces for these concepts which align very closely with Redux concepts. One of the differences is that we also extend from the Java 8 functionals (`Consumer<>`, etc) which makes stream based code much neater.

So in Rejux Java (see below for more details):

* **Actions** are any `Object` instance (ie you can dispatch an object, there is not specific Action interface to derive from). 
* **Reducers** are as in Redux, but we use a little bit of reflection to make them neater.
* **Store** is now divided into a `Dispatcher` (interface) and the actual state. Since most applications have non-trival state we jump straight to a 'combining reducer' style approach where we have many states which are combined into a single global (application) state. This, we hope, offers a flexible, type safe and hopefully not tedious way to create global state.
* **Middleware** is very much like in Redux. We have two places to put middleware which we'll explain in a moment.

We are now going to talk through each of these in detail. We'll illustrate with a simple example based on a basic calculator. Our operations (eg "Add 2")  will be actions and the current result will be kept in a part of the store.

## Actions

Actions are really simple.


```
// Zero the result
public class ZeroAction {

}

// Add the value to the result
public class AddAction {
  private final int amount;
  public AddAction(int amount) {
    this.value = amount;
  }
  
  public int getAmount() {
    return amount;
  }
}
```

We suggest you use [Lombok](https://projectlombok.org/), so that last class becomes:

```
@Data
public class AddAction {
  private final int amount;
}
```

which is much shorter and nicer.

To keep things clean and reduce code duplication, we can use inheritance (perhaps used a little over the top here, but it's just an example):

```
public interface CalculationAction {

}

@Data
public abstract class SingleValueAction implements CalculationAction{
  private final int amount;
}

@Data
@EqualsAndHashcode(callSuper=true)
public class AddAction extends SingleValueAction {

}

@Data
@EqualsAndHashcode(callSuper=true)
public class SubtractAction extends SingleValueAction {

}

@Data
@EqualsAndHashcode(callSuper=true)
public class MutiplyAction extends SingleValueAction {

}
```

That's actions done. Easy as you can hope for right?

## State

State in Rejux is just a java object. Ideally its immutable but that's your choice - but seriously make it [immutable](https://github.com/andrewoma/dexx).

Here's a bit of state which will store the current result of calculation.

```
@Data
public class Result {
     private final int value;
}
```

## Reducer

Our reducer needs to apply the actions to that state, so we could do this:

```
public class ResultReducer implements Reducer<Result> {

        // Sorry - we need this because of Java type erasure...
        public Class<Result> getType() {
                return Result.class;
        }

        public class Result reduce(Result state, Object action) {
                if(action instanceof AddAction) {
                        return new Result(state.getValue() + ((AddAction)action).getAmount());
                } else if(action instanceof ZeroAction) {
                        return new Result(0);
                }
                // No change if we didn't understand the action
                return state;
        }

}
```

But, lets be honest, that `instanceof` is ugly.... so we have a neater reflection based implementation from `ReflectingReducer<>` too:

```
public class ResultReducer extends ReflectingReducer<Result> {

        public ResultReducer() {
                // Sorry - we need this because of Java type erasure...
                super(Result.class);
        }

        public class Result add(Result state, AddAction action) {                
                return new Result(state.getValue() + action.getAmount());
        }

        public class Result zero(Result state, ZeroAction action) {
                return new Result(0);
        }

}
```
That's done the boring work for us!

## Store 

Stores and state are a bit different to Redux. That's simply because we want to take advantage of the type safety of Java.

Since Rejux doesn't know about your applications state you need to define it. You do this in an interface:

```
public interface CalculatorState {
        @Reduce(ResultReducer.class)
        Result result();
}
```

In that one interface you've defined your global state (defined by all the little state classes) and specified which reducer to use.

Now you've defined your global state, how do you turn it into a store and how do you supply the initial values for the state?

```
public Store<CalculatorState> newCalculatorState() {
        
        CalculatorState initialState = new CalculatorState() {
                public Result result() {
                        return new Result(0);
                }

        };

        return Rejux.createStore(CalculatorState.class, initialState);
}
```

You read that right, it's neat - we provide the initial state via the (type safe) interface which is the application state.

The `Store<>` interface has `state()` (or `get()`) to get the state and it also has `dispatch(action)` and `subscribe()` to monitor changes.

Lets use subscribe to listen to changes in the store, with a quick  example:

```
Store<CalculatorState> store = newCalculatorStore();
Subscription subs = store.subscribe((state) -> {
        System.out.println("The store's state changed");
        System.out.println("Result = " + state.result().getValue());
});

// Dispatch an action or two - these will hit our subscriber above:
store.dispatch(new AddAction(10));
store.dispatch(new ZeroAction(0));

// Now we are done

if(subs.isSubscribed()) {
  // You don't need to check but we are just showing the Subscription API
  subs.remove();
}

```

There's another trick for your application state: subscribable state.

In the above example we subscribed to the store. That's ok, but perhaps we are only interested in part of the store, we might want to just monitor that (in a UI component). That's a really good idea in Java as it means you can build reuseable components that are interested in only small parts of the state (ie not dependent on the whole store) 

Let's add something to the store to keep track of the history of the calculation.

What would our history state class be? Let be really basic and say a list of say Strings which say `"+2"`. We want a it to be an immutable list (because that's the right way to do state) so we'll use [Dexx](https://github.com/andrewoma/dexx). Our state is `LinkedList<String>`.

Our reducer now looks like:

```
public class HistoryReducer extends ReflectingReducer<LinkedList<String>> {

        public ResultReducer() {
                // Sorry - we need this because of Java type erasure...
                super(LinkedList.class);
        }

        public class LinkedList<String> add(LinkedList<String> state, AddAction action) {                
                return state.put("+" + action.getAmount()");
        }

        public class LinkedList<String> zero(Result state, ZeroAction action) {
                // clear the history too
                return new LinkedList<String>();
        }
}
```

Now we enhance our global CalculatorState. Since we want to be able to list just the history we use the `State<>` class as a return type. Just like `Store<>` this has `state()` and `subscribe()` functions (but not `dispatch`).

```
public interface CalculatorState {
        @Reduce(ResultReducer.class)
        Result result();
        
      @Reduce(HistoryReducer.class)
      State<LInkedList<String>> history();       
}
```

Finally how to do create that store again? 

```
public Store<CalculatorState> newCalculatorState() {
        
        CalculatorState initialState = new CalculatorState() {
                public Result result() {
                        return new Result(0);
                }

                public State<LInkedList<String>> history() {
                        return Rejux.createState(LinkedList.class, new LinkedList<String>());
                }

        };

        return Rejux.createStore(CalculatorState.class, initialState);
}
```

And now lets use it by just subscribing to history state changes:
```

Store<CalculatorState> store = newCalculatorStore();
Subscription subs = store.state().history().subscribe((state) -> {
        // NOte that state is of type LinkedList<String
        System.out.println("How did we get here");
        state.forEach( s -> System.out.println("\t"+s));
});
store.dispatch(new AddAction(10));

```

## Middleware

You can apply middleware in two places: to the global store and to each individual state. You add it as as part of the `Rejux.createStore` and `Rejux.createState` methods as we'll need in a moment.

We have two pieces of middleware built into the library:

* Filter - provides a simple middleware that allows actions to be discarded. 
* Thunk - provides very useful async like redux-thunk.

Where you apply which middleware is specific to your application. Typically middleware like thunk will be applied to the global store (`Rejux.createStore`) so its only run once for each action, whereas the filter middlware would likely be applied at the state level (`Rejux.createState`) so its applied to just that state / reducer combination.

We don't really have any use of these in out calculator, but lets pretend we do and add some middleware:

```
public Store<CalculatorState> newCalculatorState() {
        
        CalculatorState initialState = new CalculatorState() {
                public State<Result> result() {
                        return Rejux.createState(Result.class, new Result(0), new FilterMiddleware((a) -> a instanceof CalculatorAction));
                }

        };

        return Rejux.createStore(CalculatorState.class, initialState, new ThunkMiddlware());
}
```

So we've filtered out anything which isn't CalculatorAction type from our ResultReducer, and we've got thunk middleware running per action.

To test thunk lets reset the calculator if the value is already greater than 10. 

```
public class ResetIfTooBigAction implements ThunkAction<CalculatorState> {

        public void apply(Dispatcher first, CalculatorState state, Object action, Dispatcher next) {
            // We always allow the result to be zeroed!
            if(action instanceof ZeroResult) {
               next.dispatch(action);
            }
            
            if(state.result().value() < 10) {
              // We aren't too big, so I think the computer will be able to handle this
              next.dispatch(action);
            } else {
              // Oh dear, we are already over the number we can count on our fingers!
              // Let's zero things and stop this madness!
              first.dispatch(new ZeroResult());
            }
         
        }
}
```

Notice a slight different between Redux and Rejux: In Redux the first arguments are combined as store (Store = State + Dispatcher). We separate out so that the same middleware can be used in all location - the "first" dispatcher is the global store dispatcher (ie dispatch into the first part of the pipeline) and the state is either the global store or the sub state.

## Best practise

Based on our experience we've got some recommendations:

* Always use immutable state, you do not want to have bugs where state is accidentally changed outside of dispatch. Our suggestion is to use [Dexx](https://github.com/andrewoma/dexx) or [Immutables](http://immutables.github.io/).

* Think about threading. We've deliberately not dealt with that here because any implementation needs to be aware of the platform way of doing things (Android, Spring, etc). For example on Android we suggest you're subscribers runOnUiThread after dispatch. Perhaps add some middleware to ensure that?

* Create an interface in order to type your Action, so that you can logically group your actions together. Of course you can create type safe hierarchies too like we did.

* Your actions don't need a getPayload() just make them Java beans, its simpler and nice. Use [Lombok](https://projectlombok.org/) (or [Immutables](http://immutables.github.io/)) to keep your boilerplate way down.

## Alternatives

There are some alternative Java libraries to Rejux if to check out:

* [redux-java](https://github.com/glung/redux-java)
* [Bansa](https://github.com/brianegan/bansa)
* [Jedux](https://github.com/trikita/jedux)
