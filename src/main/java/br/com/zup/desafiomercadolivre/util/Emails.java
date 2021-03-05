package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Question;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
public class Emails {

    private final String EMAIL_APPLICATION = "application@email.com";

    @Autowired
    private Mailer mailer;

    public void send(@NotNull @Valid Question question) {

        String productOwner = question.getProduct().getUser().getEmail();
        String questionOwner = question.getUser().getEmail();

        mailer.messenger("<html>...</html>", "Nova pergunta . . .", questionOwner, EMAIL_APPLICATION, productOwner);
    }

    public void send(@NotNull @Valid Purchase purchase) {

        String productSeller = purchase.getProduct().getUser().getEmail();
        String productBuyer = purchase.getUser().getEmail();

        mailer.messenger("<html>...</html>", "Nova venda . . .", productBuyer, EMAIL_APPLICATION, productSeller);
    }

    public void send(Purchase purchase, UriComponentsBuilder uriComponentsBuilder) {

        String returnUrl = purchase.getGatewayType().returnUrl(uriComponentsBuilder, purchase.getId());
        String productBuyer = purchase.getUser().getEmail();

        mailer.messenger("O pagamanto falhou, acesse o link " + returnUrl + " para tentar novamente", "Erro no pagamento", "sistema", EMAIL_APPLICATION, productBuyer);
    }

    public void send(Purchase purchase, Transaction transaction) {

        PurchaseDetails purchaseDetails = new PurchaseDetails().toPurchaseDetails(purchase);
        String status = transaction.getProcessStatus().toString();
        String productBuyer = purchase.getUser().getEmail();

        mailer.messenger("O status da sua compra é :" + status + ". \nSegue algumas informações sobre sua compra: " + purchaseDetails, "Compra finalizada!", "sistema", EMAIL_APPLICATION, productBuyer);
    }
}
