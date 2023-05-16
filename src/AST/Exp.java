package AST;

import AST.Visitor.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public abstract class Exp extends ASTNode {
    public Symbols.Type type;
    public Exp(Location pos) {
        super(pos);
    }
    public abstract void accept(Visitor v);
}
