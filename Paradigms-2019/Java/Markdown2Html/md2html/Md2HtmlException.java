package md2html;

public class Md2HtmlException extends Throwable {

    Md2HtmlException(final String message) {
        super(message);
    }

    static Md2HtmlException error(final String format, final Object... args) {
        return new Md2HtmlException(String.format(format, args));
    }
}
