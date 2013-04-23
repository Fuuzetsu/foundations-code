public final class Monoid<A> {
    private final F<A, F<A, A>> product;
    private final A unity;

    private Monoid(final F<A, F<A, A>> product, final A unity) {
        this.product = product;
        this.unity = unity;
    }

    public A product(final A x, final A y) {
        return product.apply(x).apply(y);
    }

    public F<A, A> product(final A x) {
        return product.apply(x);
    }

    public A unity() {
        return unity;
    }

    public A foldl(FList<A> xs) {
        return xs.foldl(product, unity);
    }

    public A foldr(FList<A> xs) {
        return xs.foldr(product, unity);
    }

    public A concat(FList<A> xs) {
        return this.foldr(xs);
    }

    public static <A> Monoid<A> monoid(final F<A, F<A, A>> product,
                                       final A unity) {
        return new Monoid<A>(product, unity);
    }
}
