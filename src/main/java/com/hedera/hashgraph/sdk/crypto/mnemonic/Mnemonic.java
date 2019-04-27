package com.hedera.hashgraph.sdk.crypto.mnemonic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Mnemonic {
    private final int numBytes = (((256 << 8) | 8) >> 8) / 8;

    private final byte[] entropy;
    private final String phrase;
    
    // fixme: needs to handle groups of 11bits instead of bytes.

    private String phraseFromEntropy(byte[] entropy) {
        System.out.println("WHAT THE FSDVCSFVSDF " + entropy);
        //var chksumByte = new SHA256Digest(entropy).getEncodedState()[0];
        byte chksumByte = 0;

        try {
            var digest = MessageDigest.getInstance("SHA-256");

            chksumByte = digest.digest(entropy)[0];
        } catch (NoSuchAlgorithmException e) {
            // This can never happen so ignore it
        }

        var phraseJoiner = new StringJoiner(" ");

        for (byte b: this.entropy) {
            phraseJoiner.add(WordList.getWord(b));
        }

        phraseJoiner.add(WordList.getWord(chksumByte));

        return phraseJoiner.toString();
    }

    public Mnemonic(byte[] entropy) {
        this.entropy = entropy;
        this.phrase = phraseFromEntropy(entropy);
    }

    public Mnemonic() {
        var bytes = new byte[numBytes];

        ThreadLocalRandom.current().nextBytes(bytes);

        this.entropy = bytes;
        this.phrase = phraseFromEntropy(entropy);
    }

    public byte[] getEntropy() {
        return entropy;
    }

    @Override
    public String toString() {
        return phrase;
    }
}
