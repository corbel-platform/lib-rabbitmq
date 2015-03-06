package com.bq.oss.lib.rabbitmq.ioc;

import com.bq.oss.lib.rabbitmq.config.AmqpConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.bq.oss.lib.rabbitmq.config.RabbitMQConfigurer;

@Configuration
public abstract class AbstractRabbitMQConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRabbitMQConfiguration.class);

	@Bean
	public AmqpTemplate amqpTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		MessageConverter messageConverters = getMessageConverter();
		if (messageConverters != null) {
			template.setMessageConverter(messageConverters);
		}

		return template;
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(getRabbitHost(), getRabbitPort());
		connectionFactory.setUsername(getRabbitUsername());
		connectionFactory.setPassword(getRabbitPassword());
		Integer requestedHeartbeat = getRequestedHeartbeat();
		if (requestedHeartbeat != null) {
			connectionFactory.setRequestedHeartBeat(requestedHeartbeat);
		}
		Integer connectionTimeout = getConnectionTimeout();
		if (connectionTimeout != null) {
			connectionFactory.setCloseTimeout(connectionTimeout);
		}
		String virtualHost = getVirtualHost();
		if (virtualHost != null) {
			connectionFactory.setVirtualHost(virtualHost);
		}
		return connectionFactory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public AmqpConfigurer amqpConfigurer() {
		return new RabbitMQConfigurer(rabbitAdmin(), connectionFactory());
	}

	@Bean
	public AmqpConfigurationBeanPostProcessor amqpConfigurationBeanPostProcessor() {
		return new AmqpConfigurationBeanPostProcessor(amqpConfigurer());
	}

	protected abstract Environment getEnvironment();

	/**
	 * Override by subclasses to define the {@link MessageConverter} of the {@link AmqpTemplate}
	 */
	protected MessageConverter getMessageConverter() {
		return null;
	}

	protected String getRabbitHost() {
		return getEnvironment().getProperty("rabbitmq.host");
	}

	protected int getRabbitPort() {
		return getEnvironment().getProperty("rabbitmq.port", int.class);
	}

	protected String getRabbitUsername() {
		return getEnvironment().getProperty("rabbitmq.username", String.class);
	}

	protected String getRabbitPassword() {
		return getEnvironment().getProperty("rabbitmq.password", String.class);
	}

	protected Integer getRequestedHeartbeat() {
		return getEnvironment().getProperty("rabbitmq.requestedHeartbeat", Integer.class);
	}

	protected Integer getConnectionTimeout() {
		return getEnvironment().getProperty("rabbitmq.connectionTimeout", Integer.class);
	}

	protected String getVirtualHost() {
		return getEnvironment().getProperty("rabbitmq.virtualHost", String.class);
	}
}
