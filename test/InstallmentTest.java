public class InstallmentTest {
    import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class InstallmentTest {

        private Date dateOf(int year, int month0Based, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month0Based, day, 0, 0, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();
        }

        @Test
        void installment_firstDueDate_shouldMatchPlanFirstDueDate() {
            Date firstDue = dateOf(2025, Calendar.JANUARY, 15);
            Plan plan = new Plan(1000.0, 6, 10.0, firstDue);

            List<Installment> list = plan.getInstallments();
            assertFalse(list.isEmpty());

            Installment first = list.get(0);

            assertEquals(plan.getFirstDueDate().getTime(), first.getDueDate().getTime());
        }

        @Test
        void installment_count_shouldEqualNumberOfInstallments() {
            Plan plan = new Plan(1200.0, 12, 12.0, new Date());
            assertEquals(12, plan.getInstallments().size());
        }

        @Test
        void debtPaid_shouldIncrease_and_outstandingPrincipal_shouldDecrease_overTime() {
            Plan plan = new Plan(1000.0, 6, 10.0, new Date());
            List<Installment> ins = plan.getInstallments();

            Installment i1 = ins.get(0);
            Installment i2 = ins.get(1);

            assertTrue(i2.getDebtPaid() > i1.getDebtPaid(), "Debt paid should increase each month");
            assertTrue(i2.getOutstandingPrincipal() < i1.getOutstandingPrincipal(),
                    "Outstanding principal should decrease each month");
        }

        @Test
        void lastInstallment_shouldHaveZeroOutstandingPrincipal_and_zeroOutstandingInterests() {
            Plan plan = new Plan(1000.0, 6, 10.0, new Date());
            List<Installment> ins = plan.getInstallments();

            Installment last = ins.get(ins.size() - 1);

            assertEquals(0.0, last.getOutstandingPrincipal(), 0.0001);
            assertEquals(0.0, last.getOutstandingInterests(), 0.0001);
        }

        @Test
        void interestRateZero_shouldProduceZeroInterestPerInstallment() {
            Plan plan = new Plan(1200.0, 12, 0.0, new Date());
            for (Installment i : plan.getInstallments()) {
                assertEquals(0.0, i.getInterestAmount(), 0.0001);
            }
        }

        @Test
        void outstandingTotal_shouldEqualOutstandingPrincipalPlusOutstandingInterests() {
            Plan plan = new Plan(1000.0, 6, 10.0, new Date());
            Installment any = plan.getInstallments().get(2);

            assertEquals(any.getOutstandingPrincipal() + any.getOutstandingInterests(),
                    any.getOutstandingTotal(), 0.0001);
        }

        @Test
        void dueDateGetter_shouldNotExposeInternalMutableDate() {
            Date firstDue = dateOf(2025, Calendar.JANUARY, 15);
            Plan plan = new Plan(1000.0, 6, 10.0, firstDue);

            Installment first = plan.getInstallments().get(0);

            Date d1 = first.getDueDate();
            long original = d1.getTime();

            d1.setTime(original + 5L * 24 * 60 * 60 * 1000);

            Date d2 = first.getDueDate();

            assertEquals(original, d2.getTime(),
                    "getDueDate() should return a defensive copy, not the internal Date reference");
        }

        @Test
        void plan_withSingleInstallment_shouldNotCrash() {
            assertDoesNotThrow(() -> {
                Plan plan = new Plan(1000.0, 1, 10.0, new Date());
                assertEquals(1, plan.getInstallments().size());
            });
        }
    }
}
