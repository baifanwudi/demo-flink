package com.demo.base;

import com.demo.conf.EnvConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.IOException;

public abstract class AbstractFlinkStreamBase {

    protected StreamExecutionEnvironment createEnv(Boolean isLocal) {
        if (isLocal) {
            return StreamExecutionEnvironment.createLocalEnvironment();
        } else {
            return StreamExecutionEnvironment.getExecutionEnvironment();
        }
    }

    public abstract void executeProgram(String[] args, StreamExecutionEnvironment env) throws IOException;

    public void runAll(String[] args) throws Exception {
        runAll(args, EnvConfig.ENV_IS_LOCAL);
    }

    public void runAll(String[] args, Boolean isLocal) throws Exception {
        final StreamExecutionEnvironment env = createEnv(isLocal);
        executeProgram(args, env);
        env.execute(this.getClass().getName());
    }
}
