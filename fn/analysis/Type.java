package analysis;

import analysis.ConstType;

public class Type {
    public ConstType t;
    public Type t1;
    public Type t2;

    public Type(ConstType t){
        this.t = t;
    }

    public Type(Type t1, Type t2){
        this.t1 = t1;
        this.t2 = t2;
    }

    public boolean isArrow(){
        return t == null;
    }
}
