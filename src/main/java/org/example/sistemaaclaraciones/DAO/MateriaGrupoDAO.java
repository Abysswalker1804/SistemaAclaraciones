package org.example.sistemaaclaraciones.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.sql.ResultSet;
import java.sql.Statement;

public class MateriaGrupoDAO {
    private String materia;
    private String clave;
    private byte creditos;
    private byte semestre;
    private byte porcentaje_req;
    private int id_grupo;
    private byte cupo;
    private String grupo;
    private String docente;
    private byte cupo_disponible;
    private String lunes;
    private String martes;
    private String miercoles;
    private String jueves;
    private String viernes;
    private String sabado;
    private String domingo;

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public byte getCreditos() {
        return creditos;
    }

    public void setCreditos(byte creditos) {
        this.creditos = creditos;
    }

    public byte getSemestre() {
        return semestre;
    }

    public void setSemestre(byte semestre) {
        this.semestre = semestre;
    }

    public byte getPorcentaje_req() {
        return porcentaje_req;
    }

    public void setPorcentaje_req(byte porcentaje_req) {
        this.porcentaje_req = porcentaje_req;
    }

    public int getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }

    public byte getCupo() {
        return cupo;
    }

    public void setCupo(byte cupo) {
        this.cupo = cupo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public byte getCupo_disponible() {
        return cupo_disponible;
    }

    public void setCupo_disponible(byte cupo_disponible) {
        this.cupo_disponible = cupo_disponible;
    }

    public String getLunes() {
        return lunes;
    }

    public void setLunes(String lunes) {
        this.lunes = lunes;
    }

    public String getMartes() {
        return martes;
    }

    public void setMartes(String martes) {
        this.martes = martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public void setMiercoles(String miercoles) {
        this.miercoles = miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public void setJueves(String jueves) {
        this.jueves = jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public void setViernes(String viernes) {
        this.viernes = viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public void setSabado(String sabado) {
        this.sabado = sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public void setDomingo(String domingo) {
        this.domingo = domingo;
    }

    public static ObservableList<MateriaGrupoDAO> CONSULTAR_Materia(String nombreMateria){
        String query="select m.nombre as materia, m.clave as clave, g.id as id, g.grupo as grupo, g.docente as docente, g.cupo as cupo_disponible, g.lunes as lunes, g.martes as martes, g.miercoles as miercoles, g.jueves as jueves, g.viernes as viernes, g.sabado as sabado, g.domingo as domingo from materia m join materia_grupo mg on m.clave=mg.clave join grupo g on mg.id=g.id where nombre like '%"+nombreMateria+"%'";
        ObservableList<MateriaGrupoDAO>listaMat= FXCollections.observableArrayList();
        try{
            MateriaGrupoDAO objMateria;
            Statement stmt= Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objMateria=new MateriaGrupoDAO();
                objMateria.materia=res.getString("materia");
                objMateria.clave=res.getString("clave");
                objMateria.id_grupo=res.getInt("id");
                objMateria.grupo=res.getString("grupo");
                objMateria.docente=res.getString("docente");
                objMateria.cupo_disponible=res.getByte("cupo_disponible");
                objMateria.lunes=res.getString("lunes");
                objMateria.martes=res.getString("martes");
                objMateria.miercoles=res.getString("miercoles");
                objMateria.jueves=res.getString("jueves");
                objMateria.viernes=res.getString("viernes");
                objMateria.sabado=res.getString("sabado");
                objMateria.domingo=res.getString("domingo");
                listaMat.add(objMateria);
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+"\nError en MateriaDAO");
        }
        return listaMat;
    }
    public static ObservableList<MateriaGrupoDAO> CONSULTAR_Clave(String nombreClave){
        String query="select m.nombre as materia, m.clave as clave, g.id as id_grupo, g.grupo as grupo, g.docente as docente, g.cupo as cupo_disponible, g.lunes as lunes, g.martes as martes, g.miercoles as miercoles, g.jueves as jueves, g.viernes as viernes, g.sabado as sabado, g.domingo as domingo from materia m join materia_grupo mg on m.clave=mg.clave join grupo g on mg.id=g.id where m.clave like '%"+nombreClave+"%'";
        ObservableList<MateriaGrupoDAO>listaMat= FXCollections.observableArrayList();
        try{
            MateriaGrupoDAO objMateria;
            Statement stmt= Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objMateria=new MateriaGrupoDAO();
                objMateria.materia=res.getString("materia");
                objMateria.clave=res.getString("clave");
                objMateria.id_grupo=res.getInt("id_grupo");
                objMateria.grupo=res.getString("grupo");
                objMateria.docente=res.getString("docente");
                objMateria.cupo_disponible=res.getByte("cupo_disponible");
                objMateria.lunes=res.getString("lunes");
                objMateria.martes=res.getString("martes");
                objMateria.miercoles=res.getString("miercoles");
                objMateria.jueves=res.getString("jueves");
                objMateria.viernes=res.getString("viernes");
                objMateria.sabado=res.getString("sabado");
                objMateria.domingo=res.getString("domingo");
                listaMat.add(objMateria);
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+"\nError en MateriaDAO");
        }
        return listaMat;
    }
    public static ObservableList<MateriaGrupoDAO> CONSULTAR_ID(String ID){
        String query="select m.clave as clave,g.id as id, m.nombre as materia, m.creditos as creditos, g.grupo as grupo, m.semestre as semestre, m.porcentaje_req as porcentaje_req, g.cupo as cupo, g.docente as docente, g.lunes as lun, g.martes as mar, g.miercoles as mie, g.jueves as jue, g.viernes as vie, g.sabado as sab, g.domingo as dom from materia m join materia_grupo mg on m.clave=mg.clave join grupo g on mg.id=g.id where g.id='"+ID+"';";
        ObservableList<MateriaGrupoDAO>listaMat= FXCollections.observableArrayList();
        try{
            MateriaGrupoDAO objMateria;
            Statement stmt= Conexion.connection.createStatement();
            ResultSet res=stmt.executeQuery(query);
            while(res.next()){
                objMateria=new MateriaGrupoDAO();
                objMateria.clave=res.getString("clave");
                objMateria.id_grupo=res.getInt("id");
                objMateria.materia=res.getString("materia");
                objMateria.creditos=res.getByte("creditos");
                objMateria.grupo=res.getString("grupo");
                objMateria.semestre=res.getByte("semestre");
                objMateria.porcentaje_req=res.getByte("porcentaje_req");
                objMateria.cupo= res.getByte("cupo");
                objMateria.docente=res.getString("docente");
                objMateria.lunes=res.getString("lun");
                objMateria.martes=res.getString("mar");
                objMateria.miercoles=res.getString("mie");
                objMateria.jueves=res.getString("jue");
                objMateria.viernes=res.getString("vie");
                objMateria.sabado=res.getString("sab");
                objMateria.domingo=res.getString("dom");
                listaMat.add(objMateria);
            }
        }catch(Exception e){
            System.out.println(e.getMessage()+"\nError en MateriaDAO");
        }
        return listaMat;
    }
    public static String buscarClave(String id){
        String clave=null;
        String query="select m.clave as clave from materia m join materia_grupo mg on m.clave=mg.clave join grupo g on mg.id=g.id where g.id='"+id+"';";
        try{
            Statement stmt=Conexion.connection.createStatement();
            ResultSet res= stmt.executeQuery(query);
            while(res.next()){
                clave=res.getString("clave");
            }
        }catch(Exception e){System.out.println("Error en PrerequisitoDAO");}
        return clave;
    }
}
