package com.tas.event;

import com.tas.entity.Interview;
import org.springframework.context.ApplicationEvent;

public class InterviewScheduledEvent extends ApplicationEvent {
    private final Interview interview;

    public InterviewScheduledEvent(Object source, Interview interview) {
        super(source);
        this.interview = interview;
    }

    public Interview getInterview() {
        return interview;
    }
}