import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    void doCalculate_rateZero_shouldHaveZeroInterest() {
        Plan plan = new Plan(1200.0, 12, 0.0, new Date());

        assertEquals(0.0, plan.getInterestAmount(), 0.0001);
        assertEquals(plan.getPrincipalAmount(), plan.getTotalAmount(), 0.01);
        assertEquals(12, plan.getInstallments().size());
    }

    @Test
    void doCalculate_ratePositive_shouldHavePositiveInterest() {
        Plan plan = new Plan(1200.0, 12, 12.0, new Date()); // 12% yearly

        assertTrue(plan.getInterestAmount() > 0.0);
        assertTrue(plan.getTotalAmount() > plan.getPrincipalAmount());
        assertEquals(12, plan.getInstallments().size());
    }

    @Test
    void doCalculate_withAdvancePayment_shouldReduceInstallmentBase() {
        Plan planNoAdvance = new Plan(1200.0, 12, 12.0, new Date(), 0.0);
        Plan planWithAdvance = new Plan(1200.0, 12, 12.0, new Date(), 200.0);

        assertEquals(200.0, planWithAdvance.getAdvancePaymentAmount(), 0.0001);

        assertTrue(
                planWithAdvance.getSingleInstallmentAmount()
                        <= planNoAdvance.getSingleInstallmentAmount(),
                "Installment with advance payment should not be higher"
        );

        assertEquals(
                planWithAdvance.getSingleInstallmentAmount()
                        * planWithAdvance.getNumberOfInstallments()
                        + planWithAdvance.getAdvancePaymentAmount(),
                planWithAdvance.getTotalAmount(),
                0.02
        );
    }

    @Test
    void doCalculate_shouldSetLastDueDateFromLastInstallment() {
        Plan plan = new Plan(1000.0, 6, 10.0, new Date());

        assertNotNull(plan.getLastDueDate());
        assertEquals(
                plan.getInstallments().get(plan.getInstallments().size() - 1).getDueDate(),
                plan.getLastDueDate()
        );
    }

    @Test
    void toStringAsTable_shouldShowAdvanceRowWhenAdvanceIsPositive() {
        Plan planWithAdvance = new Plan(1000.0, 6, 10.0, new Date(), 100.0);

        assertTrue(planWithAdvance.getAdvancePaymentAmount() > 0);

        String table = planWithAdvance.toStringAsTable();

        assertTrue(
                table.contains("ADV"),
                "Expected advance payment row in table, but it was missing.\n" + table
        );
    }

    @Test
    void toStringAsTable_shouldNotShowAdvanceRowWhenAdvanceIsZero() {
        Plan planNoAdvance = new Plan(1000.0, 6, 10.0, new Date(), 0.0);

        String table = planNoAdvance.toStringAsTable();

        assertFalse(
                table.contains("ADV"),
                "Did not expect advance payment row when advance is zero.\n" + table
        );
    }

    @Test
    void constructor_installmentsZero_shouldThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Plan(1000.0, 0, 10.0, new Date())
        );
    }
}
