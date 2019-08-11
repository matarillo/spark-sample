public class Pagenation {
    private final boolean hasNext;
    private final boolean hasPrev;
    private final int next;
    private final int prev;

    public Pagenation(int page, int width, int count) {
        hasNext = (page + 1) * width < count;
        hasPrev = page > 0;
        next = page + ((hasNext) ? 1 : 0);
        prev = page - ((hasPrev) ? 1 : 0);
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public boolean getHasPrev() {
        return hasPrev;
    };

    public int getNext() {
        return next;
    }

    public int getPrev() {
        return prev;
    }
}