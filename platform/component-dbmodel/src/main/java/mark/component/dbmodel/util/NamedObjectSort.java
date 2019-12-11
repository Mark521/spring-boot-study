package mark.component.dbmodel.util;

import mark.component.dbmodel.constans.NamedObject;

import java.util.Comparator;

public enum NamedObjectSort
        implements Comparator<NamedObject> {

    /**
     * Alphabetical sort.
     */
    alphabetical {

        @Override
        public int compare(final NamedObject namedObject1,
                           final NamedObject namedObject2) {
            return Utility.convertForComparison(namedObject1.getFullName()).compareTo(Utility.convertForComparison(namedObject2.getFullName()));
        }
    },
    /**
     * Natural sort.
     */
    natural {

        @Override
        public int compare(final NamedObject namedObject1,
                           final NamedObject namedObject2) {
            return namedObject1.compareTo(namedObject2);
        }
    };

    public static NamedObjectSort getNamedObjectSort(final boolean alphabeticalSort) {
        if (alphabeticalSort) {
            return NamedObjectSort.alphabetical;
        } else {
            return NamedObjectSort.natural;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.util.Comparator#compare(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public abstract int compare(final NamedObject namedObject1,
                                final NamedObject namedObject2);
}