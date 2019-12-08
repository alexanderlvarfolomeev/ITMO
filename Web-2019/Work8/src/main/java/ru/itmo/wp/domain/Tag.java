package ru.itmo.wp.domain;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Tag {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Pattern(regexp = "[a-z]+")
    private String name;

    /**
     * @noinspection unused
     */
    public Tag() {
    }

    public Tag(@NotNull @Pattern(regexp = "[a-z]+") String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return Objects.equals(name, ((Tag) obj).name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}//razmer doljen bit mejdu 1 i 60
//Ne mojet bit pusto
