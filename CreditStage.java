import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.application.HostServices;


public class CreditStage extends Stage{

	private String stripcolour;
	private String midcolour;
	private final String striphex = "#38074a;";
    private final String midhex = "#d4c7d9;";
	private final double creditwidth = 620.0;
	private final double creditheight = 440.0;
	private final String fbio = "Frank Yi is currently studying Computer Science at the University of Waterloo. " + 
							 	"He enjoys programming and hopes to make a few video games in the future.";
	private final String rbio = "I like to steal gum from Frank. It took me about one month to get my own gum. " + 
								"By then I probably already stole about $20 worth of gum.";

	private static AnchorPane credtstrip = new AnchorPane();
	private static AnchorPane centerstrip = new AnchorPane();
	private static AnchorPane credbstrip = new AnchorPane();
	private static Image franklogo = new Image("./Misc/frank.jpg");
	private static ImageView frankview = new ImageView();
	private static Image rennielogo = new Image("./Misc/rennie.jpg");
	private static ImageView rennieview = new ImageView();
	private static Button frankgit = new Button();
	private static Tooltip franktip = new Tooltip("View Frank's GitHub"); 
	private static Button renniegit = new Button();
	private static Tooltip rennietip = new Tooltip("View Rennie's GitHub"); 

	private static BorderPane creditpane = new BorderPane();
	private static Label frankname = new Label();
	private static TextArea frankbio = new TextArea();
	private static Label renniename = new Label();
	private static TextArea renniebio = new TextArea();
	private static Text heading = new Text();
	private static Button close = new Button();
	private static Kettlelog kettle = new Kettlelog();

	CreditStage(){

		stripcolour = String.format("-fx-background-color: %s", striphex);
		midcolour = String.format("-fx-background-color: %s", midhex);
	    //================================================================================
	    // TOP
	    //================================================================================
		heading.setFont(new Font(18));
		heading.setFill(Color.WHITE);
		heading.setText("Contributors");
	        AnchorPane.setLeftAnchor(heading, 20.0);
	        AnchorPane.setBottomAnchor(heading, 10.0);

       	credtstrip.setPrefSize(creditwidth, 60); //(width, height)
        credtstrip.getChildren().addAll(heading);
        credtstrip.setStyle(stripcolour);

	    //================================================================================
	    // CENTER 
	    //================================================================================
        AnchorPane.setTopAnchor(frankname, 20.0);
        AnchorPane.setLeftAnchor(frankname, 180.0);
	        frankname.setFont(new Font(16));
	        frankname.setPrefHeight(40.0);
	        frankname.setPrefWidth(120.0);
	        frankname.setFont(new Font(16));
	       	frankname.setAlignment(Pos.CENTER);
	        frankname.setTextOverrun(OverrunStyle.ELLIPSIS);
	        frankname.setStyle("-fx-background-color: #c4b1c9");
	        frankname.setText("Frank Yi");

        AnchorPane.setLeftAnchor(frankbio, 180.0);
        AnchorPane.setTopAnchor(frankbio, 80.0);
	        frankbio.setPrefWidth(300.0);
	        frankbio.setPrefHeight(80.0);
	        frankbio.setEditable(false);
	       	frankbio.setStyle("-fx-opacity: 1;");
	        frankbio.setWrapText(true);
	        frankbio.setText(fbio);

       	AnchorPane.setLeftAnchor(frankview, 20.0);
        AnchorPane.setTopAnchor(frankview, 20.0);
	     	frankview.setFitHeight(140);
	        frankview.setFitWidth(140);
	        frankview.setImage(franklogo);

        AnchorPane.setRightAnchor(frankgit, 20.0);
        AnchorPane.setTopAnchor(frankgit, 75.0); 
        Image frankgitImg = new Image("./Misc/git.png");
        ImageView frankgitView = new ImageView();
        	franktip.setShowDelay(new javafx.util.Duration(75.0));
        	frankgit.setTooltip(franktip);
            frankgit.setStyle("-fx-background-color: transparent;");             
            frankgitView.setImage(frankgitImg);
            frankgitView.setFitWidth(80);
            frankgitView.setFitHeight(80);
            frankgitView.setPreserveRatio(true);
            frankgitView.setSmooth(true);
            frankgitView.setCache(true); 
            frankgit.setGraphic(frankgitView);

        AnchorPane.setBottomAnchor(renniename, 120.0);
        AnchorPane.setRightAnchor(renniename, 180.0);
	        renniename.setFont(new Font(16));
	        renniename.setPrefHeight(40.0);
	        renniename.setPrefWidth(160.0);
	        renniename.setFont(new Font(16));
	       	renniename.setAlignment(Pos.CENTER);
	        renniename.setTextOverrun(OverrunStyle.ELLIPSIS);
	        renniename.setStyle("-fx-background-color: #c4b1c9");
	        renniename.setText("Rennie Huang");

        AnchorPane.setRightAnchor(renniebio, 180.0);
        AnchorPane.setBottomAnchor(renniebio, 20.0);
	        renniebio.setPrefWidth(300.0);
	        renniebio.setPrefHeight(80.0);
	        renniebio.setEditable(false);
	       	renniebio.setStyle("-fx-opacity: 1;");
	        renniebio.setWrapText(true);
	        renniebio.setText(rbio);

       	AnchorPane.setRightAnchor(rennieview,20.0);
        AnchorPane.setBottomAnchor(rennieview, 20.0);
	     	rennieview.setFitHeight(140);
	        rennieview.setFitWidth(140);
	        rennieview.setImage(rennielogo);

        AnchorPane.setLeftAnchor(renniegit, 20.0);
        AnchorPane.setBottomAnchor(renniegit, 15.0); 
        Image renniegitImg = new Image("./Misc/git.png");
        ImageView renniegitView = new ImageView();
        	rennietip.setShowDelay(new javafx.util.Duration(75.0));
        	renniegit.setTooltip(rennietip);
            renniegit.setStyle("-fx-background-color: transparent;");             
            renniegitView.setImage(renniegitImg);
            renniegitView.setFitWidth(80);
            renniegitView.setFitHeight(80);
            renniegitView.setPreserveRatio(true);
            renniegitView.setSmooth(true);
            renniegitView.setCache(true); 
            renniegit.setGraphic(renniegitView);
 
	    centerstrip.getChildren().addAll(frankname, frankbio, renniename, renniebio, frankview, rennieview, frankgit, renniegit);
        centerstrip.setStyle(midcolour);
	    //================================================================================
	    // BOTTOM
	    //================================================================================
        close.setText("Close");
        close.setStyle("-fx-background-color: #d4c7d9;");
       	close.setSkin(new FadeButtonSkin(close));
        close.setPrefHeight(30);
        	AnchorPane.setRightAnchor(close, 5.0);
        	AnchorPane.setBottomAnchor(close, 5.0);

        credbstrip.setPrefSize(creditwidth, 40);
        credbstrip.getChildren().addAll(close);
        credbstrip.setStyle(stripcolour);

	    //================================================================================
	    // FINALIZATION
	    //================================================================================
        creditpane.setTop(credtstrip);
        creditpane.setCenter(centerstrip);
        creditpane.setBottom(credbstrip);

		this.setResizable(false);
        this.setScene(new Scene(creditpane, creditwidth, creditheight));
        this.initOwner(kettle.getPrimaryStage());
        this.initStyle(StageStyle.UNDECORATED);
        this.initModality(Modality.WINDOW_MODAL);

	}

	public void showCreditStage(){

		frankgit.setOnAction(ev -> kettle.openURL(0));
		renniegit.setOnAction(ev -> kettle.openURL(1));

		close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                    kettle.hideCreditStage();
                }
            });

	}
}