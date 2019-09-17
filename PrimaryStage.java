import Log.*;
import Item.*;
import java.util.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.scene.Scene;
import javafx.collections.*;
import javafx.scene.image.*;
import javafx.beans.value.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.*;
import javafx.scene.input.MouseEvent;
import javafx.collections.transformation.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class PrimaryStage extends Stage{

    private static double screenX = 0.0;
    private static double screenY = 0.0;

    private static double screenWidth = 1024;
    private static double screenHeight =0;
    private static double spacefromtable = 7.5;
    private static double tooltipduration = 75.0;

    //UI Items
	private static TextField searchbar = new TextField();
	private static MenuBar kettlemenu = new MenuBar();
    private static Region opaqueLayer = new Region();
    private static Button removeBtn = new Button();
    private static Button addBtn = new Button();
    private static Button notifBtn = new Button();
    private static AnchorPane topBar = new AnchorPane();
    private static TranslateTransition tt = new TranslateTransition();
    private static PauseTransition pt = new PauseTransition();
    private static SequentialTransition st;

    private static Image notifGreen = new Image("./Misc/notifGreen.png");
    private static Image notifRed = new Image("./Misc/notifRed.png");
    private static ImageView notifImage = new ImageView();

    private static TableView<Item> table = new TableView<Item>();
    private static final String[] titles = {"Name","Status","Quantity","Minimum"};
    private static TableColumn<Item, String> buttoncolumn = new TableColumn<>("");
    private static ObservableList<Log> emptylist = FXCollections.observableArrayList();
    private static Item empty = new Item("emptyid", "", "", "", "", "", "", false, false, "", "", emptylist, "0.0", "N/A", "N/A");
    private static ObservableList<String> optionList = FXCollections.observableArrayList("Sort by: Starred", "Sort by: Most Recent", "Sort by: Oldest Added", "Select All", "None");
    private static String[] emptyinfo = {"", "", "", "", "", ""};
    private static ObservableList<Item> itemsToDelete;
    private static String nosupport = "Kettlelog currently does not support creating and opening multiple tables. This feature may become available in a future release.";
    private static Alert a1 = new Alert(AlertType.NONE, nosupport, ButtonType.OK); 
    private static String atleastone = "Please have at least one item checked for this action.";
    private static Alert a2 = new Alert(AlertType.NONE, atleastone, ButtonType.OK); 
    private static String onlyone = "Please have exactly one item checked for this action.";
    private static Alert a3 = new Alert(AlertType.NONE, onlyone, ButtonType.OK); 

    private static TableColumn<Item, String>[] itemArray = (TableColumn<Item, String>[]) new TableColumn[titles.length];
    private static BorderPane base = new BorderPane();
    private ComboBox<String> optionBox;

    //Handler eventHandler = new Handler();
    Kettlelog kettle = new Kettlelog();
    private static OptionHandler optionListener = new OptionHandler();


	PrimaryStage(){ //TEMP: opaque should belong to PS. make set method to access from main
        //System.out.println("===============herePri");

	 	this.setResizable(false);
        this.setTitle("KettleLog");
        
        opaqueLayer.setStyle("-fx-background-color: #001a34;");
        opaqueLayer.setOpacity(0.7);
        opaqueLayer.setVisible(false); 
        
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        ////////////////////////////////////////////////////////////////Menu Bar

        //FILE SUBMENU
        Menu file = new Menu("File");
            //At the moment, New and Open are not supported so we'll just show a dialog box instead.
            MenuItem newtable = new Menu("New");
                newtable.setId("nosupport");
            MenuItem opentable = new Menu("Open");
                opentable.setId("nosupport");
            //Exit Tab.
            MenuItem exit = new Menu("Exit");
                exit.setId("exit");

        //EDIT SUBMENU
        Menu edit = new Menu("Edit");
            MenuItem add = new Menu("Add Item");
                add.setId("additem");
            MenuItem emenu = new Menu("Edit Item");
                emenu.setId("edititem");
            MenuItem remove = new Menu("Remove Item");
                remove.setId("removeMenu");

        //VIEW SUBMENU
        Menu view = new Menu("View");
            MenuItem notifs = new Menu("Notifications");
                notifs.setId("notifMenu");
            MenuItem iteminfo = new Menu("Item Info");
                iteminfo.setId("showinfo");

        //INFO SUBMENU
        Menu info = new Menu("Info");
            MenuItem tutorial = new Menu("Tutorial");
            tutorial.setId("tutorial");
            MenuItem credits = new Menu("Contributors");
                credits.setId("credits");
        
        //ADDING MENUITEMS TO THEIR RESPECTIVE MENUS
        file.getItems().addAll(newtable, opentable, exit);
        edit.getItems().addAll(add, emenu, remove);
        view.getItems().addAll(notifs, iteminfo);
        info.getItems().addAll(tutorial, credits);
        kettlemenu.getMenus().addAll(file, edit, view, info);

        //Setting Handlers for the Menu items
        MenuHandler menuHandler = new MenuHandler();  
            exit.setOnAction(menuHandler);
            newtable.setOnAction(menuHandler);
            opentable.setOnAction(menuHandler);
            add.setOnAction(menuHandler);
            emenu.setOnAction(menuHandler);
            remove.setOnAction(menuHandler);
            notifs.setOnAction(menuHandler);
            iteminfo.setOnAction(menuHandler);
            tutorial.setOnAction(menuHandler);
            credits.setOnAction(menuHandler);

        //================================================================================
        // TABLE
        //================================================================================

        table.setFixedCellSize(40.0);

        //decide how any rows in table
        System.out.println("Your computer's screen height is " + screenBounds.getHeight());
        
        if (screenBounds.getHeight() > 800) {
            kettle.setLargeScreenTrue();
            screenHeight = 740;
        } else {
            kettle.setLargeScreenFalse();
            screenHeight = screenBounds.getHeight() - 50;
        }

        double numRows = Math.floor((screenHeight-70-150-28)/40);
        //70 = minimum space from bottom (blue)
        //150 = height of topbar + kettlemenu
        //28 = height of table header
        //Each row is 40 tall

        table.setPrefSize(300, numRows*40+28);
        table.setPlaceholder(new Label("Sorry, your search did not match any item names."));

        buttoncolumn.prefWidthProperty().bind(table.widthProperty().multiply(0.1980));

        //COLUMN TITLES       
        for(int i=0; i<titles.length; i++)
        {
            TableColumn<Item, String> item = new TableColumn<Item, String>(titles[i]);
            item.setCellValueFactory(new PropertyValueFactory<>(titles[i].toLowerCase()));
            item.prefWidthProperty().bind(table.widthProperty().multiply(0.1980));
            item.setStyle( "-fx-alignment: CENTER-LEFT;");
            item.setResizable(false);
            itemArray[i] = item;
        }

        table.getColumns().<Item, String>addAll(buttoncolumn, itemArray[0], itemArray[1], itemArray[2], itemArray[3]);

        ColumnHandler columnChange = new ColumnHandler();
        table.getColumns().addListener(columnChange);  

        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() >= 1) {
                System.out.println("PRINTING ROW INFORMATION BELOW.");
                //onSelection();
            }
        });

        VBox tableBox = new VBox(table);

        //================================================================================
        // TOP BAR (includes add/remove, Option, search)
        //================================================================================

        //ADD BUTTON
        Tooltip addTP = new Tooltip("Add Item");
        addTP.setShowDelay(new javafx.util.Duration(tooltipduration));
        addBtn.setTooltip(addTP);
        addBtn.setId("addBtn");
        Image addIcon = new Image("./Misc/addBtnIcon.png");
        ImageView addImage = new ImageView();
        addImage.setFitHeight(35);
        addImage.setFitWidth(35);        
        addImage.setImage(addIcon);
        addImage.setPreserveRatio(true);
        addImage.setSmooth(true);
        addImage.setCache(true); 
        addBtn.setStyle("-fx-background-color: transparent;");     
        addBtn.setGraphic(addImage); 

        //REMOVE BUTTON
        Tooltip rmTP = new Tooltip("Remove Item");
        rmTP.setShowDelay(new javafx.util.Duration(tooltipduration));
        removeBtn.setTooltip(rmTP);
        removeBtn.setId("removeBtn"); 
        removeBtn.setDisable(true);
        Image rmIcon = new Image("./Misc/delBtnIcon.png");
        ImageView rmImage = new ImageView();
        rmImage.setFitHeight(35);
        rmImage.setFitWidth(35);        
        rmImage.setImage(rmIcon);
        rmImage.setPreserveRatio(true);
        rmImage.setSmooth(true);
        rmImage.setCache(true); 
        removeBtn.setStyle("-fx-background-color: transparent;");     
        removeBtn.setGraphic(rmImage); 

        //Notification
        Tooltip notifTP = new Tooltip("Notifications");
        notifTP.setShowDelay(new javafx.util.Duration(tooltipduration));
        notifBtn.setTooltip(notifTP);
        notifBtn.setId("notifBtn"); 
        
        notifImage.setFitHeight(35);
        notifImage.setFitWidth(35);        
        notifImage.setImage(notifGreen);
        notifImage.setPreserveRatio(true);
        notifImage.setSmooth(true);
        notifImage.setCache(true); 
        notifBtn.setStyle("-fx-background-color: transparent;");     
        notifBtn.setGraphic(notifImage); 

        //================================================================================
        // ADD AND REMOVE BUTTON FUNCTIONALITY
        //================================================================================
        ButtonHandler eventHandler = new ButtonHandler();  
        addBtn.setOnAction(eventHandler);
        removeBtn.setOnAction(eventHandler);
        notifBtn.setOnAction(eventHandler);
        
        //POSITIONS OF ADD AND REMOVE
        AnchorPane.setRightAnchor(addBtn, 100.0);
        AnchorPane.setBottomAnchor(addBtn, spacefromtable-10);
        AnchorPane.setRightAnchor(removeBtn, 55.0);
        AnchorPane.setBottomAnchor(removeBtn, spacefromtable-10);
        AnchorPane.setRightAnchor(notifBtn, 155.0);
        AnchorPane.setBottomAnchor(notifBtn, spacefromtable-5);
       
        //SEARCH BAR
        searchbar.setPrefWidth(screenWidth / 2.56);
        searchbar.setPromptText("Search");
        AnchorPane.setLeftAnchor(searchbar, 305.0); 
        AnchorPane.setBottomAnchor(searchbar, spacefromtable);

        
        FilteredList<Item> filteredData = new FilteredList<>(kettle.getData(), p -> true);  

        //SEARCHBAR FUNCTIONALITY
        searchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Item -> {

                // If there is nothing in the search bar, we want to display all the items. 
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // lowercasesearch is referring to what the user is searching in the search bar
                String lowercasesearch = newValue.toLowerCase();
                if (String.valueOf(Item.getName()).toLowerCase().contains(lowercasesearch)) {
                    return true;
                } else {
                    return false;
                }
            });

            SortedList<Item> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());   
            table.setItems(sortedData);

        }); 

  
        //OPTIONS COMBOBOX
        optionBox = new ComboBox<String>(optionList);
            optionBox.setPromptText("Options");
            optionBox.setPrefWidth(150.0);
    
        optionBox.valueProperty().addListener(optionListener);

        AnchorPane.setLeftAnchor(optionBox, 52.0);
        AnchorPane.setBottomAnchor(optionBox, spacefromtable);

        //KETTLELOG LOGO
        Image kettleimage = new Image("./Misc/Logo.png");
        ImageView logo = new ImageView();
        logo.setFitHeight(100);
        logo.setFitWidth(200);
        logo.setImage(kettleimage);

        AnchorPane.setLeftAnchor(logo, 50.0);
        AnchorPane.setTopAnchor(logo, 10.0);

        //TOPBAR GENERAL INFORMATION 
        topBar.setStyle("-fx-background-color: #004080;");
        topBar.setPrefSize(100, 150);
        topBar.getChildren().addAll(logo, notifBtn, addBtn, removeBtn, searchbar, optionBox);

        //ANIMATION
        tt.setDuration(javafx.util.Duration.millis(300));
        tt.setNode(notifBtn);
        tt.setFromY(0.0);
        tt.setToY(-10.0);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.setInterpolator(Interpolator.LINEAR);
        pt.setDuration(javafx.util.Duration.millis(1000));
        st = new SequentialTransition(tt, pt);
        st.setCycleCount(-1);
        
        //================================================================================
        // FINALIZATION
        //================================================================================

        BorderPane main = new BorderPane();
        main.setStyle("-fx-background-color: #004080;");
        BorderPane.setMargin(tableBox, new Insets(10, 50, 0, 50)); //top right bottom left
        main.setCenter(tableBox);
        main.setTop(topBar);

        base.setTop(kettlemenu);
        base.setCenter(main);

        StackPane root = new StackPane();
        root.getChildren().addAll(base, opaqueLayer);

        //SHOW SCENE (13 inch laptops are 1280 by 800)

        this.setOnCloseRequest(event -> {
            // do some stuff...
            Platform.exit();
            System.exit(0);
            event.consume();
        });

        this.setScene(new Scene(root, screenWidth, screenHeight));
        a1.initOwner(this);
        a2.initOwner(this);
        a3.initOwner(this);

        screenX = (screenBounds.getWidth() - screenWidth) / 2;
        screenY = (screenBounds.getHeight() - screenHeight) / 2;

        this.setX(screenX); 
        this.setY(screenY);
    }

    public void showOpaqueLayer(){
        opaqueLayer.setVisible(true); 
    }

    public void hideOpaqueLayer(){
        opaqueLayer.setVisible(false);
    }

    public double getScreenHeight(){
        return screenHeight;
    }

    public void updatePrimaryStage(ObservableList<Item> data){

        if(!data.get(0).getName().equals("")){ //if empty
            AddButtonCell cell = new AddButtonCell(); 
            buttoncolumn.setCellFactory(cell);
        }
        else{
            BlankCell blankCell = new BlankCell();
            buttoncolumn.setCellFactory(blankCell);
        }
        table.setItems(data);
    }

    public void updateNotifIcon(){
        if(kettle.hasUnreadNotif()){
            setNotifBounce(true);
            notifImage.setImage(notifRed);
        }
        else{
            setNotifBounce(false);
            notifImage.setImage(notifGreen);
        }
    }

    public void enableRemoveBtn(){
        removeBtn.setDisable(false);
    }

    public void disableRemoveBtn(){
        removeBtn.setDisable(true);

    }

    public void clearSearchBar(){
        searchbar.clear();
    }

    private void setNotifBounce(boolean bounce) {
        if(bounce){
            st.play();
        }
        else{
            st.stop();
        }
    }

    public void resetComboBox(){
        //remove the combobox
        topBar.getChildren().remove(optionBox);

        //create a new one
        optionBox = new ComboBox<String>(optionList);
        optionBox.setPromptText("Options");
        optionBox.setPrefWidth(150.0);

        AnchorPane.setLeftAnchor(optionBox, 52.0);
        AnchorPane.setBottomAnchor(optionBox, spacefromtable);

        optionBox.valueProperty().addListener(optionListener);
        topBar.getChildren().add(optionBox);
    }

    public class ColumnHandler implements ListChangeListener<TableColumn>{
        public boolean suspended;

            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    table.getColumns().setAll(buttoncolumn, itemArray[0], itemArray[1], itemArray[2], itemArray[3]);
                    this.suspended = false;
                }
            }
    }

    public class ButtonHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent e) {

            String itemClicked = ((Control)e.getSource()).getId();

            switch(itemClicked){
                case "addBtn":
                    kettle.showAddStage(0, emptyinfo, empty);
                    break;
                case "removeBtn":
                    //pass to kettlelog to see which items are checked
                    //pass into alert stage for confirmation
                    //if yes, alert stage passes back into kettlelog to set data
                    itemsToDelete=kettle.getCheckedItems();
                    kettle.showAlertStage(1, itemsToDelete, empty);
                    itemsToDelete.clear();
                    break;  
                case "notifBtn":
                    kettle.showNotifStage();
                    break;  
                default:
                    System.out.println("Default.");
            }
        }
    }

    public class MenuHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {

            MenuItem mItem = (MenuItem) event.getSource();
            String itemClicked = mItem.getId();

            switch(itemClicked){
                case "exit":
                    System.out.println("Exiting...");
                    Platform.exit();
                    System.exit(0);
                    break;
                case "nosupport":
                    a1.show(); 
                    break;
                case "additem":
                    kettle.showAddStage(0, emptyinfo, empty);
                    break;
                case "edititem":
                    ObservableList<Item> checked = kettle.getCheckedNoEmpty();
                    int length = checked.size();
                    System.out.println("The number of checked items is " + length);
                    //The user can only edit one item at a time, so we need to make sure there's only one item checked.
                    if (length == 1) {
                        Item test = checked.get(0);
                        String[] editinfo = {test.getName(), test.getQuantity(), test.getMinimum(), test.getDelivery(), test.getDesc(), test.getDate()};        
                        kettle.showAddStage(1, editinfo, test);     
                    } else {
                        a3.show();
                    }
                    break;
                case "showinfo":
                    ObservableList<Item> checked2 = kettle.getCheckedNoEmpty();
                    int length2 = checked2.size();
                    if (length2 == 1) {
                        Item rowinfo = checked2.get(0);      
                        kettle.showInfoStage(rowinfo);   
                    } else {
                        a3.show();
                    }
                    break;
                case "credits":
                    kettle.showCreditStage();
                    break;
                case "removeMenu":
                    if(kettle.getNumCheckedItems()==0){
                        a2.show();
                    }
                    else{
                        itemsToDelete=kettle.getCheckedItems();
                        kettle.showAlertStage(1, itemsToDelete, empty);
                        itemsToDelete.clear();
                    }

                    break;
                case "notifMenu":
                    kettle.showNotifStage();
                    break;
                case "tutorial":
                    kettle.showTutorialStage();
                    break;
                default:
                    System.out.println("Default.");
            }
        }
    }


}