package org.example.sistemaaclaraciones.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.sql.ResultSet;
import java.sql.Statement;

public class AlumnoDAO {
    private String no_control;
    private String nombre;
    private String tipo;
    private String estatus;
    private byte creditos_inscritos;
    private byte semestre;
    private double porcentaje_avance;
    private double promedio_aritmetico;
    private double promedio_pond;
    private short creditos_acumulados;

    public String getNo_control() {
        return no_control;
    }

    public void setNo_control(String no_control) {
        this.no_control = no_control;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public byte getCreditos_inscritos() {
        return creditos_inscritos;
    }

    public void setCreditos_inscritos(byte creditos_inscritos) {
        this.creditos_inscritos = creditos_inscritos;
    }

    public byte getSemestre() {
        return semestre;
    }

    public void setSemestre(byte semestre) {
        this.semestre = semestre;
    }

    public double getPorcentaje_avance() {
        return porcentaje_avance;
    }

    public void setPorcentaje_avance(double porcentaje_avance) {
        this.porcentaje_avance = porcentaje_avance;
    }

    public double getPromedio_aritmetico() {
        return promedio_aritmetico;
    }

    public void setPromedio_aritmetico(double promedio_aritmetico) {
        this.promedio_aritmetico = promedio_aritmetico;
    }

    public double getPromedio_pond() {
        return promedio_pond;
    }

    public void setPromedio_pond(double promedio_pond) {
        this.promedio_pond = promedio_pond;
    }

    public short getCreditos_acumulados() {
        return creditos_acumulados;
    }

    public void setCreditos_acumulados(short creditos_acumulados) {
        this.creditos_acumulados = creditos_acumulados;
    }
    public static ObservableList<AlumnoDAO> getAlumno(String no_control){
        ObservableList<AlumnoDAO> lista= FXCollections.observableArrayList();
        String query="select * from alumno where no_control='"+no_control+"'";
        try{
            AlumnoDAO objAlumno;
            Statement stmt= Conexion.connection.createStatement();
            ResultSet res= stmt.executeQuery(query);
            while(res.next()){
                objAlumno=new AlumnoDAO();
                objAlumno.no_control=res.getString("no_control");
                objAlumno.nombre=res.getString("nombre");
                objAlumno.tipo=res.getString("tipo");
                objAlumno.estatus=res.getString("estatus");
                objAlumno.creditos_inscritos=res.getByte("creditos_inscritos");
                objAlumno.semestre= res.getByte("semestre");
                objAlumno.porcentaje_avance=res.getByte("porcentaje_avance");
                objAlumno.promedio_aritmetico=res.getDouble("promedio_arit");
                objAlumno.promedio_pond=res.getDouble("promedio_pond");
                objAlumno.creditos_acumulados=res.getShort("creditos_acumulados");
                lista.add(objAlumno);
            }
        }catch (Exception e){
            System.out.println("Algo salió mal en AlumnoDAO");
        }
        return lista;
    }
}
