module com.example.projetoamc2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projetoamc2 to javafx.fxml;
    exports com.example.projetoamc2;
}