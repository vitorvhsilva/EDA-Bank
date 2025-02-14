package br.com.bank.cards.api.dto.events

import br.com.bank.cards.domain.utils.enums.StatusTransacao
import java.math.BigDecimal

data class PagarFaturaEventDTO(
    val idUsuario: String,
    val valorFatura: BigDecimal,
    val mesAnoFatura: String,
    val status: StatusTransacao
)
