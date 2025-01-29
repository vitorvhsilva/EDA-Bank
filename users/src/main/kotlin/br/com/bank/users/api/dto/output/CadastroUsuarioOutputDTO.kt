package br.com.bank.users.api.dto.output

import br.com.bank.users.domain.utils.enums.Genero
import br.com.bank.users.domain.utils.enums.StatusUsuario
import jakarta.persistence.*
import java.time.LocalDateTime

data class CadastroUsuarioOutputDTO(
    val id: String?,
    val nome: String,
    val email: String,
    val senha: String,
    val cpf: String,
    @Enumerated(EnumType.STRING)
    val genero: Genero,
    var agencia: String,
    var conta: String,
    val dataNascimento: LocalDateTime,
    var dataCriacaoConta: LocalDateTime,
    @Enumerated(EnumType.STRING)
    var statusUsuario: StatusUsuario
)
