package sparksample.model;

import lombok.Data;

@Data
public class Pagenation {

    private final boolean hasNext;
    private final boolean hasPrev;
    private final int next;
    private final int prev;
    
    public Pagenation(int page, int pageWidth, int count) {
        hasNext = (page + 1) * pageWidth < count;
        hasPrev = page > 0;
        next = page + ((hasNext) ? 1 : 0);
        prev = page - ((hasPrev) ? 1 : 0);
    }

}
