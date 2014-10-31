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
     * f0 -> Expr()
     * f1 -> <EOF>
     */
    public Vector<Type> visit(Prog n, HashMap env) {
        return n.f0.accept(this, env);
    }


   /**
    * f0 -> <FN>
    * f1 -> "("
    * f2 -> RType()
    * f3 -> <VAR>
    * f4 -> ")"
    * f5 -> "{"
    * f6 -> Expr()
    * f7 -> "}"
    */
    public Vector<Type> visit(Abs n, HashMap env){
        Vector<Type> argTypes = n.f2.accept(this, env);
        Type argType  = argTypes.elementAt(0);

        Vector<Type> exprTypes = n.f6.accept(this, env);
        Type exprType = exprTypes.elementAt(0);

        env.put(n.f3.toString(), argType);

        return buildType(new Type(argType, exprType));
    }

    /**
     * f0 -> TInt()
     * f1 -> ( <ARROW> RType() )?
     */
    public Vector<Type> visit(RType n, HashMap env) {
        Vector<Type> intType = n.f0.accept(this, env);
        Vector<Type> rType = n.f1.accept(this, env);

        if( rType.elementAt(0).isEmpty() ){
            return intType;
        }

        return buildType(new Type(intType.elementAt(0), rType.elementAt(0)));
    }

    /**
     * f0 -> <TINT>
     */
    public Vector<Type> visit(TInt n, HashMap env) {
        return buildType(ConstType.INTEGER);
    }

    /**
     * f0 -> ( "(" Expr() ")" RApp() )?
     */
    public Vector<Type> visit(RApp n, HashMap env){
        // NOTE returns null if the optional bit is not there (epsilon)
        Vector<Type> t = n.f0.accept(this, env);

        return n.f0.accept(this, env);
    }


    /**
     * f0 -> Abs() RApp()
     *       | Int()
     *       | Var() RApp()
     */
    public Vector<Type> visit(Expr n, HashMap env) {
        Type first, second;

        Vector<Type> list = n.f0.accept(this, env);

        first = list.elementAt(0);

        // if we're dealing with one of the RApp() cases
        if ( list.size() >= 2 ){
            second = list.elementAt(1);

            // if the left type is an arrow, which it better be
            if( first.isArrow() ){

                // the RApp() is empty just return the Abs() or Var() type
                if ( second.isEmpty() ) {
                    return buildType(first);
                }

                // check that the second type fits the first type of
                // the Abs() or Var() type, i.e. if t1 -> t2, t3 and t1 == t3
                if( first.t1.equals(second) ) {
                    return buildType(first.t2);
                }
            }

            // if the type of the first thing isn't t -> t
            // OR if the types of the param and the argument don't match
            return buildType(ConstType.NOPE);
        }

        // Int() only
        return buildType(first);
    }


    private Vector<Type> elementsToCollection(Enumeration<Node> e, HashMap env){
        Vector<Type> interim, result = new Vector<Type>();

        while ( e.hasMoreElements() ) {
            interim = e.nextElement().accept(this, env);

            if( interim != null ){
                result.addAll(interim);
            }
        }

        return result;
    }

    public Vector<Type> visit(NodeSequence n, HashMap env) {
        return elementsToCollection(n.elements(), env);
    }

    public Vector<Type> visit(NodeList n, HashMap env) {
        return elementsToCollection(n.elements(), env);
    }

    public Vector<Type> visit(NodeListOptional n, HashMap env) {
        if ( n.present() ) {
            return elementsToCollection(n.elements(), env);
        } else {
            return buildType(ConstType.EMPTY);
        }
    }

    public Vector<Type> visit(NodeOptional n, HashMap env) {
        if ( n.present() ) {
            Vector<Type> t = n.node.accept(this, env);

            return t;
        } else {
            return buildType(ConstType.EMPTY);
        }
    }

    public Vector<Type> visit(NodeToken n, HashMap env) {
        return null;
    }
}
