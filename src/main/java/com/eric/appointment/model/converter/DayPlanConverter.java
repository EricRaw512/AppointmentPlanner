package com.eric.appointment.model.converter;

import com.eric.appointment.model.DayPlan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.persistence.AttributeConverter;

public class DayPlanConverter implements AttributeConverter<DayPlan, String> {
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DayPlanConverter() {
        // Configure the ObjectMapper to format the JSON nicely
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String convertToDatabaseColumn(DayPlan attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle the exception or log an error
            throw new RuntimeException("Error converting DayPlan to JSON", e);
        }
    }

    @Override
    public DayPlan convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, DayPlan.class);
        } catch (JsonProcessingException e) {
            // Handle the exception or log an error
            throw new RuntimeException("Error converting JSON to DayPlan", e);
        }
    }
}
