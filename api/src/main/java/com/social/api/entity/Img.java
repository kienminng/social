package com.social.api.entity;
import javax.validation.constraints.NotBlank;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Img {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id ;
    @NotBlank
    private String url;
    private Status status;
    private ImgType imgType;
    @ManyToOne
    private Post posts;
    @ManyToOne
    private Message messages;
}
