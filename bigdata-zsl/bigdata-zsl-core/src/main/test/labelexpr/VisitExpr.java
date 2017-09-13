package labelexpr;

import java.util.HashMap;

/**
 * Created by ning on 2017/9/8.
 * User:ning
 * Date:2017/9/8
 * tIME:17:41
 */
public class VisitExpr extends LabelExprBaseVisitor<Integer>{
    private java.util.Map<String,Integer> memory = new HashMap<>();
    @Override
    public Integer visitAssign(LabelExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = visit(ctx.expr());
        memory.put(id,value);
        return value;
    }

    @Override
    public Integer visitPrintExpr(LabelExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr());
        if(value != null){
            System.out.println(value);
        }
        return 0 ;
    }

    @Override
    public Integer visitInt(LabelExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(LabelExprParser.IdContext ctx) {
        String id = ctx.ID().getText();
        if(memory.containsKey(id)){
            return memory.get(id);
        }else{
            System.out.println("not define for name:" + id);
        }
        return null ;

    }

    @Override
    public Integer visitMulDiv(LabelExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if(ctx.op.getType() == LabelExprParser.MUL) return left * right;
        return left / right;

    }

    @Override
    public Integer visitAddSub(LabelExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));  // get value of left subexpression
        int right = visit(ctx.expr(1)); // get value of right subexpression
        if ( ctx.op.getType() == LabelExprParser.ADD ) return left + right;
        return left - right; // must be SUB
    }

    @Override
    public Integer visitParents(LabelExprParser.ParentsContext ctx) {
        return visit(ctx.expr()); // return child expr's value
    }

    @Override
    public Integer visitClear(LabelExprParser.ClearContext ctx) {
        memory.clear();
        return 0 ;
    }
}
