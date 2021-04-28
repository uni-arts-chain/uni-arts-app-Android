@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package jp.co.soramitsu.feature_account_api.domain.model

import java.math.BigDecimal

data class Node(
        val id: Int,
        val name: String,
        val networkType: NetworkType,
        val link: String,
        val isDefault: Boolean
) {
    enum class NetworkType(
            val readableName: String,
            val runtimeConfiguration: RuntimeConfiguration
    ) {
        KUSAMA(
                "Kusama",
                RuntimeConfiguration(
                        pallets = DEFAULT_PALLETS,
                        addressByte = 2,
                        genesisHash = "b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe",
                        existentialDeposit = BigDecimal("0.001666666666")
                )
        ),
        POLKADOT(
                "Polkadot",
                RuntimeConfiguration(
                        pallets = PredefinedPalettes(
                                transfers = PredefinedPalettes.Transfers(13U)
                        ),
                        addressByte = 42,
                        genesisHash = "55940785b92be6342ba1007488a3f46fdbef213cd1b412d35236b03528079aaa",
                        existentialDeposit = BigDecimal("1")
                )
        ),
        WESTEND(
                "Westend",
                RuntimeConfiguration(
                        pallets = DEFAULT_PALLETS,
                        addressByte = 42,
                        genesisHash = "e143f23803ac50e8f6f8e62695d1ce9e4e1d68aa36c1cd2cfd15340213f3423e",
                        existentialDeposit = BigDecimal("0.01")
                )
        );

        companion object {
            fun <T> find(value: T, extractor: (NetworkType) -> T): NetworkType? {
                return values().find { extractor(it) == value }
            }

            fun findByAddressByte(addressByte: Byte) = find(addressByte) { it.runtimeConfiguration.addressByte }

            fun findByGenesis(genesis: String) = find(genesis) { it.runtimeConfiguration.genesisHash }
        }
    }
}

private val DEFAULT_PALLETS = PredefinedPalettes(
        transfers = PredefinedPalettes.Transfers(4U)
)