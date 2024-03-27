package za.co.publiclibrary.exceptions;

/**
 * @author <a href="mailto:s.singatha@gmail.com">Sonwabo Singatha</a>
 * @version 1.0
 * @Date 2024/03/26
 */

public class LibraryNotFoundException extends Exception
{
    public LibraryNotFoundException(String message) {
        super(message);
    }

    public LibraryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryNotFoundException(Throwable cause) {
        super(cause);
    }
}
