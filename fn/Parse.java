import parser.Fn;
import

class Parse {
    public static void main(String args[]){
        dump();
        eval();
    }

    static void dump(){
        Fn fn = new Fn(System.in);

        System.out.println( fn.getNextToken() );
        System.out.println( fn.getNextToken() );
        System.out.println( fn.getNextToken() );
        System.out.println( fn.getNextToken() );
    }

    static void eval(){
        // TODO
    }
}
