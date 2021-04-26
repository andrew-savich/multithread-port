package com.andrewsavich.port.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ShipTest {

	private static Ship underTest;

	@BeforeAll
	public static void init() {
		underTest = new Ship("TestShip", 5);
	}

	@Test
	public void unloadContainer_shouldThrowIllegalStateExceptionIfPierNull() {
		assertThrows(IllegalStateException.class, () -> underTest.unloadContainer());
	}

	@Test
	public void unloadContainer_shouldThrowIllegalStateExceptionIfContainersIsEmpty() {
		assertThrows(IllegalStateException.class, () -> underTest.unloadContainer());
	}

	@Test
	public void generateLoadedContainers_containersShouldBeNotEmpty() {
		underTest.generateLoadedContainers();

		assertTrue(underTest.getQuantityContainers() > 0);
	}

	@Test
	public void putContainer_shouldThrowExceptionIfContainersIsEmpty() {
		assertThrows(IllegalStateException.class, () -> underTest.putContainer(new Container()));
	}
}
