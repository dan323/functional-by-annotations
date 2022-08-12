****TO DO****
1. ~~Allow Classes to implement functors on another class. Example ZipList implements Functor<List<?>>~~
2. Add Applicative with `pure(A a):F<A>` and `fmap(F<Function<B,C>> fmapping, F<B> fbase): F<C>`
3. Add Monad with `bind(Function<A,F<B>> binding, F<A> base):F<B>` and `join(F<F<B>> ffbase):F<B>`
4. Add Tests for all examples
   1. ~~Functors~~
   2. Monads
   3. Applicatives
5. Add functions that make use of functors, monads or applicatives