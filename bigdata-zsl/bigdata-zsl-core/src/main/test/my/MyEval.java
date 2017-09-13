package my;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTree ;

import java.io.InputStream;

/**
 * Created by ning on 2017/9/12.
 * User:ning
 * Date:2017/9/12
 * tIME:18:29
 */
public class MyEval {
    static class TestListener extends MyBaseListener{
        @Override
        public void exitSql(MyParser.SqlContext ctx) {
            System.out.println(ctx.getText());
        }
    }
    public static void main(String[] args) throws Exception {
        InputStream      is    = System.in;
        ANTLRInputStream input = new ANTLRInputStream(is);
        MyLexer myLexer = new MyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(myLexer);
        MyParser myParser  = new MyParser(tokens);
        ParseTree tree = myParser.init();
        ParseTreeWalker walker = new ParseTreeWalker();
        TestListener listener = new TestListener();
        walker.walk(listener,tree);

    }
}
