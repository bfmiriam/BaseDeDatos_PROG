package basededatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Conexion {

    static String url = "/home/local/DANIELCASTELAO/mbacelofernandez/mibd.bd";
    static Connection connect;

    public static void conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection("jdbc:sqlite:" + url);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error en la conexión de la base de datos");
        }
    }
    
    public static void pechar() {
        try {
            connect.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la conexión");
        }
    }

    public static ArrayList<Alumno> mostrarAlumnos() {

        ArrayList<Alumno> alumnos = new ArrayList();
        conectar();
        ResultSet result = null;
        try {
            PreparedStatement st = connect.prepareStatement("SELECT * FROM Alumnos");
            result = st.executeQuery();
            while (result.next()) {
                Alumno alu = new Alumno(result.getString("DNI"), result.getString("Nombre"),
                        result.getString("Apellidos"), result.getString("Direccion"), result.getString("Telefono"));
                alumnos.add(alu);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        pechar();
        return alumnos;
    }

    public static void guardarAlumno(Alumno alumno) {
        
        conectar();
        try {
            PreparedStatement st = connect.prepareStatement("insert into alumnos "
            + "(dni, nombre, apellidos, direccion, telefono) values (?,?,?,?,?)");
            st.setString(1, alumno.getDni());
            st.setString(2, alumno.getNombre());
            st.setString(3, alumno.getApellidos());
            st.setString(4, alumno.getDireccion());
            st.setString(5, alumno.getTelefono());
            st.execute();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        pechar();
    }
    public static void borrarAlumno(String dni){
         conectar();   
        try {
            PreparedStatement st = connect.prepareStatement("DELETE FROM Alumnos WHERE dni=?");
            st.setString(1, dni);
            st.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        pechar();

    }
    public static void modificarAlumno(Alumno alumno){
        Conexion.conectar();
        try {
            PreparedStatement st = connect.prepareStatement("UPDATE Alumnos SET dni='"+
             alumno.getDni()+"', nombre='"+alumno.getNombre()+"', apellidos='"+
             alumno.getApellidos()+"', direccion='"+alumno.getDireccion()+
             "', telefono='"+alumno.getTelefono()+"' WHERE dni ='"+alumno.getDni()+"'");  
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       Conexion.pechar();
    }
    public static ArrayList<Alumno> buscarAlumno(String busqueda){
       
        ArrayList <Alumno> alumnos = new ArrayList();
        ResultSet resultado = null;
        conectar();
        try{
             PreparedStatement st=connect.prepareStatement("select * from Alumnos where DNI='"
             +busqueda+ "' or Nombre='"+busqueda+"' or Apellidos='"+busqueda+"' or Direccion='"
             +busqueda+"' or Telefono='"+busqueda+"'");
             resultado=st.executeQuery();
             
            while(resultado.next()){
                Alumno al = new Alumno(resultado.getString("dni"),resultado.getString("Nombre"),
                 resultado.getString("Apellidos"),resultado.getString("Direccion"),
                 resultado.getString("Telefono"));
                
                alumnos.add(al);
            }
        }catch(SQLException ex){
            System.out.println("Error en la búsqueda");
        }
        pechar();
        return alumnos;      
    }

}
