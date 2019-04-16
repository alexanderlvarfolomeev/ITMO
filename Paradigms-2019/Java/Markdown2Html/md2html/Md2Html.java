package md2html;

public class Md2Html {
    public static void main(String[] args) {
        try (Md2HtmlParser parser = new Md2HtmlParser(args[0], args[1])) {
            parser.parse();
        } catch (Md2HtmlException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }
}