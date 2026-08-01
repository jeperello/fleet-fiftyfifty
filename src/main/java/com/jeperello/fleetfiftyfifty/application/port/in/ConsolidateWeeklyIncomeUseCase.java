package com.jeperello.fleetfiftyfifty.application.port.in;

import com.jeperello.fleetfiftyfifty.application.dto.ConsolidateWeeklyIncomeCommand;
import com.jeperello.fleetfiftyfifty.application.dto.WeeklyConsolidationResponse;

public interface ConsolidateWeeklyIncomeUseCase {
    WeeklyConsolidationResponse consolidate(ConsolidateWeeklyIncomeCommand command);
}