package com.jeperello.fleetfiftyfifty.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.jeperello.fleetfiftyfifty.application.dto.ConsolidateWeeklyIncomeCommand;
import com.jeperello.fleetfiftyfifty.application.dto.IncomeCommand;
import com.jeperello.fleetfiftyfifty.application.dto.WeeklyConsolidationResponse;
import com.jeperello.fleetfiftyfifty.domain.FleetRole;
import com.jeperello.fleetfiftyfifty.domain.PaymentMethod;
import com.jeperello.fleetfiftyfifty.domain.Platform;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsolidateWeeklyIncomeServiceTest {

    @Test
    void shouldConsolidateWeeklyIncomesSuccessfully() {
        ConsolidateWeeklyIncomeService service = new ConsolidateWeeklyIncomeService();

        ConsolidateWeeklyIncomeCommand command = new ConsolidateWeeklyIncomeCommand(
                50,
                List.of(
                        new IncomeCommand(FleetRole.OWNER, Platform.DIDI, PaymentMethod.BANK_TRANSFER, new BigDecimal("2000.00")),
                        new IncomeCommand(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.CASH, new BigDecimal("50000.00")),
                        new IncomeCommand(FleetRole.DRIVER, Platform.DIDI, PaymentMethod.MERCADO_PAGO, new BigDecimal("26000.00")),
                        new IncomeCommand(FleetRole.DRIVER, Platform.UBER, PaymentMethod.CASH, new BigDecimal("27000.00")),
                        new IncomeCommand(FleetRole.OWNER, Platform.UBER, PaymentMethod.BANK_TRANSFER, new BigDecimal("36000.00"))
                )
        );

        WeeklyConsolidationResponse response = service.consolidate(command);

        assertEquals(new BigDecimal("141000.00"), response.grandTotal());
        assertEquals(new BigDecimal("78000.00"), response.subtotalsByPlatform().get(Platform.DIDI));
        assertEquals(new BigDecimal("63000.00"), response.subtotalsByPlatform().get(Platform.UBER));
        assertEquals(FleetRole.DRIVER, response.balance().debtor());
        assertEquals(new BigDecimal("32500.00"), response.balance().amount());
    }
}