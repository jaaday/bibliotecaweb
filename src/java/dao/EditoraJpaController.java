/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Editora;
import modelo.Endereco;

/**
 *
 * @author yagon_000
 */
public class EditoraJpaController implements Serializable {

    public EditoraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Editora editora) {
        if(editora.getEndereco() == null){
            editora.setEndereco(new Endereco());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            
            Endereco endereco = editora.getEndereco();
            em.persist(endereco);
            em.persist(editora); 
            em.getTransaction().commit();
            
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Editora> pesqEditoraNome(Editora editora){
        EntityManager em = getEntityManager();
        try{                                
            TypedQuery<Editora> query = em.createQuery("select e from Editora e where e.nome = :nome",Editora.class);
            query.setParameter("nome", editora.getNome());
            return query.getResultList();
            
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void edit(Editora editora) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            editora = em.merge(editora);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = editora.getId();
                if (findEditora(id) == null) {
                    throw new NonexistentEntityException("The editora with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Editora editora;
            try {
                editora = em.getReference(Editora.class, id);
                editora.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The editora with id " + id + " no longer exists.", enfe);
            }
            em.remove(editora);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Editora> findEditoraEntities() {
        return findEditoraEntities(true, -1, -1);
    }

    public List<Editora> findEditoraEntities(int maxResults, int firstResult) {
        return findEditoraEntities(false, maxResults, firstResult);
    }

    private List<Editora> findEditoraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Editora.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Editora findEditora(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Editora.class, id);
        } finally {
            em.close();
        }
    }

    public int getEditoraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Editora> rt = cq.from(Editora.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
