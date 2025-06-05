package com.fxexchange.fx;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class FxApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void main_shouldRunWithoutExceptions() {
		// Arrange
		String[] args = {};

		// Act & Assert
		assertDoesNotThrow(() -> FxApplication.main(args));
	}
}
