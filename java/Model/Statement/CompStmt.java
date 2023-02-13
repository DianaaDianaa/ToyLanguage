package Model.Statement;

import Exception.MyException;
import Model.ADT.MyIDict;
import Model.ADT.MyIStack;
import Model.ProgramState;
import Model.Type.Type;


public class CompStmt implements IStmt{
    IStmt first;
    IStmt snd;

    public CompStmt(IStmt first, IStmt snd)
    {
        this.first = first;
        this.snd = snd;
    }

    @Override
    public MyIDict<String, Type> typecheck(MyIDict<String, Type> typeEnv) throws MyException {
        //MyIDict<String,Type> typEnv1 = first.typecheck(typeEnv);
        //MyIDict<String,Type> typEnv2 = snd.typecheck(typEnv1);
        //return typEnv2;
        return snd.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public ProgramState execute(ProgramState state) throws Exception {
        MyIStack<IStmt> stack = state.getExeStack();
        stack.push(snd);
        stack.push(first);
        state.setExeStack(stack);
        return null;
    }

    @Override
    public String toString()
    {  return "("+first.toString() + ";" + snd.toString()+")";  }
}
