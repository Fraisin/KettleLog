import Log.*;
import Item.*;
import java.time.*; 
import java.util.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import java.time.LocalDate;
import javafx.scene.text.*;
import javafx.collections.*;
import javafx.scene.chart.*; 
import javafx.scene.image.*;
import javafx.collections.*;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import java.lang.StringBuilder;
import javafx.scene.paint.Color;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.scene.control.ScrollPane.*;
import java.time.format.DateTimeFormatter;
import java.lang.NumberFormatException;
import javafx.beans.property.SimpleBooleanProperty;

public class InfoStage extends Stage{

    //================================================================================
    // INITIALIZATION
    //================================================================================
    private String infostripcolour;
    private String infomidcolour;
    private static int presscount = 0;
    private static double infowidth = 500;
    private static double infoheight = 0;
    private static double scrollheight = 2000;
    private static double distancedown = 40.0;
    private static double tooltipduration = 75.0;
    private static String infostriphex = "#004545;";
    private static String infomidhex = "#b8d6d6;";
    private static Button help1 = new Button();
    private static Button help2 = new Button();
    private static Button refresh = new Button();
    private static Tooltip helptip1 = new Tooltip("When the current quantity of this item reaches this number, a reorder must be placed.");
    private static Tooltip helptip2 = new Tooltip("The approximate date you should place a reorder.");
    private static Tooltip refreshtip = new Tooltip("Refresh Graph");
    private static Separator line1 = new Separator();
    private static Separator line2 = new Separator();
    private static Separator line3 = new Separator();
    private static Separator line4 = new Separator();
    private static Text infotitle = new Text();
    private static Text dateadded = new Text();
    private static Text adc = new Text();
    private static Text erp = new Text();
    private static Text erd = new Text();
    private static Text warning = new Text();
    private static Text logmonitortext = new Text();
    private static Text graphtext = new Text(); //consumption graph
    private static Text editinstruction = new Text();
    private static Text delinstruction = new Text();
    private static Text cannotdelete = new Text();
    private static Label infolabel = new Label(); //item name 
    private static Label datelabel = new Label(); //date added
    private static Label adclabel = new Label(); //average daily consumption 
    private static Label erplabel = new Label(); //estimated reorder point
    private static Label erdlabel = new Label(); //estimated reorder date
    private static TextArea infodesc = new TextArea();
    private static Button infocancel = new Button();
    private static Button logdel = new Button();
    private static AnchorPane infotstrip = new AnchorPane();
    private static AnchorPane infocenter = new AnchorPane();
    private static AnchorPane infobstrip = new AnchorPane();
    private static BorderPane infoborderpane = new BorderPane();
    private static Image helpBtnImg = new Image("./Misc/help.png");
    private static Image helpBtnImg2 = new Image("./Misc/help.png");
    private static ImageView helpImg = new ImageView(); 
    private static ImageView helpImg2 = new ImageView();
    private static VBox infobox = new VBox();
    private static Kettlelog kettle = new Kettlelog();
    private static TableView<Log> logtable = new TableView<Log>();
    private static TableColumn<Log, String> dateloggedcol = new TableColumn<Log, String>("Date Logged");
    private static TableColumn<Log, String> quanloggedcol = new TableColumn<Log, String>("Quantity");
    private static BorderPane bottomborder = new BorderPane();
    private static CategoryAxis xAxis = new CategoryAxis();
    private static NumberAxis yAxis = new NumberAxis();
    private static LineChart linechart = new LineChart(xAxis, yAxis);
    private static Log initiallog = new Log();
    private static Log firstselection = new Log();
    private static Log finalselection = new Log();
    private static ObservableList<Item> placeholder = FXCollections.observableArrayList();
    private static String id;

    //Constructor
    InfoStage(){

        //Strip colours
        infostripcolour = String.format("-fx-background-color: %s", infostriphex);
        infomidcolour = String.format("-fx-background-color: %s", infomidhex);

        //Top strip of info panel which has text saying "Item Information"  
        AnchorPane.setLeftAnchor(infotitle, 50.0);
        AnchorPane.setBottomAnchor(infotitle, 5.0);    
        infotitle.setText("Item Information");
        infotitle.setFont(new Font(18));
        infotitle.setFill(Color.WHITE);
        infotstrip.setStyle(infostripcolour);
        infotstrip.setPrefSize(infowidth, 50); //(width, height)       
        infotstrip.getChildren().addAll(infotitle);

        //Item Name Label
        AnchorPane.setTopAnchor(infolabel, 25.0);
        AnchorPane.setLeftAnchor(infolabel, 100.0);
        infolabel.setFont(new Font(16));
        infolabel.setPrefHeight(50.0);
        infolabel.setPrefWidth(300.0);
        infolabel.setFont(new Font(16));
        infolabel.setAlignment(Pos.CENTER);
        infolabel.setTextOverrun(OverrunStyle.ELLIPSIS);
        infolabel.setStyle("-fx-background-color: #709c9c");

        //Date Added Text 
        AnchorPane.setLeftAnchor(dateadded, 25.0);
        AnchorPane.setTopAnchor(dateadded, 102.0);
        dateadded.setText("Date Added");
        dateadded.setFont(new Font(16));

        //Date Added Label
        AnchorPane.setRightAnchor(datelabel, 25.0);
        AnchorPane.setTopAnchor(datelabel, 100.0);
        datelabel.setPrefHeight(25.0);
        datelabel.setPrefWidth(125.0);
        datelabel.setFont(new Font(16));
        datelabel.setAlignment(Pos.CENTER);
        datelabel.setStyle("-fx-background-color: #95bfbf");

        //First Separator
        AnchorPane.setTopAnchor(line1, 110.0);
        AnchorPane.setRightAnchor(line1, 160.0);
        line1.setPrefWidth(220.0);

        //Average Daily Consumption Text
        AnchorPane.setLeftAnchor(adc, 25.0);
        AnchorPane.setTopAnchor(adc, 102.0 + distancedown); 
        adc.setText("Average Daily Consumption");
        adc.setFont(new Font(16));

        //Average Daily Consumption Label
        AnchorPane.setRightAnchor(adclabel, 25.0);
        AnchorPane.setTopAnchor(adclabel, 100.0 + distancedown);
        adclabel.setFont(new Font(14));
        adclabel.setPrefHeight(25.0);
        adclabel.setPrefWidth(125.0);
        adclabel.setAlignment(Pos.CENTER);
        adclabel.setStyle("-fx-background-color: #95bfbf");

        //Second Separator
        AnchorPane.setTopAnchor(line2, 110.0 + distancedown);
        AnchorPane.setRightAnchor(line2, 160.0);
        line2.setPrefWidth(100.0);

        //Estimated Reorder Point Text
        AnchorPane.setLeftAnchor(erp, 25.0);
        AnchorPane.setTopAnchor(erp, 102.0 + (distancedown * 2)); 
        erp.setText("Estimated Reorder Point");
        erp.setFont(new Font(16));

        //Estimated Reorder Point Label
        AnchorPane.setRightAnchor(erplabel, 25.0);
        AnchorPane.setTopAnchor(erplabel, 100.0 + (distancedown * 2));
        erplabel.setFont(new Font(14));
        erplabel.setPrefHeight(25.0);
        erplabel.setPrefWidth(125.0);
        erplabel.setAlignment(Pos.CENTER);
        erplabel.setStyle("-fx-background-color: #95bfbf");

        //Help Button for Estimated Reorder Point
        helptip1.setPrefWidth(150);
        helptip1.setWrapText(true);
        helptip1.setShowDelay(new javafx.util.Duration(tooltipduration));
        helpImg.setImage(helpBtnImg);
        helpImg.setFitWidth(20);
        helpImg.setPreserveRatio(true);
        helpImg.setSmooth(true);
        helpImg.setCache(true);  
        help1.setTooltip(helptip1);  
        help1.setGraphic(helpImg);    
        help1.setStyle("-fx-background-color: transparent;");    
        AnchorPane.setLeftAnchor(help1, 205.0);
        AnchorPane.setTopAnchor(help1, 97.0 + (distancedown * 2)); 

        //Third Separator
        AnchorPane.setTopAnchor(line3, 110.0 + (distancedown * 2));
        AnchorPane.setRightAnchor(line3, 160.0);
        line3.setPrefWidth(100.0);

        //Estimated Reorder Date Text
        AnchorPane.setLeftAnchor(erd, 25.0);
        AnchorPane.setTopAnchor(erd, 102.0 + (distancedown * 3)); 
        erd.setText("Estimated Reorder Date");
        erd.setFont(new Font(16));

        //Estimated Reorder Date Label
        AnchorPane.setRightAnchor(erdlabel, 25.0);
        AnchorPane.setTopAnchor(erdlabel, 100.0 + (distancedown * 3));
        erdlabel.setFont(new Font(14));
        erdlabel.setPrefHeight(25.0);
        erdlabel.setPrefWidth(125.0);
        erdlabel.setAlignment(Pos.CENTER);
        erdlabel.setStyle("-fx-background-color: #95bfbf");

        //Help Button for Estimated Reorder Date
        helptip2.setPrefWidth(150);
        helptip2.setWrapText(true);
        helptip2.setShowDelay(new javafx.util.Duration(tooltipduration));
        helpImg2.setImage(helpBtnImg2);
        helpImg2.setFitWidth(20);
        helpImg2.setPreserveRatio(true);
        helpImg2.setSmooth(true);
        helpImg2.setCache(true);  
        help2.setTooltip(helptip2);  
        help2.setGraphic(helpImg2);    
        help2.setStyle("-fx-background-color: transparent;");    
        AnchorPane.setLeftAnchor(help2, 200.0);
        AnchorPane.setTopAnchor(help2, 97.0 + (distancedown * 3));

        //Fourth Separator
        AnchorPane.setTopAnchor(line4, 110.0 + (distancedown * 3));
        AnchorPane.setRightAnchor(line4, 160.0);
        line4.setPrefWidth(105.0);

        //Warning text
        warning.setText(" * Create at least 3 logs for the estimated reorder point and date to appear.");
        warning.setFont(new Font(12));
        warning.setFill(Color.GREY);
        AnchorPane.setLeftAnchor(warning, 25.0);
        AnchorPane.setTopAnchor(warning, 110.0 + (distancedown * 4)); 

        double movedown = 40.0;

        //Uneditable Description Box
        AnchorPane.setLeftAnchor(infodesc, 25.0);
        AnchorPane.setTopAnchor(infodesc, 100.0 + (distancedown * 4) + 10.0 + movedown);
        infodesc.setPrefWidth(450.0);
        infodesc.setPrefHeight(125.0);
        infodesc.setEditable(false);
        infodesc.setStyle("-fx-opacity: 1;");
        infodesc.setWrapText(true);

        //Consumption Graph Text
        AnchorPane.setLeftAnchor(graphtext, 25.0);
        AnchorPane.setTopAnchor(graphtext, 95.0 + (distancedown * 8) + movedown); 
        graphtext.setText("Weekly Consumption Graph");
        graphtext.setFont(new Font(16));

        //Refresh Button
        refreshtip.setShowDelay(new javafx.util.Duration(tooltipduration));
        refresh.setTooltip(refreshtip);
        Image refreshImg = new Image("./Misc/refresh.png");
        ImageView refreshImgView = new ImageView();
            refresh.setStyle("-fx-background-color: transparent;");             
            refreshImgView.setImage(refreshImg);
            refreshImgView.setFitWidth(20);
            refreshImgView.setPreserveRatio(true);
            refreshImgView.setSmooth(true);
            refreshImgView.setCache(true); 
            refresh.setGraphic(refreshImgView);
        AnchorPane.setRightAnchor(refresh, 25.0);
        AnchorPane.setTopAnchor(refresh, 90.0 + (distancedown * 8) + movedown); 

        //================================================================================
        // CONSUMPTION GRAPH
        //================================================================================
        xAxis.setLabel("Date");
        yAxis.setLabel("Quantity");

        //Initialization of our arraylist which will eventually turn into the labels of our x-axis.
        ArrayList<String> datearraylist = new ArrayList<String>(7); 
        //Appending 7 dates to the arraylist (today + the 6 prior days).
        for (int i = 0; i > -7; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            Date date = cal.getTime();
            String datetick = new SimpleDateFormat("MM/dd").format(date);
            datearraylist.add(datetick);
        }

        //The dates are appended from most recent -> oldest, so we need to reverse the list.
        Collections.reverse(datearraylist);
        ObservableList<String> dateaxis = FXCollections.observableArrayList(datearraylist);
        xAxis.setCategories(dateaxis);

        //Adding the graph to the anchorpane.
        AnchorPane.setLeftAnchor(linechart, 7.5);
        AnchorPane.setTopAnchor(linechart, 80.0 + (distancedown * 9) + movedown);
        linechart.setPrefWidth(475.0);
        linechart.setPrefHeight(350.0);
        linechart.setLegendVisible(false);

        //Log Monitor Table Text
        AnchorPane.setLeftAnchor(logmonitortext, 25.0);
        AnchorPane.setTopAnchor(logmonitortext, 100.0 + (distancedown * 17) + movedown); 
        logmonitortext.setText("Log Table");
        logmonitortext.setFont(new Font(16));

        //Instructions for editing.
        editinstruction.setText(" * To edit a log, double click its values, make change, then press enter key.");
        editinstruction.setFont(new Font(12));
        editinstruction.setFill(Color.GREY);
        AnchorPane.setLeftAnchor(editinstruction, 25.0);
        AnchorPane.setTopAnchor(editinstruction, 100.0 + (distancedown * 18) + movedown); 

        //Instructions for deleting.
        delinstruction.setText(" * To delete a log, select it and then press the trash can on the bottom right.");
        delinstruction.setFont(new Font(12));
        delinstruction.setFill(Color.GREY);
        AnchorPane.setLeftAnchor(delinstruction, 25.0);
        AnchorPane.setTopAnchor(delinstruction, 100.0 + (distancedown * 18.7) + movedown); 

        //================================================================================
        // LOG TABLE
        //================================================================================
        logtable.setEditable(true);

        dateloggedcol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateloggedcol.setCellValueFactory(new PropertyValueFactory<>("dateLogged"));
        dateloggedcol.prefWidthProperty().bind(logtable.widthProperty().multiply(0.495));
        dateloggedcol.setStyle( "-fx-alignment: CENTER-LEFT;");
        dateloggedcol.setResizable(false);
        dateloggedcol.setSortable(false);

        //when the column is edited, the Log should be edited directly. 
        dateloggedcol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Log, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Log, String> event) {
                    String newdate = event.getNewValue();
                    String olddate = event.getOldValue();

                    int row = event.getTablePosition().getRow();
                    Log editedlog = event.getTableView().getItems().get(row);

                    //REORDER TYPE, DATE MUST BE IN CORRECT FORMAT, BUT CAN BE ANY DATE.
                    if ((editedlog.getLogType()).equals("REORDER")) {
                        if (isValidDate(newdate)) {
                            Log selectedlog =  event.getTableView().getItems().get(event.getTablePosition().getRow());
                            selectedlog.setDateLogged(newdate);
                            //System.out.println(selectedlog.getDateLogged());
                            kettle.updateLog(id, "logdate", newdate, selectedlog.getID());
                            logtable.requestFocus();
                            cannotdelete.setVisible(false);
                            kettle.updateEverything(id);
                            sortByDateLogged();
                        } else {
                            logtable.getItems().set(row, editedlog);
                            logtable.requestFocus();
                            cannotdelete.setText(" * Date must be in YYYY-MM-DD format. Do not include any spaces!");
                            cannotdelete.setVisible(true);
                        }
                    }

                    //CONSUMPTION TYPE, DATE MUST BE CORRECT FORMAT & NOT SHARE DATE WITH OTHER CONSUMPTION LOG.
                    else if (isValidDate(newdate) && !dateExists(newdate, id)){
                        Log selectedlog =  event.getTableView().getItems().get(event.getTablePosition().getRow());
                        selectedlog.setDateLogged(newdate);
                        //System.out.println(selectedlog.getDateLogged());
                        kettle.updateLog(id, "logdate", newdate, selectedlog.getID());
                        logtable.requestFocus();
                        cannotdelete.setVisible(false);
                        kettle.updateEverything(id);
                        sortByDateLogged();
                    }
                    else {
                        //If the edit is NOT valid...
                        logtable.getItems().set(row, editedlog);
                        logtable.requestFocus();

                        //if user entered an existing date for a CONSUMPTION-TYPE log it is not valid.
                        if(dateExists(newdate, id)){
                            cannotdelete.setText(" * This date already has a corresponding non-reorder log.");
                        }
                        else{
                            cannotdelete.setText(" * Date must be in YYYY-MM-DD format. Do not include any spaces!");
                        }
                        cannotdelete.setVisible(true);
                    }
                }
            }
        );

        quanloggedcol.setCellFactory(TextFieldTableCell.forTableColumn());
        quanloggedcol.setCellValueFactory(new PropertyValueFactory<>("quanLogged"));
        quanloggedcol.prefWidthProperty().bind(logtable.widthProperty().multiply(0.495));
        quanloggedcol.setStyle( "-fx-alignment: CENTER-LEFT;");
        quanloggedcol.setResizable(false);
        quanloggedcol.setSortable(false);

        quanloggedcol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Log, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Log, String> event) {
                    String newquantity = event.getNewValue();
                    String oldquantity = event.getOldValue();

                    int row = event.getTablePosition().getRow();
                    Log editedlog = event.getTableView().getItems().get(row);

                    if ((editedlog.getLogType()).equals("REORDER")) {
                        if (validReorderQuan(newquantity)) {
                            if (!overflows(newquantity)){
                                Log selectedlog =  event.getTableView().getItems().get(event.getTablePosition().getRow());
                                selectedlog.setQuanLogged(newquantity);
                                kettle.updateLog(id, "logquan", newquantity, selectedlog.getID());
                                logtable.requestFocus();
                                cannotdelete.setVisible(false);
                                kettle.updateEverything(id);
                                sortByDateLogged();
                            } else {
                                logtable.getItems().set(row, editedlog);
                                logtable.requestFocus();
                                cannotdelete.setText(" * Edited quantity is too large.");
                                cannotdelete.setVisible(true);
                            }
                        } else {
                            logtable.getItems().set(row, editedlog);
                            logtable.requestFocus();
                            cannotdelete.setText(" * Reorder quantity must be in form 'REORDER: +integer'");
                            cannotdelete.setVisible(true);
                        }
                    }
                    //CONSUMPTION TYPE ~ we only want to commit the edit if the quantity is a number. 
                    else if (newquantity.chars().allMatch( Character::isDigit )) {
                        if (!overflows(newquantity)){
                                Log selectedlog =  event.getTableView().getItems().get(event.getTablePosition().getRow());
                                selectedlog.setQuanLogged(newquantity);
                                kettle.updateLog(id, "logquan", newquantity, selectedlog.getID());
                                logtable.requestFocus();
                                cannotdelete.setVisible(false);
                                kettle.updateEverything(id);
                                sortByDateLogged();
                            } else {
                                logtable.getItems().set(row, editedlog);
                                logtable.requestFocus();
                                cannotdelete.setText(" * Edited quantity is too large.");
                                cannotdelete.setVisible(true);
                            }
                    } 
                    //If it's not an integer, don't allow the edit and display an error message.
                    else {
                        logtable.getItems().set(row, editedlog);
                        logtable.requestFocus();
                        cannotdelete.setText(" * Quantity must only contain numbers and no other characters.");
                        cannotdelete.setVisible(true);
                    }
                }
            }
        );

        logtable.getColumns().<Log, String>addAll(dateloggedcol, quanloggedcol);

        VBox logbox = new VBox(logtable);

        double tableheight = 350.0;
        double bottomgap = 40.0; 

        double movedown2 = 70.0;
        AnchorPane.setLeftAnchor(logbox, 25.0);
        AnchorPane.setTopAnchor(logbox, 90.0 + (distancedown * 18) + movedown + movedown2);
        logbox.setPrefWidth(450.0);
        logbox.setPrefHeight(tableheight);

        //Warning message for deleting logs.
        cannotdelete.setFont(new Font(12));
        cannotdelete.setFill(Color.FIREBRICK);
        AnchorPane.setLeftAnchor(cannotdelete, 25.0);
        AnchorPane.setBottomAnchor(cannotdelete, 13.0);
        cannotdelete.setVisible(false);

        //Making the columns of the table undraggable.
        ColumnHandler columnChange = new ColumnHandler();
        logtable.getColumns().addListener(columnChange);  

        //Delete button for logtable (editing is done directly, so we don't need a button.)
        Image logdelImg = new Image("./Misc/delete2.png");
        ImageView logdelImgView = new ImageView();
            logdel.setStyle("-fx-background-color: transparent;");             
            logdelImgView.setImage(logdelImg);
            logdelImgView.setFitWidth(20);
            logdelImgView.setPreserveRatio(true);
            logdelImgView.setSmooth(true);
            logdelImgView.setCache(true); 
            logdel.setGraphic(logdelImgView);

        HBox buttonbox = new HBox();
        buttonbox.getChildren().addAll(logdel);
       
        AnchorPane.setRightAnchor(buttonbox, 20.0);
        AnchorPane.setBottomAnchor(buttonbox, 4.5);
        
        AnchorPane.setTopAnchor(bottomborder, 90.0 + (distancedown * 18) + movedown + movedown2);
        bottomborder.setPrefHeight(tableheight + bottomgap);
        bottomborder.setCenter(logbox);

        //================================================================================
        // END OF TABLE INITIALIZATION
        //================================================================================
        infocenter.setStyle(infomidcolour);     
        infocenter.getChildren().addAll(infolabel, dateadded, datelabel, line1, adc, adclabel, warning);
        infocenter.getChildren().addAll(line2, erp, erplabel, line3, erd, erdlabel, line4, infodesc);
        infocenter.getChildren().addAll(graphtext, linechart, help1, help2, logmonitortext, logbox, bottomborder, buttonbox);
        infocenter.getChildren().addAll(editinstruction, delinstruction, cannotdelete, refresh);

        //SCROLLPANE CONFIGURATION
        infobox.getChildren().add(infocenter);
        ScrollPane scrollpane = new ScrollPane(infobox);
        scrollpane.setContent(infobox);
        scrollpane.setPrefHeight(scrollheight);
        scrollpane.setFitToWidth(true);
        scrollpane.setVbarPolicy(ScrollBarPolicy.NEVER);

        //CANCEL BUTTON 
        infocancel.setText("Close");
        infocancel.setPrefHeight(27.5);
        infocancel.setId("infocancel");
        InfoHandler infoHandler = new InfoHandler();
        infocancel.setOnAction(infoHandler); 
        infocancel.setStyle("-fx-background-color: #b8d6d6;");
        infocancel.setSkin(new FadeButtonSkin(infocancel));
        
        AnchorPane.setRightAnchor(infocancel, 5.0);
        AnchorPane.setTopAnchor(infocancel, 5.0);
        infobstrip.setStyle(infostripcolour);
        infobstrip.setPrefSize(infowidth, 37.5);
        infobstrip.getChildren().addAll(infocancel);  
    
        //Setting the elements of the borderpane.
        infoborderpane.setTop(infotstrip);
        infoborderpane.setCenter(scrollpane);
        infoborderpane.setBottom(infobstrip);

        if (kettle.isLargeScreen()) {
            infoheight = 700.00;
        } else {
            infoheight = kettle.getScreenHeight();
        }

        this.setScene(new Scene(infoborderpane, infowidth, infoheight));
        this.setResizable(false);
        this.initOwner(kettle.getPrimaryStage());
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.WINDOW_MODAL);
    }

    public void updateInfoStage(Item rowinfo){
    	id = rowinfo.getID();

        infolabel.setText(rowinfo.getName());
        datelabel.setText(rowinfo.getDateAdded());
        adclabel.setText(rowinfo.getADC());
        erplabel.setText(rowinfo.getROP());
        erdlabel.setText(rowinfo.getROD());

        //Setting the description to the be the same. 
        if (rowinfo.getDesc().trim().length() <= 0) {
            infodesc.setText("There is no description for this item.");
        } else {
            infodesc.setText("Item Description: " + rowinfo.getDesc());
        }

        //Setting the graph up
        updateGraph(rowinfo);

        //Setting the data for the log table. 
        logtable.setItems(rowinfo.getLogData());
        sortByDateLogged();

        //Refresh Graph functionality
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    updateGraph(rowinfo);
                    
                }
            });

        //Removing the log from the ObservableList of logs & the table.
        logdel.setOnAction(e -> {

            ObservableList<Log> selectedloglist = logtable.getItems();
            int numofcons = 0;
            int sellength = selectedloglist.size();

            //Find the number of consumption-type logs in our loglist.
            for (int i = 0; i < sellength; i++) {
                Log currentlog = selectedloglist.get(i);
                if ((currentlog.getLogType()).equals("CONSUMPTION")) {
                    numofcons++;
                }
            }

            //Cannot delete your only consumption-type log in the list!
            initiallog = logtable.getSelectionModel().getSelectedItem();

            if (initiallog==null) {
                cannotdelete.setText(" * No log selected.");
                cannotdelete.setVisible(true);
            }

            else if (((initiallog.getLogType()).equals("CONSUMPTION")) && (numofcons == 1)) {
                cannotdelete.setText(" * You cannot delete your only non-reorder log.");
                cannotdelete.setVisible(true);
            }

            else {
                //confirmation should be refreshed every time a new log is selected.
                presscount++;
                if (presscount == 1) {
                    firstselection = logtable.getSelectionModel().getSelectedItem();
                    cannotdelete.setText(" * Delete the selected log permanently? Press once more to confirm.");
                    cannotdelete.setVisible(true);
                }
                else {
                    finalselection = logtable.getSelectionModel().getSelectedItem();
                    if(firstselection.getID().equals(finalselection.getID())){
                        //Here is where the log actually gets deleted.
                        logtable.getItems().remove(finalselection);
                        cannotdelete.setVisible(false); 
                        presscount=0;
                        //We've deleted the log from our ObservableList, but now we need to get rid of it in our SQL Database.
                        String itemid = rowinfo.getID();
                        //System.out.println(itemid);
                        String logid = finalselection.getID();
                        //System.out.println(logid);
                        kettle.deleteLog(itemid, logid);
                        //update everything since a log has been deleted.
                        kettle.updateEverything(id);
                        sortByDateLogged();
                    } 
                    else {
                        cannotdelete.setText(" * WARNING: A different log has been selected for deletion.");
                        cannotdelete.setVisible(true);
                        presscount=0;
                    }
                }
                
            }

        });

    }

    public void updateGraph(Item rowinfo) {

        //Here we are getting the past 7 days in the format of the date of our logs, so we can make sure only the necessary logs are graphed.
        ArrayList<String> datelistwithyear = new ArrayList<String>(7); 
        for (int i = 0; i > -7; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, i);
            Date date = cal.getTime();
            String datetick = new SimpleDateFormat("yyyy-MM-dd").format(date);
            datelistwithyear.add(datetick);
        }

        System.out.println(datelistwithyear);

        //Synchronizing the chart to the log table. 
        XYChart.Series series = new XYChart.Series();
        linechart.getData().clear();

        ObservableList<Log> loginfo = kettle.getConsumption(rowinfo.getLogData());
        int length = loginfo.size();
        ObservableList<Log> chartinfo = FXCollections.observableArrayList();

        for (int i = 0; i < length; i++) {
            Log curlog = loginfo.get(i);
            String logdatewithyear = curlog.getDateLogged();
            //System.out.println("The log date is " + logdatewithyear);
            if (datelistwithyear.contains(logdatewithyear)) {
                //System.out.println("This log's date is in the past 7 days.");
                chartinfo.add(curlog);
            }
        }

        //chartinfo now has the logs that we need to log and nothing else!
        int chartlength = chartinfo.size();

        for (int i = 0; i < chartlength; i++) {
            String datewithyear = (chartinfo.get(i)).getDateLogged();
            //System.out.println("Date with year is " + datewithyear);
            //This date is represented in YYYY-MM-DD form. We need to change it to MM/DD. 
            String date = (datewithyear.substring(5)).replace("-", "/");
            //System.out.println(date);
            String quanstring = (chartinfo.get(i)).getQuanLogged();
            //System.out.println("The quantity is " + quanstring);
            int quantity = Integer.parseInt(quanstring);
            series.getData().add(new XYChart.Data(date, quantity));
        }

        linechart.getData().add(series);

    }

    //This method will sort the table so that the most recent logs appear at the top.
    public void sortByDateLogged(){

        ObservableList<Log> currentloglist = kettle.getLogs(id);
        ObservableList<Log> sortedloglist = kettle.sortLogsByDate(currentloglist);
        Collections.reverse(sortedloglist);
        //most recent logs are at the FRONT now since we reversed the list. 
        //We need to deal with cases where certain dates have both consumption and reorder logs. The consumption log should be FIRST.

        logtable.setItems(sortedloglist);

        /*logtable.getSortOrder().add(dateloggedcol);
        dateloggedcol.setSortType(TableColumn.SortType.DESCENDING);
        dateloggedcol.setSortable(true);
        logtable.sort();*/
    }

    //This method takes in a string and determines if it's a date in YYYY-MM-DD form.
    public static boolean isValidDate(String date) {

        if (date == null || date.length() != "yyyy-MM-dd".length()) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public boolean dateExists(String date, String id){
        //get list of dates
        //loop through all CONSUMPTION-TYPE dates, compare to date
        
        ObservableList<Log>logData = kettle.getConsumption(kettle.getLogs(id));

        for(int i = 0; i<logData.size(); i++){
            if(logData.get(i).getDateLogged().equals(date)){
                return true;
            }
        }
        return false;

    }


    //this method takes in a string and determines if it's a valid reorder quantity of form "REORDER:+ X", where X is an integer
    public boolean validReorderQuan(String reorderquan) {

        String firstpart = reorderquan.substring(0, Math.min(10, reorderquan.length()));
        System.out.println(firstpart + "hello");
        //int lengthofinteger = reorderquan.length() - 10; //length of the quantity user entered

        if (!firstpart.equals("REORDER: +")) {
            return false;
        } else {
            String intquantity = reorderquan.substring(reorderquan.lastIndexOf('+') + 1); //everything after the plus sign, so just the number
            System.out.println(intquantity + "hello");
            if (!intquantity.chars().allMatch( Character::isDigit )) {
                return false;
            } else {
                return true;
            }

        }
    }

    //This method determines if the reordered quantity of a reorder log is larger than INT_MAX.
    public boolean overflows(String reorderquan) {
        String strquantity = reorderquan.substring(reorderquan.lastIndexOf('+') + 1); //everything after the plus sign, so just the number
        int intquantity = 0;

        try {
            intquantity = Integer.parseInt(strquantity);
            return false;
        } catch (NumberFormatException e) {
            System.out.println("Edit number is too large.");
            return true;
        }

    }

    public class InfoHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent e) {

            String itemClicked = ((Control)e.getSource()).getId();

            switch(itemClicked){
                case "infocancel":
                    cannotdelete.setVisible(false);
                    presscount = 0;
                    kettle.hideInfoStage();
                    if(kettle.isNotifStageShowing()){
                        kettle.primaryStage.showOpaqueLayer();
                    }
                    break;    
                default:
                    System.out.println("Otherstuff");
            }
        }
    }

    public class ColumnHandler implements ListChangeListener<TableColumn>{
        public boolean suspended;

            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    logtable.getColumns().setAll(dateloggedcol, quanloggedcol);
                    this.suspended = false;
                }
            }
    }

}