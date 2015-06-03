package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/db")
public class DataBaseController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(method = GET, produces = "text/plain")
    @ResponseBody
    public String get() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return Boolean.toString(connection.isValid(1000));
        }
    }

}
