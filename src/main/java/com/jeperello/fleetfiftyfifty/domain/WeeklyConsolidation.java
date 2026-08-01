package com.jeperello.fleetfiftyfifty.domain;

import java.util.ArrayList;
import java.util.List;

public class WeeklyConsolidation {
    private final ProfitSplit profitSplit;
    private final List<Income> incomes;

    private WeeklyConsolidation(ProfitSplit profitSplit) {
        this.profitSplit = profitSplit;
        this.incomes = new ArrayList<>();
    }

    public static WeeklyConsolidation ofDefaultSplit() {
        return new WeeklyConsolidation(ProfitSplit.ofDefault());
    }

    public static WeeklyConsolidation withSplit(ProfitSplit profitSplit) {
        return new WeeklyConsolidation(profitSplit);
    }

    public void addIncome(Income income) {
        this.incomes.add(income);
    }

    public Money getSubtotalByPlatform(Platform platform) {
        return incomes.stream()
                .filter(income -> income.platform() == platform)
                .map(Income::amount)
                .reduce(Money.zero(), Money::add);
    }

    public Money getTotalByRole(FleetRole role) {
        return incomes.stream()
                .filter(income -> income.fleetRole() == role)
                .map(Income::amount)
                .reduce(Money.zero(), Money::add);
    }

    public Money getGrandTotal() {
        return incomes.stream()
                .map(Income::amount)
                .reduce(Money.zero(), Money::add);
    }

    public Money getTargetShareForRole(FleetRole role) {
        Money grandTotal = getGrandTotal();
        if (role == FleetRole.OWNER) {
            return grandTotal.percentage(profitSplit.ownerPercentage());
        } else {
            return grandTotal.subtract(getTargetShareForRole(FleetRole.OWNER));
        }
    }

    public BalanceResult calculateBalance() {
        Money driverCollected = getTotalByRole(FleetRole.DRIVER);
        Money driverTarget = getTargetShareForRole(FleetRole.DRIVER);

        if (driverCollected.amount().compareTo(driverTarget.amount()) > 0) {
            Money oweAmount = driverCollected.subtract(driverTarget);
            return new BalanceResult(FleetRole.DRIVER, FleetRole.OWNER, oweAmount);
        } else {
            Money ownerCollected = getTotalByRole(FleetRole.OWNER);
            Money ownerTarget = getTargetShareForRole(FleetRole.OWNER);
            Money oweAmount = ownerCollected.subtract(ownerTarget);
            return new BalanceResult(FleetRole.OWNER, FleetRole.DRIVER, oweAmount);
        }
    }
}
