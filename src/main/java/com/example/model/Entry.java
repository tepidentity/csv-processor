package com.example.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor
@ToString
public class Entry {

    @JsonIgnore
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, Object> properties = new LinkedHashMap<>();

    @JsonAnySetter
    public void setProperty(String key, Object value) {

        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {

        return properties;
    }
}
