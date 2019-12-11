package mark.component.core.i18n;

import mark.component.core.context.BaseContexts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

public class I18nMessage {

    private static final Log log = LogFactory.getLog(I18nMessage.class);

    private static MessageSource messageSource = BaseContexts.ADQ_VAR.appCtx.getBean(MessageSource.class);

    public static String getMessage(String key, Object... params) {
        return getMessage(Locale.getDefault(), key, params);
    }

    public static String getMessage(Locale locale, String key, Object... params) {
        try {
            return messageSource.getMessage(key, params, locale);
        } catch (NoSuchMessageException e) {
            log.warn("未知的国际化Key", e);
            return null;
        }
    }

}
