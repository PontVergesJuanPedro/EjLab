package gestor;
import java.util.ArrayList;
import model.Articulo;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Rubro;

public class GestorBD {
    
    private String CONN = "jdbc:sqlserver://JUANPEDRO;databaseName=javaABM";
    private String USER = "sa";
    private String PASS = "1234";
    
    public ArrayList<Articulo> obtenerArticulos() {
        ArrayList<Articulo> lista = new ArrayList<>();
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(CONN,USER,PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select a.*, r.nombre as nombreRubro from articulos a join rubros r on a.id_rubro = r.id_rubro");
            while(rs.next()) {
                int id = rs.getInt("codigo");
                String descripcion = rs.getString("descripcion");
                float precio = rs.getFloat("precio");
                int id_rubro = rs.getInt("id_rubro");
                String nombreRubro = rs.getString("nombreRubro");
                
                Rubro r = new Rubro(id_rubro, nombreRubro);
                Articulo a = new Articulo(id,descripcion,precio,r);
                lista.add(a);
            }
            stmt.close();
            conn.close();
            rs.close();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
    
    public void agregarArticulo(Articulo a) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(CONN,USER,PASS);
            PreparedStatement pstmt = conn.prepareStatement("insert into articulos values (?,?,?)");
            pstmt.setString(1,a.getDescripcion());
            pstmt.setFloat(2, a.getPrecio());
            pstmt.setInt(3, a.getRubro().getId());
            pstmt.executeUpdate();
            
            pstmt.close();
            conn.close();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarArticulo(int codigo){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(CONN,USER,PASS);
            PreparedStatement pstmt = conn.prepareStatement("delete from articulos where codigo = ?");
            pstmt.setInt(1,codigo);
            pstmt.executeUpdate();
            
            conn.close();
            pstmt.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void modificarArticulo(Articulo a) {
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(CONN,USER,PASS);
            PreparedStatement pstmt = conn.prepareStatement("update articulos set descripcion = ?, precio = ? where codigo = ?");
            pstmt.setString(1,a.getDescripcion());
            pstmt.setFloat(2,a.getPrecio());
            pstmt.setInt(3,a.getCodigo());
            pstmt.executeUpdate();
            
            conn.close();
            pstmt.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Rubro> obtenerRubros() {
        ArrayList<Rubro> lista = new ArrayList<>();
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(CONN,USER,PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from rubros");
            
            while(rs.next()) {
                int id = rs.getInt(1);
                String nombre = rs.getString(2);
                Rubro r = new Rubro(id,nombre);
                lista.add(r);
            }
            
            conn.close();
            stmt.close();
            rs.close();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GestorBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lista;
    }
}
