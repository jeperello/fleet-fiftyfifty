package com.jeperello.fleetfiftyfifty.application.dto;

import com.jeperello.fleetfiftyfifty.domain.FleetRole;
import com.jeperello.fleetfiftyfifty.domain.Platform;

import java.math.BigDecimal;
import java.util.Map;

/*
 * This class is used to transfer data from the domain layer to the application layer.
 * It is used to return the result of a weekly consolidation calculation.
 * By now is only used on my portfolio project, but it could be used in a real application.
 */
public record WeeklyConsolidationResponse(
        Map<Platform, BigDecimal> subtotalsByPlatform,
        Map<FleetRole, BigDecimal> totalsByRole,
        Map<FleetRole, BigDecimal> targetSharesByRole,
        BigDecimal grandTotal,
        BalanceResultResponse balance
) {
}