package com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.repository;

import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostDocument;
import com.fernando.ms.posts.app.infrastructure.adapter.output.persistence.models.PostUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class PostReactiveMongoRepositoryImpl implements PostReactiveMongoRepositoryCustom{

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<PostDocument> findAllUserAndPageAndSize(PostUser postUser, Long size, Long page) {
        Query query = new Query()
                .addCriteria(Criteria.where("postUser").is(postUser)) // Filtrar por autor
                .with(Sort.by(Sort.Direction.DESC, "datePost"))      // Ordenar por fecha descendente
                .skip((long) page * size)                            // Paginación: saltar los resultados
                .limit(Math.toIntExact(size));                                        // Limitar los resultados
        return mongoTemplate.find(query, PostDocument.class);
    }

}
