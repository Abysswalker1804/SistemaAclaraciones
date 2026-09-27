module org.example.sistemaaclaraciones {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.sistemaaclaraciones to javafx.fxml;
    exports org.example.sistemaaclaraciones;
}