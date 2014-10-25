package analysis;

import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.HashMap;
import analysis.Type;

public class TypeVisitor
    extends GJDepthFirst<Type, HashMap<String, Type>> {
   /**
    * f0 -> <N>
    */
   public Type visit(Int n, HashMap env) {
       return Type.INTEGER;
   }

   /**
    * f0 -> <VAR>
    */
   public Type visit(Var n, HashMap<String, Type> env){
       String varname = n.f0.toString();

       if( env.containsKey(varname) ){
           return env.get(varname);
       } else {
           // maybe throw an exception?
           return Type.NOPE;
       }
   }

    /**
     * f0 -> <FN>
     * f1 -> "("
     * f2 -> <TINT>
     * f3 -> <VAR>
     * f4 -> ")"
     * f5 -> "{"
     * f6 -> Expr()
     * f7 -> "}"
     */
    public Type visit(Abs n, HashMap env){
        // we get to cheat here because the args can only be ints
        env.put(n.f3.toString(), Type.INTEGER);

        return n.f6.accept(this, env);
        // return Type.ABSTRACTION;
    }

   /**
    * f0 -> ( "(" Expr() ")" RApp() )?
    *       | Expr()
    */
   public Type visit(RApp n, HashMap env){
       // NOTE returns null if the optional bit is not there (epsilon)
       return n.f0.accept(this, env);
   }



   /**
    * f0 -> Abs() RApp()
    *       | Int() RApp()
    *       | Var() RApp()
    */
   public Type visit(Expr n, HashMap env) {
       Type first, second;

       // note we don't have access to n.f0.f1
       // requires some chicanery

       // FAILS
       // first = n.f0.choice.f0.accept(this, env);
       first = n.f0.accept(this, env);
       second = n.f0.accept(this, env);

       // integer?
       if( first == Type.INTEGER ){
           // check for empty RApp
           if ( second == null ){
               return Type.INTEGER;
           }

           // otherwise nope
           return Type.NOPE;
       }

       // abstraction
       if( first == Type.ABSTRACTION ){
           // ??

           return Type.INT || Type.ABSTRACTION;
       }

       // If the Var is not in the env
       return Type.NOPE;
   }

   public Type visit(NodeSequence n, HashMap env) {
       // if( env.get( "parent" ) == "expr" ){

       // }

       // for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
       //     e.nextElement().accept(this,argu);
       //     _count++;
       // }

       // return _ret;
   }
}
