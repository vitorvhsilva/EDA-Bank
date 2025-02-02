package br.com.bank.users.api.http

import br.com.bank.users.api.dto.events.CartaoOutputDTO
import br.com.bank.users.api.dto.events.CatalogoCartaoOutputDTO
import br.com.bank.users.domain.utils.enums.Segmento
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient("ms-cards")
interface CartoesClient {
    @GetMapping("/cartoes/segmento/{segmento}")
    fun obterCartoesDisponiveisParaUsuario(@PathVariable segmento: Segmento): List<CatalogoCartaoOutputDTO>

    @GetMapping("cartoes/{id}")
    fun obterCartoesDisponiveisDoUsuario(@PathVariable id: String): List<CartaoOutputDTO>
}