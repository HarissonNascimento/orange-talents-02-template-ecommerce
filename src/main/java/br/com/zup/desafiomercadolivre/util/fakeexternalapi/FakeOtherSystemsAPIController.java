package br.com.zup.desafiomercadolivre.util.fakeexternalapi;

import br.com.zup.desafiomercadolivre.model.request.InvoiceRequest;
import br.com.zup.desafiomercadolivre.model.request.RankingRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FakeOtherSystemsAPIController {

    @PostMapping("/invoice")
    public void createInvoice(@RequestBody @Valid InvoiceRequest invoiceRequest) throws InterruptedException {
        System.out.println("Criando nota fiscal para " + invoiceRequest + ". . .");
        Thread.sleep(150);
    }
    @PostMapping("/ranking")
    public void ranking(@RequestBody @Valid RankingRequest rankingRequest) throws InterruptedException {
        System.out.println("Adicionando  ao ranking "+ rankingRequest + " . . . ");
        Thread.sleep(150);
    }

}
