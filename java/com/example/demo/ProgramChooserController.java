package com.example.demo;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    private ProgramExecutorController programExecutorController;

    public void setProgramExecutorController(ProgramExecutorController programExecutorController) {
        this.programExecutorController = programExecutorController;
    }
    @FXML
    private ListView<IStmt> programsListView;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() {
        programsListView.setItems(getAllStatements());
        programsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void displayProgram(ActionEvent actionEvent) {
        IStmt selectedStatement = programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error encountered!");
            alert.setContentText("No statement selected!");
            alert.showAndWait();
        } else {
            int id = programsListView.getSelectionModel().getSelectedIndex();
            try {
                selectedStatement.typecheck(new MyDict<>());
                ProgramState programState = new ProgramState(new MyStack<>(), new MyDict<>(), new MyList<>(), selectedStatement, new MyDict<>(), new MyHeap());
                IRepo repository = new Repo(programState, "log" + (id + 1) + ".txt");
                Controller controller = new Controller(repository);
                programExecutorController.setController(controller);
            } catch (Exception exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error encountered!");
                alert.setContentText(exception.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    private ObservableList<IStmt> getAllStatements() {
        List<IStmt> allStatements = new ArrayList<>();

        IStmt ex1 = new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));
        allStatements.add(ex1);

        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                        new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))),
                                new PrintStmt(new VarExp("b"))))));
        allStatements.add(ex2);

        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))),
                        new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))),
                                new PrintStmt(new VarExp("v"))))));
        allStatements.add(ex3);

        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("C:/Users/danad/IdeaProjects/lab_3/src/View/test.in.txt"))),
                        new CompStmt(new OpenRFile(new VarExp("varf")),
                                new CompStmt(new VarDeclStmt("varc", new IntType()),
                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CompStmt(new ReadFile(new VarExp("varf"), "varc"),
                                                                new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                        new CloseRFile(new VarExp("varf"))))))))));
        allStatements.add(ex4);

        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));
        allStatements.add(ex5);

        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+',new ReadHeapExp(new ReadHeapExp(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));
        allStatements.add(ex6);

        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt( new PrintStmt(new ReadHeapExp(new VarExp("v"))),
                                new CompStmt(new WriteHeapStmt("v", new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new ReadHeapExp(new VarExp("v")), new ValueExp(new IntValue(5))))))));
        allStatements.add(ex7);

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
        allStatements.add(ex8);

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
        allStatements.add(ex9);

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
        allStatements.add(ex10);

        return FXCollections.observableArrayList(allStatements);
    }
}
