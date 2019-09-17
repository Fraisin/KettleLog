import Item.*;
import Notif.*;
import java.text.*;
import java.util.*;
import javafx.util.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.*;
import javafx.collections.transformation.*;


public class NotifContainer{

	//Components
    private Button readBtn = new Button();
    private Image read = new Image("./Misc/Read.png");
    private Image unread = new Image("./Misc/Unread.png");
    private ImageView readImg = new ImageView();
    private Tooltip readTP = new Tooltip("Mark read");
    private Label message = new Label("Message");
    private Button linkBtn = new Button();
    private Image linkBtnImg = new Image("./Misc/link.png");
    private ImageView linkImg = new ImageView();  
    private Tooltip linkTP = new Tooltip("View");
    private Button delBtn = new Button();
    private Image delBtnImg = new Image("./Misc/delete2.png");
    private ImageView delImg = new ImageView();    
    private Tooltip delTP = new Tooltip("Delete");
    
    //controls
    private HBox hb;
    private Notif notif = new Notif();
    private static Kettlelog kettle = new Kettlelog();

    public NotifContainer(){
    	hb = notifBox("N/A", "N/A", 0, "N/A", "N/A");
    }

    public NotifContainer(Notif notif){
    	this.notif = notif;
    	hb = notifBox(notif.getMessage(), notif.getItemId(), notif.getReadStatus(), notif.getNotifId(), notif.getDateGenerated());
    }

    public HBox notifBox(String messageStr, String linkId, int readStatus, String notifId, String dateGenerated){
    	hb = new HBox(10);
    	NotifHandler notifHandler = new NotifHandler();

        readBtn.setStyle("-fx-background-color: transparent;");             
            readImg.setImage(read);
            readImg.setFitWidth(20);
            readImg.setPreserveRatio(true);
            readImg.setSmooth(true);
            readImg.setCache(true); 
        readBtn.setGraphic(readImg);

        readTP.setShowDelay(new javafx.util.Duration(100.0));
        readBtn.setTooltip(readTP);
        readBtn.setId("readBtn"); 
        readBtn.setOnAction(notifHandler);

        message.setPrefWidth(285.0);
        message.setPrefHeight(30.0);

        linkBtn.setStyle("-fx-background-color: transparent;");             
            linkImg.setImage(linkBtnImg);
            linkImg.setFitWidth(20);
            linkImg.setPreserveRatio(true);
            linkImg.setSmooth(true);
            linkImg.setCache(true); 
        linkBtn.setGraphic(linkImg);

        linkTP.setShowDelay(new javafx.util.Duration(100.0));
        linkBtn.setTooltip(linkTP);   
        linkBtn.setId("linkBtn");
        linkBtn.setOnAction(notifHandler);

        if(linkId.equals("N/A")){
            linkBtn.setDisable(true);
        }
        else{
            linkBtn.setDisable(false);
        }

        delBtn.setStyle("-fx-background-color: transparent;");             
            delImg.setImage(delBtnImg);
            delImg.setFitWidth(20);
            delImg.setPreserveRatio(true);
            delImg.setSmooth(true);
            delImg.setCache(true); 
        delBtn.setGraphic(delImg); 

        delTP.setShowDelay(new javafx.util.Duration(100.0));
        delBtn.setTooltip(delTP);  
        delBtn.setOnAction(notifHandler);
        delBtn.setId("delBtn");

        hb.setStyle("-fx-background-color: #ffbe5c;");
        hb.setPadding(new Insets(10, 10, 10, 10));
        hb.getChildren().addAll(readBtn, message, linkBtn, delBtn);
    	return hb;
    }

    public void setNotifVisible(boolean value){
    	hb.setVisible(value);
    }

    public boolean getNotifVisible(){
    	return hb.isVisible();
    }

    public void setMessage(String text){
    	message.setText(text);
    }

    public void setNotifReadStatus(int rd){
    	notif.setReadStatus(rd);
    }

    public void updateNotifContainer(Notif notif){
    	this.notif = notif;
    	message.setText(notif.getMessage());

    	if(notif.getItemId().equals("N/A")){
            linkBtn.setDisable(true);
        }
        else{
            linkBtn.setDisable(false);
        }

        if(kettle.getItemById(notif.getItemId())==null){
            linkBtn.setDisable(true);
        }

        if(notif.getReadStatus()==1){//read
            hb.setOpacity(0.5);
            readImg.setImage(unread);
            readTP.setText("Mark unread");
        }
        else if(notif.getReadStatus()==0){//unread
            hb.setOpacity(1);
            readImg.setImage(read);
            readTP.setText("Mark read");
        }

    }

    public Notif getNotif(){
    	return notif;
    } 

    public HBox getNotifBox(){
    	return hb;
    }   


    public class NotifHandler implements EventHandler<ActionEvent>{
    	@Override
        public void handle(ActionEvent e) {

            String itemClicked = ((Control)e.getSource()).getId();

            switch(itemClicked){
            	case "readBtn":
	            	if(notif.getReadStatus()==0){ //if unread, mark as read
	                    notif.setReadStatus(1);
	                    hb.setOpacity(0.5);
			            readImg.setImage(unread);
			            readTP.setText("Mark unread");
	                }
	                else if (notif.getReadStatus()==1){
                        notif.setReadStatus(0);
                        hb.setOpacity(1);
			            readImg.setImage(read);
			            readTP.setText("Mark read");
	                }
            		break;
            	case "delBtn":
            		hb.setVisible(false);
                    notif.setReadStatus(-1);
            		break;
                case "linkBtn":
                	Item it = kettle.getItemById(notif.getItemId());
                	kettle.showInfoStage(it);
                    break;

	            default:
	            	System.out.println("other");

            }
        }
    }

}