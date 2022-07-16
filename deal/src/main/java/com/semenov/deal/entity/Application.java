package com.semenov.deal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semenov.deal.dto.LoanOfferDTO;
import com.semenov.deal.model.ApplicationHistory;
import com.semenov.deal.model.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "application")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "client", nullable = false)
    @JsonManagedReference
    @ToString.Exclude
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit")
    private Credit credit;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Type(type = "jsonb")
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private LoanOfferDTO appliedOffer;

    @Column(name = "sing_date")
    private LocalDate singDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Type(type = "jsonb")
    @Column(name = "status_history", columnDefinition = "jsonb")
    private List<ApplicationHistory> statusHistory;
}
