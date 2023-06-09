package com.rc.autoescola.service;

import com.rc.autoescola.DTO.AlunoCreateDTO;
import com.rc.autoescola.DTO.AlunoUpdateDTO;
import com.rc.autoescola.domain.service.AlunoService;
import com.rc.autoescola.exception.NotFoundException;
import com.rc.autoescola.domain.models.Aluno;
import com.rc.autoescola.domain.repository.AlunoRepository;
import com.rc.autoescola.util.AlunoCreator;
import com.rc.autoescola.util.AlunoPatchDTOCreator;
import com.rc.autoescola.util.AlunoPostDTOCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AlunoServiceTest {
    @InjectMocks
    AlunoService alunoService;

    @Mock
    AlunoRepository alunoRepositoryMock;

    @Mock
    ModelMapper modelMapperMock;

    @BeforeEach
    void setUp(){
        PageImpl<Aluno> alunoPage = new PageImpl<>(List.of(AlunoCreator.createValidAluno()));

        BDDMockito.when(alunoRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(alunoPage);

        BDDMockito.when(alunoRepositoryMock.findAll())
                .thenReturn(List.of(AlunoCreator.createValidAluno()));

        BDDMockito.when(alunoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AlunoCreator.createValidAluno()));

        BDDMockito.when(alunoRepositoryMock.findAlunoByNomeContainingIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AlunoCreator.createValidAluno()));

        BDDMockito.when(alunoRepositoryMock.findAlunoByMatricula(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(AlunoCreator.createValidAluno()));

        BDDMockito.when(modelMapperMock.map(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(AlunoCreator.createValidAluno());

        BDDMockito.when(alunoRepositoryMock.save(ArgumentMatchers.any(Aluno.class)))
                .thenReturn(AlunoCreator.createValidAluno());

        BDDMockito.doNothing().when(alunoRepositoryMock).delete(ArgumentMatchers.any(Aluno.class));

    }

    @Test
    @DisplayName("Find All Paginated retorna uma lista de Aluno dentro de uma Page quando ocorrer sucesso")
    void findAllPaginated_ReturnsListOfAlunosInsidePageObject_WhenSuccessful() {
        String expectedName = AlunoCreator.createValidAluno().getNome();

        Page<Aluno> alunoPage = alunoService.findAllPaginated(PageRequest.of(1,1));

        Assertions.assertThat(alunoPage).isNotNull();
        Assertions.assertThat(alunoPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(alunoPage.toList().get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find All retorna uma lista de Aluno quando ocorrer sucesso")
    void findAll_ReturnsListOfAlunos_WhenSuccessful() {
        String expectedName = AlunoCreator.createValidAluno().getNome();

        List<Aluno> alunos = alunoService.findAll();

        Assertions.assertThat(alunos).isNotNull();
        Assertions.assertThat(alunos).isNotEmpty().hasSize(1);
        Assertions.assertThat(alunos.get(0).getNome()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Find By Id retorna uma aluno quando ocorrer sucesso")
    void findById_ReturnsAluno_WhenSuccessful() {
        Aluno validAluno = AlunoCreator.createValidAluno();

        Aluno aluno = alunoService.findById(1L);

        Assertions.assertThat(aluno).isNotNull();
        Assertions.assertThat(aluno.getId()).isNotNull().isEqualTo(validAluno.getId());
        Assertions.assertThat(aluno).isEqualTo(validAluno);
    }

    @Test
    @DisplayName("Find By Id lança Bad Request Exception quando nenhum aluno for encontrado pelo ID")
    void findById_ReturnsBadRequestException_WhenNoAlunoAreFound() {
        String message = "Aluno não encontrado";
        BDDMockito.when(alunoRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new NotFoundException(message));

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> alunoService.findById(1L))
                .withMessageContaining(message);
    }

    @Test
    @DisplayName("Find By Nome retorna uma lista de Alunos quando ocorrer sucesso")
    void findByNome_ReturnsListOfAlunos_WhenSuccessful() {
        String expectedName = AlunoCreator.createValidAluno().getNome();

        Aluno validAluno = AlunoCreator.createValidAluno();

        List<Aluno> alunosList = alunoService.findByName("dsdas");

        Assertions.assertThat(alunosList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .contains(validAluno);
        Assertions.assertThat(alunosList).contains(validAluno);
        Assertions.assertThat(alunosList.get(0)).isEqualTo(validAluno);
    }

    @Test
    @DisplayName("Find By Nome retorna uma lista vazia quando nenhum aluno for encontrado")
    void findByNome_ReturnsEmptyListOfAlunos_WhenNoAlunoAreFound() {
        BDDMockito.when(alunoRepositoryMock.findAlunoByNomeContainingIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        List<Aluno> alunosList = alunoService.findByName("dsdas");

        Assertions.assertThat(alunosList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Find By Matricula retorna um Aluno quando ocorrer sucesso")
    void findByMatricula_ReturnsAluno_WhenSuccessful() {
        Aluno validAluno = AlunoCreator.createValidAluno();
        Aluno aluno = alunoService.findByMatricula("111");

        Assertions.assertThat(aluno).isNotNull();
        Assertions.assertThat(aluno.getMatricula()).isEqualTo(validAluno.getMatricula());
        Assertions.assertThat(aluno).isEqualTo(validAluno);
    }

    @Test
    @DisplayName("Find By Matricula lança Bad Request Exception quando nenhum aluno for encontrado pelo ID")
    void findByMatricula_ReturnsBadRequestException_WhenNoAlunoAreFound() {
        String message = "Aluno não encontrado";
        BDDMockito.when(alunoRepositoryMock.findAlunoByMatricula(ArgumentMatchers.anyString()))
                .thenThrow(new NotFoundException(message));

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> alunoService.findByMatricula("111"))
                .withMessageContaining(message);
    }

    @Test
    @DisplayName("Save persiste o aluno no banco de dados quando ocorrer sucesso")
    void save_PersistsAluno_WhenSuccessful() {
        AlunoCreateDTO alunoPostDTO = AlunoPostDTOCreator.createAlunoPostDTO();

        Aluno alunoSaved = alunoService.save(alunoPostDTO);

        Assertions.assertThat(alunoSaved).isNotNull();
        Assertions.assertThat(alunoSaved.getId()).isNotNull();
        Assertions.assertThat(alunoSaved.getMatricula().length()).isEqualTo(10);
        Assertions.assertThat(alunoSaved).isEqualTo(AlunoCreator.createValidAluno());
    }

    @Test
    @DisplayName("Update atualiza um Aluno quando ocorrer sucesso")
    void update_UpdatesAluno_WhenSuccessful() {

        AlunoUpdateDTO alunoPatchDTO = AlunoPatchDTOCreator.createAlunoPatchDTO();

        Aluno alunoUpdated = alunoService.update(alunoPatchDTO, 1L);

        Assertions.assertThat(alunoUpdated).isNotNull();
        Assertions.assertThat(alunoUpdated.getId()).isNotNull();
        Assertions.assertThat(alunoUpdated.getMatricula().length()).isEqualTo(10);
        Assertions.assertThat(alunoUpdated).isEqualTo(AlunoCreator.createValidAluno());

        Assertions.assertThatCode(() -> alunoService.update(alunoPatchDTO, 1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Delete remove Aluno quando ocorrer sucesso")
    void delete_RemovesAluno_WhenSuccessful() {
        Assertions.assertThatCode(() -> alunoService.delete(1L)).doesNotThrowAnyException();
    }

}