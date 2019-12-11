package mark.component.dbmodel.constans;

/**
 *
 * @author liang
 */
public interface RetrieveRule {

    public static final long EMPTY = 0;
    public static final long ALL = ~0L;
    public static final long DEFAULT = 1 << 1;
    public static final long CATALOG = 1 << 2;
    public static final long SCHEMA = 1 << 3;
    public static final long TABLE = 1 << 4;
    public static final long VIEW = 1 << 5;
    public static final long PROCEDURE = 1 << 6;
    public static final long FUNCTION = 1 << 7;
    public static final long SEQUENCE = 1 << 8;
    public static final long COLUMN = 1 << 9;
    public static final long TRIGER = 1 << 10;
    public static final long PRIVILEGE = 1 << 11;
    public static final long INDEX = 1 << 12;
    public static final long SYNONYM = 1 << 13;
    public static final long FORGIGN_KEY = 1 << 14;
    public static final long PRIMARY_KEY = 1 << 15;
    public static final long CONSTRAINT = 1 << 16;
    public static final long ATTRIBUTE = 1 << 17;
    public static final long REMARK = 1 << 18;
    public static final long REG_CATALOG = 1 << 19;
    public static final long REG_SCHEMA = 1 << 20;
    public static final long REG_TABLE = 1 << 21;
    public static final long REGEX = 1 << 22;
    public static final long QUOTED = 1 << 23;
}
