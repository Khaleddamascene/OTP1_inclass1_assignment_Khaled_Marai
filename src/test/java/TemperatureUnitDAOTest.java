
import org.example.dao.TemperatureUnitDAO;
import org.example.database.DBConnection;
import org.example.model.TemperatureUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitDAOTest {

    @BeforeAll
    static void setup() throws Exception {
        DBConnection.initializeDatabase();
    }

    @Test
    void findAllReturnsUnits() throws Exception {
        TemperatureUnitDAO dao = new TemperatureUnitDAO();

        List<TemperatureUnit> units = dao.findAll();

        assertTrue(units.size() >= 3);
    }

    @Test
    void findByIdReturnsCelsius() throws Exception {
        TemperatureUnitDAO dao = new TemperatureUnitDAO();

        TemperatureUnit unit = dao.findById(1);

        assertNotNull(unit);
        assertEquals("Celsius", unit.getName());
    }
}
