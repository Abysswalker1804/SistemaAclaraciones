package org.example.sistemaaclaraciones.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.sistemaaclaraciones.Conexion.Conexion;

import java.security.spec.ECField;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class AprobadaDAO {
    private String clave;
    private String no_control;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNo_control() {
        return no_control;
    }

    public void setNo_control(String no_control) {
        this.no_control = no_control;
    }

    public static boolean REVISAR(String no_control, String clave){
        boolean flag=false;
        String query="select * from aprobada where no_control='"+no_control+"' and clave='"+clave+"'";
        try{
            Statement stmt = Conexion.connection.createStatement();
            ResultSet res= stmt.executeQuery(query);
            ObservableList<AprobadaDAO>lista= FXCollections.observableArrayList();
            while(res.next()){
                AprobadaDAO objApr=new AprobadaDAO();
                objApr.no_control=res.getString("no_control");
                objApr.clave=res.getString("clave");
                lista.add(objApr);
            }
            if(!lista.isEmpty()){flag=true;}
        }catch (Exception e){
            System.out.println("Error en AprobadaDAO");
        }
        return flag;
    }
}
