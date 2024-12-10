package com.ideadistribuidora.visus.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // @Override
    // public boolean equals(Object obj) {
    // if (!(obj instanceof AbstractEntity that)) {
    // return false; // null or not an AbstractEntity class
    // }
    // if (getId() != null) {
    // return getId().equals(that.getId());
    // }
    // return super.equals(that);
    // }
}
