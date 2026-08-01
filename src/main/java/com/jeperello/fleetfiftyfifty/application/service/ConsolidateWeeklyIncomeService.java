package com.jeperello.fleetfiftyfifty.application.service;

import com.jeperello.fleetfiftyfifty.application.dto.BalanceResultResponse;
import com.jeperello.fleetfiftyfifty.application.dto.ConsolidateWeeklyIncomeCommand;
import com.jeperello.fleetfiftyfifty.application.dto.WeeklyConsolidationResponse;
import com.jeperello.fleetfiftyfifty.application.port.in.ConsolidateWeeklyIncomeUseCase;
import com.jeperello.fleetfiftyfifty.domain.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * This class is used to implement the use case of consolidating weekly income.
 * It is used to calculate the total income for each role and platform, as well as the balance between roles.
 * It is also used to return the result of the consolidation calculation.
 */
public class ConsolidateWeeklyIncomeService implements ConsolidateWeeklyIncomeUseCase {

    @Override
    public WeeklyConsolidationResponse consolidate(ConsolidateWeeklyIncomeCommand command) {
        // 1. Instanciar el Agregado de Dominio según el esquema de reparto indicado
        WeeklyConsolidation consolidation = (command.ownerPercentage() != null)
                ? WeeklyConsolidation.withSplit(ProfitSplit.of(command.ownerPercentage()))
                : WeeklyConsolidation.ofDefaultSplit();

        // 2. Mapear DTOs de entrada e insertarlos en el Agregado
        if (command.incomes() != null) {
            command.incomes().forEach(dto ->
                    consolidation.addIncome(new Income(
                            dto.fleetRole(),
                            dto.platform(),
                            dto.paymentMethod(),
                            Money.of(dto.amount().toString())
                    ))
            );
        }

        // 3. Mapear subtotales por plataforma para la UI
        Map<Platform, BigDecimal> subtotalsByPlatform = Arrays.stream(Platform.values())
                .collect(Collectors.toMap(
                        platform -> platform,
                        platform -> consolidation.getSubtotalByPlatform(platform).amount()
                ));

        // 4. Mapear totales y metas por rol para la UI
        Map<FleetRole, BigDecimal> totalsByRole = Arrays.stream(FleetRole.values())
                .collect(Collectors.toMap(
                        role -> role,
                        role -> consolidation.getTotalByRole(role).amount()
                ));

        Map<FleetRole, BigDecimal> targetSharesByRole = Arrays.stream(FleetRole.values())
                .collect(Collectors.toMap(
                        role -> role,
                        role -> consolidation.getTargetShareForRole(role).amount()
                ));

        // 5. Calcular finiquito de saldo
        BalanceResult balanceResult = consolidation.calculateBalance();
        BalanceResultResponse balanceResponse = new BalanceResultResponse(
                balanceResult.debtor(),
                balanceResult.creditor(),
                balanceResult.amount().amount()
        );

        // 6. Retornar DTO de respuesta consolidado
        return new WeeklyConsolidationResponse(
                subtotalsByPlatform,
                totalsByRole,
                targetSharesByRole,
                consolidation.getGrandTotal().amount(),
                balanceResponse
        );
    }
}