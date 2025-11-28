package com.mycompany.myapp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Die Textblock-Entität.
 */
@Schema(description = "Die Textblock-Entität.")
@Entity
@Table(name = "textblock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Textblock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 255)
    @Column(name = "describer", length = 255)
    private String describer;

    @Size(max = 255)
    @Column(name = "nr", length = 255)
    private String nr;

    @Size(max = 255)
    @Column(name = "text", length = 255)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Textblock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriber() {
        return this.describer;
    }

    public Textblock describer(String describer) {
        this.setDescriber(describer);
        return this;
    }

    public void setDescriber(String describer) {
        this.describer = describer;
    }

    public String getNr() {
        return this.nr;
    }

    public Textblock nr(String nr) {
        this.setNr(nr);
        return this;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getText() {
        return this.text;
    }

    public Textblock text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Textblock invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Textblock)) {
            return false;
        }
        return getId() != null && getId().equals(((Textblock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Textblock{" +
            "id=" + getId() +
            ", describer='" + getDescriber() + "'" +
            ", nr='" + getNr() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
