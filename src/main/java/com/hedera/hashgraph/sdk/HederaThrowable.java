package com.hedera.hashgraph.sdk;

/**
 * Closed set of exception types thrown by this SDK;
 * where {@code HederaThrowable} is passed to a callback, it is one of these types:
 *
 * <ul>
 * <li> {@link HederaException} </li>
 * <li> {@link HederaNetworkException}</li>
 * </ul>
 */
public interface HederaThrowable {
}
