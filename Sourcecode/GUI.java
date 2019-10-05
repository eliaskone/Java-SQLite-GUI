package sample.net.sqlitetutorial;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GUI extends Application {
    TableView<sample.net.sqlitetutorial.DVDReturn> tableView = new TableView<>();
    int int1, int2;
    String str1, str2;
    @Override
    public void start(Stage primaryStage) {
        sample.net.sqlitetutorial.TableFunctions tableFunctions = new sample.net.sqlitetutorial.TableFunctions();
        Connection connection = tableFunctions.connect();
        TableFunctions.createNewTable();

        tableView.setEditable(true);

        TableColumn id_colum = new TableColumn("id");
        TableColumn titel_colum = new TableColumn("titel");
        TableColumn director_colum = new TableColumn("director");
        TableColumn year_colum = new TableColumn("year");

        Stage stage = primaryStage;
        HBox hBox = new HBox();

        Button buttonAdd = new Button("New entry");
        Button buttonDel = new Button("Delete entry");
        buttonAdd.setMinSize(100,50);
        buttonDel.setMinSize(100,50);
        VBox vBox = new VBox(buttonAdd, buttonDel);

        ResultSet DataresultSet = tableFunctions.getData(connection);

        tableView.getColumns().addAll(id_colum, titel_colum, director_colum, year_colum);
        ArrayList <sample.net.sqlitetutorial.DVDReturn> dvdData = tableFunctions.createDVDList(DataresultSet);
        tableView.getItems().addAll(dvdData);

        id_colum.setCellValueFactory(new PropertyValueFactory<sample.net.sqlitetutorial.DVDReturn, Integer>("id"));
        titel_colum.setCellValueFactory(new PropertyValueFactory<sample.net.sqlitetutorial.DVDReturn, String>("titel"));
        director_colum.setCellValueFactory(new PropertyValueFactory<sample.net.sqlitetutorial.DVDReturn, String>("director"));
        year_colum.setCellValueFactory(new PropertyValueFactory<sample.net.sqlitetutorial.DVDReturn, Integer>("year"));

        buttonAdd.setOnAction(e->{
            Button btnConfirm = new Button("Confirm");

            TextField txtid = new TextField();
            TextField txtTitel = new TextField();
            TextField txtDirector = new TextField();
            TextField txtYear = new TextField();

            Label lblId = new Label("ID");
            Label lblTitel = new Label("Titel");
            Label lblDirector = new Label("Director");
            Label lblYear = new Label("Year");
            Label lblSpace = new Label("");

            Dialog dialog = new Dialog();
            DialogPane dialogPane = new DialogPane();
            dialog.setDialogPane(dialogPane);
            dialogPane.setContent(new VBox(4, new HBox(lblId, txtid), new HBox(lblTitel, txtTitel),
                    new HBox(lblDirector, txtDirector), new HBox(lblYear, txtYear), new HBox(lblSpace),
                    new HBox(lblSpace), new HBox(btnConfirm)));
           dialogPane.getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
           dialog.show();

           btnConfirm.setOnAction(event -> {
               try {
                   int1 = Integer.valueOf(txtid.getText());
                   str1 = txtTitel.getText();
                   str2 = txtDirector.getText();
                   int2 = Integer.valueOf(txtYear.getText());

                   sample.net.sqlitetutorial.DVDReturn dvdReturn = new sample.net.sqlitetutorial.DVDReturn(int1, str1, str2, int2);
                   sample.net.sqlitetutorial.TableFunctions.dvdArray.add(dvdReturn);
                   sample.net.sqlitetutorial.TableFunctions.updateRow(int1, str1, str2, int2);
                   tableView.getItems().add(dvdReturn);
               }
               catch (NumberFormatException String){
                   Label lv = new Label("Try Again!");
                   Dialog dlgNotWorking = new Dialog();
                   DialogPane dp = new DialogPane();
                   dlgNotWorking.setDialogPane(dp);
                   dp.setContent(new VBox(4, new HBox(lv)));
                   dp.getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
                   dlgNotWorking.show();
               }
           });
        });

        buttonDel.setOnAction(event -> {
            Label label = new Label("Do you really want to delete that Entry?");
            Dialog dialog = new Dialog();
            DialogPane dialogPane = new DialogPane();
            dialog.setDialogPane(dialogPane);
            Button btncnfm = new Button("Confirm");
            dialogPane.setContent(new VBox(4, new HBox(label), new HBox(btncnfm)));
            dialogPane.getButtonTypes().add(new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));

            dialog.show();

            btncnfm.setOnAction(event1 -> {
                sample.net.sqlitetutorial.TableFunctions.delete(tableView.getSelectionModel().getSelectedItem().getId());
                sample.net.sqlitetutorial.DVDReturn slctedentry = tableView.getSelectionModel().getSelectedItem();
                tableView.getItems().remove(slctedentry);
            });

        });

        titel_colum.setCellFactory(TextFieldTableCell.forTableColumn());
        titel_colum.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                EditValues(event, "titel");
            }
        });
        director_colum.setCellFactory(TextFieldTableCell.forTableColumn());
        director_colum.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                EditValues(event, "director");
            }
        });
        hBox.getChildren().addAll(tableView, vBox);

        Scene scene = new Scene(hBox);
        Image Icon = new Image(getClass().getResourceAsStream("database3.png"));
        stage.setScene(scene);
        stage.setTitle("DVD-Management");
        stage.getIcons().add(Icon);
        stage.show();
    }

    public void EditValues(TableColumn.CellEditEvent<sample.net.sqlitetutorial.DVDReturn, String> dvdStringCellEditEvent, String type){
        try {
            sample.net.sqlitetutorial.DVDReturn dvd = tableView.getSelectionModel().getSelectedItem();
            String newVal = dvdStringCellEditEvent.getNewValue();
            if(type == "titel") {
                sample.net.sqlitetutorial.TableFunctions.update("titel", newVal, dvd.getId());
                dvd.setTitel(newVal);}
            else if(type == "director"){
                sample.net.sqlitetutorial.TableFunctions.update("director", newVal, dvd.getId());
                dvd.setDirector(newVal);}
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}