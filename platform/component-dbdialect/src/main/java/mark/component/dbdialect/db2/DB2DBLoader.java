package mark.component.dbdialect.db2;

import mark.component.dbdialect.def.DefaultDBLoader;
import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.SQLLoaderInfo;
import mark.component.core.exception.BaseException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author liang>
 */
public class DB2DBLoader extends DefaultDBLoader {

    private static String[] charList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    @Override
    public boolean supportCommand() {
        return true;
    }
    
    @Override
    public String generateCommand(SQLLoaderInfo loaderInfo) throws BaseException {
        String dataFileName = loaderInfo.getDataFileName();

        String msgLog = dataFileName + ".msg";
        String termHex = "0x" + Integer.toHexString(loaderInfo.getTermStr().charAt(0));
        String enclosedHex = "0x" + Integer.toHexString(loaderInfo.getEnclosedStr().charAt(0));
        batchFile = dataFileName + ".bat";

        StringBuilder command = new StringBuilder();
        command.append("db2 catalog tcpip node ");
        //String nodeName = intToString(connectionInfo.getHost(), connectionInfo.getPort()).toLowerCase();
        String nodeName = intToString(getRandomNode());
        String dbName = intToString(getRandomNode());
        command.append(nodeName);
        command.append(" remote ");
        command.append(loaderInfo.getHost());
        command.append(" server ");
        command.append(loaderInfo.getPort());
        command.append("\ndb2 catalog database ");
        command.append(loaderInfo.getDbName());
        command.append(" as ");
        command.append(dbName);
        command.append(" at node ");
        command.append(nodeName);
        command.append("\ndb2 connect to ");
        command.append(dbName);
        command.append(" user ");
        command.append(loaderInfo.getUserName());
        command.append(" using ");
        command.append(loaderInfo.getPassword());
        command.append("\ndb2 \"import from '");
        command.append(dataFileName);
        command.append("' of del modified by chardel");
        command.append(enclosedHex);
        command.append(" coldel");
        command.append(termHex);
        command.append(" compound=100 commitcount 10000 restartcount ");
        command.append(loaderInfo.getSkipLines());
        command.append(" messages '");
        command.append(msgLog);
        command.append("' insert into ");
        command.append(loaderInfo.getTable().getName());

        List<Column> fieldList = loaderInfo.getFieldList();
        if (fieldList != null && !fieldList.isEmpty()) {
            command.append("(");
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
        command.append("\"");
        command.append("\ndb2 connect reset");
        command.append("\ndb2 quit");

        try {
            batchCommandFile = new FileOutputStream(batchFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DB2DBLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            batchCommandFile.write(command.toString().getBytes());
        } catch (IOException ex) {
            Logger.getLogger(DB2DBLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            batchCommandFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DB2DBLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        String osStr = System.getProperty("os.name").toLowerCase();
        String returnStr;
        if (osStr.matches("^.*window.*$")) {
            returnStr = "db2cmd -c " + batchFile;
        } else {
            returnStr = "sh " + batchFile;
        }

        return returnStr;
    }

    private String getRandomNode() {
        String currentTime = System.currentTimeMillis() + "";
        return currentTime.substring(currentTime.length() - 8);
    }

    private static String intToString(String intStr) {
        String str = intStr.replaceAll("\\.", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < str.length(); i++) {
            j = Integer.parseInt(String.valueOf(str.charAt(i)));
            sb.append(charList[j]);
        }
        return sb.toString().substring(0, 8);
    }

    @Override
    public void overProcess() throws BaseException {
        if (batchCommandFile != null) {
            try {
                batchCommandFile.close();
            } catch (IOException ex) {
                throw new BaseException("",ex);
            }
        }
    }
}
