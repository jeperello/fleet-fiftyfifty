package com.jeperello.fleetfiftyfifty.application.dto;

import com.jeperello.fleetfiftyfifty.domain.FleetRole;
import com.jeperello.fleetfiftyfifty.domain.PaymentMethod;
import com.jeperello.fleetfiftyfifty.domain.Platform;

import java.math.BigDecimal;

/*
 * This class is used to transfer data from the application layer to the domain layer.
 * It is used to create a new single income.
 * It is also used to update an existing Income object.
 */
public record IncomeCommand(
        FleetRole fleetRole,
        Platform platform,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}