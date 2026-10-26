package org.example.sistemaaclaraciones;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import org.example.sistemaaclaraciones.Conexion.Conexion;
import org.example.sistemaaclaraciones.DAO.AlumnoDAO;
import org.example.sistemaaclaraciones.DAO.MateriaGrupoDAO;
import org.example.sistemaaclaraciones.Enums.EstadosSAE;
import org.fxmisc.richtext.CodeArea;

public class HelloApplication extends Application {
    private CodeArea consola;
    private TextField input;
    private String prompt="SAE (ingresar)> ";
    private Label lblPrompt;
    private EstadosSAE estado_actual=EstadosSAE.BASE;
    private boolean logged=false;
    private AlumnoDAO objAl;
    
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
        //stage.setFullScreen(true);
        stage.show();

        Conexion.crearConexion();
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
            
            Ingrese su número de control:
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
        lblPrompt.setPrefHeight(40);

        HBox filaInput = new HBox(lblPrompt, input);
        HBox.setHgrow(input, Priority.ALWAYS);

        VBox principal = new VBox(consola, filaInput);
        VBox.setVgrow(consola, Priority.ALWAYS);

        return new Scene(principal, 1280, 640);
    }

    private void procesarComando(String comando_raw){
        String comando=comando_raw.trim();
        if(!logged){
            if(comando.matches("^[0-9]{8}$")){
                objAl=AlumnoDAO.getAlumno(comando).getFirst();
                if(objAl!=null){
                    logged=true;
                    consola.appendText("Bienvenido "+objAl.getNombre()+" !\n");
                    return;
                }
            }else{
                consola.appendText("Número de control no válido!\n");
                return;
            }
        }
        //Comandos generales
        String comandos_disponibles="""
            Comandos disponibles:
            \t- inscribir\n\t\tCambia a modo inscripción por si requiere inscribir una materia que ya no tiene cupos para su carrera.
            \t- finalizar\n\t\tSi sólo necesita dar por finalizada su inscripción pero el sistema no se lo permite.
            \t- estatus\n\t\tDatos del alumno.
        """;
        if(comando.matches(".*\\?")){
            consola.appendText(comandos_disponibles+"\n");
        }else{
            switch(estado_actual){
                case EstadosSAE.BASE:                        // BASE
                    if(comando.matches("(?i)salir")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO BASE]\n");
                    }else if(comando.matches("(?i)inscribir")){
                        estado_actual=EstadosSAE.INSCRIPCION;
                        prompt="SAE (inscr) # ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO INSCRIPCIÓN]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_inscr);
                    }else if(comando.matches("(?i)finalizar")){
                        estado_actual=EstadosSAE.FINALIZAR;
                        prompt="SAE (fin) # ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO FINALIZAR]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_fin);
                    }else if(comando.matches("(?i)estatus")){
                        consultaAlumno();
                    }else {
                        consola.appendText("Comando no Reconocido!\n");
                    }
                    break;
                case EstadosSAE.INSCRIPCION:                // INSCRIPCION
                    if(comando.matches("(?i)salir")){
                        estado_actual=EstadosSAE.BASE;
                        prompt="SAE > ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO BASE]\n");
                        input.setPrefWidth(input.getWidth()-lognitud_prompt_base);
                    }else if(comando.matches("(?i)inscribir")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO INSCRIPCIÓN]\n");
                    }else if(comando.matches("(?i)finalizar")){
                        estado_actual=EstadosSAE.FINALIZAR;
                        prompt="SAE (fin) # ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO FINALIZAR]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_fin);
                    }else{
                        try{
                            String [] partes=(comando).split("\\s+", 2); //Sólo dos parámetros
                            String accion = partes[0].toLowerCase();
                            String argumento = partes.length > 1 ? partes[1].trim() : "";
                            if (accion.equals("grupos")){
                                if (argumento.isEmpty()){
                                    System.out.println("Debe especificar el nombre o la clave de la materia.");
                                }else if (argumento.equals("?")) {
                                    System.out.println("Uso: grupos <CLAVE-MATERIA>");
                                } else {
                                    buscarGrupos(argumento);
                                }
                            }else if (accion.equals("horario")){
                                if (argumento.isEmpty()){
                                    System.out.println("Debe especificar el id la materia.");
                                }else if (argumento.equals("?")) {
                                    System.out.println("Uso: horario <ID-GRUPO>");
                                }else{
                                    mostrarHorario(argumento);
                                }
                            }else{
                                consola.appendText("Comando no Reconocido!\n");
                            }
                        }catch (Exception e){
                            System.out.println("No corresponde a grupos");
                        }
                    }
                    break;
                case EstadosSAE.FINALIZAR:                  // FINALIZAR
                    if(comando.matches("(?i)salir")){
                        estado_actual=EstadosSAE.BASE;
                        prompt="SAE > ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO BASE]\n");
                        input.setPrefWidth(input.getWidth()-lognitud_prompt_base);
                    }else if(comando.matches("(?i)inscribir")){
                        estado_actual=EstadosSAE.INSCRIPCION;
                        prompt="SAE (inscr) # ";
                        lblPrompt.setText(prompt);
                        consola.appendText("[MODO INSCRIPCIÓN]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_inscr);
                    }else if(comando.matches("(?i)finalizar")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO FINALIZAR]\n");
                    }else{
                        consola.appendText("Comando no Reconocido!\n");
                    }
                    break;
            }
        }
    }
    private void buscarGrupos(String busqueda){
        if(busqueda.matches("^(ELW[1-7]|[A-Z]{3}-\\d{4})$")){
            //Es una clave
            ObservableList<MateriaGrupoDAO> lista= MateriaGrupoDAO.CONSULTAR_Clave(busqueda);
            if(lista.isEmpty()){
                consola.appendText("\nNo se encontraron coincidencias");
                return;
            }
            String fila,clave="";
            for (MateriaGrupoDAO grupo : lista) {
                if(!clave.equals(grupo.getClave())){
                    clave= grupo.getClave();
                    fila=String.format("%n|%-70s|Clave: %-10s|%n",grupo.getMateria(),grupo.getClave());
                    consola.appendText(fila);
                }
                fila = String.format(
                        "|Grupo: %-5s |ID: %-4d |Docente: %-100s |Cupo:%5d%n",
                        grupo.getGrupo(),
                        grupo.getId_grupo(),
                        grupo.getDocente(),
                        grupo.getCupo_disponible()
                );
                consola.appendText(fila);
            }
        }else{
            //Es un nombre de materia
            ObservableList<MateriaGrupoDAO> lista= MateriaGrupoDAO.CONSULTAR_Materia(busqueda);
            if(lista.isEmpty()){
                consola.appendText("\nNo se encontraron coincidencias");
                return;
            }
            String fila,clave="";
            for (MateriaGrupoDAO grupo : lista) {
                if(!clave.equals(grupo.getClave())){
                    clave= grupo.getClave();
                    fila=String.format("%n|%-70s|Clave: %-10s|%n",grupo.getMateria(),grupo.getClave());
                    consola.appendText(fila);
                }
                fila = String.format(
                        "|Grupo: %-5s |ID: %-4d |Docente: %-100s |Cupo:%5d%n",
                        grupo.getGrupo(),
                        grupo.getId_grupo(),
                        grupo.getDocente(),
                        grupo.getCupo_disponible()
                );
                consola.appendText(fila);
            }
        }
    }
    private void mostrarHorario(String id){
        MateriaGrupoDAO obj;
        ObservableList<MateriaGrupoDAO> lista= MateriaGrupoDAO.CONSULTAR_ID(id);
        if(lista.isEmpty()){
            consola.appendText("\nNo se encontraron coincidencias");
            return;
        }
        obj= lista.getFirst();
        String horario=String.format(
                "|%-70s|%n|%-70s|%n|%-70s|%n|%-70s|%n|%-70s|",
                "Lunes: "+obj.getLunes(),
                "Martes: "+obj.getMartes(),
                "Miercoles: "+obj.getMiercoles(),
                "Jueves: "+obj.getJueves(),
                "Viernes: "+obj.getViernes()
        );
        consola.appendText(horario);
    }
    public void consultaAlumno(){
        String print=String.format(
                "%n|Nombre: %-100s%n|Tipo: %-10s|%n|Estatus: %-12s|%n|Creditos inscritos: %-4d|%n|Semestre: %-3d|%n|Porcentaje avance: %.2f|%n|Promedio: %.2f|%n|Creditos acumulados: %-4d|%n",
                objAl.getNombre(),
                objAl.getTipo(),
                objAl.getEstatus(),
                objAl.getCreditos_inscritos(),
                objAl.getSemestre(),
                objAl.getPorcentaje_avance(),
                objAl.getPromedio_aritmetico(),
                objAl.getCreditos_acumulados()
        );
        consola.appendText(print);
    }

    public static void main(String[] args) {
        launch();
    }
}