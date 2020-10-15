package pt.codeflex.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.codeflex.dto.ProblemDetails;
import pt.codeflex.models.Problem;
import pt.codeflex.services.CRUDService;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.*;


public class RESTController<T, ID extends Serializable> {

    private CRUDService<T, ID> service;

    public RESTController(CRUDService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public List<T> listAll() {
        return service.getAll();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<String, Object> create(@RequestBody T json) {
        T created = service.create(json);
        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        m.put("created", created);
        return m;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public T get(@PathVariable ID id) {
        return service.get(id);
    }

    @PatchMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<String, Object> update(@PathVariable ID id, @RequestBody T json) {
        Map<String, Object> m = new HashMap<>();
        try {
            T updated = service.update(id, json);
            m.put("success", true);
            m.put("updated", updated);
        } catch (EntityNotFoundException e) {
            System.out.println("[UPDATE FAIL] Did not found resource of id: " + id);
            m.put("success", false);
            m.put("updated", null);
        }
        return m;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> delete(@PathVariable ID id) {
        Map<String, Object> m = new HashMap<>();
        try {
            service.delete(id);
            m.put("success", true);
        } catch (IllegalArgumentException e) {
            m.put("success", false);
        }
        return m;
    }

}
