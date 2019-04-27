package com.hedera.hashgraph.sdk.crypto.mnemonic

import spock.lang.Specification

class MnemonicTest extends Specification {
	def "fresh mnemonic generates successfully"() {
		when:
		def mnemonic = new Mnemonic()

		then:
		mnemonic.toString().split(" ").length == 24
		mnemonic.entropy != null
	}

	def "mnemonic generates from entropy successfully"() {
		when:
		def mnemonic = new Mnemonic("0000000000000000000000000000000000000000000000000000000000000000".getBytes())

		then:
		mnemonic.toString().split(" ").length == 24
		mnemonic.entropy != null
		mnemonic.toString().contentEquals("abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon abandon art")
	}

}
