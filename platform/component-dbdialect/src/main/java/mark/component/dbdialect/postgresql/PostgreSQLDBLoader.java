package mark.component.dbdialect.postgresql;

import mark.component.dbdialect.def.DefaultDBLoader;
import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.SQLLoaderInfo;
import mark.component.core.exception.BaseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author liang>
 */
public class PostgreSQLDBLoader extends DefaultDBLoader {

    private String cmdFile;

    public boolean supportCommand() {
        return false;
    }
    
    @Override
    public String generateCommand(SQLLoaderInfo loaderInfo) throws BaseException {
        //指定导入的编码信息
        String charaterSet = loaderInfo.getCharaterSet();
        if (charaterSet == null || charaterSet.isEmpty() || charaterSet.trim().isEmpty()) {
            charaterSet = "gbk";
        }
        String connStr = "psql -h " + loaderInfo.getHost()
                + " -p " + loaderInfo.getPort()
                + " -U " + loaderInfo.getUserName()
                + " -w -d " + loaderInfo.getDbName() + " -f ";
        String dataFileName = loaderInfo.getDataFileName();
        batchFile = dataFileName + ".bat";
        StringBuilder command = new StringBuilder();
        //设置客户端的编码环境
        command.append("\n\\encoding ");
        command.append(charaterSet);
        command.append("\n");
//        CallSysCommand.callSysCommand(command1.toString());

        command.append("\\copy ");
        command.append(loaderInfo.getTable().getName());
        List<Column> fieldList = loaderInfo.getFieldList();
        if (fieldList != null && !fieldList.isEmpty()) {
            command.append(" (");
            for (int i = 0; i < fieldList.size(); i++) {
                if (i == fieldList.size() - 1) {
                    command.append(fieldList.get(i));
                    command.append(") ");
                } else {
                    command.append(fieldList.get(i));
                    command.append(",");
                }
            }
        }
        command.append(" from '");
        command.append(dataFileName);
        command.append("' WITH DELIMITER as '");
        command.append(loaderInfo.getTermStr());
        command.append("' null '' csv ");
        if (loaderInfo.getSkipLines() > 0) {
            command.append("header ");
        }
        command.append("escape '");
        command.append(loaderInfo.getEnclosedStr());
        command.append("'");

        try {
            batchCommandFile = new FileOutputStream(batchFile);
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            batchCommandFile.write(command.toString().getBytes());
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            batchCommandFile.close();
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        cmdFile = batchFile + ".bat";
        try {
            batchCommandFile = new FileOutputStream(cmdFile);
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            String osStr = System.getProperty("os.name").toLowerCase();
            if (osStr.matches("^.*window.*$")) {
                batchCommandFile.write(("set PGPASSWORD=" + loaderInfo.getPassword() + "\n").getBytes());
            } else {
                batchCommandFile.write(("PGPASSWORD=" + loaderInfo.getPassword() + "\n").getBytes());
            }

            //生成命令
            command = new StringBuilder();
            command.append(connStr);
            command.append("\"");
            command.append(batchFile);
            command.append("\"");
            batchCommandFile.write(command.toString().getBytes());
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }
        try {
            batchCommandFile.close();
        } catch (IOException ex) {
            throw new BaseException("", ex);
        }

        return command.toString();
    }

    @Override
    public void overProcess() throws BaseException {
        try {
            super.overProcess();
        } catch (Exception e) {
            throw new BaseException(e);
        }
        if (cmdFile != null && !cmdFile.isEmpty()) {
            File file = new File(cmdFile);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
