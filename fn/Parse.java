import parser.Fn;
import parser.ParseException;
import syntaxtree.Prog;
import analysis.PrintVisitor;

class Parse {
    public static void main(String args[]){
        tree();
        actions();
    }

    static void tree() {
        Fn fn = new Fn(System.in);

        try {
            Prog p = Fn.Prog();
            PrintVisitor v = new PrintVisitor();
            p.accept(v);
        } catch (ParseException e){
            System.out.println(e.toString());
        }
    }

    static void actions(){
        // try {
        //     Fn fn = new Fn(System.in);
        //     Fn.Prog();
        // } catch (ParseException e){
        //     System.out.println(e.toString());
        // }
    }
}
