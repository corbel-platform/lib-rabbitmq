package io.corbel.lib.rabbitmq.config;

@FunctionalInterface
public interface AmqpConfiguration {

	void configure(AmqpConfigurer configurer);

}
