package pwnee.util;

/** A tuple containing two elements. */
public class Pair<A, B> {
  /** The first item in the pair. */
  public A _1;
  
  /** The second item in the pair. */
  public B _2;
  
  public Pair(A first, B second) {
    _1 = first;
    _2 = second;
  }
}
