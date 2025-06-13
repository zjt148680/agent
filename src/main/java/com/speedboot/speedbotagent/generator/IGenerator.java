package com.speedboot.speedbotagent.generator;

import reactor.core.publisher.Flux;

public interface IGenerator<R> {
    Flux<R> streamGenerate(String prompt, String identity);

    R generate(String prompt, String identity);

    Flux<R> streamGenerate(String prompt);

    R generate(String prompt);

}
