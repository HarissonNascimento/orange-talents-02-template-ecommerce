package br.com.zup.desafiomercadolivre.util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

public class EntityManagerUtil {

    public static List<?> findAll(Class<?> clazz, EntityManager entityManager) {
        return entityManager.createQuery("select o from " + clazz.getName() + " o", clazz).getResultList();
    }

    public static Optional<?> findByString(Class<?> clazz, String string, String fieldName, EntityManager entityManager){
        Query query = entityManager.createQuery("select o from " + clazz.getName() + " o where o." + fieldName + " =:value");
        query.setParameter("value", string);
        return Optional.ofNullable(query.getSingleResult());
    }
}
