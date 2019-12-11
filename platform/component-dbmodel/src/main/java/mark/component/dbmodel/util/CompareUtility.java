package mark.component.dbmodel.util;

import java.util.Iterator;
import java.util.List;

public class CompareUtility {

    public static <T extends Comparable<? super T>> int compareLists(final List<? extends T> list1,
            final List<? extends T> list2) {

        if (list1 == null) {
            return -1;
        }
        if (list2 == null) {
            return 1;
        }

        int comparison = list1.size() - list2.size();

        if (comparison == 0) {
            final Iterator<? extends T> iter1 = list1.iterator();
            final Iterator<? extends T> iter2 = list2.iterator();

            while (comparison == 0 && iter1.hasNext() && iter2.hasNext()) {
                final T object1 = iter1.next();
                final T object2 = iter2.next();

                comparison = object1.compareTo(object2);
            }
        }

        return comparison;
    }

    private CompareUtility() {
    }
}
