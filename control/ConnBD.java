package control;

import com.mysql.cj.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnBD {
    public static Connection conectar(){
        Connection conn = null;
        
        try {
            Driver drv = new Driver();
            DriverManager.registerDriver(drv);
            
            String cad = "jdbc:mysql://localhost:3306/proyecto?useSSL=false&serverTimezone=UTC";
            conn = DriverManager.getConnection(cad, "root", ""); // coloca tu contraseña si tienes
            
        } catch (SQLException e) {
            System.out.println("Error en Conexión a Servidor de BD: " + e.getMessage());
        }
        
        return conn;
    }

    
}
