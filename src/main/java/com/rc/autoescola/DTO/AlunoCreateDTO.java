package com.rc.autoescola.DTO;


import com.rc.autoescola.models.Veiculo;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AlunoCreateDTO {

    @NotBlank(message = "o campo nome precisa ser informado")
    private String nome;

    private String matricula;

    @NotBlank(message = "o campo email precisa ser informado")
    @Email(message = "o campo email precisa ser válido")
    private String email;

    private Veiculo veiculo;
}
