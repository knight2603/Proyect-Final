package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class UsuarioDataSource implements JRDataSource {

    private List<Usuario> lstUsu;
    int indice;

    public UsuarioDataSource() {
        lstUsu = new ArrayList<>();
        indice = -1;

        try {
            String sql = "select usuarios.*,usuarios.cc FROM usuarios";
            PreparedStatement ps = ConnBD.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setCc(rs.getInt("cc"));
                usu.setNombre(rs.getString("nombre"));
                usu.setLastname(rs.getString("lastname"));
                usu.setCorreo(rs.getString("correo"));
                usu.setTipo(rs.getString("tipo"));

                lstUsu.add(usu);

            }
        } catch (Exception e) {
        }

    }

    @Override
    public boolean next() throws JRException {
        indice++;
        return indice < lstUsu.size();
    }

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;

        String nomCampo = jrf.getName();
        switch (nomCampo) {
            case "cc":
                valor = lstUsu.get(indice).getCc();
                break;
            case "nombre":
                valor = lstUsu.get(indice).getNombre();
                break;
            case "lastname":
                valor = lstUsu.get(indice).getLastname();
                break;
            case "correo":
                valor = lstUsu.get(indice).getCorreo();
                break;
            case "tipo":
                valor = lstUsu.get(indice).getTipo();
                break;
        }

        return valor;
    }

}
