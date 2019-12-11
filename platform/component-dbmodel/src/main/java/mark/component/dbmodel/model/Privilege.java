package mark.component.dbmodel.model;

import mark.component.dbmodel.constans.DatabaseObject;
import mark.component.dbmodel.util.Utility;

import java.io.Serializable;
import java.util.*;

public final class Privilege<P extends DatabaseObject>
        extends AbstractDependantObject<P> {

    private final class PrivilegeGrant
            implements Serializable, Comparable<PrivilegeGrant> {

        private static final long serialVersionUID = 356151825191631484L;
        private final String grantor;
        private final String grantee;
        private final boolean isGrantable;

        public PrivilegeGrant(final String grantor,
                final String grantee,
                final boolean isGrantable) {
            this.grantor = grantor;
            this.grantee = grantee;
            this.isGrantable = isGrantable;
        }

        public int compareTo(final PrivilegeGrant otherGrant) {
            int compare = 0;
            if (compare == 0) {
                compare = grantor.compareTo(otherGrant.getGrantor());
            }
            if (compare == 0) {
                compare = grantee.compareTo(otherGrant.getGrantee());
            }
            return compare;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PrivilegeGrant other = (PrivilegeGrant) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (grantee == null) {
                if (other.grantee != null) {
                    return false;
                }
            } else if (!grantee.equals(other.grantee)) {
                return false;
            }
            if (grantor == null) {
                if (other.grantor != null) {
                    return false;
                }
            } else if (!grantor.equals(other.grantor)) {
                return false;
            }
            if (isGrantable != other.isGrantable) {
                return false;
            }
            return true;
        }

        public String getGrantee() {
            return grantee;
        }

        public String getGrantor() {
            return grantor;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + (grantee == null ? 0 : grantee.hashCode());
            result = prime * result + (grantor == null ? 0 : grantor.hashCode());
            result = prime * result + (isGrantable ? 1231 : 1237);
            return result;
        }

        public boolean isGrantable() {
            return isGrantable;
        }

        private Privilege<P> getOuterType() {
            return Privilege.this;
        }
    }
    private final Set<PrivilegeGrant> grants = new HashSet<PrivilegeGrant>();
    private static final long serialVersionUID = -1117664231494271886L;

    public Privilege(final P parent, final String name) {
        super(parent, name);
    }

    public Collection<PrivilegeGrant> getGrants() {
        final List<PrivilegeGrant> values = new ArrayList<PrivilegeGrant>(grants);
        Collections.sort(values);
        return values;
    }

    public void addGrant(final String grantor,
            final String grantee,
            final boolean isGrantable) {
        if (!Utility.isBlank(grantor) && !Utility.isBlank(grantee)) {
            grants.add(new PrivilegeGrant(grantor, grantee, isGrantable));
        }
    }
}
