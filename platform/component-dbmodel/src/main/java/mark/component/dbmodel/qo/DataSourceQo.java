package mark.component.dbmodel.qo;

import mark.component.dbmodel.constans.DatabaseType;

public class DataSourceQo {

    private DatabaseType dbType;
    private String jdbcURL;
    private String userName;
    private String passwd;
    private String schema;
    private String tableName;
    /**
     * 是否分页
     */
    private boolean isPaging;
    private Integer page = 1;
    private Integer rows = 10;

    /**
     * 查询信息
     * @return
     */
    public boolean validateTable(boolean isTable){
        return true;
    }


    public DatabaseType getDbType() {
        return dbType;
    }

    public void setDbType(DatabaseType dbType) {
        this.dbType = dbType;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isPaging() {
        return isPaging;
    }

    public void setPaging(boolean paging) {
        isPaging = paging;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getOffset(){
        if(this.page< 1){
            this.page = 1;
        }
        if(this.rows < 0 || this.rows > 1001){
            this.rows = 10;
        }
        return (this.page-1) * this.rows;
    }
    public Integer getLimit(){
        return this.page * this.rows;
    }
}
