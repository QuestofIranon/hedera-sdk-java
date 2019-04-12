package com.hedera.sdk;

import com.hedera.sdk.crypto.Key;
import com.hedera.sdk.proto.*;
import io.grpc.MethodDescriptor;

public final class GetByKeyQuery extends QueryBuilder<GetByKeyResponse> {
    private final com.hedera.sdk.proto.GetByKeyQuery.Builder builder = inner.getGetByKeyBuilder();

    GetByKeyQuery(Client client) {
        super(client);
    }

    GetByKeyQuery() {
        super((Client) null);
    }

    @Override
    protected QueryHeader.Builder getHeaderBuilder() {
        return builder.getHeaderBuilder();
    }

    @Override
    protected MethodDescriptor<Query, Response> getMethod() {
        // FIXME there is no service method that corresponds to this query
        throw new Error("not implemented");
    }

    @Override
    protected GetByKeyResponse mapResponse(Response raw) throws HederaException {
        return raw.getGetByKey();
    }

    @Override
    protected void doValidate() {
        require(builder.hasKey(), ".setKey() required");
    }

    public GetByKeyQuery setKey(Key publicKey) {
        builder.setKey(publicKey.toKeyProto());
        return this;
    }
}
