/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author yagon_000
 */
public class EMF {
    public static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("BibliotecaPU");
}
