package pt.codeflex.services;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CRUDService<T, ID extends Serializable> {


    private CrudRepository<T, ID> repository;

    public CRUDService(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T get(ID id) {
        Optional<T> byId = this.repository.findById(id);
        return byId.orElseGet(() -> null);
    }

    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        this.repository.findAll().forEach(list::add);
        return list;
    }

    public T create(T entity) {
        return this.repository.save(entity);
    }

    public List<T> createAll(List<T> entities) {
        List<T> list = new ArrayList<>();
        Iterable<T> iter = this.repository.saveAll(entities);
        iter.forEach(list::add);
        return list;
    }

    public T update(@PathVariable ID id, @RequestBody T json) {
        Optional<T> entity = this.repository.findById(id);
        BeanUtils.copyProperties(entity, json);

        if (entity.isPresent()) {
            return this.repository.save(entity.get());
        }

        throw new EntityNotFoundException(entity.getClass().getSimpleName() + " of id " + id + " not found");
    }

    public void delete(@PathVariable ID id) {
        this.repository.deleteById(id);
    }


}
