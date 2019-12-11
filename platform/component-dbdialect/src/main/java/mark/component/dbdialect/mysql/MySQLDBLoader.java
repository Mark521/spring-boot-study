package mark.component.dbdialect.mysql;

import mark.component.dbdialect.def.DefaultDBLoader;
import mark.component.core.exception.BaseException;
import mark.component.dbmodel.model.Column;
import mark.component.dbmodel.model.SQLLoaderInfo;

import java.util.List;

/**
 * @author liang>
 */
public class MySQLDBLoader extends DefaultDBLoader {

    public boolean supportCommand() {
        return false;
    }

    @Override
    public String generateCommand(SQLLoaderInfo loaderInfo) throws BaseException {
        StringBuilder command = new StringBuilder();
        command.append("load data ");
        if (loaderInfo.isIsLocal()) {
            command.append("local ");
        }
        command.append("infile '");
        command.append(loaderInfo.getDataFileName());
        command.append("' into table ");
        command.append(loaderInfo.getTable().getName());

        String charaterSet = loaderInfo.getCharaterSet();
        if (charaterSet != null && !charaterSet.isEmpty()) {
            command.append("\nCHARACTER SET ");
            command.append(charaterSet);
        }

        command.append("\n fields terminated by '");
        command.append(loaderInfo.getTermStr());
        command.append("' ENCLOSED BY '");
        command.append(loaderInfo.getEnclosedStr());
        command.append("' ESCAPED BY '");
        command.append(loaderInfo.getEnclosedStr());
        command.append("'\n LINES TERMINATED BY '");
        command.append(loaderInfo.getLineTermStr());
        command.append("' ");
        command.append("IGNORE ");
        command.append(loaderInfo.getSkipLines());
        command.append(" LINES");

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

        return command.toString();
    }

//    @Override
//    public void execCommand(Connection conn) throws AdqException {
//        if (executableCommand != null && !executableCommand.isEmpty()) {
//            Statement statement = null;
//            try {
//                statement = conn.createStatement();
//                statement.execute(executableCommand);
//
//            } catch (SQLException ex) {
//                throw new AdqException(ex, "");
//            } finally {
//                try {
//                    statement.close();
//                } catch (SQLException ex) {
//                    throw new AdqException(ex, "");
//                }
//            }
//        }
//    }
}
