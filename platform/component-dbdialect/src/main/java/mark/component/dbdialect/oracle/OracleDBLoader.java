/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbdialect.oracle;

import mark.component.dbdialect.def.DefaultDBLoader;
import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.SQLLoaderInfo;
import mark.component.core.exception.BaseException;

import java.io.*;
import java.sql.Types;
import java.util.List;

/**
 * 类名: OracleDBLoader.java
 * 创建者 >
 * 时间: 2013-4-20, 10:59:38
 */
public class OracleDBLoader extends DefaultDBLoader {

    private String dbloadLogFile = ""; //dbload命令执行日志文件
    
    public boolean supportCommand() {
        return true;
    }
    
    public String generateCommand(SQLLoaderInfo loaderInfo) throws BaseException {
        String dataFileName = loaderInfo.getDataFileName();
        batchFile = dataFileName + ".ctl";

        //生成命令之前，先写控制文件
        writeCtlFile(loaderInfo);
        //生成命令
        String connStr = loaderInfo.getUserName() + "/" + loaderInfo.getPassword() + "@"
                + loaderInfo.getHost() + ":" + loaderInfo.getPort() + "/" + loaderInfo.getDbName();

        StringBuilder command = new StringBuilder();
        command.append("sqlldr userid=");
        command.append(connStr);
        command.append(" control=");
        command.append(batchFile);
        command.append(" log=");
        command.append(dataFileName);
        command.append(".log");
        command.append(" bad=");
        command.append(dataFileName);
        command.append(".bad ");
        command.append("streamsize=10000000 date_cache=100000 COLUMNARRAYROWS=5000 "
                + "readsize=10000000 bindsize=500000 direct=true skip_index_maintenance=true "
                + "parallel=false rows=100000");

        dbloadLogFile = dataFileName + ".log";

        return command.toString();
    }

    private void writeCtlFile(SQLLoaderInfo loaderInfo) throws BaseException {
        StringBuilder writeData = new StringBuilder();
        //sqlldr命令中的可选
        writeData.append("OPTIONS(SKIP=");
        //文件头跳过的行数
        writeData.append(loaderInfo.getSkipLines());
        writeData.append(",ERRORS=0)\n");

        writeData.append("LOAD DATA\n");
        //指定字符集
        String characterSet = loaderInfo.getCharaterSet();
        if (characterSet != null) {
            writeData.append("CHARACTERSET ");
            writeData.append(characterSet);
            writeData.append("\n");
        }
        //指定的数据文件
        writeData.append("INFILE ");
        writeData.append("'");
        writeData.append(loaderInfo.getDataFileName());
        writeData.append("'\n");
        //指定导入方式
        writeData.append("APPEND");
        writeData.append("\n");
        //指定导入的表
        writeData.append("INTO TABLE ");
        writeData.append(loaderInfo.getTable().getName());
        writeData.append("\n");
        //指定条件
//        if (whenClause == null || whenClause.isEmpty() || whenClause.trim().isEmpty()) {
//        } else {
//            writeData.append("WHEN ");
//            writeData.append(whenClause);
//            writeData.append("\n");
//        }
        //指定分隔符，在固定文件输入时，文件分隔符可为空
        writeData.append("FIELDS TERMINATED BY '");
        writeData.append(loaderInfo.getTermStr());
        writeData.append("'\n");
        writeData.append("OPTIONALLY ENCLOSED BY '");
        writeData.append(loaderInfo.getEnclosedStr());
        writeData.append("'\n");
        writeData.append("TRAILING NULLCOLS\n");

        List<Column> columns = loaderInfo.getFieldList();
        //写字段部分
        if (columns != null && !columns.isEmpty()) {
            writeData.append("(");
            for (int i = 0; i < columns.size(); i++) {
                Column field = columns.get(i);
                //日期类型的要加上日期格式
                int dateType = field.getType().getJdbcType();
                if (dateType == Types.DATE
                        || dateType == Types.TIME
                        || dateType == Types.TIMESTAMP) {
                    writeData.append(field);
                    writeData.append(" date \"");
                    writeData.append(loaderInfo.getDateFormats().get(field.getName()));
                    writeData.append("\"");
                } else {
                    writeData.append(field);
                }

                if (i != columns.size() - 1) {
                    writeData.append(",\n");
                }
            }
            writeData.append(")");
        }

        try {
            batchCommandFile = new FileOutputStream(batchFile);
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            batchCommandFile.write(writeData.toString().getBytes());
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            batchCommandFile.close();
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
    }

//    @Override
//    public void execCommand(Connection conn) throws AdqException {
//        if (executableCommand != null && !executableCommand.isEmpty()) {
////            CallSysCommand.callSysCommand(executableCommand);
//        }
//    }

    @Override
    public String getDbloadError() throws BaseException {
        if (this.dbloadLogFile == null || this.dbloadLogFile.trim().isEmpty()) {
            return "";
        } else {
            File file = new File(this.dbloadLogFile);
            if (!file.canRead()) {
                return "";
            }
            BufferedReader br = null;
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                String error = null;
                while ((error = br.readLine()) != null) {
                    if (error.toUpperCase().indexOf("ORA-") >= 0) {
                        return error;
                    }
                }
                
                return error;
            } catch (IOException ex) {
                return "";
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        return "";
                    }
                }
            }
        }
    }
}
