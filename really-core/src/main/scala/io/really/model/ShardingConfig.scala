/**
 * Copyright (C) 2014-2015 Really Inc. <http://really.io>
 */
package io.really.model

import _root_.io.really.ReallyConfig

trait ShardingConfig {
  this: ReallyConfig =>

  object Sharding {
    protected val sharding = coreConfig.getConfig("collection-actor")
    val maxShards = sharding.getInt("max-shards")
    val bucketsNumber = sharding.getLong("number-of-buckets")
  }

}