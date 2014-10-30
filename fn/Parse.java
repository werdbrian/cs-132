import parser.Fn;
import parser.ParseException;
import syntaxtree.Prog;
import analysis.PrintVisitor;
import analysis.TypeVisitor;
import analysis.Type;
import java.util.HashMap;

class Parse {
    public static void main(String args[]){
        tree();
    }

    static void tree() {
        Fn fn = new Fn(System.in);

        try {
            Prog p = Fn.Prog();
            TypeVisitor visitor = new TypeVisitor();
            HashMap<String, Type> typeEnv= new HashMap<String, Type>();

            System.out.println(p.accept(visitor, typeEnv));
        } catch (ParseException e){
            System.out.println(e.toString());
        }
    }
}
