module org.example.sistemaaclaraciones {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens org.example.sistemaaclaraciones to javafx.fxml;
    exports org.example.sistemaaclaraciones;
}