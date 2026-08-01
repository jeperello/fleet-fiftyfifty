package com.jeperello.fleetfiftyfifty.application.dto;

import java.util.List;

/**
 * Command object for consolidating weekly income.
 *
 * @param ownerPercentage the optional distribution percentage of income allocated to the owner
 * @param incomes         the list of income commands to be consolidated on a Week
 */
public record ConsolidateWeeklyIncomeCommand(
        Integer ownerPercentage,
        List<IncomeCommand> incomes
) {
}