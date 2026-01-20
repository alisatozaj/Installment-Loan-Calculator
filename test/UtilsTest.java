import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    // ---------- myRound ----------
    @Test
    void myRound_shouldRoundToGivenDecimals() {
        assertEquals(12.35, Utils.myRound(12.345, 2), 0.000001);
        assertEquals(12.34, Utils.myRound(12.344, 2), 0.000001);
        assertEquals(-1.24, Utils.myRound(-1.235, 2), 0.000001);
    }

    // ---------- rpad / lpad ----------
    @Test
    void rpad_shouldPadAndTruncateCorrectly() {
        assertEquals("abc  ", Utils.rpad("abc", 5));          // default pad " "
        assertEquals("abcxx", Utils.rpad("abc", 5, "x"));     // custom pad
        assertEquals("ab", Utils.rpad("abcdef", 2));          // truncation
    }

    @Test
    void lpad_shouldPadAndTruncateCorrectly() {
        assertEquals("  abc", Utils.lpad("abc", 5));          // default pad " "
        assertEquals("xxabc", Utils.lpad("abc", 5, "x"));     // custom pad
        assertEquals("ab", Utils.lpad("abcdef", 2));          // truncation (keeps leftmost via substring)
    }

    @Test
    void pad_shouldTreatNullInputsAsEmptyAndNullPadAsSpace() {
        assertEquals("   ", Utils.rpad(null, 3, null));
        assertEquals("   ", Utils.lpad(null, 3, null));
        assertEquals("a  ", Utils.rpad("a", 3, ""));          // empty pad -> space
        assertEquals("  a", Utils.lpad("a", 3, ""));          // empty pad -> space
    }

    // ---------- double2s / date2s ----------
    @Test
    void double2s_shouldFormatWithTwoDecimals_ItalianLocale() {
        // Locale.ITALIAN formatting: grouping '.' and decimal ','
        assertEquals("1.234,50", Utils.double2s(1234.5));
        assertEquals("0,00", Utils.double2s(0.0));
    }

    @Test
    void date2s_shouldFormatAs_dd_MM_yy_andHandleNull() {
        Calendar c = Calendar.getInstance();
        c.set(2025, Calendar.JANUARY, 15, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dt = c.getTime();

        assertEquals("15/01/25", Utils.date2s(dt));
        assertEquals("", Utils.date2s(null));
    }

    // ---------- date helpers ----------
    @Test
    void getFirstDayOfMonth_shouldReturnDay1SameMonth() {
        Calendar c = Calendar.getInstance();
        c.set(2025, Calendar.MARCH, 20, 10, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date first = Utils.getFirstDayOfMonth(c.getTime());

        Calendar out = Calendar.getInstance();
        out.setTime(first);
        assertEquals(1, out.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.MARCH, out.get(Calendar.MONTH));
        assertEquals(2025, out.get(Calendar.YEAR));
    }

    @Test
    void addMonth_shouldMoveMonthForward() {
        Calendar c = Calendar.getInstance();
        c.set(2025, Calendar.JANUARY, 10, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date outDate = Utils.addMonth(c.getTime(), 2);

        Calendar out = Calendar.getInstance();
        out.setTime(outDate);
        assertEquals(Calendar.MARCH, out.get(Calendar.MONTH));
        assertEquals(2025, out.get(Calendar.YEAR));
    }

    @Test
    void addDay_shouldMoveDayForward() {
        Calendar c = Calendar.getInstance();
        c.set(2025, Calendar.JANUARY, 10, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        Date outDate = Utils.addDay(c.getTime(), 5);

        Calendar out = Calendar.getInstance();
        out.setTime(outDate);
        assertEquals(15, out.get(Calendar.DAY_OF_MONTH));
        assertEquals(Calendar.JANUARY, out.get(Calendar.MONTH));
        assertEquals(2025, out.get(Calendar.YEAR));
    }

    @Test
    void getDayOfMonth_and_getMonthNumber_shouldReturnCalendarFields() {
        Calendar c = Calendar.getInstance();
        c.set(2025, Calendar.APRIL, 7, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

        assertEquals(7, Utils.getDayOfMonth(c.getTime()));
        assertEquals(Calendar.APRIL, Utils.getMonthNumber(c.getTime())); // 0-based month (APRIL = 3)
    }

    // ---------- strf2date ----------
    @Test
    void strf2date_shouldParseValidDate() {
        Date dt = Utils.strf2date("15/01/25", "dd/MM/yy");
        assertNotNull(dt);
        assertEquals("15/01/25", Utils.date2s(dt));
    }

    @Test
    void strf2date_invalidInput_shouldReturnNull() {
        Date dt = Utils.strf2date("not-a-date", "dd/MM/yy");
        assertNull(dt);
    }

}
