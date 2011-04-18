package home.lang;

import java.util.Iterator;
import java.util.List;

public class HomeUtils {
    protected HomeUtils() {}
    public static <E> List<E> it2list(Iterator<E> it, List<E> list) {
        while(it.hasNext()) list.add(it.next());
        return list;
    }
}
