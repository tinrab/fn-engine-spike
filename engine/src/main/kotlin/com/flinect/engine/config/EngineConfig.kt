package com.flinect.engine.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class EngineConfig {
    private val config: Config

    val workerConfig: WorkerConfig

    constructor() : this(ConfigFactory.load())

    constructor(config: Config) {
        this.config = config
        config.checkValid(ConfigFactory.defaultReference(), "com.flinect.engine")
        val engineConfig = config.getConfig("com.flinect.engine")
        val worker = engineConfig.getConfig("worker")

        workerConfig = WorkerConfig(
            worker.getInt("pool-size")
        )
    }
}
