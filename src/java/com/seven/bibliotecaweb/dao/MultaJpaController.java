/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seven.bibliotecaweb.dao;

import com.seven.bibliotecaweb.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.seven.bibliotecaweb.model.Livro;
import com.seven.bibliotecaweb.model.Multa;
import com.seven.bibliotecaweb.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jaaday
 */
public class MultaJpaController implements Serializable {

    public MultaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Multa multa) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro livroId = multa.getLivroId();
            if (livroId != null) {
                livroId = em.getReference(livroId.getClass(), livroId.getId());
                multa.setLivroId(livroId);
            }
            Usuario usuarioId = multa.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                multa.setUsuarioId(usuarioId);
            }
            em.persist(multa);
            if (livroId != null) {
                livroId.getMultaCollection().add(multa);
                livroId = em.merge(livroId);
            }
            if (usuarioId != null) {
                usuarioId.getMultaCollection().add(multa);
                usuarioId = em.merge(usuarioId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Multa multa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Multa persistentMulta = em.find(Multa.class, multa.getId());
            Livro livroIdOld = persistentMulta.getLivroId();
            Livro livroIdNew = multa.getLivroId();
            Usuario usuarioIdOld = persistentMulta.getUsuarioId();
            Usuario usuarioIdNew = multa.getUsuarioId();
            if (livroIdNew != null) {
                livroIdNew = em.getReference(livroIdNew.getClass(), livroIdNew.getId());
                multa.setLivroId(livroIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                multa.setUsuarioId(usuarioIdNew);
            }
            multa = em.merge(multa);
            if (livroIdOld != null && !livroIdOld.equals(livroIdNew)) {
                livroIdOld.getMultaCollection().remove(multa);
                livroIdOld = em.merge(livroIdOld);
            }
            if (livroIdNew != null && !livroIdNew.equals(livroIdOld)) {
                livroIdNew.getMultaCollection().add(multa);
                livroIdNew = em.merge(livroIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getMultaCollection().remove(multa);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getMultaCollection().add(multa);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = multa.getId();
                if (findMulta(id) == null) {
                    throw new NonexistentEntityException("The multa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Multa multa;
            try {
                multa = em.getReference(Multa.class, id);
                multa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The multa with id " + id + " no longer exists.", enfe);
            }
            Livro livroId = multa.getLivroId();
            if (livroId != null) {
                livroId.getMultaCollection().remove(multa);
                livroId = em.merge(livroId);
            }
            Usuario usuarioId = multa.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getMultaCollection().remove(multa);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(multa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Multa> findMultaEntities() {
        return findMultaEntities(true, -1, -1);
    }

    public List<Multa> findMultaEntities(int maxResults, int firstResult) {
        return findMultaEntities(false, maxResults, firstResult);
    }

    private List<Multa> findMultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Multa.class));
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

    public Multa findMulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Multa.class, id);
        } finally {
            em.close();
        }
    }

    public int getMultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Multa> rt = cq.from(Multa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
