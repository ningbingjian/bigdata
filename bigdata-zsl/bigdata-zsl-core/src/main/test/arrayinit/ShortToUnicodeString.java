package arrayinit;

/**
 * Created by ning on 2017/9/8.
 * User:ning
 * Date:2017/9/8
 * tIME:14:49
 */
public class ShortToUnicodeString extends ArrayInitBaseListener {
    @Override
    public void enterInit(ArrayInitParser.InitContext ctx) {
        System.out.print('"');
    }

    @Override
    public void exitInit(ArrayInitParser.InitContext ctx) {
        System.out.println('"');

    }

    @Override
    public void enterValue(ArrayInitParser.ValueContext ctx) {
        if(ctx.INT() != null)
        {
            int value = Integer.valueOf(ctx.INT().getText());
            System.out.printf("||u%04x",value);
        }else{
            enterInit(ctx.init());
        }

    }
}
