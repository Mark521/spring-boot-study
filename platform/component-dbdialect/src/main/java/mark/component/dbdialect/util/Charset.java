package mark.component.dbdialect.util;

/**
 * 类名称： Charset
 * 描述：
 * 
 * 创建：   liang, Nov 28, 2012 1:59:01 PM
 *
 * 修订历史：（降序）
 * BugID/BacklogID/描述  -  开发人员  日期
 * #  
 */
public class Charset {

    public static final int FLAG_COMMON = 0;//常用
    public static final int FLAG_UNCOMMON = 1;//过期
    /* MIBenum:Defined by and used in the Printer MIB [RFC-1759].*/
    private int mibEnum;
    /* 名称 */
    private String name;
    /* 显示名称 */
    private String displayName;
    /* 别名，逗号分割 */
    private String aliasNames;
    private int flag = FLAG_COMMON;

    public Charset(String name, int mibEnum, String aliasNames, int flag) {
        this(name, "charset_" + mibEnum, mibEnum, aliasNames, flag);
//        this(name, "", mibEnum, aliasNames);
    }

    public Charset(String name, String displayName, int mibEnum, String aliasNames, int flag) {
        this.name = name;
        this.displayName = displayName;
        this.mibEnum = mibEnum;
        this.aliasNames = aliasNames;
        this.flag = flag;
    }

    public String getAliasNames() {
        return aliasNames;
    }

    public void setAliasNames(String aliasNames) {
        this.aliasNames = aliasNames;
    }

    public int getMibEnum() {
        return mibEnum;
    }

    public void setMibEnum(int mibEnum) {
        this.mibEnum = mibEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return this.getDisplayName();
    }
}
