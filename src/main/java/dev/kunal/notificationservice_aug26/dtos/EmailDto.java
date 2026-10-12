package dev.kunal.notificationservice_aug26.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {

    private String to;
    private String subject;
    private String from;
    private String body;

}
