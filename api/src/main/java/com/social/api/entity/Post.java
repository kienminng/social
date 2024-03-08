package com.social.api.entity;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @NotNull(message = "Your userid is incorrect, please login after create")
    private User CreatedUser;
    @Column(name = "create_at")
    private LocalDate createAT;
    @Column(name = "update_at")
    private LocalDate update_at;
    @Column(name = "status")
    private Status status;
    @OneToMany
    private List<Img> imgs;

    @PostLoad
    @PrePersist
    public void init() {
        if (createAT == null) {
            createAT = LocalDate.now();
        }
    }
}
