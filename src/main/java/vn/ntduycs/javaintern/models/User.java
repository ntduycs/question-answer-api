package vn.ntduycs.javaintern.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {})
@EqualsAndHashCode(of = {}, callSuper = true)
@Entity
@Table(name = "users")
@JsonIgnoreProperties({
        "password",
        "enabled",
        "accountNonExpired",
        "accountNonLocked",
        "credentialsNonExpired",
        "username",
        "authorities"
})
public class User extends BaseModel implements UserDetails {

    @NaturalId
    @Column(nullable = false)
    @NonNull
    private String email;

    @Column(nullable = false)
    @NonNull
    private String password;

    @Column(nullable = false)
    @NonNull
    private String fullName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList((GrantedAuthority) () -> "USER");
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
