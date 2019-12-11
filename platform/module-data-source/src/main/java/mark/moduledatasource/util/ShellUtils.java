package mark.moduledatasource.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import mark.component.core.exception.BaseException;
import mark.component.core.model.Pair;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ShellUtils {

    private static Log log = LogFactory.getLog(ShellUtils.class);

    public static class ShellSession {
        private Session session;

        public ShellSession(Session session) {
            this.session = session;
        }

        public Session getSession() {
            return session;
        }

        public void close() {
            session.disconnect();
        }

        @Override
        public String toString() {
            if (session == null) {
                return "";
            }
            return "host:" + session.getHost() + "\tport:" + session.getPort() + "\tuser:" + session.getUserName();
        }
    }

    // 需要外层手动关闭会话
    public static ShellSession crateSession(String host, int port, String username, String password)
            throws BaseException {
        try {
            JSch jsch = new JSch();

            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(5000);
            session.connect();

            return new ShellSession(session);
        } catch (Exception e) {
            throw new BaseException("连接远程服务器失败", e);
        }
    }

    public static String execCmd(String host, int port, String username, String password, String cmd)
            throws BaseException {
        ShellSession session = null;
        try {
            session = crateSession(host, port, username, password);
            return execCmd(session, cmd);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // 只返回成功时的输出，不成功时抛异常
    public static String execCmd(ShellSession shellSession, String cmd) throws BaseException {
        Pair<String, String> pair = execCmdResult(shellSession, cmd);
        if (!"0".equals(pair.getKey())) {
            throw new BaseException("执行命令失败");
        }
        return pair.getValue();
    }

    public static Pair<String, String> execCmdResult(String host, int port, String username, String password,
            String cmd) throws BaseException {
        ShellSession session = null;
        try {
            session = crateSession(host, port, username, password);
            return execCmdResult(session, cmd);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static Pair<String, String> execCmdResult(ShellSession shellSession, String cmd) throws BaseException {
        log.debug(shellSession + "\t" + cmd);
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) shellSession.getSession().openChannel("exec");
            channel.setCommand(cmd + ";echo $?");
            InputStream inputStream = channel.getInputStream();
            channel.connect();

            String result = IOUtils.toString(inputStream, "UTF-8");
            if (StringUtils.isBlank(result) || !result.endsWith("\n")) {
                throw new BaseException("执行命令失败");
            }
            log.debug("命令执行结果:" + result);

            // 去除最后一个换行符
            result = result.substring(0, result.length() - 1);
            // 找到错误码之前的换行符
            int index = result.lastIndexOf('\n');
            // 错误码之前没有换行符，说明命令没有输出
            if (index == -1) {
                return new Pair<String, String>(result, "");
            }
            String code = result.substring(index + 1, result.length());
            result = result.substring(0, index);
            return new Pair<String, String>(code, result);
        } catch (Exception e) {
            throw new BaseException("执行命令失败", e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public static void testFtp(String host, int port, String username, String password, String protocol, String dir)
            throws BaseException {
        ShellSession session = null;
        try {
            session = crateSession(host, port, username, password);
            testFtp(session, protocol, dir);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static void testFtp(ShellSession shellSession, String protocol, String dir) throws BaseException {
        log.debug(shellSession + "\t" + dir);
        ChannelSftp channel = null;
        try {
            dir.replace("\\\\", "/").replace("\\", "/");
            if (!dir.endsWith("/")) {
                dir += "/";
            }
            channel = (ChannelSftp) shellSession.getSession().openChannel(protocol);
            if (channel == null) {
                throw new BaseException("连接文件服务器失败，协议和端口不匹配");
            }
            channel.connect();
            channel.cd(dir);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException("连接文件服务器失败", e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public static List<File> fileList(ShellSession shellSession, String protocol, String dir) throws BaseException {
        log.debug(shellSession + "\t" + dir);
        ChannelSftp channel = null;
        try {
            dir.replace("\\\\", "/").replace("\\", "/");
            if (!dir.endsWith("/")) {
                dir += "/";
            }
            channel = (ChannelSftp) shellSession.getSession().openChannel(protocol);
            channel.connect();
            List<File> list = new ArrayList<>();
            Vector<?> vector = channel.ls(dir);
            for (Object item : vector) {
                LsEntry entry = (LsEntry) item;
                list.add(new File(dir + entry.getFilename()));
            }
            return list;
        } catch (Exception e) {
            throw new BaseException("获取远程文件列表失败", e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        testFtp("172.16.2.3", 22, "root", "123456", "sftp", "/opt");
    }

}
