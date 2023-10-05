package com.delivery.iceway;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.delivery.iceway.domain.User;
import com.delivery.iceway.generator.DeliveryGenerator;
import com.delivery.iceway.generator.ScheduledService;
import com.delivery.iceway.sensor.SensorMapper;
import com.delivery.iceway.user.UserMapper;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
// public class IcewayApplication {
public class IcewayApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(IcewayApplication.class);	
	}

	private final DeliveryGenerator deliveryGenerator;
	private final SensorMapper sensorMapper;
	private final AmqpAdmin amqpAdmin;
	private final Queue queue;
	private final ScheduledService schedul;
	private final UserMapper userMapper;

	private final AtomicInteger runCount = new AtomicInteger(1);
	private AtomicBoolean shouldRun = new AtomicBoolean(false);

	public static void main(String[] args) {
		SpringApplication.run(IcewayApplication.class, args);
	}

	/**
	 * 애플리케이션 초기화 로직.
	 * 이 메서드는 @PostConstruct 어노테이션에 의해 애플리케이션이 시작할 때 한 번만 실행됩니다.
	 */
	@PostConstruct
	public void init() {
		sensorMapper.resetDelivery();
		sensorMapper.resetRecall();
		userMapper.resetAdmin();
		String samplePwd = new BCryptPasswordEncoder().encode("1234");
		User user = new User(1, "admin", samplePwd, "ADMIN");
		userMapper.insertAll(user);
		amqpAdmin.declareQueue(queue);
	}

	/**
	 * 스케쥴러를 시작합니다.
	 */
	public void startScheduler() {
		shouldRun.set(true);
		runCount.set(1);
	}

	/**
	 * 스케쥴러를 중지합니다.
	 */
	public void stopScheduler() {
		shouldRun.set(false);
		schedul.stopAll();
	}

	/**
	 * 스케쥴러를 리셋합니다.
	 */
	public void resetScheduler() {
		shouldRun.set(false);
		schedul.stopAll();
		sensorMapper.resetDelivery();
		sensorMapper.resetRecall();
	}

	/**
	 * 주기적으로 좌표를 전송하는 스케쥴러.
	 * 이 메서드는 @Scheduled 어노테이션에 의해 주기적으로 실행됩니다.
	 */
	@Scheduled(fixedRate = 20000)
	public void scheduleSendCoordinates() {
		if (shouldRun.get()) {
			int currentCount = runCount.get();
			if (currentCount <= 20) {
				deliveryGenerator.sendCoordinates(currentCount);
				runCount.incrementAndGet();
			} else {
				shouldRun.set(false);
			}
		}
	}
}
