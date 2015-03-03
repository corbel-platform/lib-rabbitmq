package com.bq.lib.rabbitmq.config;

@FunctionalInterface
public interface AmqpConfiguration {

	void configure(AmqpConfigurer configurer);

}
