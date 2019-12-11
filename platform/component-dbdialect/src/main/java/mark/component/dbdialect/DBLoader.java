package mark.component.dbdialect;

import mark.component.dbmodel.model.SQLLoaderInfo;
import mark.component.core.exception.BaseException;

/**
 *导入文本文件到数据库
 * @author liang>
 */
public interface DBLoader {

    //是否支持命令行
    public boolean supportCommand();

    /**
     * @param loaderInfo
     * @throws Exception
     */
    public String generateCommand(SQLLoaderInfo loaderInfo) throws BaseException;

    /**
     * @param command generateCommand步骤生成的命令，执行这条命令
     */
//    public void execCommand(Connection conn) throws Exception;
    /**
     * 命令执行完成后的相关处理，比如删除中间文件，垃圾回收等
     * @throws Exception 
     */
    public void overProcess() throws Exception;

    /**
     * 获得数据库装载错误信息
     * @return
     * @throws Exception 
     */
    public String getDbloadError() throws Exception;
}
