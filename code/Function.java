public class Function {
    private Function() {
        /* Static class */
    }

    public static <T1, T2, T3> F<T1, T3> compose(final F<T2, T3> f,
                                                 final F<T1, T2> g) {
        F<T1, T3> h = new F<T1, T3>() {
            public T3 apply(T1 x) {
                return f.apply(g.apply(x));
            }
        };
    }
}
