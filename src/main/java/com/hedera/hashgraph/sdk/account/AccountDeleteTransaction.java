package com.hedera.hashgraph.sdk.account;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TransactionBuilder;
import com.hedera.hashgraph.sdk.proto.CryptoDeleteTransactionBody;
import com.hedera.hashgraph.sdk.proto.CryptoServiceGrpc;
import com.hedera.hashgraph.sdk.proto.Transaction;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;

import javax.annotation.Nullable;

// `CryptoDeleteTransaction`
public class AccountDeleteTransaction extends TransactionBuilder<AccountDeleteTransaction> {
    private final CryptoDeleteTransactionBody.Builder builder = bodyBuilder.getCryptoDeleteBuilder();

    public AccountDeleteTransaction(@Nullable Client client) {
        super(client);
    }

    public AccountDeleteTransaction setTransferAccountId(AccountId transferAccountId) {
        builder.setTransferAccountID(transferAccountId.toProto());
        return this;
    }

    public AccountDeleteTransaction setDeleteAccountId(AccountId deleteAccountId) {
        builder.setDeleteAccountID(deleteAccountId.toProto());
        return this;
    }

    @Override
    protected void doValidate() {
        require(builder.hasTransferAccountID(), ".setTransferAccountId() required");
        require(builder.hasDeleteAccountID(), ".setDeleteAccountId() required");
    }

    @Override
    protected MethodDescriptor<Transaction, TransactionResponse> getMethod() {
        return CryptoServiceGrpc.getCryptoDeleteMethod();
    }
}
