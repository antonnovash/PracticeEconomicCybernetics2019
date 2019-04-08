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

public class TableViewSample extends Application {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private final TableView<Person> tablePersons = new TableView<>();
    private final TableView<Department> tableDepartments = new TableView<>();
    private static final ObservableList<Person> dataPersons = FXCollections.observableArrayList();
    private static final ObservableList<Department> dataDepartmens = FXCollections.observableArrayList();
    final HBox hBox = new HBox(10);

    public static void main(String[] args) {
        connectionDB();
        CreateDB();
        launch(args);
        closeConnectionDB();
    }

    private static void connectionDB() {
        try {
            connection = null;
            Class.forName("org.sqlite.JDBC");
//            connection = DriverManager.getConnection("jdbc:sqlite:cardFile.db");
            connection = DriverManager.getConnection("jdbc:sqlite:drugStock.db");

            statement = connection.createStatement();
            String SQLSelect = "SELECT * from workers";
            resultSet = statement.executeQuery(SQLSelect);

            while (resultSet.next()) {
                Person person = new Person(resultSet.getString("workerID"), resultSet.getString("fullname"),
                        resultSet.getString("age"), resultSet.getString("gender"),
                        resultSet.getString("position"), resultSet.getString("trackDepartment"));
                dataPersons.add(person);
            }

            SQLSelect = "SELECT * from departments";
            resultSet = statement.executeQuery(SQLSelect);

            while (resultSet.next()) {
                Department dep = new Department(resultSet.getString("departmentID"), resultSet.getString("department_name"), resultSet.getString("description"));
                dataDepartmens.add(dep);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void CreateDB() {
        try {
//            statement = connection.createStatement();
            statement.execute("CREATE TABLE if not exists 'departments' " +
                    "('departmentID' INTEGER PRIMARY KEY AUTOINCREMENT,  'department_name' text, 'description' text);");

            statement.execute("CREATE TABLE if not exists 'workers' ('workerID' INTEGER PRIMARY KEY AUTOINCREMENT, 'fullname' text, " +
                    " 'age' INT, 'gender' text, 'position' text," +
                    " 'trackDepartment' INTEGER REFERENCES departments(departmentID) DEFERRABLE INITIALLY DEFERRED);");

//            statement.execute("CREATE TABLE IF NOT EXISTS 'drugstore'")
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void closeConnectionDB() {
        try {
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(new Group());
        stage.setTitle("Table card worker");
        stage.setWidth(675);
        stage.setHeight(535);

        final Label label = new Label("Card worker");
        label.setFont(new Font("Arial", 20));


        tablePersons.setEditable(true);
        tableDepartments.setEditable(true);


        TableColumn<Person, String> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(30);
        idCol.setMaxWidth(30);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        idCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) ->
//                        t.getTableView().getItems().get(
//                        t.getTablePosition().getRow()).setId(t.getNewValue()));


        TableColumn<Person, String> fullnameCol =
                new TableColumn<>("Full name");
        fullnameCol.setPrefWidth(200);
        fullnameCol.setMaxWidth(200);
        fullnameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        fullnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        fullnameCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setFullname(t.getNewValue());
//                });


        TableColumn<Person, String> ageCol =
                new TableColumn<>("Age");
        ageCol.setPrefWidth(100);
        ageCol.setMaxWidth(100);
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        ageCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setAge(t.getNewValue());
//                });


        TableColumn<Person, String> genderCol =
                new TableColumn<>("Gender");
        genderCol.setPrefWidth(100);
        genderCol.setMaxWidth(100);
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        genderCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setGender(t.getNewValue());
//                });


        TableColumn<Person, String> positionCol =
                new TableColumn<>("Position");
        positionCol.setPrefWidth(100);
        positionCol.setMaxWidth(100);
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionCol.setCellFactory(TextFieldTableCell.forTableColumn());
//        positionCol.setOnEditCommit(
//                (CellEditEvent<Person, String> t) -> {
//                    t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setPosition(t.getNewValue());
//                });

        TableColumn<Person, String> trackDepartmentCol = new TableColumn<>("Department");
        trackDepartmentCol.setPrefWidth(100);
        trackDepartmentCol.setMaxWidth(100);
        trackDepartmentCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Person, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Person, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().trackDepartmentProperty();
                    }
                });


        tablePersons.setItems(dataPersons);
        tablePersons.getColumns().addAll(idCol, fullnameCol, ageCol, genderCol, positionCol, trackDepartmentCol);
        tablePersons.setMaxHeight(200);

        TableColumn<Department, String> DepartmentCol =
                new TableColumn<>("Dep ID");
        DepartmentCol.setMinWidth(55);
        DepartmentCol.setPrefWidth(55);
        DepartmentCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Department, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Department, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().idProperty();
                    }
                });

        TableColumn<Department, String> DepNameCol =
                new TableColumn<>("Department name");
        DepNameCol.setMinWidth(125);
        DepNameCol.setPrefWidth(125);
        DepNameCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Department, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Department, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().NameProperty();
                    }
                });


        TableColumn<Department, String> DepInfoCol =
                new TableColumn<>("Description");
        DepInfoCol.setMinWidth(450);
        DepInfoCol.setPrefWidth(450);
        DepInfoCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Department, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Department, String> p) {
                        // p.getValue() returns the Person instance for a particular TableView row
                        return p.getValue().InfoProperty();
                    }
                });

        tableDepartments.setItems(dataDepartmens);
        tableDepartments.getColumns().addAll(DepartmentCol, DepNameCol, DepInfoCol);
        tableDepartments.setMaxHeight(200);


        final TextField addFullName = new TextField();
        addFullName.setMaxWidth(fullnameCol.getPrefWidth());
        addFullName.setPromptText("Full name");

        final TextField addAge = new TextField();
        addAge.setMaxWidth(ageCol.getPrefWidth());
        addAge.setPromptText("Age");

        final TextField addGender = new TextField();
        addGender.setMaxWidth(genderCol.getPrefWidth());
        addGender.setPromptText("Gender");

        final TextField addPosition = new TextField();
        addPosition.setMaxWidth(positionCol.getPrefWidth());
        addPosition.setPromptText("Position");

        final TextField addTrackDepartment = new TextField();
        addTrackDepartment.setMaxWidth(trackDepartmentCol.getPrefWidth());
        addTrackDepartment.setPromptText("Id department");


        final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            dataPersons.add(new Person(
                    //addID.getText(),
                    "appear after restart",
                    addFullName.getText(),
                    addAge.getText(),
                    addGender.getText(),
                    addPosition.getText(),
                    addTrackDepartment.getText()));

            //write data in db
            insert(addFullName.getText(), addAge.getText(), addGender.getText(),
                    addPosition.getText(), addTrackDepartment.getText());

            //addID.clear();
            addFullName.clear();
            addAge.clear();
            addGender.clear();
            addPosition.clear();
            addTrackDepartment.clear();
        });

        hBox.getChildren().addAll(addFullName, addAge, addGender, addPosition, addTrackDepartment, addButton);

        final VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, tablePersons, tableDepartments, hBox);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

    }

    private void insert(String fullname, String age, String gender, String position, String trackDepartment) {
        String sql = "INSERT INTO workers(fullname,age,gender,position,trackDepartment) VALUES(?,?,?,?,?)";
//
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                pstmt.setString(1, fullname);
//                pstmt.setInt(2, Integer.parseInt(age));
//                pstmt.setString(3, gender);
//                pstmt.setString(4, position);
//                pstmt.setInt(5, Integer.parseInt(trackDepartment));
//
//                pstmt.executeUpdate();
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//        }
    }

    public static class Department {
        private final SimpleStringProperty department_id;
        private final SimpleStringProperty department_name;
        private final SimpleStringProperty department_info;

        private Department(String dep_id, String dep_name, String dep_info) {

            this.department_id = new SimpleStringProperty(dep_id);
            this.department_name = new SimpleStringProperty(dep_name);
            this.department_info = new SimpleStringProperty(dep_info);

        }

        public String getId() {
            return department_id.get();
        }

        public SimpleStringProperty idProperty() {
            return department_id;
        }

        public void setId(String id) {
            this.department_id.set(id);
        }

        public String getName() {
            return department_name.get();
        }

        public SimpleStringProperty NameProperty() {
            return department_name;
        }

        public void setName(String name) {
            this.department_name.set(name);
        }

        public String getInfo() {
            return department_info.get();
        }

        public SimpleStringProperty InfoProperty() {
            return department_info;
        }

        public void setInfo(String info) {
            this.department_info.set(info);
        }
    }

    public static class Person {

        private final SimpleStringProperty id;
        private final SimpleStringProperty fullname;
        private final SimpleStringProperty age;
        private final SimpleStringProperty gender;
        private final SimpleStringProperty position;
        private SimpleStringProperty trackDepartment;

        private Person(String id, String fullname, String age, String gender,
                       String position, String trackDepartment) {

            this.id = new SimpleStringProperty(id);
            this.fullname = new SimpleStringProperty(fullname);
            this.age = new SimpleStringProperty(age);
            this.gender = new SimpleStringProperty(gender);
            this.position = new SimpleStringProperty(position);
            this.trackDepartment = new SimpleStringProperty(trackDepartment);

        }

        public String getId() {
            return id.get();
        }

        public SimpleStringProperty idProperty() {
            return id;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public String getFullname() {
            return fullname.get();
        }

        public SimpleStringProperty fullnameProperty() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname.set(fullname);
        }

        public String getAge() {
            return age.get();
        }

        public SimpleStringProperty ageProperty() {
            return age;
        }

        public void setAge(String age) {
            this.age.set(age);
        }

        public String getGender() {
            return gender.get();
        }

        public SimpleStringProperty genderProperty() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender.set(gender);
        }

        public String getPosition() {
            return position.get();
        }

        public SimpleStringProperty positionProperty() {
            return position;
        }

        public void setPosition(String position) {
            this.position.set(position);
        }

        public String getTrackDepartment() {
            return trackDepartment.get();
        }

        SimpleStringProperty trackDepartmentProperty() {
            return trackDepartment;
        }

        public void setTrackDepartment(String trackDepartment) {
            this.trackDepartment.set(trackDepartment);
        }
    }
}