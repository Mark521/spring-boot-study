/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbmodel.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 类名: SQLLoaderInfo.java
 * 创建者 >
 * 时间: 2013-4-20, 9:58:11
 */
public class SQLLoaderInfo implements Serializable {
    
    //数据库服务主机IP
    private String host;
    //数据库服务端口
    private short port;
    //数据库服务名
    private String dbName;
    //数据库用户名
    private String userName;
    //数据库密码
    private String password;
    //要导入的表
    private Table table;
    //数据文件
    private String dataFileName;
    //文件的列分隔符
    private String termStr = ",";
    //文件行分隔符
    private String lineTermStr = "\\n";
    //单字符串分隔符
    private String enclosedStr = "\"";
    //字符集
    private String charaterSet;
    //是否为客户端本地文件，否，则为服务器文件
    private boolean isLocal;
    //跳过的行数，如一般csv文件含头信息，一般将这一行信息跳过
    private int skipLines;
    //字段
    private List<Column> fieldList;
    //字段格式
    private Map<String, String> dateFormats;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public short getPort() {
        return port;
    }

    public void setPort(short port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getDataFileName() {
        return dataFileName;
    }

    public void setDataFileName(String dataFileName) {
        this.dataFileName = dataFileName;
    }

    public String getTermStr() {
        return termStr;
    }

    public void setTermStr(String termStr) {
        this.termStr = termStr;
    }

    public String getLineTermStr() {
        return lineTermStr;
    }

    public void setLineTermStr(String lineTermStr) {
        this.lineTermStr = lineTermStr;
    }

    public String getEnclosedStr() {
        return enclosedStr;
    }

    public void setEnclosedStr(String enclosedStr) {
        this.enclosedStr = enclosedStr;
    }

    public String getCharaterSet() {
        return charaterSet;
    }

    public void setCharaterSet(String charaterSet) {
        this.charaterSet = charaterSet;
    }

    public boolean isIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(int skipLines) {
        this.skipLines = skipLines;
    }

    public List<Column> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Column> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, String> getDateFormats() {
        return dateFormats;
    }

    public void setDateFormats(Map<String, String> dateFormats) {
        this.dateFormats = dateFormats;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
