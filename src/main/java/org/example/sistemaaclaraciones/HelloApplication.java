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
import org.example.sistemaaclaraciones.DAO.*;
import org.example.sistemaaclaraciones.Enums.EstadosSAE;
import org.fxmisc.richtext.CodeArea;

public class HelloApplication extends Application {
    //Constantes
    private final String prompt_ingresar="SAE (ingresar)> ";
    private final byte longitud_prompt_ingresar=124;
    private final String prompt_base="SAE > ";
    private final byte longitud_prompt_base=47;
    private final String prompt_inscr="SAE (inscr)# ";
    private final byte longitud_prompt_inscr=101;
    private final String prompt_fin="SAE (fin) # ";
    private final byte longitud_prompt_fin=93;
    private final String prompt_confirmacion="[si/no]?: ";
    private final byte longitud_prompt_confirmacion=78;
    //Variables
    private CodeArea consola;
    private TextField input;
    private String prompt=prompt_ingresar;
    private Label lblPrompt;
    private EstadosSAE estado_actual=EstadosSAE.BASE;
    private boolean logged=false;
    private AlumnoDAO objAl;
    private boolean confirmacion=false;
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
        lblPrompt.setPrefHeight(30);
        lblPrompt.setPrefWidth(longitud_prompt_ingresar);

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
                    consola.appendText("\nBienvenido "+objAl.getNombre()+" !\n");
                    lblPrompt.setText(prompt_base);
                    lblPrompt.setPrefWidth(longitud_prompt_base);
                    return;
                }
            }else{
                consola.appendText("Número de control no válido!\n");
                return;
            }
        }
        //A partir de aquí comandos si está loggeado
        //Comandos generales
        String comandos_disponibles="""
            Comandos disponibles:
            \t- inscripcion\n\t\tCambia a modo inscripción por si requiere inscribir una materia que ya no tiene cupos para su carrera.
            \t- finalizar\n\t\tSi sólo necesita dar por finalizada su inscripción pero el sistema no se lo permite.
            \t- estatus\n\t\tDatos del alumno.
        """;
        //if(comando.matches(".*\\?")){
        if(comando.equals("?")){
            consola.appendText(comandos_disponibles+"\n");
        }else{
            switch(estado_actual){
                case EstadosSAE.BASE:                        // BASE
                    if(comando.matches("(?i)salir")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO BASE]\n");
                    }else if(comando.matches("(?i)inscripcion")){
                        estado_actual=EstadosSAE.INSCRIPCION;
                        prompt=prompt_ingresar;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_ingresar);
                        consola.appendText("[MODO INSCRIPCIÓN]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_inscr);
                    }else if(comando.matches("(?i)finalizar")){
                        estado_actual=EstadosSAE.FINALIZAR;
                        prompt=prompt_fin;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_fin);
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
                        prompt=prompt_base;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_base);
                        consola.appendText("[MODO BASE]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_base);
                    }else if(comando.matches("(?i)inscripcion")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO INSCRIPCIÓN]\n");
                    }else if(comando.matches("(?i)finalizar")){
                        estado_actual=EstadosSAE.FINALIZAR;
                        prompt=prompt_fin;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_fin);
                        consola.appendText("[MODO FINALIZAR]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_fin);
                    }else{
                        try{
                            String [] partes=(comando).split("\\s+", 2); //Sólo dos parámetros
                            String accion = partes[0].toLowerCase();
                            String argumento = partes.length > 1 ? partes[1].trim() : "";
                            String argumento2=partes[2].toLowerCase();
                            if (accion.equals("grupos")){
                                if(argumento.equals("externos")){
                                    if (argumento2.isEmpty()){
                                        consola.appendText("Debe especificar el nombre o la clave de la materia.\n");
                                    }else if (argumento2.equals("?")) {
                                        consola.appendText("Uso: grupos externos <CLAVE-MATERIA>\n");
                                    } else {
                                        buscarGruposExternos(argumento);
                                    }
                                }else{
                                    if (argumento.isEmpty()){
                                        consola.appendText("Debe especificar el nombre o la clave de la materia.\n");
                                    }else if (argumento.equals("?")) {
                                        consola.appendText("Uso: grupos <CLAVE-MATERIA>\n");
                                    } else {
                                        buscarGrupos(argumento);
                                    }
                                }
                            }else if (accion.equals("horario")){
                                if (argumento.isEmpty()){
                                    consola.appendText("Debe especificar el id del grupo.\n");
                                }else if (argumento.equals("?")) {
                                    consola.appendText("Uso: horario <ID-GRUPO>\n");
                                }else{
                                    mostrarHorario(argumento);
                                }
                            }else if (accion.equals("inscribir")){
                                if (argumento.isEmpty()){
                                    consola.appendText("Debe especificar el id del grupo.\n");
                                }else if (argumento.equals("?")) {
                                    consola.appendText("Uso: inscribir <ID-GRUPO>\n");
                                }else{
                                    inscribirGrupo(argumento);
                                }
                            }else if (accion.equals("desinscribir")){
                                if (argumento.isEmpty()){
                                    consola.appendText("Debe especificar el id del grupo.\n");
                                }else if (argumento.equals("?")) {
                                    consola.appendText("Uso: desinscribir <ID-GRUPO>\n");
                                }else{
                                    desinscribirGrupo(argumento);
                                }
                            }else{
                                consola.appendText("Comando no Reconocido!\n");
                            }
                        }catch (Exception e){
                            consola.appendText("Comando no Reconocido o Error de Sintaxis!\n");
                        }
                    }
                    break;
                case EstadosSAE.FINALIZAR:                  // FINALIZAR
                    if(comando.matches("(?i)salir")){
                        estado_actual=EstadosSAE.BASE;
                        prompt=prompt_base;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_base);
                        consola.appendText("[MODO BASE]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_base);
                    }else if(comando.matches("(?i)inscripcion")){
                        estado_actual=EstadosSAE.INSCRIPCION;
                        prompt=prompt_ingresar;
                        lblPrompt.setText(prompt);
                        lblPrompt.setPrefWidth(longitud_prompt_ingresar);
                        consola.appendText("[MODO INSCRIPCIÓN]\n");
                        input.setPrefWidth(input.getWidth()-longitud_prompt_inscr);
                    }else if(comando.matches("(?i)finalizar")){
                        consola.appendText("[YA SE ENCUENTRA EN MODO FINALIZAR]\n");
                    }else{
                        try{
                            String [] partes=(comando).split("\\s+", 2); //Sólo dos parámetros
                            String accion = partes[0].toLowerCase();
                            String argumento = partes.length > 1 ? partes[1].trim() : "";
                            if(accion.equals("terminar") && argumento.equals("inscripcion")){
                                consola.appendText("\nSe revisa a continuación si son sus últimas materias...\n");
                                if(objAl!=null && objAl.getCreditos_acumulados()>=235 && objAl.getSemestre()>=8){
                                    System.exit(0);
                                }else {
                                    consola.appendText("\nNo cuenta con los criterios para comprobar que son sus últimas materias.\nPor favor, envíe su aclaración manual al coordinador con el comando 'aclaración' de la siguiente forma:\n aclaracion <Descripción menor a 500 caracteres>.");
                                }
                            }else{
                                consola.appendText("Comando no Reconocido!\n");
                            }
                        }catch(Exception e){
                            consola.appendText("Comando no Reconocido o Error de Sintaxis!\n");
                        }
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
                "|Materia: %-70s%n|Docente: %-100s%n|Lunes: %-70s%n|Martes: %-70s%n|Miercoles: %-70s%n|Jueves: %-70s%n|Viernes: %-70s%n",
                obj.getMateria(),
                obj.getDocente(),
                obj.getLunes(),
                obj.getMartes(),
                obj.getMiercoles(),
                obj.getJueves(),
                obj.getViernes()
        );
        consola.appendText(horario);
    }
    private void consultaAlumno(){
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

    private void inscribirGrupo(String id){
        MateriaGrupoDAO grupo=MateriaGrupoDAO.CONSULTAR_ID(id).getFirst();
        if(grupo==null){
            consola.appendText("\nID incorrecto! No existe dicho grupo.\n");
            return;
        }
        String confirmacion=String.format("Desea inscribirse en:%nMateria: %-100s%nGrupo: %-3s%nDocente: %-100s%n",grupo.getMateria(),grupo.getGrupo(),grupo.getDocente());
        consola.appendText(confirmacion);
        prompt=prompt_confirmacion;
        lblPrompt.setText(prompt);
        lblPrompt.setPrefWidth(longitud_prompt_confirmacion);
        input.setOnAction(e -> {
            String cmd = input.getText().trim();
            consola.appendText(prompt + cmd + "\n");
            input.clear();
            switch (cmd){
                case "si":
                    //Reglas de inscripción
                    if(grupo.getSemestre()<=objAl.getSemestre()){//Revisar si puede tomarla por semestre
                        if(grupo.getPorcentaje_req()<=objAl.getPorcentaje_avance()){//Revisar si tiene el porcentaje que requiere la materia
                            ObservableList<String> lista_requisitos= PrerequisitoDAO.getMateriasRequisito(id);//Revisar Requisitos
                            if(lista_requisitos.isEmpty()){//Si no hay requisitos
                                InscripcionDAO.INSCRIBIR(objAl.getNo_control(),grupo.getId_grupo()+"");
                                //Actualizar carga del Alumno
                                AlumnoDAO.actualizarCarga(objAl.getNo_control(),grupo.getCreditos(),true);
                                consola.appendText("\nGrupo Inscrito con exito!\n");
                            }else{
                                //Si hay materias requisito
                                boolean requisitos_completos=true;
                                for(String clave: lista_requisitos){
                                    if(!AprobadaDAO.REVISAR(objAl.getNo_control(),clave)){
                                        requisitos_completos=false;
                                    }
                                }
                                if(requisitos_completos){
                                    //Cumple con los requisitos
                                    InscripcionDAO.INSCRIBIR(objAl.getNo_control(),grupo.getId_grupo()+"");
                                    //Actualizar carga del Alumno
                                    AlumnoDAO.actualizarCarga(objAl.getNo_control(),grupo.getCreditos(),true);
                                }else{
                                    //No cumple con los requisitos
                                    consola.appendText("\nNo se cumplen los prerequisitos necesarios!\nAbortando operación!\n");
                                }
                            }
                        }else{
                            consola.appendText("\nNo tienes el porcentaje de avance necesario para esta materia!\nAbortando operación!\n");
                        }
                    }else{
                        consola.appendText("\nEsta materia no corresponde a tu semestre!\nAbortando operación!\n");
                    }
                    //Devolver la consola a normal
                    prompt=prompt_inscr;
                    lblPrompt.setText(prompt);
                    lblPrompt.setPrefWidth(longitud_prompt_inscr);
                    input.setOnAction(event -> {
                        String cmd2 = input.getText().trim();
                        consola.appendText(prompt + cmd2 + "\n");
                        input.clear();
                        procesarComando(cmd2);
                    });
                    break;
                case "no":
                    consola.appendText("\nOperación cancelada!\n");
                    //Devolver la consola a normal
                    prompt=prompt_inscr;
                    lblPrompt.setText(prompt);
                    lblPrompt.setPrefWidth(longitud_prompt_inscr);
                    input.setOnAction(event -> {
                        String cmd2 = input.getText().trim();
                        consola.appendText(prompt + cmd2 + "\n");
                        input.clear();
                        procesarComando(cmd2);
                    });
                    break;
                default:
                    consola.appendText("\nRespuesta inválida!\nEscribir si o no.");
            }
        });
    }
    private void desinscribirGrupo(String id){
        MateriaGrupoDAO grupo=MateriaGrupoDAO.CONSULTAR_ID(id).getFirst();
        if(grupo==null){
            consola.appendText("\nID incorrecto! No existe dicho grupo.\n");
            return;
        }
        String confirmacion=String.format("Desea DESinscribirse de:%nMateria: %-100s%nGrupo: %-3s%nDocente: %-100s%n",grupo.getMateria(),grupo.getGrupo(),grupo.getDocente());
        consola.appendText(confirmacion);
        prompt=prompt_confirmacion;
        lblPrompt.setText(prompt);
        lblPrompt.setPrefWidth(longitud_prompt_confirmacion);
        input.setOnAction(e -> {
            String cmd = input.getText().trim();
            consola.appendText(prompt + cmd + "\n");
            input.clear();
            switch (cmd){
                case "si":
                    InscripcionDAO.DESINSCRIBIR(objAl.getNo_control(), grupo.getId_grupo()+"");
                    //Actualizar carga del Alumno
                    AlumnoDAO.actualizarCarga(objAl.getNo_control(),grupo.getCreditos(),false);
                    consola.appendText("\nDesinscripción exitosa!\n");
                    //Devolver la consola a normal
                    prompt=prompt_inscr;
                    lblPrompt.setText(prompt);
                    lblPrompt.setPrefWidth(longitud_prompt_inscr);
                    input.setOnAction(event -> {
                        String cmd2 = input.getText().trim();
                        consola.appendText(prompt + cmd2 + "\n");
                        input.clear();
                        procesarComando(cmd2);
                    });
                    break;
                case "no":
                    consola.appendText("\nOperación cancelada!\n");
                    //Devolver la consola a normal
                    prompt=prompt_inscr;
                    lblPrompt.setText(prompt);
                    lblPrompt.setPrefWidth(longitud_prompt_inscr);
                    input.setOnAction(event -> {
                        String cmd2 = input.getText().trim();
                        consola.appendText(prompt + cmd2 + "\n");
                        input.clear();
                        procesarComando(cmd2);
                    });
                    break;
                default:
            }
        });
    }
    private void buscarGruposExternos(String busqueda){} //Terminar
    public static void main(String[] args) {
        launch();
    }
}