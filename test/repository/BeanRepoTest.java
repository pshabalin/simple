package repository;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import com.google.common.collect.Lists;
import model.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.testng.annotations.Test;


@Test
public class BeanRepoTest {

    public void testGetAll() {
        JdbcTemplate template = mock(JdbcTemplate.class);
        when(template.query(anyString(), any(RowMapper.class))).thenReturn(Lists.newArrayList(new Bean(), new Bean()));

        BeanRepo repo = new BeanRepo();
        repo.jdbcTemplate = template;

        assertEquals(repo.getAll().size(), 2);
        verify(template);
    }

}
