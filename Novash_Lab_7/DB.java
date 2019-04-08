import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;


public class DB extends Application {
    private static String[] arguments;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    //public static AddElement addElement = null;

    private final TableView<Drug> tableDrugs = new TableView<>();
    private final TableView<Delivery> tableDeliveries = new TableView<>();
    private static final ObservableList<Drug> dataDrug = FXCollections.observableArrayList();
    private static final ObservableList<Delivery> dataDelivery = FXCollections.observableArrayList();
    final HBox hBox = new HBox(10);

    public static void main(String[] args) {
        connectionDB();
        CreateDB();
        launch(args);
        closeConnectionDB();
    }

    private static void connectionDB()
    {
        try {
            connection = null;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:drugStock.db");

            statement = connection.createStatement();
            String SQLSelect = "SELECT * from drugs";
            resultSet = statement.executeQuery(SQLSelect);

            while (resultSet.next()) {
                Drug drug = new Drug(resultSet.getString("drugID"), resultSet.getString("fulltitle"),
                        resultSet.getString("price"), resultSet.getString("weight"),
                        resultSet.getString("officialname"), resultSet.getString("trackDelivery"));
                dataDrug.add(drug);
            }

            SQLSelect = "SELECT * from deliveries";
            resultSet = statement.executeQuery(SQLSelect);

            while (resultSet.next()) {
                Delivery delivery = new Delivery(resultSet.getString("deliveryID"),
                        resultSet.getString("delivery_date"), resultSet.getString("delivery_provider"));
                dataDelivery.add(delivery);
            }


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CreateDB()
    {
        try {
            statement.execute("CREATE TABLE if not exists 'deliveries' " +
                    "('deliveryID' INTEGER PRIMARY KEY AUTOINCREMENT,  'delivery_date' text, 'delivery_provider' text);");

            statement.execute("CREATE TABLE if not exists 'drugs' ('drugrID' INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "'fulltitle' text, 'price' INT, 'weight' text, 'officialname' text," +
                    " 'trackDelivery' INTEGER REFERENCES deliveries(deliveryID) DEFERRABLE INITIALLY DEFERRED);");

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void start(Stage stage) {

        Scene scene = new Scene(new Group());
        stage.setTitle("Table drug stock");
        stage.setWidth(765);
        stage.setHeight(535);

        final Label label = new Label("Drug Stock");
        label.setFont(new Font("Arial", 20));


        tableDrugs.setEditable(true);
        tableDeliveries.setEditable(true);

        TableColumn<Drug, String> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(30);
        idCol.setMaxWidth(30);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        idCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) ->
//                        t.getTableView().getItems().get(
//                        t.getTablePosition().getRow()).setId(t.getNewValue()));


        TableColumn<Drug, String> fulltitleCol =
                new TableColumn<>("Full title");
        fulltitleCol.setPrefWidth(200);
        fulltitleCol.setMaxWidth(200);
        fulltitleCol.setCellValueFactory(new PropertyValueFactory<>("fulltitle"));
        fulltitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        fullnameCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setFullname(t.getNewValue());
//                });


        TableColumn<Drug, String> priceCol =
                new TableColumn<>("Price");
        priceCol.setPrefWidth(100);
        priceCol.setMaxWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        ageCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setAge(t.getNewValue());
//                });


        TableColumn<Drug, String> weightCol =
                new TableColumn<>("Weight");
        weightCol.setPrefWidth(100);
        weightCol.setMaxWidth(100);
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        genderCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setGender(t.getNewValue());
//                });


        TableColumn<Drug, String> officialNameCol =
                new TableColumn<>("Official name");
        officialNameCol.setPrefWidth(100);
        officialNameCol.setMaxWidth(100);
        officialNameCol.setCellValueFactory(new PropertyValueFactory<>("officialname"));
        officialNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        positionCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setPosition(t.getNewValue());
//                });

        TableColumn<Drug, String> trackDeliveryCol = new TableColumn<>("Delivery");
        trackDeliveryCol.setPrefWidth(100);
        trackDeliveryCol.setMaxWidth(100);
        trackDeliveryCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Drug, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Drug, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().trackDeliveryProperty();
                    }
                });


        tableDrugs.setItems(dataDrug);
        tableDrugs.getColumns().addAll(idCol, fulltitleCol, priceCol, weightCol, officialNameCol, trackDeliveryCol);
        tableDrugs.setMaxHeight(200);

        TableColumn<Delivery, String> DeliveryCol =
                new TableColumn<>("Del ID");
        DeliveryCol.setMinWidth(55);
        DeliveryCol.setPrefWidth(55);
        DeliveryCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Delivery, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Delivery, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().idProperty();
                    }
                });

        TableColumn<Delivery, String> DelDateCol =
                new TableColumn<>("Delivery date");
        DelDateCol.setMinWidth(125);
        DelDateCol.setPrefWidth(125);
        DelDateCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Delivery, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Delivery, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().dateProperty();
                    }
                });


        TableColumn<Delivery, String> DelProviderCol =
                new TableColumn<>("Provider");
        DelProviderCol.setMinWidth(450);
        DelProviderCol.setPrefWidth(450);
        DelProviderCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Delivery, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Delivery, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().providerProperty();
                    }
                });

        tableDeliveries.setItems(dataDelivery);
        tableDeliveries.getColumns().addAll(DeliveryCol, DelDateCol, DelProviderCol);
        tableDeliveries.setMaxHeight(200);


        final TextField addFullTitle = new TextField();
        addFullTitle.setMaxWidth(111);
        addFullTitle.setPromptText("Full title");

        final TextField addPrice = new TextField();
        addPrice.setMaxWidth(111);
        addPrice.setPromptText("Price");

        final TextField addWeight = new TextField();
        addWeight.setMaxWidth(111);
        addWeight.setPromptText("Weight");

        final TextField addOfficialName = new TextField();
        addOfficialName.setMaxWidth(111);
        addOfficialName.setPromptText("Official name");

        final TextField addDeliveryDate = new TextField();
        addDeliveryDate.setMaxWidth(111);
        addDeliveryDate.setPromptText("Delivery date");

        final TextField addDeliveryProvider = new TextField();
        addDeliveryProvider.setMaxWidth(120);
        addDeliveryProvider.setPromptText("Delivery provider");

        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            String deliveryID = contains(addDeliveryDate.getText(), addDeliveryProvider.getText());

            if (deliveryID.equals("-1")) {
                deliveryID = "" + (dataDelivery.size() + 1);
                dataDelivery.add(new Delivery(
                        "appear after restart",
                        addDeliveryDate.getText(),
                        addDeliveryProvider.getText()));
                insert(addDeliveryDate.getText(), addDeliveryProvider.getText());
            }

            dataDrug.add(new Drug(
                    //addID.getText(),
                    "appear after restart",
                    addFullTitle.getText(),
                    addPrice.getText(),
                    addWeight.getText(),
                    addOfficialName.getText(),
                    deliveryID));

            //write data in db
            insert(addFullTitle.getText(), addPrice.getText(), addWeight.getText(),
                    addOfficialName.getText(), deliveryID);

            addFullTitle.clear();
            addPrice.clear();
            addWeight.clear();
            addOfficialName.clear();
            addDeliveryDate.clear();
            addDeliveryProvider.clear();
        });

        hBox.getChildren().addAll(addFullTitle, addPrice, addWeight, addOfficialName, addDeliveryDate, addDeliveryProvider, addButton);
        hBox.setSpacing(5);

        final VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, tableDrugs, tableDeliveries, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

    }

    private void insert(String deliveryDate, String deliveryProvider) {
        String sql = "INSERT INTO deliveries(delivery_date, delivery_provider) VALUES(?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, deliveryDate);
            pstmt.setString(2, deliveryProvider);

            pstmt.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void insert(String fulltitle, String price, String weight, String officialname, String trackDelivery) {
        String sql = "INSERT INTO drugs(fulltitle,price,weight,officialname,trackDelivery) VALUES(?,?,?,?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, fulltitle);
            pstmt.setInt(2, Integer.parseInt(price));
            pstmt.setString(3, weight);
            pstmt.setString(4, officialname);
            pstmt.setInt(5, Integer.parseInt(trackDelivery));

            pstmt.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private String contains(String deliveryDate, String deliveryProvider) {
        for (Delivery delivery : dataDelivery) {
            if (delivery.getDate().equals(deliveryDate) && delivery.getProvider().equals(deliveryProvider)) {
                return delivery.getId();
            }
        }
        return "-1";
    }


    private static void closeConnectionDB(){
        try {
            resultSet.close();
            connection.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}