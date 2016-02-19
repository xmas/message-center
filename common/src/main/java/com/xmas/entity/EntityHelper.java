package com.xmas.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Generic class for using as Spring bean
 * Is written for saving evaluated by scripts entities into db
 * Instance of this class should be created in Spring context for concrete entity
 * Application context has to have CrudRepository for this class with name [classname]Repository
 * @param <T> type of served entity
 */
public class EntityHelper<T extends EvaluatedEntity> {

    private ApplicationContext applicationContext;

    private final Class<T> entityClass;

    private final CrudRepository<T, Long> repository;

    private final String entitiesJsonFileName;

    /**
     * Constructor for using in Spring java-based context
     * @param entityClass class of entities that will be processed by this helper
     * @param context application context. Is used to get entity repository from it
     */
    public EntityHelper(Class<T> entityClass, ApplicationContext context) {
        applicationContext = context;
        this.entityClass = entityClass;
        repository = getRepository();
        entitiesJsonFileName = getEntitiesJsonFileName();
    }

    public void save(String fullDirPath, Object parent) {
        repository.save(parse(fullDirPath, parent));
    }

    @SuppressWarnings("unchecked")
    private CrudRepository<T, Long> getRepository() {
        String repositoryName = entityClass.getSimpleName().toLowerCase() + "Repository";
        if (applicationContext.containsBean(repositoryName)) {
            return (CrudRepository<T, Long>) applicationContext.getBean(repositoryName, CrudRepository.class);
        } else {
            throw new NoSuchBeanDefinitionException(repositoryName);
        }
    }

    private String getEntitiesJsonFileName() {
        return entityClass.getSimpleName().toLowerCase() + "s.json";
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    private List<T> parse(String fullDirPath, Object parent) {
        try {
            return collect(mapper.reader()
                    .forType(entityClass)
                    .readValues(getRawData(fullDirPath)), parent);
        } catch (IOException e) {
            throw new ProcessingException("Can't get " + entityClass.getSimpleName() + " from output file. Probably script evaluate wrong file structure");
        }
    }

    private List<T> collect(Iterator<T> answerIterator, Object parent) {
        List<T> res = new ArrayList<>();
        answerIterator.forEachRemaining(e -> res.add(fillDefaultFields(e, parent)));
        return res;
    }

    private String getRawData(String fullDirPath) {
        String answersFilePath = Paths.get(fullDirPath, entitiesJsonFileName).toString();
        return new String(FileUtil.getFile(answersFilePath));
    }

    private T fillDefaultFields(T entity, Object parent){
        entity.setParent(parent);
        entity.setDate(LocalDateTime.now());
        return entity;
    }

}
