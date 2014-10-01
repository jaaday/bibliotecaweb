

package logica;

import dao.EnderecoJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Usuario;
import util.EMF;

/**
 *
 * @author yagon_000
 */
public class LogicaUsusario {
    private final UsuarioJpaController daoUsuario;
    private final EnderecoJpaController daoEndereco;
    
    public LogicaUsusario(){
        daoUsuario = new UsuarioJpaController(EMF.factory);
        daoEndereco = new EnderecoJpaController(EMF.factory);
    }
    
    public void novoUsuario(Usuario u){
        daoUsuario.create(u);
    }
    
    public void alterarUsuario(Usuario u){
        
        try {
            daoUsuario.edit(u);
        } catch (Exception ex) {
            Logger.getLogger(LogicaUsusario.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            daoEndereco.edit(u.getEndereco());
        } catch (Exception ex) {
            Logger.getLogger(LogicaUsusario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void excluirUsuario(Usuario u){
        try {
            daoUsuario.destroy(u.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LogicaUsusario.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            daoEndereco.destroy(u.getEndereco().getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(LogicaUsusario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Usuario> listarUsuario(){
        return daoUsuario.findUsuarioEntities();
    }
    
    public Usuario pesquisarUsuarioCPF(Usuario u){
        return daoUsuario.pesquisarUsuarioCPF(u);
    }
    
    public String removerMascara(String text){
        return text.replaceAll("[.-]", "");  
    }
}
