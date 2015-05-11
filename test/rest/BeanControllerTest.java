package rest;

import model.Bean;
import org.testng.annotations.Test;
import repository.BeanRepo;

import java.util.ArrayList;

import static org.mockito.Mockito.*;


@Test
public class BeanControllerTest {

    public void testGet() {
        BeanController rest = new BeanController();
        BeanRepo repo = mock(BeanRepo.class);
        when(repo.getAll()).thenReturn(new ArrayList<Bean>());

        rest.repo = repo;
        rest.get();

        verify(repo).getAll();
    }
}
