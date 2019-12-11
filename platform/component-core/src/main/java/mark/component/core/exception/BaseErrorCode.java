package mark.component.core.exception;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 错误码对象
 * 
 * @author liang
 *
 */
public class BaseErrorCode implements Cloneable {

    public long code;
    public boolean i18n;// 该错误码是否支持国际化
    public String msg;// 错误描述
    public int dialogType;// 默认为1，1：Tips提示（3秒自动消失），2：固定标题的弹出框（点确定按钮才消失），3：自定义标题的弹出框（点确定按钮才消失）
    public String dialogTitle;

    public BaseErrorCode(long code, String msg) {
        this.code = code;
        this.i18n = false;
        this.msg = msg;
        this.dialogType = 1;
        this.dialogTitle = null;
    }

    public BaseErrorCode(long code, boolean i18n, String msg, int dialogType, String dialogTitle) {
        this.code = code;
        this.i18n = i18n;
        this.msg = msg;
        this.dialogType = dialogType;
        this.dialogTitle = dialogTitle;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public boolean isI18n() {
        return i18n;
    }

    public void setI18n(boolean i18n) {
        this.i18n = i18n;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    @Override
    public String toString() {
        return "code:" + code + ",msg:" + msg + ",dialogType:" + dialogType + ",dialogTitle:" + dialogTitle;
    }

    @Override
    public BaseErrorCode clone() {
        BaseErrorCode o;
        try {
            o = (BaseErrorCode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
        return o;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(code);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        BaseErrorCode aec = (BaseErrorCode) obj;
        return code == aec.code;
    }

    public BaseException newException() {
        return new BaseException(code, msg, dialogType, dialogTitle);
    }

    public BaseException newException(Throwable ex) {
        return new BaseException(code, msg, dialogType, dialogTitle, ex);
    }
}
