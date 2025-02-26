package com.fernando.ms.posts.app.application.services.proxy;

import com.fernando.ms.posts.app.application.ports.output.ExternalUserOutputPort;
import com.fernando.ms.posts.app.application.ports.output.PostPersistencePort;

public class ProcessFactory {
    private ProcessFactory() {
    }
    public static IProcess validateSave(ExternalUserOutputPort externalUserOutputPort) {
        return new RuleSavePostProxy(externalUserOutputPort);
    }

    public static IProcess validateUpdate(ExternalUserOutputPort externalUserOutputPort,PostPersistencePort postPersistencePort,String id) {
        return new RuleUpdatePostProxy(externalUserOutputPort,postPersistencePort,id);
    }

    public static IProcess validateDelete(PostPersistencePort postPersistencePort,String id) {
        return new RuleDeletePostProxy(postPersistencePort,id);
    }
}
