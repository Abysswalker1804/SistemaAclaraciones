module org.example.sistemaaclaraciones {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.sistemaaclaraciones to javafx.fxml;
    exports org.example.sistemaaclaraciones;
}