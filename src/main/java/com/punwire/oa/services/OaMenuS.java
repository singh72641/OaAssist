package com.punwire.oa.services;

import com.punwire.oa.domain.OaMenu;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by kanwal on 1/19/14.
 */
@Stateless
public class OaMenuS {

    @PersistenceContext
    private EntityManager entityManager;

    public OaMenu getMenu(String name)
    {
        TypedQuery<OaMenu> query =
                entityManager.createNamedQuery("OaMenu.findByName", OaMenu.class);
        query.setParameter("name",name);

        OaMenu menu = query.getSingleResult();
        return menu;
    }
}
