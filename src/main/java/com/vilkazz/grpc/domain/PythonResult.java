package com.vilkazz.grpc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PythonResult.
 */
@Entity
@Table(name = "python_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PythonResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "request_hash", nullable = false)
    private String requestHash;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private Integer output;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public PythonResult requestHash(String requestHash) {
        this.requestHash = requestHash;
        return this;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public String getInput() {
        return input;
    }

    public PythonResult input(String input) {
        this.input = input;
        return this;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getOutput() {
        return output;
    }

    public PythonResult output(Integer output) {
        this.output = output;
        return this;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PythonResult pythonResult = (PythonResult) o;
        if (pythonResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pythonResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PythonResult{" +
            "id=" + getId() +
            ", requestHash='" + getRequestHash() + "'" +
            ", input='" + getInput() + "'" +
            ", output=" + getOutput() +
            "}";
    }
}
