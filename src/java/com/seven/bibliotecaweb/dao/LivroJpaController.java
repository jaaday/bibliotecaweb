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
import com.seven.bibliotecaweb.model.Livro;
import java.util.ArrayList;
import java.util.Collection;
import com.seven.bibliotecaweb.model.Multa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Jaaday
 */
public class LivroJpaController implements Serializable {

    public LivroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Livro livro) {
        if (livro.getEmprestimoCollection() == null) {
            livro.setEmprestimoCollection(new ArrayList<Emprestimo>());
        }
        if (livro.getMultaCollection() == null) {
            livro.setMultaCollection(new ArrayList<Multa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Emprestimo> attachedEmprestimoCollection = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoCollectionEmprestimoToAttach : livro.getEmprestimoCollection()) {
                emprestimoCollectionEmprestimoToAttach = em.getReference(emprestimoCollectionEmprestimoToAttach.getClass(), emprestimoCollectionEmprestimoToAttach.getId());
                attachedEmprestimoCollection.add(emprestimoCollectionEmprestimoToAttach);
            }
            livro.setEmprestimoCollection(attachedEmprestimoCollection);
            Collection<Multa> attachedMultaCollection = new ArrayList<Multa>();
            for (Multa multaCollectionMultaToAttach : livro.getMultaCollection()) {
                multaCollectionMultaToAttach = em.getReference(multaCollectionMultaToAttach.getClass(), multaCollectionMultaToAttach.getId());
                attachedMultaCollection.add(multaCollectionMultaToAttach);
            }
            livro.setMultaCollection(attachedMultaCollection);
            em.persist(livro);
            for (Emprestimo emprestimoCollectionEmprestimo : livro.getEmprestimoCollection()) {
                Livro oldLivroIdOfEmprestimoCollectionEmprestimo = emprestimoCollectionEmprestimo.getLivroId();
                emprestimoCollectionEmprestimo.setLivroId(livro);
                emprestimoCollectionEmprestimo = em.merge(emprestimoCollectionEmprestimo);
                if (oldLivroIdOfEmprestimoCollectionEmprestimo != null) {
                    oldLivroIdOfEmprestimoCollectionEmprestimo.getEmprestimoCollection().remove(emprestimoCollectionEmprestimo);
                    oldLivroIdOfEmprestimoCollectionEmprestimo = em.merge(oldLivroIdOfEmprestimoCollectionEmprestimo);
                }
            }
            for (Multa multaCollectionMulta : livro.getMultaCollection()) {
                Livro oldLivroIdOfMultaCollectionMulta = multaCollectionMulta.getLivroId();
                multaCollectionMulta.setLivroId(livro);
                multaCollectionMulta = em.merge(multaCollectionMulta);
                if (oldLivroIdOfMultaCollectionMulta != null) {
                    oldLivroIdOfMultaCollectionMulta.getMultaCollection().remove(multaCollectionMulta);
                    oldLivroIdOfMultaCollectionMulta = em.merge(oldLivroIdOfMultaCollectionMulta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Livro livro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Livro persistentLivro = em.find(Livro.class, livro.getId());
            Collection<Emprestimo> emprestimoCollectionOld = persistentLivro.getEmprestimoCollection();
            Collection<Emprestimo> emprestimoCollectionNew = livro.getEmprestimoCollection();
            Collection<Multa> multaCollectionOld = persistentLivro.getMultaCollection();
            Collection<Multa> multaCollectionNew = livro.getMultaCollection();
            Collection<Emprestimo> attachedEmprestimoCollectionNew = new ArrayList<Emprestimo>();
            for (Emprestimo emprestimoCollectionNewEmprestimoToAttach : emprestimoCollectionNew) {
                emprestimoCollectionNewEmprestimoToAttach = em.getReference(emprestimoCollectionNewEmprestimoToAttach.getClass(), emprestimoCollectionNewEmprestimoToAttach.getId());
                attachedEmprestimoCollectionNew.add(emprestimoCollectionNewEmprestimoToAttach);
            }
            emprestimoCollectionNew = attachedEmprestimoCollectionNew;
            livro.setEmprestimoCollection(emprestimoCollectionNew);
            Collection<Multa> attachedMultaCollectionNew = new ArrayList<Multa>();
            for (Multa multaCollectionNewMultaToAttach : multaCollectionNew) {
                multaCollectionNewMultaToAttach = em.getReference(multaCollectionNewMultaToAttach.getClass(), multaCollectionNewMultaToAttach.getId());
                attachedMultaCollectionNew.add(multaCollectionNewMultaToAttach);
            }
            multaCollectionNew = attachedMultaCollectionNew;
            livro.setMultaCollection(multaCollectionNew);
            livro = em.merge(livro);
            for (Emprestimo emprestimoCollectionOldEmprestimo : emprestimoCollectionOld) {
                if (!emprestimoCollectionNew.contains(emprestimoCollectionOldEmprestimo)) {
                    emprestimoCollectionOldEmprestimo.setLivroId(null);
                    emprestimoCollectionOldEmprestimo = em.merge(emprestimoCollectionOldEmprestimo);
                }
            }
            for (Emprestimo emprestimoCollectionNewEmprestimo : emprestimoCollectionNew) {
                if (!emprestimoCollectionOld.contains(emprestimoCollectionNewEmprestimo)) {
                    Livro oldLivroIdOfEmprestimoCollectionNewEmprestimo = emprestimoCollectionNewEmprestimo.getLivroId();
                    emprestimoCollectionNewEmprestimo.setLivroId(livro);
                    emprestimoCollectionNewEmprestimo = em.merge(emprestimoCollectionNewEmprestimo);
                    if (oldLivroIdOfEmprestimoCollectionNewEmprestimo != null && !oldLivroIdOfEmprestimoCollectionNewEmprestimo.equals(livro)) {
                        oldLivroIdOfEmprestimoCollectionNewEmprestimo.getEmprestimoCollection().remove(emprestimoCollectionNewEmprestimo);
                        oldLivroIdOfEmprestimoCollectionNewEmprestimo = em.merge(oldLivroIdOfEmprestimoCollectionNewEmprestimo);
                    }
                }
            }
            for (Multa multaCollectionOldMulta : multaCollectionOld) {
                if (!multaCollectionNew.contains(multaCollectionOldMulta)) {
                    multaCollectionOldMulta.setLivroId(null);
                    multaCollectionOldMulta = em.merge(multaCollectionOldMulta);
                }
            }
            for (Multa multaCollectionNewMulta : multaCollectionNew) {
                if (!multaCollectionOld.contains(multaCollectionNewMulta)) {
                    Livro oldLivroIdOfMultaCollectionNewMulta = multaCollectionNewMulta.getLivroId();
                    multaCollectionNewMulta.setLivroId(livro);
                    multaCollectionNewMulta = em.merge(multaCollectionNewMulta);
                    if (oldLivroIdOfMultaCollectionNewMulta != null && !oldLivroIdOfMultaCollectionNewMulta.equals(livro)) {
                        oldLivroIdOfMultaCollectionNewMulta.getMultaCollection().remove(multaCollectionNewMulta);
                        oldLivroIdOfMultaCollectionNewMulta = em.merge(oldLivroIdOfMultaCollectionNewMulta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = livro.getId();
                if (findLivro(id) == null) {
                    throw new NonexistentEntityException("The livro with id " + id + " no longer exists.");
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
            Livro livro;
            try {
                livro = em.getReference(Livro.class, id);
                livro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The livro with id " + id + " no longer exists.", enfe);
            }
            Collection<Emprestimo> emprestimoCollection = livro.getEmprestimoCollection();
            for (Emprestimo emprestimoCollectionEmprestimo : emprestimoCollection) {
                emprestimoCollectionEmprestimo.setLivroId(null);
                emprestimoCollectionEmprestimo = em.merge(emprestimoCollectionEmprestimo);
            }
            Collection<Multa> multaCollection = livro.getMultaCollection();
            for (Multa multaCollectionMulta : multaCollection) {
                multaCollectionMulta.setLivroId(null);
                multaCollectionMulta = em.merge(multaCollectionMulta);
            }
            em.remove(livro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Livro> findLivroEntities() {
        return findLivroEntities(true, -1, -1);
    }

    public List<Livro> findLivroEntities(int maxResults, int firstResult) {
        return findLivroEntities(false, maxResults, firstResult);
    }

    private List<Livro> findLivroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Livro.class));
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

    public Livro findLivro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Livro.class, id);
        } finally {
            em.close();
        }
    }

    public int getLivroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Livro> rt = cq.from(Livro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
