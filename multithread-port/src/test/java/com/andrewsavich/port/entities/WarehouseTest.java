package com.andrewsavich.port.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class WarehouseTest {
	@Test
	public void getContainer_shouldThrowExceptionIfContainersIsEmpty() {
		Warehouse warehouse = new Warehouse(10);
		
		assertThrows(IllegalStateException.class, () -> warehouse.getContainer());
	}
}
