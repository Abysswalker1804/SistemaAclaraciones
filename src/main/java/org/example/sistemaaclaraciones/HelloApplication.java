package org.example.sistemaaclaraciones;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import org.example.sistemaaclaraciones.Enums.EstadosSAE;
import org.fxmisc.richtext.CodeArea;

public class HelloApplication extends Application {
    private CodeArea consola;
    private TextField input;
    private String prompt="SAE > ";
    private Label lblPrompt;
    private EstadosSAE estado_actual=EstadosSAE.BASE;
    int inputStart;
    
    //Constantes
    private final int lognitud_prompt_base=30;
    private final int longitud_prompt_inscr=60;
    private final int longitud_prompt_fin=50;
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene=CrearUI();
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        stage.setTitle("Sistema de Aclaraciones");
        stage.setScene(scene);
        stage.show();
    }

    private Scene CrearUI() {
        consola = new CodeArea();
        consola.setEditable(false);
        consola.setWrapText(true);
        consola.getStyleClass().add("consola");
        consola.setPrefSize(640, 600);
        consola.appendText("""
            Bienvenido a S.A.E. (Sistema de Aclaraciones Embebido)
            Para recibir información del sistema, puede teclear "?"
            """);

        input = new TextField();
        input.getStyleClass().add("consola");
        input.setPrefWidth(580);
        Platform.runLater(input::requestFocus);

        input.setOnAction(e -> {
            String cmd = input.getText().trim();
            consola.appendText(prompt + cmd + "\n");
            input.clear();
            procesarComando(cmd);
        });

        lblPrompt = new Label(prompt);
        lblPrompt.getStyleClass().add("consola");
        lblPrompt.setPrefHeight(30);

        HBox filaInput = new HBox(lblPrompt, input);
        HBox.setHgrow(input, Priority.ALWAYS);

        VBox principal = new VBox(consola, filaInput);
        VBox.setVgrow(consola, Priority.ALWAYS);

        return new Scene(principal, 640, 640);
    }

    private void procesarComando(String comando){
        //Comandos generales
        String comandos_disponibles="""
            Comandos disponibles:
            \t- inscribir\n\t\tCambia a modo inscripción por si requiere inscribir una materia que ya no tiene cupos para su carrera.
            \t- finalizar\n\t\tSi sólo necesita dar por finalizada su inscripción pero el sistema no se lo permite.
        """;
        switch(estado_actual){
            case EstadosSAE.BASE:
                if(comando.matches(".*\\?")){
                    consola.appendText(comandos_disponibles+"\n");
                }else if(comando.matches("(?i)inscribir")){
                    if(estado_actual!=EstadosSAE.INSCRIPCION){
                        estado_actual=EstadosSAE.INSCRIPCION;
                        prompt="SAE (inscr) # ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO INSCRIPCIÓN]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_inscr);
                    }
                }
                break;
            case EstadosSAE.INSCRIPCION:
                if(comando.matches("(?i)inscribir")){
                    consola.appendText("[YA SE ENCUENTRA EN MODO INSCRIPCIÓN]\n");
                }
                break;
            case EstadosSAE.FINALIZAR:
                break;
        }
            
            
        if(comando.matches("(?i)inscribir")){ //Estado INSCRIBIR
            
        }else if(comando.matches("(?i)finalizar")){ //Estado FINALIZAR
            if(estado_actual!=EstadosSAE.FINALIZAR){
                estado_actual=EstadosSAE.FINALIZAR;
                prompt="SAE (fin) # ";
                lblPrompt.setText(prompt);
                consola.appendText("[MODO FINALIZAR]\n");
                input.setPrefWidth(input.getWidth()-longitud_prompt_fin);
            }else{
                consola.appendText("[YA SE ENCUENTRA EN MODO FINALIZAR]\n");
            }
        }else if(comando.matches("(?i)salir")){ //Estado BASE
            if(estado_actual!=EstadosSAE.BASE){
                estado_actual=EstadosSAE.BASE;
                prompt="SAE > ";
                lblPrompt.setText(prompt);
                consola.appendText("[MODO BASE]\n");
                input.setPrefWidth(input.getWidth()-lognitud_prompt_base);
            }else{
                consola.appendText("[YA SE ENCUENTRA EN MODO BASE]\n");
            }
        }else{
            consola.appendText("Comando no Reconocido!\n");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}