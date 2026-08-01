package com.jeperello.fleetfiftyfifty.application.dto;

import com.jeperello.fleetfiftyfifty.domain.FleetRole;

import java.math.BigDecimal;

/*
 * This class is used to transfer data from the domain layer to the application layer.
 * It is used to return the result of a balance calculation.
 */
public record BalanceResultResponse(
        FleetRole debtor,
        FleetRole creditor,
        BigDecimal amount
) {
}