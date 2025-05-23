module lk.ijse.inp {
    requires javafx.controls;
    requires javafx.fxml;


    opens lk.ijse.inp to javafx.fxml;
    exports lk.ijse.inp;
}