import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Usage {
    public static void main(String[] args) {
        F<Object, Void> print = new F<Object, Void>() {
            public Void apply(Object x) {
                System.out.println(x);
                return null;
            }
        };

        F<Integer, Integer> square = new F<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x * x;
            }
        };

        F<Integer, Integer> addFive = new F<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x + 5;
            }
        };

        F<Integer, Integer> timesThree = new F<Integer, Integer>() {
            public Integer apply(Integer x) {
                return x * 3;
            }
        };


        Monoid<Integer> intMon = Usage.intAddMonoid();
        Monoid<String> strMon = Usage.stringConcatMonoid();
        Monoid<F<Integer, Integer>> compMon = Usage.funcMonoid();
        Monoid<List<Integer>> listAppMonoid = Usage.listAppendMonoid();

        List<Integer> someInts = new LinkedList<Integer>();
        someInts.add(2);
        someInts.add(3);
        someInts.add(4);

        FList<Integer> integerList = new FList<Integer>(someInts);
        print.apply("Folding left: " + integerList);
        print.apply(intMon.foldl(integerList));
        print.apply("Folding right: " + integerList);
        print.apply(intMon.foldr(integerList));

        List<String> someStrings = new LinkedList<String>();
        someStrings.add("Why, ");
        someStrings.add("hello ");
        someStrings.add("world!");

        FList<String> stringList = new FList<String>(someStrings);
        print.apply("Folding left " + stringList);
        print.apply(strMon.foldl(stringList));
        print.apply("Folding right " + stringList);
        print.apply(strMon.foldr(stringList));

        List<F<Integer, Integer>> someFuncs =
            new LinkedList<F<Integer, Integer>>();
        someFuncs.add(square);
        someFuncs.add(addFive);
        someFuncs.add(timesThree);
        FList<F<Integer, Integer>> funcList
            = new FList<F<Integer, Integer>>(someFuncs);
        print.apply("Folding left [f x = x * x, f x = x + 5, f x = x * 3]");
        Integer x = compMon.foldl(funcList).apply(7);
        print.apply("Applying the result of the fold to 7: " + x);
        print.apply("Folding right [f x = x * x, f x = x + 5, f x = x * 3]");
        Integer y = compMon.foldr(funcList).apply(7);
        print.apply("Applying the result of the fold to 7: " + y);

        List<List<Integer>> someLists = new LinkedList<List<Integer>>();
        someLists.add(Arrays.asList(1, 3, 4));
        someLists.add(Arrays.asList(25, 7, 16));
        someLists.add(Arrays.asList(735, 17, 8));
        FList<List<Integer>> listList = new FList<List<Integer>>(someLists);
        print.apply("Folding left " + someLists);
        print.apply(listAppMonoid.foldl(listList));
        print.apply("Folding right " + someLists);
        print.apply(listAppMonoid.foldr(listList));

    }


    public static Monoid<Integer> intAddMonoid() {
        F<Integer, F<Integer, Integer>> add =
            new F<Integer, F<Integer, Integer>>() {
            public F<Integer, Integer> apply(final Integer x) {
                F<Integer, Integer> f = new F<Integer, Integer>() {
                    public Integer apply(final Integer y) {
                        return x + y;
                    }
                };
                return f;
            }
        };
        return Monoid.monoid(add, 0);
    }

    public static Monoid<String> stringConcatMonoid() {
        F<String, F<String, String>> concat =
            new F<String, F<String, String>>() {
            public F<String, String> apply(final String x) {
                F<String, String> f = new F<String, String>() {
                    public String apply(final String y) {
                        return x + y;
                    }
                };
                return f;
            }
        };
        return Monoid.monoid(concat, "");
    }

    public static <A> Monoid<List<A>> listAppendMonoid() {
        F<List<A>, F<List<A>, List<A>>> append =
            new F<List<A>, F<List<A>, List<A>>>() {
            public F<List<A>, List<A>> apply(final List<A> xs) {
                F<List<A>, List<A>> f = new F<List<A>, List<A>>() {
                    public List<A> apply(final List<A> ys) {
                        /* Make sure we don't mutate the original lists */
                        List<A> nl = new LinkedList<A>();
                        nl.addAll(xs);
                        nl.addAll(ys);
                        return nl;
                    }
                };
                return f;
            }
        };
        return Monoid.monoid(append, new LinkedList<A>());
    }


    public static <A> Monoid<F<A, A>> funcMonoid() {
        F<F<A, A>, F<F<A, A>, F<A, A>>> comp =
            new F<F<A, A>, F<F<A, A>, F<A, A>>>() {
            public F<F<A, A>, F<A, A>> apply(final F<A, A> f) {
                F<F<A, A>, F<A, A>> g = new F<F<A, A>, F<A, A>>() {
                    public F<A, A> apply(final F<A, A> h) {
                        return Function.compose(f, h);
                    }
                };
                return g;
            }
        };

        F<A, A> id = new F<A, A>() {
            public A apply(final A x) {
                return x;
            }
        };
        return Monoid.monoid(comp, id);
    }
}
