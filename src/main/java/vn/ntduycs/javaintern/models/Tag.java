package vn.ntduycs.javaintern.models;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false, of = {"name"})
@Entity
@Table(name = "tags")
public class Tag extends BaseModel {

    @Column(nullable = false)
    @NaturalId
    @NonNull
    private String name;

}
