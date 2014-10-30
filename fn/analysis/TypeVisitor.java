package analysis;

import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.HashMap;
import java.util.Vector;
import analysis.Type;
import analysis.ConstType;
import java.util.Enumeration;

public class TypeVisitor
    extends GJDepthFirst<Vector<Type>, HashMap<String, Type>> {

    Vector<Type> buildType(Type t){
        Vector<Type> typeVec = new Vector<Type>();
        typeVec.add(t);
        return typeVec;
    }

    Vector<Type> buildType(ConstType t){
        return buildType(new Type(t));
    }


    /**
     * f0 -> <N>
     */
    public Vector<Type> visit(Int n, HashMap env) {
        return buildType(ConstType.INTEGER);
    }

    /**
     * f0 -> <VAR>
     */
    public Vector<Type> visit(Var n, HashMap<String, Type> env){
        String varname = n.f0.toString();

        if( env.containsKey(varname) ){
            return buildType(env.get(varname));
        } else {
            return buildType(ConstType.NOPE);
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
    public Vector<Type> visit(Abs n, HashMap env){
        // TODO allow arrow types
        env.put(n.f3.toString(), new Type(ConstType.INTEGER));

        return n.f6.accept(this, env);
    }

    /**
     * f0 -> ( "(" Expr() ")" RApp() )?
     *       | Expr()
     */
    public Vector<Type> visit(RApp n, HashMap env){
        // NOTE returns null if the optional bit is not there (epsilon)
        return n.f0.accept(this, env);
    }


    /**
     * f0 -> Abs() RApp()
     *       | Int() RApp()
     *       | Var() RApp()
     */
    public Vector<Type> visit(Expr n, HashMap env) {
        Type first, second;

        // If the Var is not in the env
        return buildType(ConstType.NOPE);
    }

    public Vector<Type> visit(NodeSequence n, HashMap env) {
        Vector<Type> result = new Vector<Type>();

        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            result.addAll(e.nextElement().accept(this, env));
        }

        return result;
    }
}
