import org.example.dao.TempRecordDAO;
import org.example.database.DBConnection;
import org.example.model.TempRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordDAOTest {

    @BeforeAll
    static void setup() throws Exception {
        DBConnection.initializeDatabase();
    }

    @Test
    void saveAndFindRecord() throws Exception {

        TempRecordDAO dao = new TempRecordDAO();

        int before = dao.findAll().size();

        dao.save(100, 2, 37.777, 1);

        List<TempRecord> records = dao.findAll();

        assertEquals(before + 1, records.size());

        TempRecord record = records.get(0);

        assertEquals(100, record.getValue(), 0.001);
        assertEquals(2, record.getUnitId());
        assertEquals(37.777, record.getConvertedValue(), 0.001);
        assertEquals(1, record.getConvertedUnitId());
    }
}