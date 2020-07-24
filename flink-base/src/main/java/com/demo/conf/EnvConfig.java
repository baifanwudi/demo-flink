package com.demo.conf;

import com.demo.util.ConfigUtils;

public class EnvConfig {

    public static final Boolean ENV_IS_LOCAL= Boolean.parseBoolean(ConfigUtils.getPros("env.local.boolean"));
}
