package mark.component.core.exception;

import mark.component.core.i18n.I18nMessage;
import mark.component.core.context.BaseContexts;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * 错误码工厂
 * 
 * @author liang
 *
 */
public class ErrorCodeFactory {

    private static final Map<Long, BaseErrorCode> allErrorCode = new HashMap<Long, BaseErrorCode>();

    public static final BaseErrorCode System_Success = new BaseErrorCode(0, "成功");//
    public static final BaseErrorCode System_Not_Found = new BaseErrorCode(404, "请求没有找到");//
    public static final BaseErrorCode System_Method_Not_Allowed = new BaseErrorCode(405, "请求方法不允许");//
    public static final BaseErrorCode System_General_Error = new BaseErrorCode(1000, "出错啦！请重试");//
    public static final BaseErrorCode System_Param_Exception = new BaseErrorCode(1001, "请求参数异常");//
    public static final BaseErrorCode System_Access_Denied = new BaseErrorCode(1002, "系统拒绝访问");// 未登录
    public static final BaseErrorCode System_Permission_Denied = new BaseErrorCode(1003, "系统拒绝访问");// 没有权限
    public static final BaseErrorCode System_Force_Exit = new BaseErrorCode(1004, "系统强制退出");//
    public static final BaseErrorCode System_Data_Sync_Exception = new BaseErrorCode(1005, "数据同步异常");//

    public static final BaseErrorCode Login_AuthCode_Error = new BaseErrorCode(1100, "验证码错误");//
    public static final BaseErrorCode Login_Password_Exception = new BaseErrorCode(1101, "密码异常");// 无法生成公私钥对、Session中找不到私钥、密码无法使用私钥解密
    public static final BaseErrorCode Login_Username_Error = new BaseErrorCode(1102, "用户不存在");//
    public static final BaseErrorCode Login_Password_Error = new BaseErrorCode(1103, "密码错误");//
    public static final BaseErrorCode Login_User_Pwd_Error = new BaseErrorCode(1104, "用户或密码错误");//
    public static final BaseErrorCode Login_User_Disabled = new BaseErrorCode(1105, "用户被禁用");//
    public static final BaseErrorCode Login_IP_Disabled = new BaseErrorCode(1106, "IP被禁止");//
    public static final BaseErrorCode Login_License_Expire = new BaseErrorCode(1107, "您的产品使用期限已到期，请联系管理员进行激活");//
    public static final BaseErrorCode Login_Cas_Exception = new BaseErrorCode(1108, "单点登录失败");//
    public static final BaseErrorCode Login_Password_Expired = new BaseErrorCode(1109, "密码已过期");//
    public static final BaseErrorCode Login_User_Expired = new BaseErrorCode(1110, "账号已过期");//

    public static final BaseErrorCode Asb_Service_Not_Found = new BaseErrorCode(2401, "无可用服务");//
    public static final BaseErrorCode Asb_App_Not_Specify = new BaseErrorCode(2402, "广播消息未指定实现者");//
    public static final BaseErrorCode Asb_Msg_Timeout = new BaseErrorCode(2403, "消息超时");//

    private static final Map<String, BaseErrorCode> insetErrorCode = new HashMap<String, BaseErrorCode>();
    static {
        insetErrorCode.put("system_success", System_Success);
        insetErrorCode.put("system_not_found", System_Not_Found);
        insetErrorCode.put("system_method_not_allowed", System_Method_Not_Allowed);
        insetErrorCode.put("system_general_error", System_General_Error);
        insetErrorCode.put("system_param_exception", System_Param_Exception);
        insetErrorCode.put("system_access_denied", System_Access_Denied);
        insetErrorCode.put("system_permission_denied", System_Permission_Denied);
        insetErrorCode.put("system_force_exit", System_Force_Exit);
        insetErrorCode.put("system_data_sync_exception", System_Data_Sync_Exception);

        insetErrorCode.put("login_authcode_error", Login_AuthCode_Error);
        insetErrorCode.put("login_password_exception", Login_Password_Exception);
        insetErrorCode.put("login_username_error", Login_Username_Error);
        insetErrorCode.put("login_password_error", Login_Password_Error);
        insetErrorCode.put("login_user_pwd_error", Login_User_Pwd_Error);
        insetErrorCode.put("login_user_disabled", Login_User_Disabled);
        insetErrorCode.put("login_ip_disabled", Login_IP_Disabled);
        insetErrorCode.put("login_license_expire", Login_License_Expire);
        insetErrorCode.put("login_cas_exception", Login_Cas_Exception);
        insetErrorCode.put("login_password_expired", Login_Password_Expired);
        insetErrorCode.put("login_user_expired", Login_User_Expired);

        insetErrorCode.put("asb_service_not_found", Asb_Service_Not_Found);
        insetErrorCode.put("asb_app_not_specify", Asb_App_Not_Specify);
        insetErrorCode.put("asb_msg_timeout", Asb_Msg_Timeout);
    }

    public static BaseErrorCode getErrorCode(long code, Object... params) {
        BaseErrorCode aec = allErrorCode.get(code);
        boolean isI18n = false;
        if (aec == null) {
            aec = ErrorCodeFactory.System_General_Error;
            // 在XML中未定义，继续在国际化文件中查找
            isI18n = true;
        } else {
            isI18n = aec.i18n;
        }
        aec = aec.clone();
        if (isI18n) {
            String msg = I18nMessage.getMessage(code + "", params);
            if (msg != null) {
                aec.setMsg(msg);
            }
        }
        aec.code = code;
        return aec;
    }

    public static void initErrorCode() throws Exception {
        String path = BaseContexts.ADQ_VAR.webRootPath + "/WEB-INF/etc/error-code.xml";
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setExpandEntityReferences(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        try (Reader reader = new InputStreamReader(FileUtils.openInputStream(file), "UTF-8")) {
            Document doc = builder.parse(new InputSource(reader));
            Element e = doc.getDocumentElement();
            NodeList errors = e.getChildNodes();
            if (errors != null && errors.getLength() != 0) {

                for (int i = 0; i < errors.getLength(); i++) {
                    Node n = errors.item(i);
                    if (n.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    if ("error".equalsIgnoreCase(n.getNodeName())) {
                        String codeStr = getAttrValue(n, "code");
                        if (StringUtils.isBlank(codeStr)) {
                            throw new Exception("错误码不能为空");
                        }
                        long code;
                        try {
                            code = Long.parseLong(codeStr);
                        } catch (NumberFormatException ee) {
                            throw new Exception("错误码必须是纯数字");
                        }
                        if (ErrorCodeFactory.allErrorCode.containsKey(code)) {
                            throw new Exception("错误码重复：" + code);
                        }
                        boolean i18n = Boolean.valueOf(getAttrValue(n, "i18n"));
                        String msg = getAttrValue(n, "msg");
                        if (StringUtils.isBlank(msg)) {
                            throw new Exception("错误描述不能为空");
                        }
                        int type = 1;
                        String typeStr = getAttrValue(n, "dialogType");
                        if (StringUtils.isNotBlank(typeStr)) {
                            try {
                                type = Integer.parseInt(typeStr);
                            } catch (NumberFormatException ee) {
                                throw new Exception("对话框类型只能是纯数字");
                            }
                        }
                        String title = getAttrValue(n, "dialogTitle");

                        BaseErrorCode aec;
                        BaseErrorCode insetError = insetErrorCode.get(getAttrValue(n, "id"));
                        if (insetError != null) {
                            insetError.code = code;
                            insetError.i18n = i18n;
                            insetError.msg = msg;
                            insetError.dialogType = type;
                            insetError.dialogTitle = title;
                            aec = insetError;
                        } else {
                            aec = new BaseErrorCode(code, i18n, msg, type, title);
                        }
                        ErrorCodeFactory.allErrorCode.put(code, aec);
                    }
                }
            }
        }
    }

    private static String getAttrValue(Node node, String attr) {
        NamedNodeMap map = node.getAttributes();
        if (map != null) {
            Node id = map.getNamedItem(attr);
            if (id != null) {
                return id.getTextContent();
            }
        }
        return null;
    }
}
