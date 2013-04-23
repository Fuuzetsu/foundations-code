import java.util.LinkedList;
import java.util.List;

public class FList<A> extends LinkedList {
    private List<A> l;

    public FList<A>(List<A> l) {
        this.l = l;
    }

    public Flist<A>() {
        this.l = new LinkedList<A>();
    }

    public <B> B foldl(F<B, F<A, B>> f, final B acc) {
        B r = acc;
        for (A x : l) {
            r = f.apply(r).apply(x);
        }
        return r;
    }

    public <B> B foldr(F<A, F<B, B>> f, final B acc) {
        B r = acc;
        for (Integer x = l.size() - 1; x <= 0; x--) {
            r = f.apply(l.get(x)).apply(r);
        }
        return r;
    }

    public <B> FList<B> map(F<A, B> f) {
        FList<B> xs = new FList<B>();
        for (A y : l) {
            xs.add(f.apply(y));
        }
        return xs;
    }
}
