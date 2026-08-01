package com.jeperello.fleetfiftyfifty;

import com.jeperello.fleetfiftyfifty.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeeklyConsolidationTest {

    @Test
    void shouldCalculateConsolidationWithDefaultFiftyFiftySplit() {
        // Arrange
        WeeklyConsolidation consolidation = WeeklyConsolidation.ofDefaultSplit();

        // DiDi Incomes
        consolidation.addIncome(new Income(FleetRole.OWNER, Platform.DIDI, PaymentMethod.BANK_TRANSFER, Money.of("2000.00")));
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.CASH, Money.of("50000.00")));
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.MERCADO_PAGO, Money.of("26000.00")));

        // Uber Incomes
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.UBER, PaymentMethod.CASH, Money.of("27000.00")));
        consolidation.addIncome(new Income(FleetRole.OWNER, Platform.UBER, PaymentMethod.BANK_TRANSFER, Money.of("36000.00")));

        // Act & Assert - Subtotals by Platform
        assertEquals(Money.of("78000.00"), consolidation.getSubtotalByPlatform(Platform.DIDI));
        assertEquals(Money.of("63000.00"), consolidation.getSubtotalByPlatform(Platform.UBER));

        // Act & Assert - Totals collected by Role
        assertEquals(Money.of("38000.00"), consolidation.getTotalByRole(FleetRole.OWNER));
        assertEquals(Money.of("103000.00"), consolidation.getTotalByRole(FleetRole.DRIVER));

        // Act & Assert - Grand Total
        assertEquals(Money.of("141000.00"), consolidation.getGrandTotal());

        // Act & Assert - Target Shares (50% / 50%)
        assertEquals(Money.of("70500.00"), consolidation.getTargetShareForRole(FleetRole.OWNER));
        assertEquals(Money.of("70500.00"), consolidation.getTargetShareForRole(FleetRole.DRIVER));

        // Act & Assert - Balance Calculation
        BalanceResult balance = consolidation.calculateBalance();
        assertEquals(FleetRole.DRIVER, balance.debtor());
        assertEquals(FleetRole.OWNER, balance.creditor());
        assertEquals(Money.of("32500.00"), balance.amount());
    }

    @Test
    void shouldCalculateConsolidationWithCustomSixtyFortySplit() {
        // Arrange (60% Owner / 40% Driver)
        WeeklyConsolidation consolidation = WeeklyConsolidation.withSplit(ProfitSplit.of(60));

        // Incomes
        consolidation.addIncome(new Income(FleetRole.OWNER, Platform.DIDI, PaymentMethod.BANK_TRANSFER, Money.of("2000.00")));
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.CASH, Money.of("50000.00")));
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.MERCADO_PAGO, Money.of("26000.00")));
        consolidation.addIncome(new Income(FleetRole.DRIVER, Platform.UBER, PaymentMethod.CASH, Money.of("27000.00")));
        consolidation.addIncome(new Income(FleetRole.OWNER, Platform.UBER, PaymentMethod.BANK_TRANSFER, Money.of("36000.00")));

        // Act & Assert - Target Shares (60% Owner: $84,600.00 | 40% Driver: $56,400.00)
        assertEquals(Money.of("84600.00"), consolidation.getTargetShareForRole(FleetRole.OWNER));
        assertEquals(Money.of("56400.00"), consolidation.getTargetShareForRole(FleetRole.DRIVER));

        // Act & Assert - Balance Calculation ($103,000.00 - $56,400.00 = $46,600.00)
        BalanceResult balance = consolidation.calculateBalance();
        assertEquals(FleetRole.DRIVER, balance.debtor());
        assertEquals(FleetRole.OWNER, balance.creditor());
        assertEquals(Money.of("46600.00"), balance.amount());
    }
}