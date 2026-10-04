

import org.example.model.TempRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordTest {

    @Test
    void recordStoresValues() {
        LocalDateTime now = LocalDateTime.now();

        TempRecord record =
                new TempRecord(1, 100, 2, 37.777, 1, now);

        assertEquals(1, record.getId());
        assertEquals(100, record.getValue());
        assertEquals(2, record.getUnitId());
        assertEquals(37.777, record.getConvertedValue());
        assertEquals(1, record.getConvertedUnitId());
        assertEquals(now, record.getCreatedAt());
    }
}
