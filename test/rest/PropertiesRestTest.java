package rest;

import model.Bean;
import org.testng.annotations.Test;
import repository.BeanRepo;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;


@Test
public class PropertiesRestTest {

    public void testGet() {
        PropertiesRest rest = new PropertiesRest();
        BeanRepo repo = mock(BeanRepo.class);
        when(repo.getAll()).thenReturn(new ArrayList<Bean>());

        rest.repo = repo;
        rest.get();

        verify(repo).init(1);
        verify(repo).getAll();
    }
}
