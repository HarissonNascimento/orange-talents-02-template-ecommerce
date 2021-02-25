package br.com.zup.desafiomercadolivre.util;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class EntityManagerUtil {

    public static List<?> findAll(Class<?> clazz, EntityManager entityManager) {
        return entityManager.createQuery("select o from " + clazz.getName() + " o", clazz).getResultList();
    }

    public static Optional<?> findFirst(Class<?> clazz, EntityManager entityManager){
        return Optional.ofNullable(entityManager.createQuery("select o from " + clazz.getName() + " o", clazz).getSingleResult());
    }
}
