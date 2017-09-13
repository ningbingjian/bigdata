package arrayinit;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Created by ning on 2017/9/8.
 * User:ning
 * Date:2017/9/8
 * tIME:14:38
 */
public class Test {
    public static void main(String[] args) throws Exception{
        ANTLRInputStream stream = new ANTLRInputStream(System.in);
        ArrayInitLexer lexer = new ArrayInitLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArrayInitParser parser = new ArrayInitParser(tokens);
        ParseTree tree = parser.init();
        System.out.println(tree.toStringTree(parser));




    }
}
