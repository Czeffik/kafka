package com.trzewik.kafka.infrastructure.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
class InformationCallback implements ListenableFutureCallback<SendResult<String, Information>> {
    @Override
    public void onFailure(Throwable ex) {
        log.info(ex.getMessage());
    }

    @Override
    public void onSuccess(SendResult<String, Information> result) {
        log.info(result.toString());
    }
}
