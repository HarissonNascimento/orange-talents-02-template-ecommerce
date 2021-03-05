package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class Ranking implements SuccessPurchaseEvent {

    @Override
    public void process(Purchase purchase) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request =
                Map.of("purchaseId", purchase.getId(), "sellerId", purchase.getProduct().getUser().getId());

        restTemplate.postForEntity("http://localhost:8080/ranking", request, String.class);
    }
}
