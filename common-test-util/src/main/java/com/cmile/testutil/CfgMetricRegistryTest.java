/*
 * Copyright 2024 cmile inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cmile.testutil;

import com.cmile.serviceutil.metric.CfgMetricRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author nishant-pentapalli
 */
@Configuration
@Import({CfgMetricRegistry.class})
public class CfgMetricRegistryTest {

  @Bean
  @Qualifier("simpleTestMeterRegistry")
  @ConditionalOnProperty(name = "meter.registry.simple.enabled", matchIfMissing = true)
  public SimpleMeterRegistry simpleTestMeterRegistry() {
    return new SimpleMeterRegistry();
  }
}