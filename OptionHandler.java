import javafx.beans.value.*;

public class OptionHandler implements ChangeListener<String>{

    private static Kettlelog kettle = new Kettlelog();
    private static OptionComparators object = new OptionComparators(); //class will get data and table itself
    private static int optionSel = 0;

    @Override
    public void changed(ObservableValue ov, String oldValue, String newValue){

        switch(newValue){
            case "Sort by: Starred":
                optionSel = 1;
                object.sortByStarred();
                kettle.clearSearchBar();
                break;
            case "Sort by: Most Recent":
                optionSel = 2;
                object.sortByMostRecent(2);
                kettle.clearSearchBar();
                break;
            case "Sort by: Oldest Added":
                optionSel = 3;
                object.sortByMostRecent(3);
                kettle.clearSearchBar();
                break;
            case "Select All":
                kettle.setAllChecked(true);
                kettle.primaryStage.updatePrimaryStage(kettle.getData());
                kettle.primaryStage.resetComboBox();
                break;
            case "None":
                kettle.primaryStage.resetComboBox();
                kettle.clearSearchBar();
                optionSel = 0;
            default:
                optionSel = 0;
        }

    }
    
    public int getOptionSel(){
        return optionSel;
    }

}