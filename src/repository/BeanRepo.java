package repository;

import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class BeanRepo {

    static final Logger log = LoggerFactory.getLogger(BeanRepo.class);

    public List<Bean> getAll() {
        return Arrays.asList(new Bean("key 1", "value 1"), new Bean("key 2", "value 2"));
    }
}
