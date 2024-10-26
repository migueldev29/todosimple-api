package com.migueldev.todosimple.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.migueldev.todosimple.models.enums.ProfileEnum;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {
    public interface CreateUser {}
    public interface UpdateUser {}

    public static final String TABLE_NAME = "user";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class) //Aplica regra quando utilizar a interface CreateUser
    @NotEmpty(groups = CreateUser.class) //
    @Size(groups = CreateUser.class, min = 2, max = 100) //
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY) //Não exibir a senha visivelmente no JSON
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = { CreateUser.class, UpdateUser.class }) //Aplica regra quando utilizar a interface CreateUser e UpdateUser
    @NotEmpty(groups = { CreateUser.class, UpdateUser.class }) //
    @Size(groups = { CreateUser.class, UpdateUser.class }, min = 8, max = 60) //
    private String password;

    @OneToMany(mappedBy = "user") //Relacionamento(um usuário pode ter várias tasks) mapeado lá dentro de tasks pelo atributo user
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = Access.WRITE_ONLY) //Foi colocado para que quando buscar um usuário não trazer as Tasks, apenas posso inserir tasks para o usuário.
    @CollectionTable(name = "user_profile") //Tabela nova que associa o usuário e perfil
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        /*  
         *  Stream = Transformo em uma sequência de elementos que pode ser manipulada
         *  Map = Uma sequência de elemtos que contém Chave e Valor
         *  x -> ProfileEnum.toEnum(x) = Percorre a sequência de perfis, executa o metódo toEnum passando o código e obtém o valor no formato ProfileEnum para cada um
         *  .collect(Collectors.toSet()) = Transforma em formato de lista que a lista Set consiga receber.
        */
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }
}
