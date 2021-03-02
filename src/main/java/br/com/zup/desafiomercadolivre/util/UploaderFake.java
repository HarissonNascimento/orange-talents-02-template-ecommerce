package br.com.zup.desafiomercadolivre.util;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Primary
@Component
public class UploaderFake implements Uploader {

    public Set<String> send(List<MultipartFile> images) {
        return images.stream()
                .map(i -> "http://fakelink.io/" + UUID.randomUUID().toString() + i.getOriginalFilename())
                .collect(toSet());
    }
}
