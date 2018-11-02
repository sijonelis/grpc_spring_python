package com.vilkazz.grpc.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the PythonResult entity.
 */
public class PythonResultDTO implements Serializable {

    private Long id;

    @NotNull
    private String requestHash;

    private String input;

    private Integer output;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PythonResultDTO pythonResultDTO = (PythonResultDTO) o;
        if (pythonResultDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pythonResultDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PythonResultDTO{" +
            "id=" + getId() +
            ", requestHash='" + getRequestHash() + "'" +
            ", input='" + getInput() + "'" +
            ", output=" + getOutput() +
            "}";
    }
}
