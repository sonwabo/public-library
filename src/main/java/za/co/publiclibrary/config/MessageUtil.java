package za.co.publiclibrary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/28
 */

@Component
@RequiredArgsConstructor
public class MessageUtil
{
    private final MessageSource messageSource;
    public String getMessage(final String key, final Object... args)
    {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

    public enum ExceptionMsgKey
    {
        LIBRARY_NOT_FOUND("ex.library.not.found"),
        BOOK_NOT_FOUND("ex.book.not.found.message"),
        BOOK_NOT_ASSC_WITH_LIB("ex.book.not.assc.with.lib");

        private final String value;

        ExceptionMsgKey(final String value)
        {
            this.value = value;
        }

        public String value(){
            return this.value;
        }
    }
}