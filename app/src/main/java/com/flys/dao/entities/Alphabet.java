package com.flys.dao.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 */
public class Alphabet implements Serializable {
    private String name;

    public Alphabet() {
    }

    public Alphabet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alphabet)) return false;
        Alphabet alphabet = (Alphabet) o;
        return getName().equals(alphabet.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Alphabet{" +
                "name='" + name + '\'' +
                '}';
    }
}
