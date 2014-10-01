/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.seven.bibliotecaweb.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jaaday
 */
@Entity
@Table(name = "emprestimo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Emprestimo.findAll", query = "SELECT e FROM Emprestimo e"),
    @NamedQuery(name = "Emprestimo.findById", query = "SELECT e FROM Emprestimo e WHERE e.id = :id"),
    @NamedQuery(name = "Emprestimo.findByDataEmprestimo", query = "SELECT e FROM Emprestimo e WHERE e.dataEmprestimo = :dataEmprestimo"),
    @NamedQuery(name = "Emprestimo.findByDataPrevista", query = "SELECT e FROM Emprestimo e WHERE e.dataPrevista = :dataPrevista"),
    @NamedQuery(name = "Emprestimo.findByDataDevolucao", query = "SELECT e FROM Emprestimo e WHERE e.dataDevolucao = :dataDevolucao")})
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_emprestimo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmprestimo;
    @Column(name = "data_prevista")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrevista;
    @Column(name = "data_devolucao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDevolucao;
    @JoinColumn(name = "livro_id", referencedColumnName = "id")
    @ManyToOne
    private Livro livroId;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioId;

    public Emprestimo() {
    }

    public Emprestimo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Livro getLivroId() {
        return livroId;
    }

    public void setLivroId(Livro livroId) {
        this.livroId = livroId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprestimo)) {
            return false;
        }
        Emprestimo other = (Emprestimo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.seven.bibliotecaweb.model.Emprestimo[ id=" + id + " ]";
    }
    
}
