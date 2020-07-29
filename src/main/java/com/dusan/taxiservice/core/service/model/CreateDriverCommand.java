package com.dusan.taxiservice.core.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDriverCommand extends CreateUserCommand {

    private long vehicleId;
}
