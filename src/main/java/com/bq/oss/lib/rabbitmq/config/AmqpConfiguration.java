package com.bq.oss.lib.rabbitmq.config;

@FunctionalInterface
public interface AmqpConfiguration {

	void configure(AmqpConfigurer configurer);

}
