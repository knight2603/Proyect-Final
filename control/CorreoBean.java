package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import modelo.Usuario;

@ManagedBean
@ViewScoped

public class CorreoBean {

    private String asunto, contenido;
    private List<String> dest;
    private List<Usuario> listaUsuarios;

    public void listarUsuarios() {

        listaUsuarios = new ArrayList<>();


        try {
            String sql = "SELECT * FROM usuarios";
            PreparedStatement ps = ConnBD.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usu = new Usuario();
                usu.setNombre(rs.getString("nombre"));
                usu.setCorreo(rs.getString("correo"));

                listaUsuarios.add(usu);

            }

        } catch (SQLException e) {
        }
    }

    public void enviarCorreo() {
        final String user = "homecopyjd@gmail.com";
        final String pass = "hwxn qdzy frui sapf";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Auth User
        Session sesion = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
    });
        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(user));
            
            //lista destinatarios
            InternetAddress[] dests = new InternetAddress[dest.size()];
            int i = 0;
            
            Iterator itr = dest.iterator();
            
            while(itr.hasNext()){
                InternetAddress ndir = new InternetAddress(itr.next().toString());
                dests[i]= ndir;
                i++;
            }
            
            mensaje.setRecipients(Message.RecipientType.TO,dests);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);
            
            Transport.send(mensaje);
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"exito", "Mensaje enviado exitosamente"));
            
            
        } catch (MessagingException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"error", "Error enviando el mensaje"));
        }
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<String> getDest() {
        return dest;
    }

    public void setDest(List<String> dest) {
        this.dest = dest;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

}
