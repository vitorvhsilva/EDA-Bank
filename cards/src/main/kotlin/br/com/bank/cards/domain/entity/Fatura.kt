package br.com.bank.cards.domain.entity

import br.com.bank.cards.domain.utils.enums.StatusFatura
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "tb_fatura")
data class Fatura(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val idFatura: String? = null,
    @ManyToOne @JoinColumn(name = "id_cartao")
    val cartao: Cartao,
    var valorFatura: BigDecimal,
    @Column(unique = true)
    val mesAnoFatura: String,
    @Enumerated(EnumType.STRING)
    var status: StatusFatura
)
