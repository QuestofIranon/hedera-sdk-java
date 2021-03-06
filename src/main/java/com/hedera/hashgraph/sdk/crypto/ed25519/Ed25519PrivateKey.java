package com.hedera.hashgraph.sdk.crypto.ed25519;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.edec.EdECObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * An ed25519 private key.
 *
 * <p>To obtain an instance, see {@link #generate()} or {@link #fromString(String)}.
 */
@SuppressWarnings("Duplicates")
public final class Ed25519PrivateKey {
    final Ed25519PrivateKeyParameters privKeyParams;
    // computed from private key and memoized
    @Nullable
    private Ed25519PublicKey publicKey;

    private Ed25519PrivateKey(Ed25519PrivateKeyParameters privKeyParams) {
        this.privKeyParams = privKeyParams;
    }

    private Ed25519PrivateKey(Ed25519PrivateKeyParameters privKeyParams, @Nullable Ed25519PublicKeyParameters pubKeyParams) {
        this.privKeyParams = privKeyParams;

        if (pubKeyParams != null) {
            this.publicKey = new Ed25519PublicKey(pubKeyParams);
        }
    }

    static Ed25519PrivateKey fromBytes(byte[] keyBytes) {
        Ed25519PrivateKeyParameters privKeyParams;
        Ed25519PublicKeyParameters pubKeyParams = null;

        if (keyBytes.length == Ed25519.SECRET_KEY_SIZE) {
            // if the decoded bytes matches the length of a private key, try that
            privKeyParams = new Ed25519PrivateKeyParameters(keyBytes, 0);
        } else if (keyBytes.length == Ed25519.SECRET_KEY_SIZE + Ed25519.PUBLIC_KEY_SIZE) {
            // some legacy code delivers private and public key pairs concatted together
            try {
                // this is how we read only the first 32 bytes
                privKeyParams = new Ed25519PrivateKeyParameters(new ByteArrayInputStream(keyBytes, 0, Ed25519.SECRET_KEY_SIZE));
                // read the remaining 32 bytes as the public key
                pubKeyParams = new Ed25519PublicKeyParameters(keyBytes, Ed25519.SECRET_KEY_SIZE);

                return new Ed25519PrivateKey(privKeyParams, pubKeyParams);
            } catch (IOException e) {
                // TODO throw a better checked exception
                throw new RuntimeException(e);
            }
        } else {
            // decode a properly DER-encoded private key descriptor
            var privateKeyInfo = PrivateKeyInfo.getInstance(keyBytes);

            try {
                var privateKey = privateKeyInfo.parsePrivateKey();
                privKeyParams = new Ed25519PrivateKeyParameters(((ASN1OctetString) privateKey).getOctets(), 0);

                var pubKeyData = privateKeyInfo.getPublicKeyData();

                if (pubKeyData != null) {
                    pubKeyParams = new Ed25519PublicKeyParameters(pubKeyData.getOctets(), 0);
                }

            } catch (IOException e) {
                // TODO: throw a better checked exception
                throw new RuntimeException(e);
            }
        }

        return new Ed25519PrivateKey(privKeyParams, pubKeyParams);
    }

    /**
     * Recover a private key from its text-encoded representation.
     *
     * @param privateKeyString the hex-encoded private key string
     * @return the restored private key
     * @throws org.bouncycastle.util.encoders.DecoderException if the hex string is invalid
     * @throws RuntimeException if the decoded key was invalid
     */
    public static Ed25519PrivateKey fromString(String privateKeyString) {
        // TODO: catch unchecked `DecoderException`
        var keyBytes = Hex.decode(privateKeyString);
        return fromBytes(keyBytes);
    }

    /** @return a new private key using {@link java.security.SecureRandom} */
    public static Ed25519PrivateKey generate() {
        return generate(new SecureRandom());
    }

    /** @return a new private key using the given {@link java.security.SecureRandom} */
    public static Ed25519PrivateKey generate(SecureRandom secureRandom) {
        return new Ed25519PrivateKey(new Ed25519PrivateKeyParameters(secureRandom));
    }

    /** @return the public key counterpart of this private key to share with the hashgraph */
    public Ed25519PublicKey getPublicKey() {
        if (publicKey == null) {
            publicKey = new Ed25519PublicKey(privKeyParams.generatePublicKey());
        }

        return publicKey;
    }

    byte[] toBytes() {
        return privKeyParams.getEncoded();
    }

    @Override
    public String toString() {
        PrivateKeyInfo privateKeyInfo;

        try {
            privateKeyInfo = new PrivateKeyInfo(
                    new AlgorithmIdentifier(EdECObjectIdentifiers.id_Ed25519), new DEROctetString(privKeyParams.getEncoded()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] encoded;

        try {
            encoded = privateKeyInfo.getEncoded("DER");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Hex.toHexString(encoded);
    }
}
