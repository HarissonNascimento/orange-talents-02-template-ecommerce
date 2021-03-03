package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Category;
import br.com.zup.desafiomercadolivre.model.domain.Characteristic;
import br.com.zup.desafiomercadolivre.model.domain.Product;
import br.com.zup.desafiomercadolivre.model.domain.User;
import br.com.zup.desafiomercadolivre.model.request.CharacteristicPostRequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.zup.desafiomercadolivre.util.EntityManagerUtil.findByString;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ProductCreatorUtil {


    public static Product persistProduct(String email, EntityManager entityManager, Category category) {

        User user = (User) findByString(User.class, email, "email", entityManager)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        List<Characteristic> collectList = getCharacteristicListWithMoreThanThree().stream().map(CharacteristicPostRequestBody::toCharacteristic)
                .collect(Collectors.toList());

        Product product = new Product("TestProduct", 10, BigDecimal.valueOf(10), collectList, "DescriptionProduct", category, user);

        entityManager.persist(product);
        return product;
    }

    private static List<CharacteristicPostRequestBody> getCharacteristicListWithMoreThanThree() {
        return Arrays.asList(
                new CharacteristicPostRequestBody("TestCharacteristic1", "DescriptionCharacteristic1"),
                new CharacteristicPostRequestBody("TestCharacteristic2", "DescriptionCharacteristic2"),
                new CharacteristicPostRequestBody("TestCharacteristic3", "DescriptionCharacteristic3")
        );
    }
}
