package br.com.bank.users.domain.service

import br.com.bank.users.api.dto.input.CadastroUsuarioInputDTO
import br.com.bank.users.api.dto.output.CadastroUsuarioOutputDTO
import br.com.bank.users.domain.entity.Usuario
import br.com.bank.users.domain.repository.UsuarioRepository
import br.com.bank.users.domain.utils.mappers.UsuarioMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UsuarioService (
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapperImpl: UsuarioMapper
) {
    fun cadastrarUsuario(dto: CadastroUsuarioInputDTO): ResponseEntity<CadastroUsuarioOutputDTO> {
        var usuario: Usuario = usuarioMapperImpl.cadastroInputParaEntidade(dto)

        injetarAgenciaeConta(usuario)
        usuario = usuarioRepository.save(usuario)

        val usuarioOutput: CadastroUsuarioOutputDTO = usuarioMapperImpl.entidadeParaCadastroOutput(usuario)

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioOutput)
    }

    private fun injetarAgenciaeConta(usuario: Usuario) {
        var agencia: String
        var conta: String
        do {
            agencia = Random.nextInt(1000).toString().padStart(3, '0')
            conta = "${Random.nextInt(100000).toString().padStart(5, '0')}-${Random.nextInt(10)}"
        } while (usuarioRepository.existsByContaAndAgencia(conta, agencia))

        usuario.agencia = agencia
        usuario.conta = conta
    }
}