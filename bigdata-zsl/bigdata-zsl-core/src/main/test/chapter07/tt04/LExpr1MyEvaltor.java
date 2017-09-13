package chapter07.tt04;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Created by ning on 2017/9/12.
 * User:ning
 * Date:2017/9/12
 * tIME:14:29
 */
public class LExpr1MyEvaltor {
    public static class Listener extends LExpr1BaseListener{
        /** maps nodes to integers with Map<ParseTree,Integer> */
        ParseTreeProperty<Integer> values = new ParseTreeProperty<Integer>();

        @Override
        public void exitS(LExpr1Parser.SContext ctx) {
            setValue(ctx, getValue(ctx.e())); // like: int s() { return e(); }
        }

        @Override
        public void exitAdd(LExpr1Parser.AddContext ctx) {
            int left = getValue(ctx.e(0));
            int right = getValue(ctx.e(1));
            values.put(ctx,left + right);
        }

        @Override
        public void exitMult(LExpr1Parser.MultContext ctx) {
            int left = getValue(ctx.e(0));
            int right = getValue(ctx.e(1));
            values.put(ctx,left * right);
        }

        @Override
        public void exitInt(LExpr1Parser.IntContext ctx) {
            setValue(ctx,Integer.valueOf(ctx.INT().getText()));
        }
        public void setValue(ParseTree node,int value){
            values.put(node,value);
        }
        public int getValue(ParseTree node){
            return values.get(node);
        }
    }
    public static void main(String[] args) throws Exception{
        CharStream        is     = CharStreams.fromStream(System.in);
        LExpr1Lexer       lexer  = new LExpr1Lexer(is);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LExpr1Parser      parser = new LExpr1Parser(tokens);
        ParseTree         tree   = parser.s();
        ParseTreeWalker walker = new ParseTreeWalker();
        System.out.println(tree.toStringTree(parser));
        Listener listener = new Listener();
        walker.walk(listener,tree);
        System.out.println("result is :" + listener.getValue(tree));
    }
}
