package com.jeperello.fleetfiftyfifty.domain;

public record Income(
        FleetRole fleetRole,
        Platform platform,
        PaymentMethod paymentMethod,
        Money amount
) {
}
