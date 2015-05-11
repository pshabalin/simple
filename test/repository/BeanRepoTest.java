package repository;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

@Test
public class BeanRepoTest {

    public void testGetAll() {
        BeanRepo repo = new BeanRepo();
        assertEquals(repo.getAll().size(), 2);
    }

}
