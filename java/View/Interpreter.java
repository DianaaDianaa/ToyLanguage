package View;

import Controller.Controller;
import Model.ADT.MyDict;
import Model.ADT.MyHeap;
import Model.ADT.MyList;
import Model.ADT.MyStack;
import Model.Expression.*;
import Model.ProgramState;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Repository.IRepo;
import Repository.Repo;

public class Interpreter {
    public static void main(String[] args){

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));

        //int v; v=2;Print(v)
        IStmt ex1 = new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
        try {
            ex1.typecheck(new MyDict<>());
            ProgramState prg1 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex1, new MyDict<>(), new MyHeap());
            IRepo repo1 = new Repo(prg1,"log1.txt");
            Controller ctr1 = new Controller(repo1);
            menu.addCommand(new RunExampleCommand("1", ex1.toString(), ctr1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //int a;int b; a=2+3*5;b=a+1;Print(b)
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                        new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))),
                                new PrintStmt(new VarExp("b"))))));
        try {
            ex2.typecheck(new MyDict<>());
            ProgramState prg2 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex2, new MyDict<>(), new MyHeap());
            IRepo repo2 = new Repo(prg2,"log2.txt");
            Controller ctr2 = new Controller(repo2);
            menu.addCommand(new RunExampleCommand("2", ex2.toString(), ctr2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                        new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                new PrintStmt(new VarExp("v"))))));
        try {
            ex3.typecheck(new MyDict<>());
            ProgramState prg3 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex3, new MyDict<>(), new MyHeap());
            IRepo repo3 = new Repo(prg3,"log3.txt");
            Controller ctr3 = new Controller(repo3);
            menu.addCommand(new RunExampleCommand("3", ex3.toString(), ctr3));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        string varf;
        varf="test.in";
        openRFile(varf);
        int varc;
        readFile(varf,varc);
        print(varc);
        readFile(varf,varc);
        print(varc)
        closeRFile(varf)
        */
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("C:/Users/danad/IdeaProjects/lab_3/src/View/test.in.txt"))),
                        new CompStmt(new OpenRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))))))))));
        try {
            ex4.typecheck(new MyDict<>());
            ProgramState prg4 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex4, new MyDict<>(), new MyHeap());
            IRepo repo4 = new Repo(prg4, "log4.txt");
            Controller ctr4 = new Controller(repo4);
            menu.addCommand(new RunExampleCommand("4", ex4.toString(), ctr4));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        Ref int v;
        new(v,20);
        Ref Ref int a;
        new(a,v);
        print(v);
        print(a)
        */
        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));
        try {
            ex5.typecheck(new MyDict<>());
            ProgramState prg5 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex5, new MyDict<>(), new MyHeap());
            IRepo repo5 = new Repo(prg5, "log5.txt");
            Controller ctr5 = new Controller(repo5);
            menu.addCommand(new RunExampleCommand("5", ex5.toString(), ctr5));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        Ref int v;
        new(v,20);
        Ref Ref int a;
        new(a,v);
        print(rH(v));
        print(rH(rH(a))+5)
        */
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+',new ReadHeapExp(new ReadHeapExp(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));
        try {
            ex6.typecheck(new MyDict<>());
            ProgramState prg6 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex6, new MyDict<>(), new MyHeap());
            IRepo repo6 = new Repo(prg6, "log6.txt");
            Controller ctr6 = new Controller(repo6);
            menu.addCommand(new RunExampleCommand("6", ex6.toString(), ctr6));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        Ref int v;
        new(v,20);
        print(rH(v));
        wH(v,30);
        print(rH(v)+5);
        */
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt( new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5))))))));
        try {
            ex7.typecheck(new MyDict<>());
            ProgramState prg7 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex7, new MyDict<>(), new MyHeap());
            IRepo repo7 = new Repo(prg7, "log7.txt");
            Controller ctr7 = new Controller(repo7);
            menu.addCommand(new RunExampleCommand("7", ex7.toString(), ctr7));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        Ref int v;
        new(v,20);
        Ref Ref int a;
        new(a,v);
        new(v,30);
        print(rH(rH(a)))
        */
        IStmt declare_v = new VarDeclStmt("v", new RefType(new IntType()));
        IStmt alloc_v_1 = new NewStmt("v", new ValueExp(new IntValue(20)));
        IStmt declare_a = new VarDeclStmt("a", new RefType(new RefType(new IntType())));
        IStmt alloc_a = new NewStmt("a", new VarExp("v"));
        IStmt alloc_v_2 = new NewStmt("v", new ValueExp(new IntValue(30)));
        IExp read_a_1 = new ReadHeapExp(new VarExp("a"));
        IExp read_a_2 = new ReadHeapExp(read_a_1);
        IStmt print_a = new PrintStmt(read_a_2);
        IStmt ex8 = new CompStmt(declare_v, new CompStmt(alloc_v_1, new CompStmt(declare_a,
                new CompStmt(alloc_a, new CompStmt(alloc_v_2, print_a)))));
        try {
            ex8.typecheck(new MyDict<>());
            ProgramState prg8 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex8, new MyDict<>(), new MyHeap());
            IRepo repo8 = new Repo(prg8, "log8.txt");
            Controller ctr8 = new Controller(repo8);
            menu.addCommand(new RunExampleCommand("8", ex8.toString(), ctr8));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*
        int v;
        v=4;
        (while (v>0) print(v); v=v-1);
        print(v)
        */
        IStmt declare_v9 = new VarDeclStmt("v", new IntType());
        IStmt assign_v = new AssignStmt("v", new ValueExp(new IntValue(4)));
        IExp rel_expr = new RelationalExp(new VarExp("v"), new ValueExp(new IntValue(0)), ">");
        IStmt print_v_1 = new PrintStmt(new VarExp("v"));
        IExp arithmetic_v = new ArithExp('-', new VarExp("v"), new ValueExp(new IntValue(1)));
        IStmt assign_v_2 = new AssignStmt("v", arithmetic_v);
        IStmt compoundStatement_v = new CompStmt(print_v_1, assign_v_2);
        IStmt whileStatement_v = new WhileStmt(rel_expr, compoundStatement_v);
        IStmt print_v_2 = new PrintStmt(new VarExp("v"));
        IStmt ex9 = new CompStmt(declare_v9, new CompStmt(assign_v, new CompStmt(whileStatement_v, print_v_2)));
        try {
            ex9.typecheck(new MyDict<>());
            ProgramState prg9 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex9, new MyDict<>(), new MyHeap());
            IRepo repo9 = new Repo(prg9, "log9.txt");
            Controller ctr9 = new Controller(repo9);
            menu.addCommand(new RunExampleCommand("9", ex9.toString(), ctr9));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // int v; Ref int a; v=10; new(a,22); fork(wH(a,30);v=32;print(v);print(rH(a))); print(v);print(rH(a))
        IStmt declare_v_10 = new VarDeclStmt("v", new IntType());
        IStmt declare_a_10 = new VarDeclStmt("a", new RefType(new IntType()));
        IStmt assign_v_10 = new AssignStmt("v", new ValueExp(new IntValue(10)));
        IStmt alloc_a_10 = new NewStmt("a", new ValueExp(new IntValue(22)));
        IStmt write_a_10 = new WriteHeapStmt("a", new ValueExp(new IntValue(30)));
        IStmt assign_v_10_2 = new AssignStmt("v", new ValueExp(new IntValue(32)));
        IStmt print_v_10 = new PrintStmt(new VarExp("v"));
        IExp read_heap_a = new ReadHeapExp(new VarExp("a"));
        IStmt print_a_10 = new PrintStmt(read_heap_a);
        IStmt fork_10 = new ForkStmt(new CompStmt(write_a_10, new CompStmt(assign_v_10_2, new CompStmt(print_v_10, print_a_10))));
        IStmt ex10 = new CompStmt(declare_v_10, new CompStmt(declare_a_10, new CompStmt(assign_v_10, new CompStmt(alloc_a_10, new CompStmt(fork_10,
                new CompStmt(print_v_10, print_a_10))))));
        try {
            ex10.typecheck(new MyDict<>());
            ProgramState prg10 = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), ex10, new MyDict<>(), new MyHeap());
            IRepo repo10 = new Repo(prg10, "log10.txt");
            Controller ctr10 = new Controller(repo10);
            menu.addCommand(new RunExampleCommand("10", ex10.toString(), ctr10));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        menu.show();
    }
}
