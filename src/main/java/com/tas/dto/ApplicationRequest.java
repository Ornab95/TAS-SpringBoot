package com.tas.dto;

import lombok.Data;

@Data
public class ApplicationRequest {
    private Long jobId;
    private Long candidateId;
    private String coverLetter;
}