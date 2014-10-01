/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seven.bibliotecaweb.dao;

import com.seven.bibliotecaweb.dao.exceptions.NonexistentEntityException;
import com.seven.bibliotecaweb.model.Emprestimo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.seven.bibliotecaweb.model.Livro;
import com.seven.bibliotecaweb.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jaaday
 */
public class EmprestimoJpaController implements Serializable {

    public EmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emprestimo emprestimo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro livroId = emprestimo.getLivroId();
            if (livroId != null) {
                livroId = em.getReference(livroId.getClass(), livroId.getId());
                emprestimo.setLivroId(livroId);
            }
            Usuario usuarioId = emprestimo.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                emprestimo.setUsuarioId(usuarioId);
            }
            em.persist(emprestimo);
            if (livroId != null) {
                livroId.getEmprestimoCollection().add(emprestimo);
                livroId = em.merge(livroId);
            }
            if (usuarioId != null) {
                usuarioId.getEmprestimoCollection().add(emprestimo);
                usuarioId = em.merge(usuarioId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emprestimo emprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprestimo persistentEmprestimo = em.find(Emprestimo.class, emprestimo.getId());
            Livro livroIdOld = persistentEmprestimo.getLivroId();
            Livro livroIdNew = emprestimo.getLivroId();
            Usuario usuarioIdOld = persistentEmprestimo.getUsuarioId();
            Usuario usuarioIdNew = emprestimo.getUsuarioId();
            if (livroIdNew != null) {
                livroIdNew = em.getReference(livroIdNew.getClass(), livroIdNew.getId());
                emprestimo.setLivroId(livroIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                emprestimo.setUsuarioId(usuarioIdNew);
            }
            emprestimo = em.merge(emprestimo);
            if (livroIdOld != null && !livroIdOld.equals(livroIdNew)) {
                livroIdOld.getEmprestimoCollection().remove(emprestimo);
                livroIdOld = em.merge(livroIdOld);
            }
            if (livroIdNew != null && !livroIdNew.equals(livroIdOld)) {
                livroIdNew.getEmprestimoCollection().add(emprestimo);
                livroIdNew = em.merge(livroIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getEmprestimoCollection().remove(emprestimo);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getEmprestimoCollection().add(emprestimo);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = emprestimo.getId();
                if (findEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.");
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
            Emprestimo emprestimo;
            try {
                emprestimo = em.getReference(Emprestimo.class, id);
                emprestimo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.", enfe);
            }
            Livro livroId = emprestimo.getLivroId();
            if (livroId != null) {
                livroId.getEmprestimoCollection().remove(emprestimo);
                livroId = em.merge(livroId);
            }
            Usuario usuarioId = emprestimo.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getEmprestimoCollection().remove(emprestimo);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(emprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emprestimo> findEmprestimoEntities() {
        return findEmprestimoEntities(true, -1, -1);
    }

    public List<Emprestimo> findEmprestimoEntities(int maxResults, int firstResult) {
        return findEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<Emprestimo> findEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emprestimo.class));
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

    public Emprestimo findEmprestimo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emprestimo> rt = cq.from(Emprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
