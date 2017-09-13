package expr;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by ning on 2017/9/8.
 * User:ning
 * Date:2017/9/8
 * tIME:16:10
 */
public class ExprJoyRide {
    public static void main(String[] args)throws Exception {
        String inputFile = null;
        if ( args.length>0 ) inputFile = args[0];
        InputStream is = System.in;
        if ( inputFile!=null ) is = new FileInputStream(inputFile);
        CharStream        input  = CharStreams.fromStream(is);
        ExprLexer         lexer  = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser        parser = new ExprParser(tokens);
        ParseTree         tree   = parser.prog(); // parse; start at prog <label id="code.tour.main.6"/>
        System.out.println(tree.toStringTree(parser)); // print tree as text <label id="code.tour.main.7"/>
    }
}
