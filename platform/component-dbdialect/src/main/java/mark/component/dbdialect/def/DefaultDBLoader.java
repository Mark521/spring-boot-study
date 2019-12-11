/*
 * 
//*******************************************************     

//**任何人或组织未经授权，不能修改代码     
//*******************************************************       
 */
package mark.component.dbdialect.def;
 
import mark.component.dbdialect.DBLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 类名: DefaultDBLoader.java
 * 创建者 >
 * 时间: 2013-4-20, 11:01:40
 */
public abstract class DefaultDBLoader implements DBLoader {

    //命令文件对应的文件名
    protected String batchFile;
    //命令文件流
    protected FileOutputStream batchCommandFile = null;
    //产生的可执行的命令
//    protected String executableCommand = "";

    @Override
    public void overProcess() throws Exception {
        if (batchCommandFile != null) {
            try {
                batchCommandFile.close();
            } catch (IOException ex) {
                throw new Exception(ex);
            }
            File file = new File(batchFile);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public String getDbloadError() throws Exception {
        return "";
    }
}
