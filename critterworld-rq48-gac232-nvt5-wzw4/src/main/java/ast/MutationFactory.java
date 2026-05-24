package ast;

import ast.mutations.*;
import cms.util.maybe.Maybe;

/** A factory that produces the Mutation objects corresponding to each mutation */
public class MutationFactory {

    /** Returns a Remove mutation object */
    public static Mutation getRemove() {
        return new Remove();
    }

    /** Returns a Swap mutation object */
    public static Mutation getSwap() {
        return new Swap();
    }

    /** Returns a Replace mutation object */
    public static Mutation getReplace() {
        return new Replace();
    }

    /** Returns a Transform mutation object */
    public static Mutation getTransform() {
        return new Transform();
    }

    /** Returns an Insert mutation object */
    public static Mutation getInsert() {
        return new Insert();
    }

    /** Returns a Duplicate mutation object */
    public static Mutation getDuplicate() {
        return new Duplicate();
    }
}
