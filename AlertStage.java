import Log.*;
import Item.*; 
import java.time.*; 
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.collections.*;
import javafx.scene.image.*;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import java.time.format.DateTimeFormatter;



public class AlertStage extends Stage{

    private final String striphexdelete = "#610031;";
    private final String deletemidhex = "#dfccd5;";
    private final String striphexreorder = "#ffb300;";
    private final String reordermidhex = "#ffe5b7;";
    private final double alertwidth = 500.0;
    private final double alertw_to_h = 1.42857;
    private final double alertheight = alertwidth / alertw_to_h; //(350 tall)

    private static int overflow = 0;
    private static Text deltext = new Text();
    private static Image kettleonlyimage = new Image("./Misc/kettle.png");
    private static Image deliveryboxes = new Image("./Misc/delivery.png");
    private static ImageView kettle = new ImageView();
    private static ImageView delivery = new ImageView();
    private static Text delconfirm = new Text();
    private static AnchorPane alerttstrip = new AnchorPane();
    private static VBox alertcentervbox = new VBox(10);
    private static VBox reordervbox1 = new VBox(10);
    private static VBox reordervbox2 = new VBox(10);
    private static Text delundo = new Text();
    private static Text delperm = new Text();
    private static Label itemlabel = new Label();
    private static AnchorPane alertcenter = new AnchorPane();
    private static Button alertcancel = new Button();
    private static Button alertdelete = new Button();
    private static HBox alertbbx = new HBox(15);
    private static AnchorPane alertbstrip = new AnchorPane();
    private static BorderPane alertpane = new BorderPane();
    private static ObservableList<Item> itemsToDelete = FXCollections.observableArrayList();
    private static Text datereceived = new Text();
    private static Text amountreceived = new Text();
    private static Text instruction1 = new Text();
    private static Text instruction2 = new Text();
    private static DatePicker datepicker = new DatePicker();
    private static TextField amountbox = new TextField();
    private static Text emptywarning = new Text();
    private static String id;

    private static Kettlelog kettleclass = new Kettlelog();

    AlertStage(){
        //System.out.println("===============hereAlert");

        double reordermoveup = 20.0;

        //Top part of the pane.
        deltext.setFont(new Font(18));
        AnchorPane.setLeftAnchor(deltext, 30.0);
        AnchorPane.setBottomAnchor(deltext, 10.0);
        alerttstrip.setPrefSize(alertwidth, 75); //(width, height)
        alerttstrip.getChildren().addAll(deltext);

        //Center part of the pane

        kettle.setFitHeight(150);
        kettle.setFitWidth(150);
        kettle.setImage(kettleonlyimage);
        AnchorPane.setLeftAnchor(kettle, 10.0);
        AnchorPane.setTopAnchor(kettle, 40.0);

        delivery.setFitHeight(125);
        delivery.setFitWidth(125);
        delivery.setImage(deliveryboxes);
        AnchorPane.setLeftAnchor(delivery, 60.0);
        AnchorPane.setTopAnchor(delivery, 30.0);

        //VBOX FOR DELETING STAGE
        delconfirm.setText("Are you sure you want to delete");
        delconfirm.setFont(new Font(16));

        itemlabel.setFont(new Font(16));
        itemlabel.setPrefHeight(50.0);
        itemlabel.setPrefWidth(280.0);
        itemlabel.setAlignment(Pos.CENTER);
        itemlabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        itemlabel.setStyle("-fx-background-color: #cbadc3;");

        delperm.setFont(new Font(14));

        delundo.setText("You can't undo this action.");
        delundo.setFont(new Font(14));

        alertcentervbox.setPrefSize(220.0, 230.0);
        alertcentervbox.getChildren().addAll(delconfirm, itemlabel, delperm, delundo);
        alertcentervbox.setPadding(new Insets(50.0, 0.0, 0.0, 0.0));

        AnchorPane.setTopAnchor(alertcentervbox, 0.0);
        AnchorPane.setRightAnchor(alertcentervbox, 30.0);

        //VBOX FOR LOGGING A REORDER

        datereceived.setText("Date Received:");
        datereceived.setFont(new Font(16));

        datepicker.setPrefWidth(175);
        datepicker.setEditable(false);

        //reordervbox1.setPrefSize(100.0, 200.0);
        reordervbox1.getChildren().addAll(datereceived, datepicker);
        //reordervbox1.setPadding(new Insets(70.0, 0.0, 0.0, 0.0));

        AnchorPane.setBottomAnchor(reordervbox1, 130.0 + reordermoveup);
        AnchorPane.setLeftAnchor(reordervbox1, 250.0);

        amountreceived.setText("Amount Received:");
        amountreceived.setFont(new Font(16));

        amountbox.setPrefWidth(175);
        amountbox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                    amountbox.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        reordervbox2.getChildren().addAll(amountreceived, amountbox);
        AnchorPane.setBottomAnchor(reordervbox2, 60.0 + reordermoveup);
        AnchorPane.setLeftAnchor(reordervbox2, 250.0);

        instruction1.setText(" * The date should be when the reorder ARRIVES, not when it was ordered.");
        instruction1.setFont(new Font(12));
        instruction1.setFill(Color.SLATEGREY);
        AnchorPane.setBottomAnchor(instruction1, 40.0);
        AnchorPane.setLeftAnchor(instruction1, 50.0);

        instruction2.setText(" * Unlike regular logs, a reorder log can share a date with any other log.");
        instruction2.setFont(new Font(12));
        instruction2.setFill(Color.SLATEGREY);
        AnchorPane.setBottomAnchor(instruction2, 20.0);
        AnchorPane.setLeftAnchor(instruction2, 50.0);

        emptywarning.setFont(new Font(12));
        emptywarning.setFill(Color.BLACK);
        AnchorPane.setBottomAnchor(emptywarning, 20.0);
        AnchorPane.setLeftAnchor(emptywarning, 20.0);

        alertcenter.getChildren().addAll(kettle, reordervbox1, reordervbox2, alertcentervbox, delivery, instruction1, instruction2);

        //Bottom part of the pane which has the two buttons "Cancel" and "Delete". 

        alertcancel.setText("Cancel");
        alertcancel.setPrefHeight(30);
        alertcancel.setId("alertcancel");

        alertdelete.setPrefHeight(30);
        alertdelete.setId("confirmation");
        alertbbx.getChildren().addAll(alertcancel, alertdelete);

        alertcancel.setSkin(new FadeButtonSkin(alertcancel));
        alertdelete.setSkin(new FadeButtonSkin(alertdelete));

        AnchorPane.setRightAnchor(alertbbx, 7.5);
        AnchorPane.setTopAnchor(alertbbx, 7.5);
        
        alertbstrip.setPrefSize(alertwidth, 50); //(width, height)
        alertbstrip.getChildren().addAll(emptywarning, alertbbx);

        alertpane.setTop(alerttstrip);
        alertpane.setCenter(alertcenter);
        alertpane.setBottom(alertbstrip);

        this.setResizable(false);
        this.setScene(new Scene(alertpane, alertwidth, alertheight));
        this.initOwner(kettleclass.getPrimaryStage());
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.WINDOW_MODAL);

    }

    public void updateAlertStage(int popuptype, ObservableList<Item> items, Item rowinfo){

        //POPUPTYPE 0 = REORDERING ITEM
        //POPUPTYPE 1 = DELETING ITEM(S)
        id = rowinfo.getID();
        alertcentervbox.setVisible(false);
        reordervbox1.setVisible(false);
        reordervbox2.setVisible(false);
        kettle.setVisible(false);
        delivery.setVisible(false);
        instruction1.setVisible(false);
        instruction2.setVisible(false);
        emptywarning.setVisible(false);
        alertcancel.setTextFill(Color.BLACK);
        alertdelete.setTextFill(Color.BLACK);

        datepicker.setValue(LocalDate.now());

        //================================================================================
        // LOGGING A REORDER
        //================================================================================
        if (popuptype == 0) {

            alertcancel.setStyle("-fx-background-color: #f7f0e4;");  
            alertdelete.setStyle("-fx-background-color: #ffe5b7;");

            String stripcolour = String.format("-fx-background-color: %s", striphexreorder);
            String alertmidcolour = String.format("-fx-background-color: %s", reordermidhex);
            alerttstrip.setStyle(stripcolour);
            alertcenter.setStyle(alertmidcolour);
            alertbstrip.setStyle(stripcolour);
            amountbox.setText("");

            reordervbox1.setVisible(true);
            reordervbox2.setVisible(true);
            delivery.setVisible(true);
            instruction1.setVisible(true);
            instruction2.setVisible(true);
            alertdelete.setText("Confirm"); 
            deltext.setText("Log Reorder"); 
            deltext.setFill(Color.BLACK); 

            //We only want the addstage's opaque layer to go away, NOT the primarystage's
            alertcancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    kettleclass.hideAlertStage(0);
                    AddStage addStage = kettleclass.getAddStage();
                    addStage.hideOpaqueLayer();
                    itemsToDelete.clear();
                    alertcentervbox.setVisible(false);
                    reordervbox1.setVisible(false);
                    reordervbox2.setVisible(false);
                    kettle.setVisible(false);
                    delivery.setVisible(false);
                    instruction1.setVisible(false);
                    instruction2.setVisible(false); 
                    emptywarning.setVisible(false);
                    overflow = 0;
                }
            });

            alertdelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    boolean incomplete = false;
                    String logid = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

                    String amountnum = amountbox.getText();
                    //If the amount received textbox is empty, display the error message. 
                    if (amountnum.isEmpty()) {
                        incomplete = true;
                    }

                    if (incomplete) {
                        emptywarning.setText("* You must specify an amount received.");
                        emptywarning.setVisible(true);
                    }

                    else {
                        //Checking for overflow with the amount number.
                        int amountint = 0;

                        try {
                            amountint = Integer.parseInt(amountnum);
                            overflow = 1;

                        } catch (NumberFormatException e) {
                            System.out.println("Number is too large.");
                            emptywarning.setText("* Your amount received number is too large.");
                            emptywarning.setVisible(true);
                            overflow--;
                        }

                        if (overflow == 1) {
                            //We want to add the log to the LogList, with a type of "REORDER" and a quantity of "REORDER: + X"
                            String logtype = "REORDER";
                            String reorderdate = datepicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String reorderquan = "REORDER: +" + amountbox.getText();

                            //Our current list of logs. We need to append the reorder to it!
                            ObservableList<Log> currentloglist = rowinfo.getLogData();
                            Log newlog = new Log(logid, logtype, reorderdate, reorderquan);

                            //Adding the log to our observablelist and then setting our item's log data to be this list.
                            currentloglist.add(0, newlog);
                            rowinfo.setLogData(currentloglist);
                           
                            //Adding the log to our SQL Database's Log table
                            kettleclass.addLog(rowinfo.getID(), logid, logtype, reorderdate, reorderquan);

                            //Also, update everything since a new reorder has been added.
                            kettleclass.updateEverything(rowinfo.getID());

                            //Editing our SQL Database's Info table
                            kettleclass.editInfoTable(rowinfo.getID(), rowinfo.getName(), 
                                rowinfo.getStatus(), rowinfo.getQuantity(), rowinfo.getMinimum(), 
                                rowinfo.getDelivery(), rowinfo.getDesc(), 0, rowinfo.getDateAdded(), 
                                rowinfo.getADC(), rowinfo.getROP(), rowinfo.getROD()); 

                            //Also, update the table from infostage.
                            InfoStage infoStage = kettleclass.getInfoStage();
                            
                            //if the user confirms the reorder log, we want to hide both the addstage and primarystage opaque's layers
                            kettleclass.hideAlertStage(0);
                            AddStage addStage = kettleclass.getAddStage();
                            kettleclass.hideAlertStage(1);
                            kettleclass.hideAddStage();
                            emptywarning.setVisible(false);
                            overflow = 0;

                        }
                    }  
                }
            });


        }

        //================================================================================
        // DELETING AN ITEM
        //================================================================================
        else {

            alertcancel.setStyle("-fx-background-color: #dfccd5;");
            alertdelete.setStyle("-fx-background-color: #cbadc3;");

            String stripcolour = String.format("-fx-background-color: %s", striphexdelete);
            String alertmidcolour = String.format("-fx-background-color: %s", deletemidhex);
            alerttstrip.setStyle(stripcolour);
            alertcenter.setStyle(alertmidcolour);
            alertbstrip.setStyle(stripcolour);

            alertcentervbox.setVisible(true);
            kettle.setVisible(true);
            alertdelete.setText("Delete Item");   
            deltext.setText("Confirm Deletion");
            deltext.setFill(Color.WHITE);

            itemsToDelete.addAll(items);

            boolean hasEmpty = false;

            for(int i =0; i<itemsToDelete.size(); i++){
                if(itemsToDelete.get(i).getID().equals("emptyID")){
                    hasEmpty = true;
                }
            }
        
            if(itemsToDelete.size()==1 || (itemsToDelete.size()==2 && hasEmpty==true)){
                String itemName = itemsToDelete.get(0).getName();

                if(itemName.equals("") && itemsToDelete.size()==2){
                    itemName = itemsToDelete.get(1).getName();
                }

                System.out.println("Deleting: "+itemName);
                
                itemlabel.setText(itemName+ "?");
                delperm.setText("This item will be deleted permanently.");
            }
            else{
                itemlabel.setText("the selected items?");
                delperm.setText("The items will be deleted permanently.");
            }

            //We want to hide the PrimaryStage's opaque layer in this scenario.
            alertcancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    kettleclass.hideAlertStage(1);
                    itemsToDelete.clear();
                    alertcentervbox.setVisible(false);
                    reordervbox1.setVisible(false);
                    reordervbox2.setVisible(false);
                    kettle.setVisible(false);
                    delivery.setVisible(false);
                    instruction1.setVisible(false);
                    instruction2.setVisible(false);
                    emptywarning.setVisible(false);
                }
            });

            //CONFIRMATION BUTTON 
            alertdelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    kettleclass.setData(itemsToDelete, 2);
                    itemsToDelete.clear();
                    alertcentervbox.setVisible(false);
                    reordervbox1.setVisible(false);
                    reordervbox2.setVisible(false);
                    kettle.setVisible(false);
                    delivery.setVisible(false);
                    instruction1.setVisible(false);
                    instruction2.setVisible(false);
                    emptywarning.setVisible(false);
                }
            });

        }
        

    }

}