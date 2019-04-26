package com.hedera.hashgraph.sdk.crypto.mnemonic;

import org.bouncycastle.crypto.digests.SHA256Digest;

import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Mnemonic {
    private final int numBytes = (((256 << 8) | 8) >> 8) / 8;

    private final byte[] entropy;
    private final String phrase;

    public Mnemonic() {
        var bytes = new byte[numBytes];

        ThreadLocalRandom.current().nextBytes(bytes);

        this.entropy = bytes;

        var chksumByte = new SHA256Digest(entropy).getEncodedState()[0];

        var phraseJoiner = new StringJoiner(" ");

        for (byte b: this.entropy) {
            phraseJoiner.add(WordList.getWord(b));
        }

        phraseJoiner.add(WordList.getWord(chksumByte));

        phrase = phraseJoiner.toString();
    }

    public byte[] getEntropy() {
        return entropy;
    }

    @Override
    public String toString() {
        return phrase;
    }
}
