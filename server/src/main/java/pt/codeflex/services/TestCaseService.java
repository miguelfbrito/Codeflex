package pt.codeflex.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pt.codeflex.models.TestCase;

@Service
public class TestCaseService extends CRUDService<TestCase, Long> {


    private TestCaseService testCaseService;

    @Autowired
    public TestCaseService(CrudRepository<TestCase, Long> repository) {
        super(repository);
    }


}
