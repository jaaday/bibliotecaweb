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
import com.seven.bibliotecaweb.model.Emprestimo;
import java.util.ArrayList;
import java.util.Collection;
import com.seven.bibliotecaweb.model.Multa;
import com.seven.bibliotecaweb.model.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jaaday
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getEmprestimoCollection() == null) {
            usuario.setEmprestimoCollection(new ArrayList<Emprestimo>());
        }
        if (usuario.getMultaCollection() == null) {
            usuario.setMultaCollection(new ArrayList<Multa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Emprestimo> attachedEmprestimoCollection = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoCollectionEmprestimoToAttach : usuario.getEmprestimoCollection()) {
                emprestimoCollectionEmprestimoToAttach = em.getReference(emprestimoCollectionEmprestimoToAttach.getClass(), emprestimoCollectionEmprestimoToAttach.getId());
                attachedEmprestimoCollection.add(emprestimoCollectionEmprestimoToAttach);
            }
            usuario.setEmprestimoCollection(attachedEmprestimoCollection);
            Collection<Multa> attachedMultaCollection = new ArrayList<Multa>();
            for (Multa multaCollectionMultaToAttach : usuario.getMultaCollection()) {
                multaCollectionMultaToAttach = em.getReference(multaCollectionMultaToAttach.getClass(), multaCollectionMultaToAttach.getId());
                attachedMultaCollection.add(multaCollectionMultaToAttach);
            }
            usuario.setMultaCollection(attachedMultaCollection);
            em.persist(usuario);
            for (Emprestimo emprestimoCollectionEmprestimo : usuario.getEmprestimoCollection()) {
                Usuario oldUsuarioIdOfEmprestimoCollectionEmprestimo = emprestimoCollectionEmprestimo.getUsuarioId();
                emprestimoCollectionEmprestimo.setUsuarioId(usuario);
                emprestimoCollectionEmprestimo = em.merge(emprestimoCollectionEmprestimo);
                if (oldUsuarioIdOfEmprestimoCollectionEmprestimo != null) {
                    oldUsuarioIdOfEmprestimoCollectionEmprestimo.getEmprestimoCollection().remove(emprestimoCollectionEmprestimo);
                    oldUsuarioIdOfEmprestimoCollectionEmprestimo = em.merge(oldUsuarioIdOfEmprestimoCollectionEmprestimo);
                }
            }
            for (Multa multaCollectionMulta : usuario.getMultaCollection()) {
                Usuario oldUsuarioIdOfMultaCollectionMulta = multaCollectionMulta.getUsuarioId();
                multaCollectionMulta.setUsuarioId(usuario);
                multaCollectionMulta = em.merge(multaCollectionMulta);
                if (oldUsuarioIdOfMultaCollectionMulta != null) {
                    oldUsuarioIdOfMultaCollectionMulta.getMultaCollection().remove(multaCollectionMulta);
                    oldUsuarioIdOfMultaCollectionMulta = em.merge(oldUsuarioIdOfMultaCollectionMulta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            Collection<Emprestimo> emprestimoCollectionOld = persistentUsuario.getEmprestimoCollection();
            Collection<Emprestimo> emprestimoCollectionNew = usuario.getEmprestimoCollection();
            Collection<Multa> multaCollectionOld = persistentUsuario.getMultaCollection();
            Collection<Multa> multaCollectionNew = usuario.getMultaCollection();
            Collection<Emprestimo> attachedEmprestimoCollectionNew = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoCollectionNewEmprestimoToAttach : emprestimoCollectionNew) {
                emprestimoCollectionNewEmprestimoToAttach = em.getReference(emprestimoCollectionNewEmprestimoToAttach.getClass(), emprestimoCollectionNewEmprestimoToAttach.getId());
                attachedEmprestimoCollectionNew.add(emprestimoCollectionNewEmprestimoToAttach);
            }
            emprestimoCollectionNew = attachedEmprestimoCollectionNew;
            usuario.setEmprestimoCollection(emprestimoCollectionNew);
            Collection<Multa> attachedMultaCollectionNew = new ArrayList<Multa>();
            for (Multa multaCollectionNewMultaToAttach : multaCollectionNew) {
                multaCollectionNewMultaToAttach = em.getReference(multaCollectionNewMultaToAttach.getClass(), multaCollectionNewMultaToAttach.getId());
                attachedMultaCollectionNew.add(multaCollectionNewMultaToAttach);
            }
            multaCollectionNew = attachedMultaCollectionNew;
            usuario.setMultaCollection(multaCollectionNew);
            usuario = em.merge(usuario);
            for (Emprestimo emprestimoCollectionOldEmprestimo : emprestimoCollectionOld) {
                if (!emprestimoCollectionNew.contains(emprestimoCollectionOldEmprestimo)) {
                    emprestimoCollectionOldEmprestimo.setUsuarioId(null);
                    emprestimoCollectionOldEmprestimo = em.merge(emprestimoCollectionOldEmprestimo);
                }
            }
            for (Emprestimo emprestimoCollectionNewEmprestimo : emprestimoCollectionNew) {
                if (!emprestimoCollectionOld.contains(emprestimoCollectionNewEmprestimo)) {
                    Usuario oldUsuarioIdOfEmprestimoCollectionNewEmprestimo = emprestimoCollectionNewEmprestimo.getUsuarioId();
                    emprestimoCollectionNewEmprestimo.setUsuarioId(usuario);
                    emprestimoCollectionNewEmprestimo = em.merge(emprestimoCollectionNewEmprestimo);
                    if (oldUsuarioIdOfEmprestimoCollectionNewEmprestimo != null && !oldUsuarioIdOfEmprestimoCollectionNewEmprestimo.equals(usuario)) {
                        oldUsuarioIdOfEmprestimoCollectionNewEmprestimo.getEmprestimoCollection().remove(emprestimoCollectionNewEmprestimo);
                        oldUsuarioIdOfEmprestimoCollectionNewEmprestimo = em.merge(oldUsuarioIdOfEmprestimoCollectionNewEmprestimo);
                    }
                }
            }
            for (Multa multaCollectionOldMulta : multaCollectionOld) {
                if (!multaCollectionNew.contains(multaCollectionOldMulta)) {
                    multaCollectionOldMulta.setUsuarioId(null);
                    multaCollectionOldMulta = em.merge(multaCollectionOldMulta);
                }
            }
            for (Multa multaCollectionNewMulta : multaCollectionNew) {
                if (!multaCollectionOld.contains(multaCollectionNewMulta)) {
                    Usuario oldUsuarioIdOfMultaCollectionNewMulta = multaCollectionNewMulta.getUsuarioId();
                    multaCollectionNewMulta.setUsuarioId(usuario);
                    multaCollectionNewMulta = em.merge(multaCollectionNewMulta);
                    if (oldUsuarioIdOfMultaCollectionNewMulta != null && !oldUsuarioIdOfMultaCollectionNewMulta.equals(usuario)) {
                        oldUsuarioIdOfMultaCollectionNewMulta.getMultaCollection().remove(multaCollectionNewMulta);
                        oldUsuarioIdOfMultaCollectionNewMulta = em.merge(oldUsuarioIdOfMultaCollectionNewMulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Collection<Emprestimo> emprestimoCollection = usuario.getEmprestimoCollection();
            for (Emprestimo emprestimoCollectionEmprestimo : emprestimoCollection) {
                emprestimoCollectionEmprestimo.setUsuarioId(null);
                emprestimoCollectionEmprestimo = em.merge(emprestimoCollectionEmprestimo);
            }
            Collection<Multa> multaCollection = usuario.getMultaCollection();
            for (Multa multaCollectionMulta : multaCollection) {
                multaCollectionMulta.setUsuarioId(null);
                multaCollectionMulta = em.merge(multaCollectionMulta);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
