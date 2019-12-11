package mark.component.core.exception;

/**
 * 通用业务异常
 * 
 * @author liang
 *
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private long code;
    private String msg;
    private int dialogType;// 默认为1，1：Tips提示（3秒自动消失），2：固定标题的弹出框（点确定按钮才消失），3：自定义标题的弹出框（点确定按钮才消失）
    private String dialogTitle;
    private Object result;// 该结果会返回给前端

    public BaseException(String msg) {
        super(msg);
        this.code = ErrorCodeFactory.System_General_Error.code;
        this.msg = msg;
        this.dialogType = ErrorCodeFactory.System_General_Error.dialogType;
        this.dialogTitle = ErrorCodeFactory.System_General_Error.dialogTitle;
    }

    public BaseException(String msg, Throwable ex) {
        super(msg, ex);
        this.code = ErrorCodeFactory.System_General_Error.code;
        this.msg = msg;
        this.dialogType = ErrorCodeFactory.System_General_Error.dialogType;
        this.dialogTitle = ErrorCodeFactory.System_General_Error.dialogTitle;
    }

    public BaseException(Throwable ex) {
        super(ex);
        this.code = ErrorCodeFactory.System_General_Error.code;
        this.msg = ErrorCodeFactory.System_General_Error.msg;
        this.dialogType = ErrorCodeFactory.System_General_Error.dialogType;
        this.dialogTitle = ErrorCodeFactory.System_General_Error.dialogTitle;
    }

    BaseException(long code, String msg, int dialogType, String dialogTitle) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.dialogType = dialogType;
        this.dialogTitle = dialogTitle;
    }

    BaseException(long code, String msg, int dialogType, String dialogTitle, Throwable ex) {
        super(ex);
        this.code = code;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
