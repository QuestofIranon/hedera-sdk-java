package com.hedera.hashgraph.sdk.crypto.mnemonic;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

public final class Seed {
    private final PKCS5S2ParametersGenerator paramGenerator = new PKCS5S2ParametersGenerator(new SHA512Digest());
    private final int iterations = 2048;

    private final byte[] bytes;

    public Seed(Mnemonic phrase, String password) {

        var salt = new StringBuilder("mnemonic")
            .append(password)
            .toString()
            .getBytes();

        paramGenerator.init(phrase.getEntropy(), salt, iterations);

        bytes = ((KeyParameter) paramGenerator.generateDerivedMacParameters(512)).getKey();
    }

    public byte[] getBytes() {
        return bytes;
    }
}
